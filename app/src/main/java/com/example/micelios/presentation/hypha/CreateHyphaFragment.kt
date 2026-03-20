package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MemberDraft
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentCreateHyphaBinding
import com.example.micelios.domain.model.HyphaType
import com.example.micelios.presentation.common.SessionManager

class CreateHyphaFragment : Fragment() {

    private var _binding: FragmentCreateHyphaBinding? = null
    private val binding get() = _binding!!

    private lateinit var createHyphaViewModel: HyphaListViewModel

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

        val database = MiceliosDatabase.getDatabase(requireContext())
        val hyphaRepository = HyphaRepository(database.hyphaDao())
        val userRepository = UserRepository(database.userDao())
        val sessionManager = SessionManager(requireContext().applicationContext)

        createHyphaViewModel = HyphaListViewModel(
            hyphaRepository = hyphaRepository,
            userRepository = userRepository,
            sessionManager = sessionManager
        )

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonCreateHypha.setOnClickListener {
            val name = binding.editTextHyphaName.text.toString().trim()
            val description = binding.editTextHyphaDescription.text.toString().trim()
            val memberUserIdText = binding.editTextMemberUserId.text.toString().trim()
            val memberDisplayName = binding.editTextMemberDisplayName.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Digite o nome da hypha", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val memberUserId = memberUserIdText.toLongOrNull()
            if (memberUserId == null || memberDisplayName.isBlank()) {
                Toast.makeText(requireContext(), "Adicione um membro válido", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            createHyphaViewModel.createHypha(
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

            Toast.makeText(requireContext(), "Hypha criada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}