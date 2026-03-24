package com.example.micelios.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.AuthRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.FeedMoment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val momentRepository: MomentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _moments = MutableStateFlow<List<FeedMoment>>(emptyList())
    val moments: StateFlow<List<FeedMoment>> = _moments

    fun loadFeed() {
        val currentUserId = authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {
            momentRepository.getFeedMomentsForUser(currentUserId).collect {
                _moments.value = it
            }
        }
    }
}