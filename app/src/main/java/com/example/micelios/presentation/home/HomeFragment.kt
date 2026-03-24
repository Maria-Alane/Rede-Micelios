package com.example.micelios.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeAdapter()
        binding.recyclerMoments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMoments.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moments.collect { moments ->
                adapter.submitList(moments)
                binding.textViewEmptyState.visibility =
                    if (moments.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewModel.loadFeed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}