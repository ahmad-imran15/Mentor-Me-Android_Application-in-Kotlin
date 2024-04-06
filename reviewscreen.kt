package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class reviewscreen : BaseActivity() {


    private lateinit var database: FirebaseDatabase
    private lateinit var reviewsRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewscreen)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        reviewsRef = database.getReference("reviews")

        // Retrieve the user's name from the intent extras
        val userName = intent.getStringExtra("name")

        // Find the TextView by its ID
        val textViewUserName = findViewById<TextView>(R.id.textView)

        // Set the user's name to the TextView
        textViewUserName.text = "Hi, I'm $userName"
    }
    fun submit_feedback(view: View)
    {
        // Retrieve the user's name and experience
        val name = intent.getStringExtra("name")
        val experience = findViewById<EditText>(R.id.edit_text).text.toString()


        if (experience.isEmpty()) {
            // Display a toast message indicating that all fields must be filled
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a Review object
        val review = Review(name, experience)

        // Generate a unique key for the review
        val reviewKey = reviewsRef.push().key

        // Save the review to the Firebase database under the "reviews" table
        if (reviewKey != null) {
            reviewsRef.child(reviewKey).setValue(review)
                .addOnSuccessListener {
                    // Review saved successfully
                    Toast.makeText(this, "Review submitted successfully", Toast.LENGTH_SHORT).show()
                    // Optionally, navigate to another screen or perform other actions

                    // Navigate back to aboutme screen with the user's name
                    val intent = Intent(this, aboutme::class.java).apply {
                        putExtra("name", name)
                    }
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    // Failed to save the review
                    Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun backreview(view: View)
    {
        val userName = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, aboutme::class.java).apply {
            putExtra("name", userName)
        }
        startActivity(intent)
    }
}