package com.example.micelios.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.micelios.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { user ->
                if (user != null) {
                    binding.textViewProfileAvatar.text = user.name.take(2).uppercase()
                    binding.textViewUserName.text = user.name
                    binding.textViewUserBio.text =
                        if (user.bio.isBlank()) "Sem bio ainda." else user.bio
                } else {
                    binding.textViewProfileAvatar.text = "--"
                    binding.textViewUserName.text = "Usuário não encontrado"
                    binding.textViewUserBio.text = "Sem bio ainda."
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collect { hyphas ->
                binding.textViewUserHyphas.text =
                    if (hyphas.isEmpty()) {
                        "Você ainda não participa de nenhuma hypha."
                    } else {
                        hyphas.joinToString("\n") { "• ${it.name}" }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moments.collect { moments ->
                binding.textViewUserMoments.text =
                    if (moments.isEmpty()) {
                        "Você ainda não publicou momentos recentes."
                    } else {
                        moments.joinToString("\n\n") { "• ${it.content}" }
                    }
            }
        }

        viewModel.loadProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}