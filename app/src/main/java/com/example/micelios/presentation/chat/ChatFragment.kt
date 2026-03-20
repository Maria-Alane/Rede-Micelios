package com.example.micelios.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.MessageRepository
import com.example.micelios.databinding.FragmentChatBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var fragmentChatBinding: FragmentChatBinding? = null
    private val binding get() = fragmentChatBinding!!

    private lateinit var viewModel: ChatViewModel

    private var hyphaId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hyphaId = arguments?.getLong("hyphaId", -1L) ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentChatBinding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hyphaId == -1L) {
            Toast.makeText(requireContext(), "Hypha inválida para o chat", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val database = MiceliosDatabase.getDatabase(requireContext())
        val messageRepository = MessageRepository(database.messageDao())
        val sessionManager = SessionManager(requireContext().applicationContext)
        val currentUserId = sessionManager.getCurrentUserId()

        viewModel = ChatViewModel(messageRepository)

        binding.buttonSendMessage.setOnClickListener {
            val text = binding.editTextMessage.text.toString().trim()

            if (currentUserId == null) {
                Toast.makeText(requireContext(), "Sessão inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Digite uma mensagem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.sendMessage(hyphaId, currentUserId, text)
            binding.editTextMessage.text?.clear()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                val messageText = messages.joinToString("\n") {
                    "${it.senderDisplayName}: ${it.content}"
                }
                binding.textViewMessageList.text = messageText
            }
        }

        viewModel.loadMessages(hyphaId)
    }

    override fun onDestroyView() {
        fragmentChatBinding = null
        super.onDestroyView()
    }
}