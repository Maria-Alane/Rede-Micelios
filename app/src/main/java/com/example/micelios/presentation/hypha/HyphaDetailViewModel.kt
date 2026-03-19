package com.example.micelios.presentation.hypha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.domain.model.Hypha
import com.example.micelios.domain.model.HyphaMember
import com.example.micelios.domain.model.Moment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HyphaDetailViewModel(
    private val hyphaRepository: HyphaRepository,
    private val momentRepository: MomentRepository
) : ViewModel() {

    private val _hypha = MutableStateFlow<Hypha?>(null)
    val hypha: StateFlow<Hypha?> = _hypha

    private val _members = MutableStateFlow<List<HyphaMember>>(emptyList())
    val members: StateFlow<List<HyphaMember>> = _members

    private val _moments = MutableStateFlow<List<Moment>>(emptyList())
    val moments: StateFlow<List<Moment>> = _moments

    fun loadHypha(hyphaId: Long) {
        viewModelScope.launch {
            hyphaRepository.getHyphaById(hyphaId).collect {
                _hypha.value = it
            }
        }

        viewModelScope.launch {
            hyphaRepository.getMembersByHypha(hyphaId).collect {
                _members.value = it
            }
        }

        viewModelScope.launch {
            momentRepository.getMomentsByHypha(hyphaId).collect {
                _moments.value = it
            }
        }
    }
}