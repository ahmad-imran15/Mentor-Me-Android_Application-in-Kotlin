package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class editprofilescreen : BaseActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextCountry: EditText
    private lateinit var editTextCity: EditText
    private lateinit var btnUploadPicture: TextView
    private lateinit var btnUploadVideo: ImageView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var sImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofilescreen)

        // Initialize EditText fields
        editTextName = findViewById(R.id.editprofile_name)
        editTextEmail = findViewById(R.id.editprofile_email)
        editTextPhoneNumber = findViewById(R.id.editprofile_phonenumber)
        editTextCountry = findViewById(R.id.editprofile_country)
        editTextCity = findViewById(R.id.editprofile_city)
        btnUploadPicture = findViewById(R.id.btnUploadPicture)
        btnUploadVideo = findViewById(R.id.btnUploadVideo)
        btnUploadVideo.visibility = View.GONE // Hide the video upload button

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        // Fetch and display user data
        fetchUserDataFromDatabase()

        // Set click listener for the Update Profile button
        findViewById<TextView>(R.id.textView7).setOnClickListener {
            updateProfile()
            val userName = findViewById<EditText>(R.id.editprofile_name)

            intent.putExtra("name", userName.text)
        }
        findViewById<TextView>(R.id.textView15).setOnClickListener{

            val userNam = intent.getStringExtra("name")

            // Navigate back to the aboutme screen with the user's name
            val intent = Intent(this, mainmenu::class.java).apply {
                putExtra("name", userNam)
            }
            startActivity(intent)
        }

        // Set click listener for the upload picture button
        btnUploadPicture.setOnClickListener {
            val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
            myfileintent.type = "image/*"
            ActivityResultLauncher.launch(myfileintent)
        }
    }

    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

                // Display the selected image in the ImageView
                // In the onActivityResult method, set the selected bitmap to the ImageView
                btnUploadVideo.setImageBitmap(myBitmap)

                inputStream?.close()

                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserDataFromDatabase() {
        val user = firebaseAuth.currentUser
        user?.let { currentUser ->
            val uid = currentUser.uid
            val query = databaseReference.child(uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val userData = dataSnapshot.getValue(UserData::class.java)
                        userData?.let { user ->
                            editTextName.setText(user.name)
                            editTextEmail.setText(user.email)
                            editTextPhoneNumber.setText(user.number)
                            editTextCountry.setText(user.country)
                            editTextCity.setText(user.city)
                        }
                    } else {
                        Toast.makeText(
                            this@editprofilescreen,
                            "User data not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@editprofilescreen,
                        "Error fetching user data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun updateProfile() {
        // Get updated profile data from EditText fields
        val name = editTextName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val contactNumber = editTextPhoneNumber.text.toString().trim()
        val country = editTextCountry.text.toString().trim()
        val city = editTextCity.text.toString().trim()

        // Update the user's profile data in the database
        val user = firebaseAuth.currentUser
        user?.let {
            val userData = mapOf(
                "name" to name,
                "email" to email,
                "contactNumber" to contactNumber,
                "country" to country,
                "city" to city,
                "profileImage" to sImage // Add profile image data to the user data
            )
            databaseReference.child(it.uid).updateChildren(userData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@editprofilescreen,
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to the main menu screen
                        val intent = Intent(this@editprofilescreen, mainmenu::class.java)
                        intent.putExtra("updatedName", name)
                        startActivity(intent)

                        finish() // Close the current activity to prevent going back to it when pressing back button
                    } else {
                        Toast.makeText(
                            this@editprofilescreen,
                            "Failed to update profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    fun back_editprofilescreen(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, mainmenu::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }

    fun bookedsessions(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, bookedsessionscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }
}
