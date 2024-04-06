package com.muhammadahmad.i210790


import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class aboutme : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutme)
        // Retrieve the user's name from the intent extras
        val userName = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")

        // Find the TextView by its ID
        val textViewUserName = findViewById<TextView>(R.id.textView)

        // Set the user's name to the TextView
        textViewUserName.text = "Hi, I'm $userName"

        // Find the TextView by its ID
        val textViewdescription = findViewById<TextView>(R.id.textView6)

        // Set the user's name to the TextView
        textViewdescription.text = "$description"

    }

    fun booksession(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, booksession::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }
    fun bakeaboutme(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, searchscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }


    fun drop_review(view: View)
    {
        val intent = Intent(this, reviewscreen::class.java).apply {
            // Retrieve the user's name from the intent extras
            val userName = intent.getStringExtra("name")
            // Pass the user's name to the review screen
            putExtra("name", userName)
        }
        startActivity(intent)
    }

    fun join_community(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, addnewmentor::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }

}