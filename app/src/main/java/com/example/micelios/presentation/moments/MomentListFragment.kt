package com.example.micelios.presentation.moments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.MomentRepository
import com.example.micelios.databinding.FragmentMomentListBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MomentListFragment : Fragment() {

    private var fragmentMomentListBinding: FragmentMomentListBinding? = null
    private val binding get() = fragmentMomentListBinding!!

    private lateinit var viewModel: MomentListViewModel

    private var selectedHyphaId: Long? = null
    private var hyphaIds = emptyList<Long>()

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collectLatest { hyphas ->
                if (hyphas.isEmpty()) {
                    binding.textViewMomentList.text = "Você ainda não participa de nenhuma hypha."
                    binding.autoCompleteHypha.setText("")
                    binding.autoCompleteHypha.isEnabled = false
                    selectedHyphaId = null
                } else {
                    binding.textViewMomentList.text = "Escolha a hypha e publique seu momento."
                    binding.autoCompleteHypha.isEnabled = true

                    hyphaIds = hyphas.map { it.id }
                    val hyphaNames = hyphas.map { it.name }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        hyphaNames
                    )
                    binding.autoCompleteHypha.setAdapter(adapter)

                    binding.autoCompleteHypha.setOnItemClickListener { _, _, position, _ ->
                        selectedHyphaId = hyphaIds[position]
                    }
                }
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
        }

        viewModel.loadUserHyphas()
    }

    override fun onDestroyView() {
        fragmentMomentListBinding = null
        super.onDestroyView()
    }
}