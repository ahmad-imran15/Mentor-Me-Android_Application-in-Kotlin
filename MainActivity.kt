package com.muhammadahmad.i210790

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

private const val TAG = "MainActivity"
private const val CHANNEL_ID = "screenshot_channel"
private const val NOTIFICATION_ID = 123
class MainActivity : AppCompatActivity(),ScreenshotDetectionDelegate.ScreenshotDetectionListener {


    private lateinit var etToken: EditText

    private val screenshotDetectionDelegate = ScreenshotDetectionDelegate(this, this)

    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etToken = findViewById(R.id.messageText)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            // val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, token)
         //   Toast.makeText(baseContext,"your device token is "+ token, Toast.LENGTH_SHORT).show()
            etToken.setText(token)

        })

        checkReadExternalStoragePermission()

        // Start the screenshot detection service

        // Delay for 5 seconds before navigating to the login screen
        val delayMillis: Long = 1000
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            login()
        }
        handler.postDelayed(runnable, delayMillis)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION -> {
                if (grantResults.getOrNull(0) == PackageManager.PERMISSION_DENIED) {
                    showReadExternalStoragePermissionDeniedMessage()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission()
        }
    }

    private fun requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION)
    }

    private fun showReadExternalStoragePermissionDeniedMessage() {
        Toast.makeText(this, "Read external storage permission has denied", Toast.LENGTH_SHORT).show()
    }



    override fun onStart() {
        super.onStart()
        screenshotDetectionDelegate.startScreenshotDetection()
    }

    override fun onStop() {
        super.onStop()
        screenshotDetectionDelegate.stopScreenshotDetection()
    }




    private fun login() {
        val logi = Intent(this, loginscreenmain::class.java)
        startActivity(logi)
        finish()
    }

    override fun onScreenCaptured(path: String) {
        Log.d(TAG, "Screenshot captured: $path")

        // Perform your action here when a screenshot is captured
        sendNotification(this)
    }

    override fun onScreenCapturedWithDeniedPermission() {

        Log.d(TAG, "onScreenCapturedWithDeniedPermission: permission for read external storage")
    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Screenshot Detection"
            val descriptionText = "Notification for Screenshot Detection"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Screenshot Detected")
            .setContentText("A screenshot has been captured.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(context: Context) {
        createNotificationChannel(context)
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, buildNotification(context).build())
        }
    }


}
