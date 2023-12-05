package com.example.chitchat

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatApp : Application() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate() {
        super.onCreate()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userStatusRef = database.reference.child("user_status").child(currentUser.uid)
            userStatusRef.setValue("online")

            userStatusRef.onDisconnect().setValue("offline")
        }
    }
}
