package com.example.micelios.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.FeedMoment
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val momentRepository: MomentRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _moments = MutableStateFlow<List<FeedMoment>>(emptyList())
    val moments: StateFlow<List<FeedMoment>> = _moments

    fun loadFeed() {
        val currentUserId = sessionManager.getCurrentUserId() ?: return

        viewModelScope.launch {
            momentRepository.getFeedMomentsForUser(currentUserId).collect { list ->
                _moments.value = list
            }
        }
    }
}