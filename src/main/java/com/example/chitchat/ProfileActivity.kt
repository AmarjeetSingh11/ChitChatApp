package com.example.chitchat

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chitchat.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {

   lateinit var binding:ActivityProfileBinding

   //firebase ka authentication ka variable banaya ga
   lateinit var auth:FirebaseAuth

   //Firebase database ka variable banya ga
   lateinit var database: FirebaseDatabase

   //firebase storage ka variable banya ga
    lateinit var storage:FirebaseStorage

    //jo image select kara ga usko select kara ga uska url ko copy kara ga
    lateinit var SelectedImage:Uri

    //dialog banya ga
    lateinit var dialog:AlertDialog.Builder



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dialog ko initialize kiya hai
        dialog=AlertDialog.Builder(this)
            .setMessage("Updating Profile..")
            .setCancelable(false)



        //database ko initialize kara ga
        database=FirebaseDatabase.getInstance()

        //storage of firebase ko initialize kara ga
        storage=FirebaseStorage.getInstance()

        // Firebase Authentication ko initialize kara ga ab
        auth=FirebaseAuth.getInstance()

        //image ko initialize

        //jab image ko select kara ga tab ka code
        binding.userImage.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"

            startActivityForResult(intent,1)
        }
        //jab continue btn pa click hoga toh kya karna hai wow likha ga issma
        binding.btnNext.setOnClickListener {
            //username enter kiya ya nai wow check kar rha hai
            if(binding.edUSerName.text!!.isEmpty()){
                Toast.makeText(this,"Pleas Enter the User Name",Toast.LENGTH_SHORT).show()

                //image enter ki hai ya nai wow check kar rha hai
            }else if(SelectedImage == null){
                Toast.makeText(this,"Pleas Select Your Image",Toast.LENGTH_SHORT).show()
            }else uploadData()
        }
    }

    private fun uploadData(){
        //issma ham data ko upload karaga
        val reference=storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(SelectedImage).addOnCompleteListener{
            if(it.isSuccessful){
                //agar image successful upload hua to real database ma wow image ko get kara ga
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
      val user=UserModel(auth.uid.toString(), binding.edUSerName.text.toString(),auth.currentUser!!.phoneNumber.toString(),imgUrl)
        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Data Inserted Successfully",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){

            if(data.data != null){

                SelectedImage=data.data!!

                binding.userImage.setImageURI(SelectedImage)
            }
        }
    }
}