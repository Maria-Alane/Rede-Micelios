package com.example.micelios.presentation.hypha

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micelios.R
import com.example.micelios.data.local.database.MiceliosDatabase
import com.example.micelios.data.repository.HyphaRepository
import com.example.micelios.data.repository.UserRepository
import com.example.micelios.databinding.FragmentHyphaListBinding
import com.example.micelios.presentation.common.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HyphaListFragment : Fragment() {

    private var _binding: FragmentHyphaListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HyphaListViewModel
    private lateinit var hyphaListAdapter: HyphaListAdapter

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

        hyphaListAdapter = HyphaListAdapter { hyphaId ->
            val bundle = Bundle().apply {
                putLong("hyphaId", hyphaId)
            }
            findNavController().navigate(
                R.id.action_hyphaListFragment_to_hyphaDetailFragment,
                bundle
            )
        }

        binding.recyclerHyphas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHyphas.adapter = hyphaListAdapter

        binding.fabCreateHypha.setOnClickListener {
            findNavController().navigate(R.id.createHyphaFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hyphas.collectLatest { hyphas ->
                binding.textViewEmptyHyphas.visibility =
                    if (hyphas.isEmpty()) View.VISIBLE else View.GONE

                hyphaListAdapter.submitList(hyphas)
            }
        }

        viewModel.loadHyphas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}