package com.example.chitchat.ui


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chitchat.R
import com.example.chitchat.databinding.ActivityCallingScreenBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


class CallingScreen : AppCompatActivity() {

    private lateinit var binding:ActivityCallingScreenBinding

    private val appID="c75e256f977c492fbb4d7032fe2c7bc1"
    private val channelName="ChitChat"
    private val token="007eJxTYFCplHcr05hZpVp4gyn+1Z53n2dYH+77v/Y1Y9h3buUvD+oUGJLNTVONTM3SLM3Nk00sjdKSkkxSzA2MjdJSjZLNk5INhY6npTYEMjKscnrExMgAgSA+B4NzRmaJc0ZiCQMDAAvxIfo="
    private val uid=0


    private var isJoined=false
    private var agroEngine: RtcEngine? = null
    private var localSurfaceView: SurfaceView?=null
    private var remoteSurfaceView: SurfaceView?=null



    private val PERMISSION_ID=12
    private val REQUESTED_PERMISSION=
        arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
        )

    private fun checkSelfPermission():Boolean{
        return !(ContextCompat.checkSelfPermission(
            this,REQUESTED_PERMISSION[0]
        )!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    REQUESTED_PERMISSION[1]
                )!= PackageManager.PERMISSION_GRANTED)
    }

    private fun showMessage(message:String){
        runOnUiThread{
            Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun setUpVideoSdkEngine(){

        try {


            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appID

            config.mEventHandler = mIRtcEngineEventHandler
            agroEngine = RtcEngine.create(config)
            agroEngine!!.enableVideo()
        }catch (e:Exception){
            showMessage(e.message.toString())
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityCallingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if(!checkSelfPermission()){
            ActivityCompat.requestPermissions(
                this,REQUESTED_PERMISSION,PERMISSION_ID
            )

        }

        setUpVideoSdkEngine()

        binding.joinButton.setOnClickListener {
            joinCall()
        }

        binding.leaveButton.setOnClickListener {
            leaveCall()
        }



//        binding.callingBackbtn.setOnClickListener {
//            val intent=Intent(this,CallFragment::class.java)
//            startActivity(intent)
//        }



    }



    override fun onDestroy() {
        super.onDestroy()
        agroEngine!!.stopPreview()
        agroEngine!!.leaveChannel()

        Thread {
            RtcEngine.destroy()
            agroEngine=null
        }.start()
    }


    private fun leaveCall() {

        if(!isJoined){
            showMessage("Join a channel first")
        }else{

            agroEngine!!.leaveChannel()
            showMessage("You left the Channel")
            if(remoteSurfaceView!=null) remoteSurfaceView!!.visibility= View.GONE
            if(localSurfaceView!=null) localSurfaceView!!.visibility= View.GONE

            isJoined=false

        }
    }

    private fun joinCall() {

        if(checkSelfPermission()){
            val option= ChannelMediaOptions()
            option.channelProfile= Constants.CHANNEL_PROFILE_COMMUNICATION
            option.clientRoleType= Constants.CLIENT_ROLE_BROADCASTER

            setupLocalVideo()
            localSurfaceView!!.visibility= View.VISIBLE
            agroEngine!!.startPreview()
            agroEngine!!.joinChannel(token,channelName,uid,option)
        }else{
            Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT).show()
        }
    }

    private val mIRtcEngineEventHandler: IRtcEngineEventHandler =
        object : IRtcEngineEventHandler(){

            override fun onUserJoined(uid: Int, elapsed: Int) {
                showMessage("Remote User joined $uid")

                runOnUiThread {
                    setupRemoteVideo(uid)
                }
            }

            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                isJoined=true
                showMessage("Joined channel $channel")
            }

            override fun onUserOffline(uid: Int, reason: Int) {

                showMessage("User Offline")

                runOnUiThread {
                    remoteSurfaceView!!.visibility = View.GONE
                }

            }


        }

    private fun setupRemoteVideo(uid:Int){
        remoteSurfaceView= SurfaceView(baseContext)
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        binding.remoteUser.addView(remoteSurfaceView)

        agroEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
    }

    private fun setupLocalVideo(){
        localSurfaceView= SurfaceView(baseContext)
        binding.localUser.addView(localSurfaceView)

        agroEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                0
            )
        )
    }

}