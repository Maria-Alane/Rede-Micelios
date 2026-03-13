package com.example.micelios.presentation.moments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MomentListViewModel(
    private val momentRepository: MomentRepository
) : ViewModel() {

    private val _moments = MutableStateFlow<List<Moment>>(emptyList())
    val moments: StateFlow<List<Moment>> get() = _moments

    fun loadMoments(hyphaId: Long) {
        viewModelScope.launch {
            momentRepository.getMomentsByHypha(hyphaId).collect {
                _moments.value = it
            }
        }
    }

    fun createMoment(
        hyphaId: Long,
        creatorName: String,
        content: String,
        photoUri: String? = null
    ) {
        if (content.isBlank()) return

        viewModelScope.launch {
            momentRepository.insertMoment(
                hyphaId = hyphaId,
                creatorName = creatorName,
                content = content,
                photoUri = photoUri
            )
        }
    }
}