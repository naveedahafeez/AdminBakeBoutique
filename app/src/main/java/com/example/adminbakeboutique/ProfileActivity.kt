package com.example.adminbakeboutique

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.adminbakeboutique.databinding.ActivityProfileBinding

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.models.UserProfile

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance().getReference("users")
        userId = "unique_user_id"


        fetchProfileData()


        binding.editProfileButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    private fun fetchProfileData() {
        database.child(userId).get().addOnSuccessListener {
            val profile = it.getValue(UserProfile::class.java)
            profile?.let {
                binding.profileName.text = it.name
                binding.profileEmail.text = it.email
                binding.profilePhone.text = "Phone: ${it.phone}"
                binding.profilePassword.text = "Password: *********"
            }
        }.addOnFailureListener {
            Log.e("ProfileActivity", "Error fetching profile data", it)
        }
    }
}
