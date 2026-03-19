package com.example.micelios.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.databinding.FragmentHomeBinding
import com.example.micelios.presentation.common.SessionManager
import com.example.micelios.presentation.common.TimeFormatter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = MiceliosDatabase.getDatabase(requireContext())
        val repository = MomentRepository(
            momentDao = database.momentDao(),
            hyphaDao = database.hyphaDao()
        )
        val sessionManager = SessionManager(requireContext().applicationContext)

        viewModel = HomeViewModel(repository, sessionManager)

        adapter = HomeAdapter()
        binding.recyclerMoments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMoments.adapter = adapter

        val sessionStartTime = sessionManager.getSessionStartTime()
        binding.textViewEntryTime.text =
            "Você entrou ${TimeFormatter.formatElapsedTime(sessionStartTime)}"

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moments.collect { moments ->
                adapter.submitList(moments)
            }
        }

        viewModel.loadFeed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}