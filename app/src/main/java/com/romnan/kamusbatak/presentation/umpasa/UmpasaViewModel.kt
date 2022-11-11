package com.romnan.kamusbatak.presentation.umpasa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.domain.model.UmpasaCategory
import com.romnan.kamusbatak.domain.repository.CulturalContentRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.presentation.umpasa.model.UmpasaPresentation
import com.romnan.kamusbatak.presentation.umpasa.model.toPresentation
import com.romnan.kamusbatak.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UmpasaViewModel @Inject constructor(
    private val culturalContentRepository: CulturalContentRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val _umpasasList = mutableStateListOf<UmpasaPresentation>()
    val umpasasList: List<UmpasaPresentation> = _umpasasList

    var category by mutableStateOf(UmpasaCategory.ALL)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onReceivedCategoryName(name: String) {
        category = try {
            UmpasaCategory.valueOf(name)
        } catch (e: Exception) {
            UmpasaCategory.ALL
        }
        getUmpasasList()
    }

    private var getUmpasasListJob: Job? = null
    private fun getUmpasasList() {
        getUmpasasListJob?.cancel()
        getUmpasasListJob = viewModelScope.launch {
            culturalContentRepository.getUmpasas(category = category).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> isLoading = true

                    is Resource.Success -> {
                        isLoading = false
                        if (!result.data.isNullOrEmpty()) {
                            _umpasasList.clear()
                            _umpasasList.addAll(result.data.map { it.toPresentation(false) })
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun onClickSeeMeaning(index: Int) {
        _umpasasList[index] = umpasasList[index].copy(
            isMeaningShown = !umpasasList[index].isMeaningShown
        )
    }
}