package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.databinding.FragmentCreateHyphaBinding
import com.example.micelios.data.repository.MemberDraft
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateHyphaFragment : Fragment() {

    private var _binding: FragmentCreateHyphaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateHyphaViewModel by viewModels()

    private var selectedInvitedMember: MemberDraft? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateHyphaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        observeInviteLookup()
        observeUiState()

        binding.editTextMemberEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                selectedInvitedMember = null
                binding.textViewSelectedMember.text = "Nenhum membro selecionado"
                viewModel.clearInviteLookup()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        binding.buttonSearchMemberByEmail.setOnClickListener {
            viewModel.searchMemberByEmail(binding.editTextMemberEmail.text.toString())
        }

        binding.buttonCreateHypha.setOnClickListener {
            viewModel.createHypha(
                name = binding.editTextHyphaName.text.toString(),
                description = binding.editTextHyphaDescription.text.toString(),
                invitedMember = selectedInvitedMember
            )
        }
    }

    private fun observeInviteLookup() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.inviteLookupState.collect { state ->
                when (state) {
                    InviteLookupUiState.Idle -> {
                        binding.textViewSelectedMember.text = "Nenhum membro selecionado"
                    }

                    InviteLookupUiState.Loading -> {
                        binding.textViewSelectedMember.text = "Buscando usuário..."
                    }

                    is InviteLookupUiState.Found -> {
                        selectedInvitedMember = state.member
                        binding.textViewSelectedMember.text =
                            "Convidado: ${state.member.displayName} (${state.email})"
                    }

                    InviteLookupUiState.NotFound -> {
                        selectedInvitedMember = null
                        binding.textViewSelectedMember.text = "Usuário não encontrado"
                    }

                    is InviteLookupUiState.Error -> {
                        selectedInvitedMember = null
                        binding.textViewSelectedMember.text = state.message
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    CreateHyphaUiState.Idle -> {
                        binding.buttonCreateHypha.isEnabled = true
                    }

                    CreateHyphaUiState.Loading -> {
                        binding.buttonCreateHypha.isEnabled = false
                    }

                    CreateHyphaUiState.Success -> {
                        binding.buttonCreateHypha.isEnabled = true
                        Toast.makeText(requireContext(), "Hypha criada", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                    is CreateHyphaUiState.Error -> {
                        binding.buttonCreateHypha.isEnabled = true
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