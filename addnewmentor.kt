package com.muhammadahmad.i210790

import BaseActivity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.muhammadahmad.i210790.databinding.ActivityAddnewmentorBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import kotlin.io.encoding.Base64

class addnewmentor : BaseActivity() {

    var sImage:String?= ""

    private lateinit var db:DatabaseReference
    private lateinit var binding:ActivityAddnewmentorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddnewmentorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase instances
    }
    fun backtomainmenu(view: View)
    {
        val forgot = Intent(this,mainmenu::class.java)
        startActivity(forgot)
        // finish()

    }
    fun upload(view: View) {
        // Get user input
        var email = binding.addnewmentorEmail.text.toString()
        var description = binding.addnewmentorDescription.text.toString()
        var status = binding.addnewmentorStatus.text.toString()
        if (email.isEmpty() || description.isEmpty() || status.isEmpty() || sImage.isNullOrEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        db = FirebaseDatabase.getInstance().getReference("mentors")

        val item = datamentor(email, description, status, sImage)
        val databaseReference = FirebaseDatabase.getInstance().reference

        val id = databaseReference.push().key

        db.child(id.toString()).setValue(item).addOnSuccessListener {
            binding.addnewmentorEmail.text.clear()
            binding.addnewmentorDescription.text.clear()
            binding.addnewmentorStatus.text.clear()

            sImage = ""

            Toast.makeText(this, "Mentor Added Successfully",Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener{
                Toast.makeText(this, "Data Not Inserted",Toast.LENGTH_SHORT).show()
            }



    }

    fun make_photo(view: View) {

        var myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)

    }

    fun make_video(view: View) {
        // Handle opening video recording functionality if needed
    }
    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                sImage = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

                binding.btnUploadVideo.setImageBitmap(myBitmap)
                inputStream?.close()

                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
