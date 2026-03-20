package com.example.micelios.presentation.moments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.databinding.FragmentMomentListBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MomentListFragment : Fragment() {

    private var _binding: FragmentMomentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MomentListViewModel
    private lateinit var hyphaSelectorAdapter: PostHyphaSelectorAdapter

    private var selectedHyphaId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = MiceliosDatabase.getDatabase(requireContext())
        val momentRepository = MomentRepository(
            momentDao = database.momentDao(),
            hyphaDao = database.hyphaDao()
        )
        val hyphaRepository = HyphaRepository(database.hyphaDao())
        val sessionManager = SessionManager(requireContext().applicationContext)

        viewModel = MomentListViewModel(
            momentRepository = momentRepository,
            hyphaRepository = hyphaRepository,
            sessionManager = sessionManager
        )

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        hyphaSelectorAdapter = PostHyphaSelectorAdapter { hyphaId ->
            selectedHyphaId = hyphaId
        }

        binding.recyclerHyphas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHyphas.adapter = hyphaSelectorAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collectLatest { hyphas ->
                if (hyphas.isEmpty()) {
                    selectedHyphaId = null
                }

                hyphaSelectorAdapter.submitList(hyphas)
                binding.textViewEmptyHyphas.visibility =
                    if (hyphas.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.buttonPostMoment.setOnClickListener {
            val text = binding.editTextMoment.text.toString().trim()
            val hyphaId = selectedHyphaId

            if (hyphaId == null) {
                Toast.makeText(requireContext(), "Escolha uma hypha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Digite um momento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createMoment(hyphaId, text)
            binding.editTextMoment.text?.clear()
            Toast.makeText(requireContext(), "Momento publicado", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        viewModel.loadUserHyphas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}