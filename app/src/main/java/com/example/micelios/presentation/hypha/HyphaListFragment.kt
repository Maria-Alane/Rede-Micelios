package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.R
import com.example.micelios.databinding.FragmentHyphaListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HyphaListFragment : Fragment() {

    private var _binding: FragmentHyphaListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HyphaListViewModel by viewModels()
    private lateinit var hyphaListAdapter: HyphaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHyphaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hyphaListAdapter = HyphaListAdapter { hyphaId ->
            findNavController().navigate(
                R.id.action_hyphaListFragment_to_hyphaDetailFragment,
                Bundle().apply { putString("hyphaId", hyphaId) }
            )
        }

        binding.recyclerHyphas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHyphas.adapter = hyphaListAdapter

        binding.fabCreateHypha.setOnClickListener {
            findNavController().navigate(R.id.createHyphaFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collect { hyphas ->
                binding.textViewEmptyHyphas.visibility =
                    if (hyphas.isEmpty()) View.VISIBLE else View.GONE
                hyphaListAdapter.submitList(hyphas)
            }
        }

        viewModel.loadHyphas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}