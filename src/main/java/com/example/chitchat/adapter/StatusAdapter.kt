package com.example.chitchat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.R
import com.example.chitchat.Status

// StatusAdapter.kt

class StatusAdapter(private val statusList: List<Status>) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameText: TextView = itemView.findViewById(R.id.textViewUserName)
        val statusText: TextView = itemView.findViewById(R.id.textViewStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val currentStatus = statusList[position]
        holder.userNameText.text = currentStatus.userName
        holder.statusText.text = currentStatus.text
    }

    override fun getItemCount(): Int {
        return statusList.size
    }
}

