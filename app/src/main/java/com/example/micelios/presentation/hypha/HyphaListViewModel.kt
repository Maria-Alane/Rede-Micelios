package com.example.micelios.presentation.hypha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.domain.model.Hypha
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HyphaListViewModel @Inject constructor(
    private val hyphaRepository: HyphaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _hyphas = MutableStateFlow<List<Hypha>>(emptyList())
    val hyphas: StateFlow<List<Hypha>> = _hyphas

    fun loadHyphas() {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            hyphaRepository.getHyphasForUser(currentUserId).collect {
                _hyphas.value = it
            }
        }
    }
}