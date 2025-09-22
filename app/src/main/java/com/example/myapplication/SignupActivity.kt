package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    private companion object {
        private const val TAG = "SignupActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signupActionButton.setOnClickListener {
            val email = binding.emailEditTextSignup.text.toString().trim()
            val password = binding.passwordEditTextSignup.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditTextSignup.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) { // Firebase requires passwords to be at least 6 characters
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, "Sign up successful.", Toast.LENGTH_SHORT).show()
                        // val user = auth.currentUser // You can get the user here if needed
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity() // Finish this activity and all parent activities
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
        }

        binding.loginPromptButton.setOnClickListener {
            // Navigate back to LoginActivity
            // No need to start a new LoginActivity if it's already in the back stack.
            // Simply finishing this activity should take the user back if LoginActivity was the previous one.
            // However, to be explicit or if LoginActivity might have been finished:
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Clears the activity stack on top of LoginActivity
            startActivity(intent)
            finish() // Finish SignupActivity
        }
    }
}