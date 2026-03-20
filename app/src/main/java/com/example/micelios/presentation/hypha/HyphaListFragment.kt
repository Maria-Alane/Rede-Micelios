package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.micelios.R
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MemberDraft
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentHyphaListBinding
import com.example.micelios.domain.model.Hypha
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collectLatest { hyphas ->
                renderHyphaList(hyphas)
            }
        }

        viewModel.loadHyphas()
    }

    private fun renderHyphaList(hyphas: List<Hypha>) {
        if (hyphas.isEmpty()) {
            binding.textViewHyphaList.text = "Nenhuma hypha criada ainda."
            return
        }

        val fullText = buildString {
            hyphas.forEachIndexed { index, hypha ->
                append(hypha.name)
                append("\n")
                append(hypha.type.name)
                if (hypha.description.isNotBlank()) {
                    append(" • ")
                    append(hypha.description)
                }
                if (index != hyphas.lastIndex) {
                    append("\n\n")
                }
            }
        }

        val spannable = SpannableString(fullText)
        var start = 0

        hyphas.forEach { hypha ->
            val blockText = buildString {
                append(hypha.name)
                append("\n")
                append(hypha.type.name)
                if (hypha.description.isNotBlank()) {
                    append(" • ")
                    append(hypha.description)
                }
            }

            val end = start + blockText.length

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val bundle = Bundle().apply {
                        putLong("hyphaId", hypha.id)
                    }
                    findNavController().navigate(
                        R.id.action_hyphaListFragment_to_hyphaDetailFragment,
                        bundle
                    )
                }
            }

            spannable.setSpan(
                clickableSpan,
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            start = end + 2
        }

        binding.textViewHyphaList.text = spannable
        binding.textViewHyphaList.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}