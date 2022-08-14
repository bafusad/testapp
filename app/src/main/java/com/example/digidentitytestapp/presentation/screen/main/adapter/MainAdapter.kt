package com.example.digidentitytestapp.presentation.screen.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digidentitytestapp.R
import com.example.digidentitytestapp.databinding.RecyclerItemLoadingBinding
import com.example.digidentitytestapp.databinding.RecyclerItemMainBinding
import com.example.digidentitytestapp.domain.entity.Item
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException

class MainAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<MainCell, RecyclerView.ViewHolder>(MainDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_ITEM -> ItemViewHolder(
                RecyclerItemMainBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            VIEW_TYPE_LOADING -> LoadingViewHolder(
                RecyclerItemLoadingBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("No such view type $viewType!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cell = getItem(position)
        if (cell is MainCell.ListItem) {
            (holder as ItemViewHolder).bind(cell.item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is MainCell.ListItem -> VIEW_TYPE_ITEM
            MainCell.Loading -> VIEW_TYPE_LOADING
        }
    }

    inner class ItemViewHolder(private val binding: RecyclerItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            with(binding) {
                Picasso.get()
                    .load(item.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageViewPhoto)

                textViewId.text = root.context.getString(R.string.main_item_id, item.id)
                textViewConfidence.text =
                    root.context.getString(R.string.main_item_confidence, item.confidence.toString())
                textViewText.text = item.text

                root.setOnClickListener { onItemClick(item) }
            }
        }

    }

    inner class LoadingViewHolder(binding: RecyclerItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_LOADING = 2
    }
}