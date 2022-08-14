package com.example.digidentitytestapp.presentation.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.digidentitytestapp.R
import com.example.digidentitytestapp.databinding.FragmentDetailsBinding
import com.example.digidentitytestapp.domain.entity.Item
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getParcelable<Item>(ARG_ITEM)?.let { viewModel.init(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiItem.collectLatest(::updateState)
            }
        }
    }

    private fun updateState(state: DetailsUIState) {
        state.item?.let { item ->
            with(binding) {
                Picasso
                    .get()
                    .load(item.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageViewPhoto)

                textViewId.text = root.context.getString(R.string.main_item_id, item.id)
                textViewConfidence.text =
                    root.context.getString(
                        R.string.main_item_confidence,
                        item.confidence
                    )
                textViewText.text = item.text
            }
        }
    }

    companion object {
        private const val ARG_ITEM = "item"

        fun newInstance(item: Item): DetailsFragment {
            return DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ITEM, item)
                }
            }
        }
    }
}