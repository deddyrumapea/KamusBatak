package com.romnan.kamusbatak.presentation.partuturan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.domain.repository.CulturalContentRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.presentation.partuturan.model.PartuturanPresentation
import com.romnan.kamusbatak.presentation.partuturan.model.toPresentation
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
class PartuturanViewModel @Inject constructor(
    private val culturalContentRepository: CulturalContentRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val _partuturansList = mutableStateListOf<PartuturanPresentation>()
    val partuturansList: List<PartuturanPresentation> = _partuturansList

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getPartuturans()
    }

    private var getPartuturansJob: Job? = null
    private fun getPartuturans() {
        getPartuturansJob?.cancel()
        getPartuturansJob = viewModelScope.launch {
            culturalContentRepository.getPartuturans().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> isLoading = true
                    is Resource.Success -> {
                        isLoading = false
                        if (!result.data.isNullOrEmpty()) {
                            _partuturansList.clear()
                            _partuturansList.addAll(result.data.map {
                                it.toPresentation(isDescriptionShown = false)
                            })
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun onClickShowDescription(index: Int) {
        _partuturansList[index] = partuturansList[index].copy(
            isDescriptionShown = !partuturansList[index].isDescriptionShown,
        )
    }
}