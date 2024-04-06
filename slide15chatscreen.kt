package com.muhammadahmad.i210790

import BaseActivity
import MessageAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.muhammadahmad.i210790.databinding.ActivitySlide15chatscreenBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.IOException

class slide15chatscreen : BaseActivity() {

    private lateinit var binding: ActivitySlide15chatscreenBinding
    private lateinit var messageAdapter: MessageAdapter
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var outputFilePath: String
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlide15chatscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the name from the intent
        val name = intent.getStringExtra("name")

        // Set the name to the TextView
        binding.usernameTextView.text = name
        // Fixed chat ID for Ahmad and Ali's conversation
        val chatID = "Ahmad_Ali_Conversation"

        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Retrieve receiver ID from intent
        val receiverID = intent.getStringExtra("id") ?: ""

        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        checkAndRequestPermissions()
        messageAdapter = MessageAdapter(this, mutableListOf())
        binding.recyclerView2.adapter = messageAdapter
        // Load chat messages
        loadChatMessages(chatID)
        // Automatically scroll to the end of the RecyclerView
        binding.recyclerView2.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.recyclerView2.postDelayed({
                    binding.recyclerView2.smoothScrollToPosition(messageAdapter.itemCount - 1)
                }, 100)
            }
        }
        binding.send.setOnClickListener {
            val message = binding.yourMessage.text.toString().trim()
            Log.d("SendMessage", "Message text: $message")

            if (message.isNotEmpty()) {
                sendMessage(message, null)
                addMessageToRecyclerView(message)
            } else {
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            }
        }

        binding.gallery.setOnClickListener {
            openGallery()
        }

        findViewById<ImageView>(R.id.microphone).setOnClickListener {
            toggleVoiceRecording()
        }

        binding.camera.setOnClickListener {
            openCamera()
        }
        binding.clip.setOnClickListener {
            openGallery2()
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun toggleVoiceRecording() {
        if (isRecording) {
            stopRecording()
        } else {
            if (checkPermissions()) {
                startRecording()
            } else {
                requestPermissions()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            RECORD_AUDIO_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                startRecording()
            } else {
                Toast.makeText(
                    this,
                    "Permissions required for recording audio are denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startRecording() {
        try {
            outputFilePath = "${externalCacheDir?.absolutePath}/audio_message.mp3" // Change file extension to .mp3
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // Change output format to MPEG-4
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // Change audio encoder to AAC for MP3 format compatibility
                setOutputFile(outputFilePath)
                prepare()
                start()
            }
            isRecording = true
            // Change button appearance to indicate recording
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            binding.microphone.setImageResource(R.drawable.stop_recording)
            Log.d(TAG, "startRecording: Recording started")
        } catch (e: IOException) {
            Log.e(TAG, "startRecording: ${e.message}")
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false
            // Change button appearance to indicate not recording
            binding.microphone.setImageResource(R.drawable.microphone)
            Log.d(TAG, "stopRecording: Recording stopped")

            // Send the recorded voice message
            sendVoiceMessage(outputFilePath)
        } catch (e: Exception) {
            Log.e(TAG, "stopRecording: ${e.message}")
        }
    }

    private fun sendVoiceMessage(filePath: String) {
        val senderID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val receiverID = intent.getStringExtra("id") ?: ""
        val chatID = "Ahmad_Ali_Conversation"

        val storageRef = FirebaseStorage.getInstance().reference.child("messages").child(chatID)
            .child("audio_messages").child(UUID.randomUUID().toString() + ".mp3")
        val audioFile = Uri.fromFile(File(filePath))

        storageRef.putFile(audioFile)
            .addOnSuccessListener { taskSnapshot ->
                // Audio uploaded successfully, get the download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val audioUrl = uri.toString()
                    sendMessage(audioUrl, null) // Send the URL of the audio file
                }.addOnFailureListener { e ->
                    // Handle failure
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    private fun playAudio(audioUrl: String) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioUrl)
                prepare()
                start()
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }

    override fun onStop() {
        super.onStop()
        // Release media player when activity is stopped
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun loadChatMessages(chatId: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId)
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<MessageModel>()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(MessageModel::class.java)
                    message?.let {
                        messageList.add(it)
                    }
                }
                messageAdapter.updateMessages(messageList)
                scrollToLastMessage() // Automatically scroll to the last message
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@slide15chatscreen, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun scrollToLastMessage() {
        binding.recyclerView2.postDelayed({
            binding.recyclerView2.smoothScrollToPosition(messageAdapter.itemCount - 1)
        }, 100)
    }

    private fun sendMessage(messageText: String?, imageUrl: String?) {
        val senderID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val receiverID = intent.getStringExtra("id") ?: ""

        // Fixed chat ID for Ahmad and Ali's conversation
        val chatID = "Ahmad_Ali_Conversation"

        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val message = MessageModel(
            senderId = senderID,
            receiverId = receiverID,
            message = messageText ?: imageUrl, // Set message text to image URL if messageText is null
            imageUrl = imageUrl,
            currentTime = currentTime,
            currentDate = currentDate
        )

        // Store message in the database
        val databaseRef = FirebaseDatabase.getInstance().getReference("chats").child(chatID)
        val messageId = databaseRef.push().key ?: ""
        databaseRef.child(messageId).setValue(message)
            .addOnSuccessListener {
                binding.yourMessage.text!!.clear()
                Log.d("SendMessagee", "Message text: ${messageText ?: imageUrl}") // Log the message text
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photo = result.data?.extras?.get("data") as Bitmap
                // Convert bitmap to Uri
                val imageUri = getImageUri(photo)
                // Upload the captured image to Firebase
                uploadImageToFirebase(imageUri)
            }
        }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun openGallery2() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Allow all file types
        galleryLauncher.launch(intent)
    }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                Log.d("SelectedImageUri", "URI: $selectedImageUri")
                selectedImageUri?.let {
                    uploadImageToFirebase(it)
                }
            }
        }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val filename = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/messages")
            .child("Ahmad_Ali_Conversation").child("image_messages").child("$filename.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Set the message to the image URL
                    sendMessage(imageUrl, imageUrl)
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to get download URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addMessageToRecyclerView(messageText: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val newMessage = MessageModel(
            senderId = senderId,
            message = messageText,
            currentTime = currentTime,
            currentDate = currentDate
        )
        messageAdapter.addMessage(newMessage)
        binding.recyclerView2.smoothScrollToPosition(messageAdapter.itemCount - 1)
    }

    companion object {
        private const val TAG = "slide15chatscreen"
        private const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 101
    }

    private fun addMessageToRecyclerView(message: MessageModel) {
        Log.d("AddToRecyclerView", "Message: ${message.message}")
        messageAdapter.addMessage(message)
        binding.recyclerView2.smoothScrollToPosition(messageAdapter.itemCount - 1)
    }

    fun make_call_slide15chatscreen(view: View)
    {
        val userName = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, attendclass::class.java).apply {
            putExtra("name", userName)
        }
        startActivity(intent)
    }

    fun make_videocall_slide15chatscreen(view: View)
    {
        val userName = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, callingscreen::class.java).apply {
            putExtra("name", userName)
        }
        startActivity(intent)
    }
}