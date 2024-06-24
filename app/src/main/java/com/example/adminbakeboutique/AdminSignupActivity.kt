package com.example.adminbakeboutique

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminSignupActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_signup)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm_password)
        signupButton = findViewById(R.id.signup_button)


        signupButton.setOnClickListener {
            if (validateInput()) {

                val username = usernameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()


                handleSignup(username, email, password)
            } else {
                Snackbar.make(signupButton, "Invalid input, please check again.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        return username.isNotEmpty() && email.isNotEmpty() &&
                password.isNotEmpty() && password == confirmPassword
    }

    private fun handleSignup(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    val userId = user?.uid

                    val userMap = hashMapOf(
                        "username" to username,
                        "email" to email
                    )

                    userId?.let {
                        database.getReference("admins").child(it)
                            .setValue(userMap)
                            .addOnSuccessListener {
                                Log.d("AdminSignup", "User profile created for $username")
                                Snackbar.make(signupButton, "Signup successful!", Snackbar.LENGTH_SHORT).show()
                                navigateToLogin()
                            }
                            .addOnFailureListener { e ->
                                Log.w("AdminSignup", "Error adding document", e)
                                Snackbar.make(signupButton, "Signup failed. Please try again.", Snackbar.LENGTH_SHORT).show()
                            }
                    }
                } else {

                    Log.w("AdminSignup", "createUserWithEmail:failure", task.exception)
                    Snackbar.make(signupButton, "Authentication failed: ${task.exception?.message}", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToLogin() {

        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("username", usernameEditText.text.toString())
        startActivity(intent)
        finish()
    }
}
