package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class makephoto : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makephoto)
    }
    fun video_makevideo(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, makevideo::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)

    }
    fun click(view: View)
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