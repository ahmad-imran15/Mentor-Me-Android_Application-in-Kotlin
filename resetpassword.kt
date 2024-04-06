package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class resetpassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resetpassword)
    }

    fun resetpassword(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, loginscreenmain::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
        finish()

    }

    fun login(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, loginscreenmain::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
   //     finish()

    }


    fun forgot_screen(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, forgotpassword::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
   //     finish()

    }
}