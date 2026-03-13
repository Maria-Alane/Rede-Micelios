package com.example.micelios.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.MessageRepository
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentChatBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var fragmentChatBinding: FragmentChatBinding? = null
    private val binding get() = fragmentChatBinding!!

    private lateinit var viewModel: ChatViewModel

    private var hyphaId: Long = 1L
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hyphaId = it.getLong("hyphaId", 1L)
        }
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

        val database = MiceliosDatabase.getDatabase(requireContext())
        val repository = MessageRepository(database.messageDao())
        val userRepository = UserRepository(database.userDao())
        viewModel = ChatViewModel(repository)

        viewLifecycleOwner.lifecycleScope.launch {
            userRepository.getUser().collectLatest { user ->
                userName = user?.name
            }
        }

        binding.buttonSendMessage.setOnClickListener {
            val text = binding.editTextMessage.text.toString()

            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Digite uma mensagem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userName.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Salve seu perfil primeiro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.sendMessage(hyphaId, userName!!, text)
            binding.editTextMessage.text?.clear()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                val messageText = messages.joinToString("\n") { "${it.senderName}: ${it.content}" }
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