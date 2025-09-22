package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth // Added import
import com.google.firebase.ktx.Firebase     // Added import

@SuppressLint("CustomSplashScreen") //This is a custom splash screen, not using the Splash API
class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_splash) // setContentView will be called after checking auth status

        // Initialize Firebase Auth
        val auth = Firebase.auth

        // Check if user is signed in (non-null) and navigate accordingly.
        if (auth.currentUser != null) {
            // User is already signed in, navigate to MainActivity directly
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // User is not signed in, show splash and then navigate to LoginActivity
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, SPLASH_TIME_OUT)
        }
    }
}