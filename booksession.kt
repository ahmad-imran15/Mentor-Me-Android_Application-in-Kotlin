package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class booksession : BaseActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userName: String
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private var selectedTimeView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booksession)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("bookings")

        // Retrieve user name from intent extras
        userName = intent.getStringExtra("name").toString()

        // Display user name
        val textViewName = findViewById<TextView>(R.id.textView)
        textViewName.text = userName

        // Initialize CalendarView and set OnDateChangeListener
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }

        // Set click listeners for time slots
        findViewById<TextView>(R.id.textView10).setOnClickListener {
            handleTimeSlotClick(it as TextView, "12:00 PM")
        }

        findViewById<TextView>(R.id.textView11).setOnClickListener {
            handleTimeSlotClick(it as TextView, "10:00 AM")
        }

        findViewById<TextView>(R.id.textView12).setOnClickListener {
            handleTimeSlotClick(it as TextView, "11:00 AM")
        }
    }

    private fun handleTimeSlotClick(view: TextView, time: String) {
        if (selectedTimeView != null) {
            // Reset previously selected time slot background
            selectedTimeView?.setBackgroundResource(android.R.color.transparent)
        }
        // Update selected time slot background
        selectedTimeView = view
        selectedTime = time
        view.setBackgroundResource(R.drawable.greenbackgrundwhitebutton)
    }

    fun book_appointment(view: View) {
        if (::selectedDate.isInitialized && ::selectedTime.isInitialized) {
            // Save booking information to Firebase Realtime Database
            val bookingData = mapOf(
                "userName" to userName,
                "selectedDate" to selectedDate,
                "selectedTime" to selectedTime
            )
            databaseReference.push().setValue(bookingData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Booking successful
                        Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // Booking failed
                        Toast.makeText(this, "Failed to book appointment", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // No date selected
            Toast.makeText(this, "Please select a date and time", Toast.LENGTH_SHORT).show()
        }
    }

    fun backbookappointment(view: View) {
        val userNam = intent.getStringExtra("name")

        // Navigate back to the aboutme screen with the user's name
        val intent = Intent(this, aboutme::class.java).apply {
            putExtra("name", userNam)
        }
        startActivity(intent)
    }
}
