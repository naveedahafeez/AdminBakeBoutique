package com.example.adminbakeboutique

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.adminbakeboutique.databinding.ActivityEditProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.models.UserProfile

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("users")
        userId = "unique_user_id"

        fetchProfileData()


        binding.saveButton.setOnClickListener {
            val newName = binding.editName.text.toString()
            val newEmail = binding.editEmail.text.toString()
            val newPhone = binding.editPhone.text.toString()
            val newPassword = binding.editPassword.text.toString()

            updateProfile(newName, newEmail, newPhone, newPassword)


            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }

    private fun fetchProfileData() {
        database.child(userId).get().addOnSuccessListener {
            val profile = it.getValue(UserProfile::class.java)
            profile?.let {
                binding.editName.setText(it.name)
                binding.editEmail.setText(it.email)
                binding.editPhone.setText(it.phone)
            }
        }.addOnFailureListener {
            Log.e("EditProfileActivity", "Error fetching profile data", it)
        }
    }

    private fun updateProfile(name: String, email: String, phone: String, password: String) {
        val updatedProfile = UserProfile(userId, name, email, phone, password)
        database.child(userId).setValue(updatedProfile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("EditProfileActivity", "Profile updated successfully")
            } else {
                Log.e("EditProfileActivity", "Error updating profile", task.exception)
            }
        }
    }
}
