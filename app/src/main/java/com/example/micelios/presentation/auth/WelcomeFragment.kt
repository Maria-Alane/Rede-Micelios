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
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentWelcomeBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels {
        WelcomeViewModelFactory(
            userRepository = UserRepository(
                MiceliosDatabase.getDatabase(requireContext()).userDao()
            ),
            sessionManager = SessionManager(requireContext().applicationContext)
        )
    }

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

        binding.buttonEnterMicelios.setOnClickListener {
            val name = binding.editTextWelcomeName.text.toString()
            viewModel.registerLocalUser(name)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is WelcomeUiState.Idle -> {
                        binding.buttonEnterMicelios.isEnabled = true
                    }

                    is WelcomeUiState.Loading -> {
                        binding.buttonEnterMicelios.isEnabled = false
                    }

                    is WelcomeUiState.Success -> {
                        binding.buttonEnterMicelios.isEnabled = true
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