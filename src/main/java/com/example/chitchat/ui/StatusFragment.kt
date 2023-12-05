package com.example.chitchat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chitchat.R
import com.example.chitchat.Status
import com.example.chitchat.adapter.StatusAdapter
import com.example.chitchat.databinding.FragmentStatusBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [StatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


// StatusFragment.kt

class StatusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private val statusList = mutableListOf<Status>()
    private lateinit var statusAdapter: StatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and StatusAdapter
        statusAdapter = StatusAdapter(statusList)
        binding.recyclerViewStatus.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewStatus.adapter = statusAdapter

        // Firebase Database reference
        val database = FirebaseDatabase.getInstance()
        val statusReference = database.getReference("statuses")

        // Read statuses from Firebase
        statusReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                statusList.clear()
                for (statusSnapshot in snapshot.children) {
                    val status = statusSnapshot.getValue(Status::class.java)
                    status?.let {
                        val userId = it.userId
                        val userName = it.userName ?: "Default Name"
                        val newStatus = Status(userId, userName, it.text)
                        statusList.add(newStatus)
                    }
                }
                statusAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("StatusFragment", "Error reading statuses from Firebase", error.toException())
            }
        })

        binding.buttonUploadStatus.setOnClickListener {
            uploadStatus()
        }
    }

    private fun uploadStatus() {
        val statusText = binding.editTextStatus.text.toString()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (statusText.isNotEmpty()) {
            val userId = currentUser?.uid
            if (userId != null) {
                getUsernameFromFirebase(userId) { displayName ->
                    if (!displayName.isNullOrBlank()) {
                        val newStatus = Status(userId, displayName, statusText)
                        val statusReference =
                            FirebaseDatabase.getInstance().getReference("statuses")
                        statusReference.push().setValue(newStatus)

                        // Optionally, clear the EditText after uploading
                        binding.editTextStatus.text.clear()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Display name not set. Please update your profile.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "User not authenticated. Please log in.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "Status text cannot be empty", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getUsernameFromFirebase(uid: String, callback: (String?) -> Unit) {
        val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val displayName = snapshot.child("name").getValue(String::class.java)
                callback.invoke(displayName)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("StatusFragment", "Error reading user data from Firebase", error.toException())
                callback.invoke(null)
            }
        })
    }

}
