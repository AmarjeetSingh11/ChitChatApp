package com.example.chitchat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chitchat.R
import com.example.chitchat.UserModel
import com.example.chitchat.adapter.ChatAdapter
import com.example.chitchat.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters


    lateinit var binding: FragmentChatBinding
    lateinit var database: FirebaseDatabase
    lateinit var userList:ArrayList<UserModel>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment



       binding=FragmentChatBinding.inflate(layoutInflater)

        database= FirebaseDatabase.getInstance()
        userList=ArrayList()




        database.reference.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for (snapshot1 in snapshot.children){
                        val user=snapshot1.getValue(UserModel::class.java)
                        if(user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }
                    binding.UserListRecyclerView.adapter=ChatAdapter(requireContext(),userList)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        return binding.root
    }

}