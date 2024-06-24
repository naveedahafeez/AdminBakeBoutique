package com.example.adminbakeboutique

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminbakeboutique.databinding.ActivityAddItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.models.Item
import java.util.UUID

class AddItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemBinding
    private lateinit var adminDatabase: DatabaseReference
    private lateinit var sharedDatabase: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adminDatabase = FirebaseDatabase.getInstance().getReference("AdminBakeBoutique/items")
        sharedDatabase = FirebaseDatabase.getInstance().getReference("BakeBoutique/items")
        storage = FirebaseStorage.getInstance()

        val selectImageResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            selectedImageUri = uri
            binding.itemImage.setImageURI(uri)
        }

        binding.selectImageButton.setOnClickListener {
            selectImageResultLauncher.launch("image/*")
        }

        binding.addItemButton.setOnClickListener {
            addItemToDatabase()
        }
    }

    private fun addItemToDatabase() {
        val itemName = binding.itemName.text.toString()
        val itemDescription = binding.itemDescription.text.toString()
        val itemPrice = binding.itemPrice.text.toString().toDoubleOrNull()

        if (itemName.isEmpty() || itemDescription.isEmpty() || itemPrice == null || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val itemId = adminDatabase.push().key ?: UUID.randomUUID().toString()
        val storageRef = storage.reference.child("items_images/$itemId.jpg")

        selectedImageUri?.let { uri ->
            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { url ->
                    val item = Item(itemId, itemName, itemDescription, itemPrice, url.toString())
                    adminDatabase.child(itemId).setValue(item).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("AddItemActivity", "Item added successfully to AdminBakeBoutique database")
                            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()


                            sharedDatabase.child(itemId).setValue(item)
                                .addOnCompleteListener { sharedTask ->
                                    if (sharedTask.isSuccessful) {
                                        Log.d("AddItemActivity", "Item also added to BakeBoutique database")
                                        Toast.makeText(this, "Item also added to BakeBoutique database", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.d("AddItemActivity", "Failed to add item to BakeBoutique database")
                                        Toast.makeText(this, "Failed to add item to BakeBoutique database", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Log.d("AddItemActivity", "Failed to add item to AdminBakeBoutique database")
                            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener {
                Log.d("AddItemActivity", "Failed to upload image")
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
