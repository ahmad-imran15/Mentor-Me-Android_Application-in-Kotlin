package com.muhammadahmad.i210790

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas

class callingscreen : AppCompatActivity() {
    private val PERMISSION_ID = 12
    private val app_id = "eab0fe4108d748178d3cbf4d59fc9745"
    private val channelName = "channel"
    private val token = "007eJxTYNjwLmvPAaX5XqnMPzV+2HL4+LAkCx48ESOmfsrFeXXlIVcFhtTEJIO0VBNDA4sUcxMLQ3OLFOPkpDSTFFPLtGRLcxPT40/50xoCGRmUBFIYGRkgEMRnZ0jOSMzLS81hYAAANAMehQ=="
    private var uid = 0
    var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private var localSurfaceView: SurfaceView? = null
    private var remoteSurfaceView: SurfaceView? = null
    private val REQUESTED_PERMISSION = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.CAMERA
    )
    private fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this, REQUESTED_PERMISSION[0]
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this, REQUESTED_PERMISSION[1]
                ) != PackageManager.PERMISSION_GRANTED)
    }

    private fun setupVideoSdkEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = app_id
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine!!.enableVideo()
        } catch (e: Exception) {
        }
    }

    private var startTime: Long = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callingscreen)

        if (!checkSelfPermission()) {
            ActivityCompat
                .requestPermissions(
                    this, REQUESTED_PERMISSION, PERMISSION_ID
                )
        }
        setupVideoSdkEngine()
        joinCall()

        // Start timer
        startTime = SystemClock.elapsedRealtime()
        startTimer()

        val back1 = findViewById<TextView>(R.id.joinbutton)
        back1.setOnClickListener {
            finish() // This will close the current activity and go back to the previous one
        }
    }

    private fun leaveCall() {
        if (!isJoined) {
        } else {
            agoraEngine?.leaveChannel()
            if (remoteSurfaceView != null) remoteSurfaceView!!.visibility = View.GONE
            if (localSurfaceView != null) localSurfaceView!!.visibility = View.GONE

            isJoined = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        agoraEngine!!.stopPreview()
        agoraEngine!!.leaveChannel()

        Thread {
            RtcEngine.destroy()
            agoraEngine = null
        }.start()

        // Stop timer
        stopTimer()
    }

    private fun joinCall() {
        if (checkSelfPermission()) {
            val option = ChannelMediaOptions()
            option.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION

            option.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
            setupLocalVideo()
            localSurfaceView!!.visibility = View.VISIBLE
            agoraEngine!!.startPreview()
            agoraEngine!!.joinChannel(token, channelName, uid, option)
        }
    }

    private val mRtcEventHandler: IRtcEngineEventHandler =
        object : IRtcEngineEventHandler() {
            override fun onUserJoined(uid: Int, elapsed: Int) {
                runOnUiThread { setupRemoteVideo(uid) }
            }

            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                isJoined = true
            }

            override fun onUserOffline(uid: Int, reason: Int) {
                runOnUiThread {
                    remoteSurfaceView!!.visibility = View.GONE
                }
            }
        }

    private fun setupRemoteVideo(uid: Int) {
        remoteSurfaceView = SurfaceView(baseContext)
        remoteSurfaceView!!.setZOrderMediaOverlay(false)
        val remoteView = findViewById<FrameLayout>(R.id.remote_user)
        remoteView.addView(remoteSurfaceView)

        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
    }

    private fun setupLocalVideo() {
        localSurfaceView = SurfaceView(baseContext)
        localSurfaceView!!.setZOrderMediaOverlay(true)
        val localView = findViewById<FrameLayout>(R.id.local_user)
        localView.addView(localSurfaceView)

        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                0
            )
        )
    }

    private fun startTimer() {
        handler.postDelayed(timerRunnable, 1000)
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = SystemClock.elapsedRealtime() - startTime
            val seconds = (elapsedTime / 1000) % 60
            val minutes = (elapsedTime / 1000) / 60
            val timeString = String.format("%02d:%02d", minutes, seconds)
            findViewById<TextView>(R.id.textView3).text = timeString
            handler.postDelayed(this, 1000)
        }

    }

    fun backbookappointment(view: View) {
        val userNam = intent.getStringExtra("name")
        leaveCall()

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, slide15chatscreen::class.java).apply {
            putExtra("name", userNam)
        }

        startActivity(intent)
        finish()
    }
}

