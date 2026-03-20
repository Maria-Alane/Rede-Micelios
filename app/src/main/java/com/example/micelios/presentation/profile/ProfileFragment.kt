package com.example.micelios.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentProfileBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        val database = MiceliosDatabase.getDatabase(requireContext())

        ProfileViewModelFactory(
            userRepository = UserRepository(database.userDao()),
            hyphaRepository = HyphaRepository(database.hyphaDao()),
            momentRepository = MomentRepository(
                momentDao = database.momentDao(),
                hyphaDao = database.hyphaDao()
            ),
            sessionManager = SessionManager(requireContext().applicationContext)
        )
    }

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
            viewModel.user.collectLatest { user ->
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
            viewModel.hyphas.collectLatest { hyphas ->
                binding.textViewUserHyphas.text =
                    if (hyphas.isEmpty()) {
                        "Você ainda não participa de nenhuma hypha."
                    } else {
                        hyphas.joinToString("\n") { "• ${it.name}" }
                    }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moments.collectLatest { moments ->
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

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val hyphaRepository: HyphaRepository,
    private val momentRepository: MomentRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(
                userRepository = userRepository,
                hyphaRepository = hyphaRepository,
                momentRepository = momentRepository,
                sessionManager = sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}