package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.chitchat.adapter.ViewPagerAdapter
import com.example.chitchat.databinding.ActivityMainBinding
import com.example.chitchat.ui.CallFragment
import com.example.chitchat.ui.ChatFragment
import com.example.chitchat.ui.StatusFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //in this fragment we will pass that will show in one screen like chats,status,call ets

        //fragments ki array list banyaga iss ma ham
        val fragmentArrayList=ArrayList<Fragment>()


        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        //authentication ko intialize kardiya hai use kara ga osa
        auth=FirebaseAuth.getInstance()

        //jab user login nai hai toh usko phone number wali activity ma bhej daga

        if(auth.currentUser == null){
            startActivity(Intent(this,NumberActivity::class.java))
            finish()
        }



        //ab adapter ko initialize kara ga
        val adapter=ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        //ab viewpage ko adapter ma set kara daga

        binding.viewPager.adapter=adapter
        binding.tabs.setupWithViewPager(binding.viewPager)



    }
}