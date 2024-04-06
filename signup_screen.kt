package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.muhammadahmad.i210790.databinding.ActivitySignupScreenBinding


class signup_screen : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_signup_screen)
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        val signupButton = findViewById<TextView>(R.id.signup)

        signupButton.setOnClickListener{
            val signupEmail = findViewById<EditText>(R.id.signup_email).text.toString()
            val signupName = findViewById<EditText>(R.id.signup_name).text.toString()
            val signupCountry = findViewById<EditText>(R.id.signup_country).text.toString()
            val signupCity = findViewById<EditText>(R.id.signup_city).text.toString()
            val signupNumber = findViewById<EditText>(R.id.signup_contatctnumber).text.toString()
            val signupPassword = findViewById<EditText>(R.id.signup_password).text.toString()


            if(signupEmail.isNotEmpty()&& signupPassword.isNotEmpty()&&signupName.isNotEmpty()&&signupCity.isNotEmpty() && signupCountry.isNotEmpty() && signupNumber.isNotEmpty())
            {
                signup_User(signupName, signupEmail,signupNumber,signupCountry,signupCity,signupPassword)
            }
            else
            {
                Toast.makeText(this@signup_screen, "Please fill all fields", Toast.LENGTH_SHORT).show()

            }

        }







    }



    private fun signup_User(name:String, email:String, number:String,country:String,city:String,password:String)
    {
        // Check if the email is valid






        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this@signup_screen, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (number.length != 11) {
            Toast.makeText(this@signup_screen, "Phone number should be 11 digits long", Toast.LENGTH_SHORT).show()
            return
        }

        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        val phoneNumberRegex = Regex("\\d{11}")

        if (!emailRegex.matches(email)) {
            Toast.makeText(this@signup_screen, "Invalid email format", Toast.LENGTH_SHORT).show()
            return
        }
        if (!phoneNumberRegex.matches(number)) {
            Toast.makeText(this@signup_screen, "Invalid phone number format", Toast.LENGTH_SHORT).show()
            return
        }




        //=============================================================
        val mAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User is signed up successfully
                    val user = mAuth.currentUser
                    if (user != null) {
                        val userData = UserData(user.uid, name, email, number, country, city, password, image = null)
                        databaseReference.child(user.uid).setValue(userData)
                        Toast.makeText(this@signup_screen, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@signup_screen, loginscreenmain::class.java))
                        finish()
                    }
                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this@signup_screen, "Sign Up failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun login(view: View)
    {
        val logi = Intent(this,loginscreenmain::class.java)
        startActivity(logi)
  //      finish()

    }
    fun signup(view: View)
    {

        val forgot = Intent(this,loginscreenmain::class.java)
        startActivity(forgot)
    //    finish()



    }



}