package com.argumelar.supermediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.argumelar.supermediaapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("posisi", "OnCreate")

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        Log.i("posisi", "onStart")
        val currentUser = auth.currentUser
        if (currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
//            this.onStart()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("posisi", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("posisi", "OnPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("posisi", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("posisi", "onDestroy")
    }
}