package com.example.micelios.presentation.moments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.databinding.FragmentMomentListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MomentListFragment : Fragment() {

    private var _binding: FragmentMomentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MomentListViewModel by viewModels()
    private lateinit var hyphaSelectorAdapter: PostHyphaSelectorAdapter

    private var selectedHyphaId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        hyphaSelectorAdapter = PostHyphaSelectorAdapter { hyphaId ->
            selectedHyphaId = hyphaId
        }

        binding.recyclerHyphas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHyphas.adapter = hyphaSelectorAdapter

        observeHyphas()
        observeUiState()

        binding.buttonPostMoment.setOnClickListener {
            val hyphaId = selectedHyphaId
            if (hyphaId.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Escolha uma hypha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createMoment(
                hyphaId = hyphaId,
                content = binding.editTextMoment.text.toString().trim()
            )
        }

        viewModel.loadUserHyphas()
    }

    private fun observeHyphas() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collect { hyphas ->
                if (hyphas.isEmpty()) {
                    selectedHyphaId = null
                }

                hyphaSelectorAdapter.submitList(hyphas)
                binding.textViewEmptyHyphas.visibility =
                    if (hyphas.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    MomentListUiState.Idle -> {
                        binding.buttonPostMoment.isEnabled = true
                    }

                    MomentListUiState.Posting -> {
                        binding.buttonPostMoment.isEnabled = false
                    }

                    MomentListUiState.Success -> {
                        binding.buttonPostMoment.isEnabled = true
                        binding.editTextMoment.text?.clear()
                        Toast.makeText(requireContext(), "Momento publicado", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                    is MomentListUiState.Error -> {
                        binding.buttonPostMoment.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetUiState()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}