package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chitchat.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class NumberActivity : AppCompatActivity() {

    lateinit var binding:ActivityNumberBinding
    lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth=FirebaseAuth.getInstance()

        //koi login hai ya nai ya dekha ga ham

        if(auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


        binding.btnContinue.setOnClickListener {
            //iss ma ham check kara ga edit text empty to nai hana

            if (binding.edNumber.text!!.isEmpty()){
                Toast.makeText(this,"Pleas enter the Phone Number",Toast.LENGTH_SHORT).show()
            }else{

                //agar number enter hogya toh usko otp activity ma bhej daga

                val intent =Intent(this,OTPActivity::class.java)
                intent.putExtra("number",binding.edNumber.text.toString())
                startActivity(intent)
            }
        }

    }
}