package com.example.micelios.presentation.hypha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.domain.model.Hypha
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HyphaListViewModel(
    private val hyphaRepository: HyphaRepository
) : ViewModel() {

    private val _hyphas = MutableStateFlow<List<Hypha>>(emptyList())
    val hyphas: StateFlow<List<Hypha>> = _hyphas

    fun loadHyphas() {
        viewModelScope.launch {
            hyphaRepository.getAllHyphas().collect { list ->
                _hyphas.value = list
            }
        }
    }

    fun createHypha(name: String, description: String) {
        if (name.isBlank()) return

        viewModelScope.launch {
            hyphaRepository.insertHypha(name, description)
        }
    }
}