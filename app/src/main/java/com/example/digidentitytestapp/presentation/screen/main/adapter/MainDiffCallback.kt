package com.example.digidentitytestapp.presentation.screen.main.adapter

import androidx.recyclerview.widget.DiffUtil

object MainDiffCallback : DiffUtil.ItemCallback<MainCell>() {

    override fun areItemsTheSame(oldItem: MainCell, newItem: MainCell): Boolean {
        return if (oldItem is MainCell.ListItem && newItem is MainCell.ListItem) {
            return oldItem.item.id == newItem.item.id
        } else if (oldItem is MainCell.Loading && newItem is MainCell.Loading) {
            return true
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: MainCell, newItem: MainCell): Boolean {
        return if (oldItem is MainCell.ListItem && newItem is MainCell.ListItem) {
            return oldItem.item.id == newItem.item.id
                    && oldItem.item.text == newItem.item.text
                    && oldItem.item.confidence == newItem.item.confidence
                    && oldItem.item.image == newItem.item.image
        } else {
            true
        }
    }
}