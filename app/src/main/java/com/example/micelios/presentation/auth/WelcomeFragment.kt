package com.example.micelios.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.R
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels {
        WelcomeViewModelFactory(
            UserRepository(
                MiceliosDatabase.getDatabase(requireContext()).userDao()
            )
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

        viewModel.checkUser()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hasUser.collectLatest { hasUser ->
                if (hasUser) {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }

        binding.buttonEnterMicelios.setOnClickListener {
            val name = binding.editTextWelcomeName.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Digite seu nome", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveUser(name = name)
            Toast.makeText(requireContext(), "Bem-vinda ao Micélios", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class WelcomeViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WelcomeViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}