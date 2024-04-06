package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class slide16chatmessages : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide16chatmessages)
    }

    fun make_call_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, attendclass::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    //    finish()

    }
    fun make_videocall_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, callingscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
      //  finish()

    }



    fun add_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, addnewmentor::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
      //  finish()

    }


    fun search_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, searchscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    //    finish()

    }

    fun chats_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, chatscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
        finish()

    }
    fun profilescreen_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, editprofilescreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
  //      finish()

    }
    fun home_slide16chatmessages(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, mainmenu::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    //    finish()

    }

}