package com.example.chitchat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.chitchat.ChatActivity
import com.example.chitchat.R
import com.example.chitchat.UserModel
import com.example.chitchat.databinding.ChatUserLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class ChatAdapter(var context :Context,var list:ArrayList<UserModel>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {



    inner class ChatViewHolder(view: View) : ViewHolder(view) {

        var binding: ChatUserLayoutBinding = ChatUserLayoutBinding.bind(view)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_user_layout, parent, false)
        )


    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val user = list[position]
        Glide.with(context).load(user.image).into(holder.binding.UserImageProfile)
        holder.binding.UserName.text = user.name

        //fetch the recent message from the user
        // Fetch the recent chat message for this user
//        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
//        val otherUserUid = user.uid
//
//        val databaseReference = FirebaseDatabase.getInstance().getReference("messages")
//        if (currentUserUid != null) {
//            if (otherUserUid != null) {
//                databaseReference.child(currentUserUid).child(otherUserUid)
//                    .orderByChild("timestamp")
//                    .limitToLast(1)
//                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.exists()) {
//                                for (childSnapshot in snapshot.children) {
//                                    val message = childSnapshot.child("text").value.toString()
//                                    holder.binding.UserMessage.text = message
//                                }
//                            } else {
//                                holder.binding.UserMessage.text = "No messages yet"
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            // Handle error
//                        }
//                    })
//            }
//        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("uid", user.uid)
            intent.putExtra("name",user.name)
            intent.putExtra("image",user.image)
            context.startActivity(intent)
        }







    }
}