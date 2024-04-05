package com.example.cyber.ui.post_edits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cyber.databinding.PostItemLayoutBinding

class PostListAdapter(
    private val onItemActionListener: OnItemActionListener? = null,
    private val doesButtonsVisible: Boolean
) :
    ListAdapter<PostModelEntity, PostListAdapter.ViewHolder>(DiffUtilsCallback) {

    inner class ViewHolder(private val binding: PostItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostModelEntity) {
            binding.btnDelete.isVisible = doesButtonsVisible
            binding.btnUpdate.isVisible = doesButtonsVisible
            binding.title.text = item.title
            binding.desc.text = item.description

            binding.btnDelete.setOnClickListener {
                onItemActionListener?.onDeleteItem(item)

            }
            binding.btnUpdate.setOnClickListener {
                onItemActionListener?.onEditItem(item)
            }

            if (!doesButtonsVisible) {
                binding.root.setOnClickListener {
                    onItemActionListener?.openPostDetails(item)
                }
            }
        }
    }

    object DiffUtilsCallback : DiffUtil.ItemCallback<PostModelEntity>() {
        override fun areItemsTheSame(oldItem: PostModelEntity, newItem: PostModelEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PostModelEntity, newItem: PostModelEntity) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PostItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


data class PostModelEntity(
    val id: Long? = null,
    val title: String,
    val description: String
)