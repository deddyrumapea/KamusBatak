package com.romnan.kamusbatak.presentation.quizGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizGamesViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    var isUpdatingLocalDb by mutableStateOf(false)
        private set

    val isOfflineSupported: StateFlow<Boolean?> =
        dictionaryRepository.localDbLastUpdatedAt.map { it != null }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var onUpdateLocalDbJob: Job? = null
    fun onUpdateLocalDb() {
        onUpdateLocalDbJob?.cancel()
        onUpdateLocalDbJob = viewModelScope.launch {
            dictionaryRepository.updateLocalDb().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isUpdatingLocalDb = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> isUpdatingLocalDb = true
                    is Resource.Success -> isUpdatingLocalDb = false
                }
            }.launchIn(this)
        }
    }
}