package com.example.digidentitytestapp.presentation.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digidentitytestapp.R
import com.example.digidentitytestapp.databinding.FragmentMainBinding
import com.example.digidentitytestapp.domain.entity.Item
import com.example.digidentitytestapp.presentation.screen.details.DetailsFragment
import com.example.digidentitytestapp.presentation.screen.main.adapter.MainAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefreshLayout.setOnRefreshListener(viewModel::onSwipeRefresh)

        initRecyclerView()

        observe()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest(::renderUIState)
            }
        }
    }

    private fun renderUIState(state: MainScreenState) {
        (binding.recyclerView.adapter as? MainAdapter)?.submitList(state.cells)

        binding.swipeRefreshLayout.isRefreshing = state.isSwipeRefreshing

        state.errorMessage?.let { message ->
            Snackbar
                .make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.main_retry) { viewModel.onRetry() }
                .show()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = MainAdapter(::onItemClick)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.onRecyclerViewScrolled(
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    )
                }
            })
        }
    }

    private fun onItemClick(item: Item) {
        // TODO escape null requireActivity() or requireFragmentManager()
        with(activity) {
            this?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container_view, DetailsFragment.newInstance(item))
                ?.addToBackStack(DetailsFragment::class.java.simpleName)
                ?.commit()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}