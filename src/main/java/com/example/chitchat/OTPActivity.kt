package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chitchat.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpactivityBinding
    lateinit var auth: FirebaseAuth
    lateinit var verificationId:String

    //alert dialog box banaya ga iss ma

    lateinit var dialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()



        //alert dialog box show hoga user ko

        val builder=AlertDialog.Builder(this)
        builder.setMessage("Pleas Wait...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog=builder.create()
        dialog.show()


        //phone number ko get kara ga iss

        val phoneNumber="+91"+intent.getStringExtra("number")


        //ab firebase authentication daga ab
        val option=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            //iss ma 60 second tak otp valid rha ga

            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)

            .setCallbacks(object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    TODO("Not yet implemented")
                }

                override fun onVerificationFailed(p0: FirebaseException) {

                    //agar verification failed hua toh toast show kar daga

                    dialog.dismiss()
                   Toast.makeText(this@OTPActivity,
                           "${p0}\n",Toast.LENGTH_LONG).show()

                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationId=p0
                }

            }).build()


        //ab otp send ka code hai yaa


        //ab jo bhi otp hoga wow user ko chala jaya ga

        PhoneAuthProvider.verifyPhoneNumber(option)


        //jab continue button pa click kara ga toh yaa hoga

        binding.btnLogin.setOnClickListener {


            //agar otp dala nai hai toh toast show kara ga

            if(binding.edOtp.text!!.isEmpty()){

                Toast.makeText(this,"Pleas Enter the OTP ",Toast.LENGTH_SHORT).show()
            }else{
                dialog.show()


                //PhoneAuthProvider.getCredential(verificationId, binding.edOtp.text.toString()):
                // This part creates a credential object using
                // the verification ID (received during the phone number verification process)
                // and the OTP that the user entered in the edOtp field.


                val credential=PhoneAuthProvider.getCredential(verificationId,binding.edOtp.text.toString())

                //auth.signInWithCredential(credential): This part uses the
                // Firebase authentication system (auth) to sign in the user
                // using the provided credential. If the credential is valid,
                // it lets the user access the app's features and content.

                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful){

                            //if successful then dialog show kara ga
                            dialog.dismiss()

                            //profile acttivity ma bhejdaga
                            
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()

                        }else{
                            Toast.makeText(this,"Error ${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }



    }
}