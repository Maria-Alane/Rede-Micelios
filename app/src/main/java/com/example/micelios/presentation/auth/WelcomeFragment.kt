package com.example.micelios.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.R
import com.example.micelios.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
        viewModel.checkExistingSession()

        binding.buttonEnterMicelios.setOnClickListener {
            viewModel.register(
                name = binding.editTextWelcomeName.text.toString(),
                email = binding.editTextWelcomeEmail.text.toString(),
                password = binding.editTextWelcomePassword.text.toString()
            )
        }

        binding.buttonLogin.setOnClickListener {
            viewModel.signIn(
                email = binding.editTextWelcomeEmail.text.toString(),
                password = binding.editTextWelcomePassword.text.toString()
            )
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    WelcomeUiState.Idle -> {
                        binding.buttonEnterMicelios.isEnabled = true
                        binding.buttonLogin.isEnabled = true
                    }

                    WelcomeUiState.Loading -> {
                        binding.buttonEnterMicelios.isEnabled = false
                        binding.buttonLogin.isEnabled = false
                    }

                    WelcomeUiState.Success -> {
                        binding.buttonEnterMicelios.isEnabled = true
                        binding.buttonLogin.isEnabled = true

                        Toast.makeText(
                            requireContext(),
                            "Bem-vinda ao Micélios",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(
                            R.id.action_welcomeFragment_to_homeFragment
                        )
                    }

                    is WelcomeUiState.Error -> {
                        binding.buttonEnterMicelios.isEnabled = true
                        binding.buttonLogin.isEnabled = true

                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
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