    package com.muhammadahmad.i210790

    import BaseActivity
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.view.View
    import android.widget.TextView
    import com.google.firebase.auth.FirebaseAuth


    class mainmenu : BaseActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_mainmenu)
        }

        override fun onResume() {
            super.onResume()
            loadUserName()
        }
        private fun loadUserName() {
            // Retrieve the name from the intent extras
            val userName = intent.getStringExtra("name")
            val userID = intent.getStringExtra("id")

            // Retrieve the updated name from the intent extras
            val updatedName = intent.getStringExtra("updatedName")

            // Display the user's name in the TextView
            val textViewUserName = findViewById<TextView>(R.id.textView)
            textViewUserName.text = userName

            // Set the updated name in the TextView if available
            updatedName?.let {
                textViewUserName.text = it
            }

            // Set click listener for the profile TextView
            val textViewProfile = findViewById<TextView>(R.id.textView56)
            textViewProfile.setOnClickListener {
                val intent = Intent(this, editprofilescreen::class.java).apply {
                    putExtra("name", userName)
                }
                startActivity(intent)
            }
        }
        fun search(view: View)
        {
            val userNam = intent.getStringExtra("name")

            // Navigate back to the aboutme screen with the user's name
            val intent = Intent(this, searchscreen::class.java).apply {
                putExtra("name", userNam)
            }
            startActivity(intent)
        }

        fun chat(view: View)
        {
            val userName = intent.getStringExtra("name")
            val userID = intent.getStringExtra("id")

            // Navigate to the chatscreen activity with the user's ID and name
            val intent = Intent(this, chatscreen::class.java).apply {
                putExtra("id", userID)
                putExtra("name", userName)
            }
            startActivity(intent)
        }

        fun add(view: View)
        {
            val userNam = intent.getStringExtra("name")

            // Navigate back to the aboutme screen with the user's name
            val intent = Intent(this, addnewmentor::class.java).apply {
                putExtra("name", userNam)
            }
            startActivity(intent)
        }


        fun chatscreen(view: View) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userName = if (currentUser?.uid == "WtvjJaooKGgf0gUT03bqXtxSVnC3") {
                "Ali"
            } else {
                "Ahmad Imran"
            }

            // Navigate back to the aboutme screen with the user's name
            val intent = Intent(this, chatscreen::class.java).apply {
                putExtra("name", userName)
            }
            startActivity(intent)
        }



        fun profile(view: View)
        {
            val userNam = intent.getStringExtra("name")

            // Navigate back to the aboutme screen with the user's name
            val intent = Intent(this, editprofilescreen::class.java).apply {
                putExtra("name", userNam)
            }
            startActivity(intent)
        }
        fun backloginscreen(view: View) {
            val userNam = intent.getStringExtra("name")

            // Navigate back to the loginscreenmain and finish all other activities
            val intent = Intent(this, loginscreenmain::class.java).apply {
                putExtra("name", userNam)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish() // Finish the current activity
        }


        fun notifications(view: View)
        {
            val userNam = intent.getStringExtra("name")

            // Navigate back to the aboutme screen with the user's name
            val intent = Intent(this, notificationscreen::class.java).apply {
                putExtra("name", userNam)
            }
            startActivity(intent)
        }

        fun mentor1(view: View) {
            val userName = "Ahmad Imran" // Replace "Hardcoded Name" with the desired name
            val description = "For me, I am a passionate UX designer at Google with a focus on" +
                    " creating user-centric and intuitive interfaces. With 10 years of" +
                    " experience, I have had the opportunity to work on diverse" +
                    " projects that have shaped my understanding of design " +
                    " principles and user experience"
            val intent = Intent(this, aboutme::class.java).apply {
                putExtra("name", userName)
                putExtra("description", description)
            }
            startActivity(intent)
        }

        fun mentor2(view: View) {
            val userName = "Umer Farooq" // Replace "Hardcoded Name" with the desired name
            val description = "I am a creative graphic designer with a flair for visual storytelling." +
            " With a strong background in branding and typography, I specialize in" +
                    " crafting captivating designs that resonate with audiences." +
                    " My passion for art and design drives me to continually seek out" +
                    " new inspirations and push the boundaries of creativity."
            val intent = Intent(this, aboutme::class.java).apply {
                putExtra("name", userName)
                putExtra("description", description)
            }
            startActivity(intent)
        }


        fun mentor3(view: View) {
            val userName = "Zarrar khan" // Replace "Hardcoded Name" with the desired name
            val description = "I am a dedicated software engineer specializing in Android app development." +
                    " With a keen eye for detail and a passion for crafting high-quality code," +
                    " I strive to create efficient and user-friendly applications." +
                    " My extensive experience in the field has honed my skills in" +
                    " problem-solving and collaboration, making me a valuable asset" +
                    " to any development team."

            val intent = Intent(this, aboutme::class.java).apply {
                putExtra("name", userName)
                putExtra("description", description)
            }
            startActivity(intent)
        }

    }