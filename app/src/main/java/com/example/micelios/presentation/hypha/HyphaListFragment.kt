package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.databinding.FragmentHyphaListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HyphaListFragment : Fragment() {

    private var _binding: FragmentHyphaListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HyphaListViewModel

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

        val database = MiceliosDatabase.getDatabase(requireContext())
        val repository = HyphaRepository(database.hyphaDao())
        viewModel = HyphaListViewModel(repository)

        binding.buttonCreateHypha.setOnClickListener {
            val name = binding.editTextHyphaName.text.toString().trim()
            val description = binding.editTextHyphaDescription.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Digite o nome da hypha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createHypha(name, description)
            binding.editTextHyphaName.text?.clear()
            binding.editTextHyphaDescription.text?.clear()
            Toast.makeText(requireContext(), "Hypha criada", Toast.LENGTH_SHORT).show()
        }

        binding.buttonOpenChat.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("hyphaId", 1L)
            }
            findNavController().navigate(com.example.micelios.R.id.chatFragment, bundle)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collectLatest { hyphas ->
                binding.textViewHyphaList.text =
                    if (hyphas.isEmpty()) {
                        "Nenhuma hypha criada ainda."
                    } else {
                        hyphas.joinToString("\n\n") { hypha ->
                            "• ${hypha.name}"
                        }
                    }
            }
        }

        viewModel.loadHyphas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}