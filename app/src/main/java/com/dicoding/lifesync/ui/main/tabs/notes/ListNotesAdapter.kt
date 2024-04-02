package com.dicoding.lifesync.ui.main.tabs.notes

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.lifesync.data.local.entity.NoteEntity
import com.dicoding.lifesync.databinding.ItemNotesBinding

class ListNotesAdapter : ListAdapter<NoteEntity, ListNotesAdapter.NoteViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListNotesAdapter.NoteViewHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNotesAdapter.NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(getItem(holder.adapterPosition))
        }
    }

    class NoteViewHolder(val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note : NoteEntity){
            binding.tvNoteTitle.text = note.title
            binding.tvNoteDesc.text = note.description

            Glide.with(binding.root.context)
                .load(note.image)
                .into(binding.ivNoteImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.noteId == newItem.noteId
            }
            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: NoteEntity)
    }
}