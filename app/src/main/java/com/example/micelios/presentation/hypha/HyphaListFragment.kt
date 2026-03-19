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
import com.example.micelios.data.repository.MemberDraft
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentHyphaListBinding
import com.example.micelios.domain.model.HyphaType
import com.example.micelios.presentation.common.SessionManager
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
        val hyphaRepository = HyphaRepository(database.hyphaDao())
        val userRepository = UserRepository(database.userDao())
        val sessionManager = SessionManager(requireContext().applicationContext)

        viewModel = HyphaListViewModel(
            hyphaRepository = hyphaRepository,
            userRepository = userRepository,
            sessionManager = sessionManager
        )

        binding.buttonCreateHypha.setOnClickListener {
            val name = binding.editTextHyphaName.text.toString().trim()
            val description = binding.editTextHyphaDescription.text.toString().trim()
            val memberUserIdText = binding.editTextMemberUserId.text.toString().trim()
            val memberDisplayName = binding.editTextMemberDisplayName.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Digite o nome da hypha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (memberUserIdText.isBlank() || memberDisplayName.isBlank()) {
                Toast.makeText(requireContext(), "Adicione pelo menos mais um membro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val memberUserId = memberUserIdText.toLongOrNull()
            if (memberUserId == null) {
                Toast.makeText(requireContext(), "ID do membro inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createHypha(
                name = name,
                description = description,
                type = HyphaType.PRIVATE,
                additionalMembers = listOf(
                    MemberDraft(
                        userId = memberUserId,
                        displayName = memberDisplayName
                    )
                )
            )

            binding.editTextHyphaName.text?.clear()
            binding.editTextHyphaDescription.text?.clear()
            binding.editTextMemberUserId.text?.clear()
            binding.editTextMemberDisplayName.text?.clear()

            Toast.makeText(requireContext(), "Hypha criada", Toast.LENGTH_SHORT).show()
        }

        binding.buttonOpenHyphaById.setOnClickListener {
            val hyphaIdText = binding.editTextOpenHyphaId.text.toString().trim()
            val hyphaId = hyphaIdText.toLongOrNull()

            if (hyphaId == null) {
                Toast.makeText(requireContext(), "Digite um ID válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putLong("hyphaId", hyphaId)
            }
            findNavController().navigate(R.id.action_hyphaListFragment_to_hyphaDetailFragment, bundle)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collectLatest { hyphas ->
                binding.textViewHyphaList.text =
                    if (hyphas.isEmpty()) {
                        "Nenhuma hypha criada ainda."
                    } else {
                        hyphas.joinToString("\n\n") { hypha ->
                            "ID ${hypha.id}\n• ${hypha.name}\n${hypha.type}"
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