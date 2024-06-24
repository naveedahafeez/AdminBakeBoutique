package com.example.adminbakeboutique
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adminbakeboutique.AddItemActivity
import com.example.adminbakeboutique.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addItemButton.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }


        binding.viewItemsButton.setOnClickListener {
            startActivity(Intent(this, ViewItemsActivity::class.java))
        }


        binding.logoutButton.setOnClickListener {

            val sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.adminProfileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.pendingOrdersLayout.setOnClickListener {
            startActivity(Intent(this, PendingOrdersActivity::class.java))
        }

        binding.completedOrdersLayout.setOnClickListener {
            startActivity(Intent(this, CompletedOrdersActivity::class.java))
        }

        updateCounts()
    }

    private fun updateCounts() {

        binding.pendingOrdersCount.text = "5"
        binding.completedOrdersCount.text = "10"
        binding.totalEarnings.text = "Rs 5000"
    }
}
