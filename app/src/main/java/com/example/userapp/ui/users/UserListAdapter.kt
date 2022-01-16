package com.example.userapp.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.userapp.databinding.LayoutUserItemBinding
import com.example.userapp.model.User

class UserListAdapter(private val onItemClick: (User) -> Unit) :
    ListAdapter<User, UserListAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutUserItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: LayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding) {
                name.text = user.name
                email.text = user.email
                gender.text = user.gender
            }

            itemView.setOnLongClickListener {
                onItemClick(user)
                true
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}