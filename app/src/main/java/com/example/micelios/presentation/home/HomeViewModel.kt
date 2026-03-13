package com.example.micelios.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val momentRepository: MomentRepository
) : ViewModel() {

    private val _moments = MutableStateFlow<List<Moment>>(emptyList())
    val moments: StateFlow<List<Moment>> = _moments

    fun loadFeed() {
        viewModelScope.launch {
            momentRepository.getFeedMoments().collect { list ->
                _moments.value = list
            }
        }
    }
}