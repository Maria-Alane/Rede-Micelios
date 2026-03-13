package com.example.micelios.presentation.moments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentMomentListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MomentListFragment : Fragment() {

    private var fragmentMomentListBinding: FragmentMomentListBinding? = null
    private val binding get() = fragmentMomentListBinding!!

    private lateinit var viewModel: MomentListViewModel

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
        fragmentMomentListBinding = FragmentMomentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = MiceliosDatabase.getDatabase(requireContext())
        val repository = MomentRepository(database.momentDao())
        val userRepository = UserRepository(database.userDao())
        viewModel = MomentListViewModel(repository)

        viewModel.loadMoments(hyphaId)

        viewLifecycleOwner.lifecycleScope.launch {
            userRepository.getUser().collectLatest { user ->
                userName = user?.name
            }
        }

        binding.buttonPostMoment.setOnClickListener {
            val text = binding.editTextMoment.text.toString()

            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Digite um momento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userName.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Salve seu perfil primeiro", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createMoment(hyphaId, userName!!, text)
            binding.editTextMoment.text?.clear()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moments.collectLatest { moments ->
                val textList = moments.joinToString("\n") { "${it.creatorName}: ${it.content}" }
                binding.textViewMomentList.text = textList
            }
        }
    }

    override fun onDestroyView() {
        fragmentMomentListBinding = null
        super.onDestroyView()
    }
}