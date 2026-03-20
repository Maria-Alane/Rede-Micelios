package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.R
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.databinding.FragmentHyphaDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HyphaDetailFragment : Fragment() {

    private var _binding: FragmentHyphaDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HyphaDetailViewModel

    private var hyphaId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hyphaId = arguments?.getLong("hyphaId", -1L) ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHyphaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hyphaId == -1L) {
            Toast.makeText(requireContext(), "Hypha inválida", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val database = MiceliosDatabase.getDatabase(requireContext())
        val hyphaRepository = HyphaRepository(database.hyphaDao())
        val momentRepository = MomentRepository(
            momentDao = database.momentDao(),
            hyphaDao = database.hyphaDao()
        )

        viewModel = HyphaDetailViewModel(hyphaRepository, momentRepository)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonOpenChat.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("hyphaId", hyphaId)
            }
            findNavController().navigate(R.id.action_hyphaDetailFragment_to_chatFragment, bundle)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hypha.collectLatest { hypha ->
                if (hypha != null) {
                    binding.textViewHyphaName.text = hypha.name
                    binding.textViewHyphaDescription.text =
                        if (hypha.description.isBlank()) "Sem descrição" else hypha.description
                    binding.textViewHyphaType.text = hypha.type.name
                    binding.textViewHyphaBadge.text = hypha.name.take(2).uppercase()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.members.collectLatest { members ->
                binding.textViewMembers.text =
                    if (members.isEmpty()) {
                        "Nenhum membro"
                    } else {
                        members.joinToString("\n") { "• ${it.displayName} (${it.role.name})" }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moments.collectLatest { moments ->
                binding.textViewHyphaMoments.text =
                    if (moments.isEmpty()) {
                        "Nenhum momento nesta hypha ainda."
                    } else {
                        moments.joinToString("\n\n") {
                            "${it.creatorDisplayName}: ${it.content}"
                        }
                    }
            }
        }

        viewModel.loadHypha(hyphaId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}