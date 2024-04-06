package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class makevideo : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makevideo)
    }
    fun photo_makephoto(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, makephoto::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
        finish()

    }
    fun madevideo(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, addnewmentor::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
        finish()

    }
}