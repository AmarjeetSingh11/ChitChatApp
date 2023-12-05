package com.example.chitchat

import android.R.attr.button
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chitchat.adapter.MessageAdapter
import com.example.chitchat.databinding.ActivityChatBinding
import com.example.chitchat.ui.CallingScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.ValueEventListener


import java.util.Date


class ChatActivity : AppCompatActivity() {

    //binding ko declare kiya hai iss ma
    lateinit var binding: ActivityChatBinding

    //ab message ko firebase ma store kara ga isliya varible banya ga instance(database)
    lateinit var database: FirebaseDatabase


    //message jo send kar rha hai uski userid stroe karni hai isliya variable banya ga
    lateinit var senderUId: String

    //ab jo message receive kar rha hai uska variable banya ga
    lateinit var receiverUId: String

    //ab kya kara ga dono sender or receiver ko merge kar daga ek variable mai jiss sa ek variable ma store ho jaya
    lateinit var SenderRoom: String
    lateinit var ReceiverRoom: String

    //list define kara ga
    lateinit var list: ArrayList<MessageModel>

    lateinit var id: String
    //firebase authentication
//
//    private lateinit var auth: FirebaseAuth


//    private lateinit var userStatusRef: DatabaseReference
//    private lateinit var userOnlineStatus: TextView
//    private lateinit var auth: FirebaseAuth
//    private lateinit var userStatusListener: ValueEventListener

//    private lateinit var videobtn: ZegoSendCallInvitationButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding ko assign kiya hai issma
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sender ki UID receive kara ga
        senderUId = FirebaseAuth.getInstance().uid.toString()
        //receiver ki UID ko receive kara ga
//        receiverUId = intent.getStringExtra("uid")!!

        receiverUId = intent.getStringExtra("uid") ?: run {
            // Handle the case where "uid" is null
            Toast.makeText(this, "Error: UID is null", Toast.LENGTH_SHORT).show()
            finish() // Finish the activity if necessary
            return@run ""
        }

        //list ko intialize kardiya hai
        list = ArrayList()


        val name = intent.getStringExtra("name")!!
        val image = intent.getStringExtra("image")!!

        binding.userNameTextView.text = name
        Glide.with(applicationContext).load(image).into(binding.userImageView)


        //iss ma dono sender and receiver ko message dekha isliya ya likha hai
        SenderRoom = senderUId + receiverUId

        ReceiverRoom = receiverUId + senderUId


        //database wala variable ko initialize kara ga
        database = FirebaseDatabase.getInstance()


//        auth = FirebaseAuth.getInstance()
//        auth = FirebaseAuth.getInstance()


        //ya ham action perform kara ga ki send button pa click hoga toh kaya karna hai

        binding.sendMessage.setOnClickListener {

            //ab check kara ga user na koi message likha hai ya nai
            if (binding.MessageBox.text.isEmpty()) {
                //empty hai toh toast dekha daga

                Toast.makeText(this, "Pleas Write Your Message", Toast.LENGTH_SHORT).show()
            } else {

                //agar message likha hai messageBox ma toh usko Firebase Database Ma Store Kar aga


                //iss ma kya ho rha hai messagemodel data class ma data bhej rha hai
                val message =
                    MessageModel(binding.MessageBox.text.toString(), senderUId, Date().time)

                //iss ma ham kya kara ga randomkey generate kar ka waha message ko store kardaga
                val randomKey = database.reference.push().key

                //ab ham database ma node banaya ga jaha sari chats store hogi
                database.reference.child("Chats")
                    .child(SenderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {

                        //jab sender ka room ma data store ho jaya ga tab ham receiver ka room ma data ko store kara ga

                        database.reference.child("Chats")
                            .child(ReceiverRoom).child("message").child(randomKey!!)
                            .setValue(message).addOnSuccessListener {

                                //message jab send ho jaya ga toh messagebox ko null kar daga
                                binding.MessageBox.text = null

                                Toast.makeText(this, "Message Send", Toast.LENGTH_SHORT).show()
                            }
                    }

            }
        }

        //message show hoga recyclerview ma

        database.reference.child("Chats").child(SenderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    binding.recyclerView.adapter = MessageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error is $error", Toast.LENGTH_SHORT).show()
                }
            })


        //back button code
        val buttonback = findViewById<ImageView>(R.id.btnBack)
        buttonback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }








//
//        val username: String = receiverUId




    }

}



//        binding.btnCall.setOnClickListener {
//
//
//            if(username.isNotEmpty()){
//
//
//                val intent = Intent(this, CallingScreen::class.java)
//
//                // Put data into the Intent
//
//                intent.putExtra("key", username)
//
//// Start the second activity
//                startActivity(intent)
////                videocallsevice(username)
//                videocallsevice(username)
//            }
//
//
//
//        }



//    private fun videocallsevice(uid: String) {
//        val application = application
//        val appID: Long = 1806454537
//        val appSign = "d9f939803d3a269368dc5f2369d69a8afbbfc0e9f3ac1df172ae5a5f511fe86f"
//        val userID: String = uid
//        val userName: String = uid
//        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
//        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true
//        val notificationConfig = ZegoNotificationConfig()
//        notificationConfig.sound = "zego_uikit_sound_call"
//        notificationConfig.channelID = "CallInvitation"
//        notificationConfig.channelName = "CallInvitation"
//
//        try {
//            ZegoUIKitPrebuiltCallInvitationService.init(
//                application,
//                appID,
//                appSign,
//                userID,
//                userName,
//                callInvitationConfig
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


//    private fun videocallsevice(uid: String) {
//
//
//
////        val application = application
////        val appID: Long = 1806454537
////        val appSign = "d9f939803d3a269368dc5f2369d69a8afbbfc0e9f3ac1df172ae5a5f511fe86f"
////        val userID:String=uid
////        val userName:String=uid
////        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
////        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true
////        val notificationConfig = ZegoNotificationConfig()
////        notificationConfig.sound = "zego_uikit_sound_call"
////        notificationConfig.channelID = "CallInvitation"
////        notificationConfig.channelName = "CallInvitation"
////        ZegoUIKitPrebuiltCallInvitationService.init(
////            application,
////            appID,
////            appSign,
////            userID,
////            userName,
////            callInvitationConfig
////        )
//
//
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        ZegoUIKitPrebuiltCallInvitationService.unInit()
//    }







//        videobtn = findViewById(R.id.btnVideoCall)


//            videocallsevice(username)
//
//
//
//        videobtn.setOnClickListener(View.OnClickListener {
//            startvideocall(username)
//        })
//



//        val application: Application = application
//        val appID: Long = 578083363
//        val appSign:String = "eec78e0e75938be67a3d1b2967676a50c0b5881e4c76a95ee9bcc77718944f22"
//        val userID: String = uid
//        val userName: String = uid
//
//        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
//            notifyWhenAppRunningInBackgroundOrQuit = true
//        }
//
//        val notificationConfig = ZegoNotificationConfig().apply {
//            sound = "zego_uikit_sound_call"
//            channelID = "CallInvitation"
//            channelName = "CallInvitation"
//        }
//
//        ZegoUIKitPrebuiltCallInvitationService.init(
//            application,
//            appID,
//            appSign,
//            userID,
//            userName,
//            callInvitationConfig
//        )





//    private fun videocallsevice(uid: String) {
//
//
////        val appID: Long = 578083363
////        val appSign = "eec78e0e75938be67a3d1b2967676a50c0b5881e4c76a95ee9bcc77718944f22"
////        val application = application
////        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
////        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true
////        val notificationConfig = ZegoNotificationConfig()
////        notificationConfig.sound = "zego_uikit_sound_call"
////        notificationConfig.channelID = "CallInvitation"
////        notificationConfig.channelName = "CallInvitation"
////        ZegoUIKitPrebuiltCallInvitationService.init(
////            application,
////            appID,
////            appSign,
////            uid,
////            uid,
////            callInvitationConfig
////        )


//
//        val application: Application = application
//        val appID: Long = 578083363
//        val appSign: String = "eec78e0e75938be67a3d1b2967676a50c0b5881e4c76a95ee9bcc77718944f22"
//        val userID: String = uid
//        val userName: String = uid
//
//        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
//            notifyWhenAppRunningInBackgroundOrQuit = true
//        }
//
//        val notificationConfig = ZegoNotificationConfig().apply {
//            sound = "zego_uikit_sound_call"
//            channelID = "CallInvitation"
//            channelName = "CallInvitation"
//        }
//
//        ZegoUIKitPrebuiltCallInvitationService.init(
//            application,
//            appID,
//            appSign,
//            userID,
//            userName,
//            callInvitationConfig
//        )
//
//
//    }
//
//    private fun startvideocall(receiverId: String) {
//        videobtn.setIsVideoCall(true)
//        videobtn.resourceID = "zego_uikit_call"
//        videobtn.setInvitees(listOf(ZegoUIKitUser(receiverId)))
//    }






//
//    private fun getUserIdByUsername(username: String, callback: (String?) -> Unit) {
//        val usersRef: DatabaseReference = database.reference.child("users")
//        val query: Query = usersRef.orderByChild("name").equalTo(username)
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (userSnapshot in dataSnapshot.children) {
//                        val userId: String = userSnapshot.key.orEmpty()
//                        callback(userId)
//                        return
//                    }
//                }
//                callback(null)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                callback(null)
//            }
//        })
//    }




//        videobtn = findViewById(R.id.btnVideoCall)
//
//        // Call your method
//
//
//        val username: String = name
//
//        videocallsevice(username)
//
//        if(username.isNotEmpty()){
//
//            startvideocall(username)
//        }




//    private fun videocallsevice(username:String){
//        val appID:Long=578083363
//        val appSign="eec78e0e75938be67a3d1b2967676a50c0b5881e4c76a95ee9bcc77718944f22"
//        val application=application
//        val callInvitationConfig=ZegoUIKitPrebuiltCallInvitationConfig()
//        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit=true
//        val notificationConfig=ZegoNotificationConfig()
//        notificationConfig.sound="zego_uikit_sound_call"
//        notificationConfig.channelID="CallInvitation"
//        notificationConfig.channelName="CallInvitation"
//
//        ZegoUIKitPrebuiltCallInvitationService.init(application,appID,appSign,username,username,callInvitationConfig)
//
//
//    }
//
//    private fun startvideocall(receiverId:String){
//        videobtn.setIsVideoCall(true)
//        videobtn.resourceID="zego_uikit_call"
//        videobtn.setInvitees(listOf( ZegoUIKitUser(receiverId)))
//    }




//val appID: Long = 1806454537 // Replace with your Zego App ID
//val appSign = "d9f939803d3a269368dc5f2369d69a8afbbfc0e9f3ac1df172ae5a5f511fe86f" // Replace with your Zego App Sign Key
//
//ZegoExpressEngine.createEngine(
//appID,
//appSign,
//true, // Enable test environment
//ZegoScenario.GENERAL,
//application
//)
//
//
//
//
//binding.btnAudioCall.setOnClickListener {
//    val targetUserId = receiverUId // Replace with the UID of the user you want to call
//
//    // Start an audio call
//    initiateAudioCall(targetUserId)
//}

//

//// Set up Zego SDK
//val appID: Long = 1806454537
//val appSign: String = "d9f939803d3a269368dc5f2369d69a8afbbfc0e9f3ac1df172ae5a5f511fe86f"
//
//ZegoExpressEngine.createEngine(appID, appSign, true, ZegoScenario.GENERAL, application)

//
//binding.btnAudioCall.setOnClickListener {
//    // Get the user's UID whom you want to call
//    val targetUserId = receiverUId // Change this to the UID of the user you want to call
//
//    // Start an audio call
//    initiateAudioCall(targetUserId)
//}
//
//binding.btnVideoCall.setOnClickListener {
//    // Get the user's UID whom you want to call
//    val targetUserId = receiverUId // Change this to the UID of the user you want to call
//
//    // Start a video call
//    initiateVideoCall(targetUserId)
//}




//private fun initiateAudioCall(targetUserId: String) {
//    // Use Zego SDK to initiate an audio call
//    val user = ZegoExpressEngine.getUser(senderUId)
//    val config = ZegoAudioConfig()
//    ZegoExpressEngine.getEngine().startAudioCall(user, config)
//}
//
//private fun initiateVideoCall(targetUserId: String) {
//    // Use Zego SDK to initiate a video call
//    val user = ZegoExpressEngine.getEngine(senderUId)
//    val config = ZegoVideoConfig(ZegoVideoConfig.Preset.HD720P)
//    ZegoExpressEngine.getEngine().st(user, config)
//}
//
//// Add onDestroy to release Zego resources
//override fun onDestroy() {
//    ZegoExpressEngine.destroyEngine(null)
//    super.onDestroy()
//}












    //        startService(username)
//
//
//
//
//
//        audiocall(username)
//        videocall(username)
//
//    fun startService(username:String){
//        val application: Application = getApplication()// Android's application context
//        val appID: Long = 1806454537// yourAppID
//        val appSign: String = "d9f939803d3a269368dc5f2369d69a8afbbfc0e9f3ac1df172ae5a5f511fe86f"// yourAppSign
////        val userID: String = // yourUserID, userID should only contain numbers, English characters, and '_'.
//        val userName: String = username// yourUserName
//
//        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
//            notifyWhenAppRunningInBackgroundOrQuit = true
//        }
//
//        val notificationConfig = ZegoNotificationConfig().apply {
//            sound = "zego_uikit_sound_call"
//            channelID = "CallInvitation"
//            channelName = "CallInvitation"
//        }
//
//        ZegoUIKitPrebuiltCallInvitationService.init(application, appID, appSign, userName, callInvitationConfig.toString())
//    }
//    fun audiocall(username:String){
//        binding.btnAudioCall.setIsVideoCall(false)
//        binding.btnAudioCall.setResourceID("zego_uikit_call")
//        binding.btnAudioCall.setInvitees(listOf(ZegoUIKitUser(username)))
//    }
//    fun videocall(username:String){
//        binding.btnVideoCall.setIsVideoCall(true)
//        binding.btnVideoCall.setResourceID("zego_uikit_call")
//        binding.btnVideoCall.setInvitees(listOf(ZegoUIKitUser(username)))
//    }



