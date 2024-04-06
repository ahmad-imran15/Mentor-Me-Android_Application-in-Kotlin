package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class searchscreen : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchscreen)
    }

    fun searchscreen(view: View)
    {
        val intent = Intent(this, aboutme::class.java).apply {
            putExtra("name", intent.getStringExtra("name"))
        }
        startActivity(intent)
    }

    fun backsearch(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, mainmenu::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
        finish()

    }

    fun homesearch_searchscreen(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, mainmenu::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }

    fun addsearch(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, addnewmentor::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }

    fun searchsearch(view: View)
    {
        val forgot = Intent(this,searchscreen::class.java)
        startActivity(forgot)
        finish()

    }

    fun chatsearch(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, chatscreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)

    }

    fun profilesearch(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, editprofilescreen::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }


}

