package com.example.micelios.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    private var hyphaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hyphaId = arguments?.getString("hyphaId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeHyphaId = hyphaId
        if (safeHyphaId.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Hypha inválida para o chat", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        chatAdapter = ChatAdapter(currentUserId)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.recyclerMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMessages.adapter = chatAdapter

        observeMessages()
        observeUiState()

        binding.buttonSendMessage.setOnClickListener {
            val text = binding.editTextMessage.text.toString().trim()

            if (currentUserId == null) {
                Toast.makeText(requireContext(), "Sessão inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.sendMessage(safeHyphaId, currentUserId, text)
            binding.editTextMessage.text?.clear()
        }

        viewModel.loadMessages(safeHyphaId)
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                chatAdapter.submitList(messages)
                binding.textViewEmptyChat.visibility =
                    if (messages.isEmpty()) View.VISIBLE else View.GONE

                if (messages.isNotEmpty()) {
                    binding.recyclerMessages.scrollToPosition(messages.lastIndex)
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    ChatUiState.Idle -> binding.buttonSendMessage.isEnabled = true
                    ChatUiState.Sending -> binding.buttonSendMessage.isEnabled = false
                    is ChatUiState.Error -> {
                        binding.buttonSendMessage.isEnabled = true
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