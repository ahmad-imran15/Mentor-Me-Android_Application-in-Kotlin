package com.muhammadahmad.i210790

import BaseActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammadahmad.i210790.databinding.ActivityLoginscreenmainBinding


class loginscreenmain : BaseActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var loginEmailEditText: EditText
    private lateinit var loginPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginscreenmain)


        loginEmailEditText = findViewById(R.id.login_email)
        loginPasswordEditText = findViewById(R.id.login_password)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

    }

    fun loginUser(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    if (user != null) {
                        fetchUserDataFromDatabase(user.email)
                    } else {
                        // User is null, authentication failed
                        Toast.makeText(this@loginscreenmain, "User Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@loginscreenmain, "Login failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchUserDataFromDatabase(email: String?) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.reference.child("users")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = dataSnapshot.children

                for (userSnapshot in users) {
                    val userEmail = userSnapshot.child("email").getValue(String::class.java)
                    val userName = userSnapshot.child("name").getValue(String::class.java)
                    val userId = userSnapshot.key
                    if (userEmail == email) {
                        // Found matching user, navigate to mainmenu with name
                        Toast.makeText(this@loginscreenmain, "Login Successful", Toast.LENGTH_SHORT).show()
                        Log.d("fetchUserData", "Name retrieved: $userName") // Log the retrieved name
                        val intent = Intent(this@loginscreenmain, mainmenu::class.java)
                        intent.putExtra("name", userName)
                        intent.putExtra("id", userId)
                        startActivity(intent)
                        finish()
                        return
                    }
                }

                // User not found in the database
                Toast.makeText(this@loginscreenmain, "User not found", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@loginscreenmain, "Error fetching user data", Toast.LENGTH_SHORT).show()
            }
        })
    }



    fun login(view: View) {
        val email = loginEmailEditText.text.toString()
        val password = loginPasswordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginUser(email, password)
        } else {
            Toast.makeText(this@loginscreenmain, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }



    fun signup(view: View)
    {
        val logi = Intent(this,signup_screen::class.java)
        startActivity(logi)
        finish()

    }


    fun menu_loginscreen(view: View)
    {
        val logi = Intent(this,mainmenu::class.java)
        startActivity(logi)
        finish()

    }

    fun forgot(view: View)
    {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, forgotpassword::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)

    }


}