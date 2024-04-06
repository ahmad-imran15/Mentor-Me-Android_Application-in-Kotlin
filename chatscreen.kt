package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class chatscreen : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatscreen)


        val userName = intent.getStringExtra("name")

        // Find the TextView by its id
        val textView43 = findViewById<TextView>(R.id.textView43)

        // Set the text of the TextView to the retrieved name
        textView43.text = userName
    }

    fun community_slides16chatmessages(view: View) {
        val intent = Intent(this, slide16chatmessages::class.java)
        startActivity(intent)
    }

    fun johncooper(view: View) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = if (currentUser?.uid == "WtvjJaooKGgf0gUT03bqXtxSVnC3") {
            "Ali"
        } else {
            "Ahmad Imran"
        }

        val userID = currentUser?.uid ?: ""
        val chatKey = if (userID < "WtvjJaooKGgf0gUT03bqXtxSVnC3") {
            "$userID-WtvjJaooKGgf0gUT03bqXtxSVnC3"
        } else {
            "WtvjJaooKGgf0gUT03bqXtxSVnC3-$userID"
        }

        // Navigate to the slide15chatscreen activity with Ahmad's ID, name, and chat key
        val intent = Intent(this, slide15chatscreen::class.java).apply {
            putExtra("id", "WtvjJaooKGgf0gUT03bqXtxSVnC3")
            putExtra("name", userName)
            putExtra("chat_id", chatKey)
        }
        startActivity(intent)
    }




    fun search_chatscreen(view: View) {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, searchscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }

    fun chat_chatscreen(view: View) {
        // No need to navigate to the same activity
    }

    fun add_chatscreen(view: View) {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, addnewmentor::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }

    fun profile_chatscreen(view: View) {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, editprofilescreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }


        fun homescreen_chatscreen(view: View) {
            val userName = intent.getStringExtra("name")

            // Toggle between "Ahmad" and "Ali"
            val toggledName = if (userName == "Ahmad") "Ali" else "Ahmad Imran"

            val intent = Intent(this, mainmenu::class.java).apply {
                putExtra("name", toggledName)
            }
            startActivity(intent)
        }



}
