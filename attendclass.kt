package com.muhammadahmad.i210790

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

class attendclass : AppCompatActivity() {
    private val PERMISSION_ID = 12
    private val app_id = "36466575ce8a45c8b66c3efe957d9afd"
    private val channelName = "voicecall"
    private val token = "007eJxTYMhjjZZVyC7m+XpmZul/mVjPxUt5Jj3Z2mC5xsfuW86ZjCgFBmMzEzMzU3PT5FSLRBPTZIskM7Nk49S0VEtT8xTLxLSUGZUCaQ2BjAyXj/1kYWSAQBCfk6EsPzM5NTkxJ4eBAQC1cyG0"
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private var startTime: Long = 0
    private val handler = Handler()

    private val REQUESTED_PERMISSION = arrayOf(
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.INTERNET,
        android.Manifest.permission.ACCESS_NETWORK_STATE
        // Add more permissions as needed
    )

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, REQUESTED_PERMISSION[0]
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupVoiceSdkEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = app_id
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine!!.enableAudio()
        } catch (e: Exception) {
            showToast("Failed to initialize voice call: ${e.message}")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendclass)

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(
                this, REQUESTED_PERMISSION, PERMISSION_ID
            )
        }
        setupVoiceSdkEngine()
        joinCall()

        // Start timer
        startTime = SystemClock.elapsedRealtime()
        startTimer()

        val hangupButton = findViewById<TextView>(R.id.joinbuttonn)
        hangupButton.setOnClickListener {
            hangupCall()
        }
    }

    private fun leaveCall() {
        if (!isJoined) {
            return
        }
        agoraEngine?.leaveChannel()
        isJoined = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        agoraEngine?.leaveChannel()
        RtcEngine.destroy()
        agoraEngine = null
    }

    private fun joinCall() {
        if (checkSelfPermission()) {
            agoraEngine!!.joinChannel(token, channelName, "", uid)
        } else {
            showToast("Permission denied for audio recording")
            finish()
        }
    }

    private val mRtcEventHandler: IRtcEngineEventHandler =
        object : IRtcEngineEventHandler() {
            override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                isJoined = true
                showToast("Voice call connected")
            }
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
            findViewById<TextView>(R.id.textView5).text = timeString
            handler.postDelayed(this, 1000)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun hangupCall() {
        leaveCall()
        finish()
    }
}
