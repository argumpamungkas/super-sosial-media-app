package com.argumelar.supermediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.argumelar.supermediaapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.etFullName.setText(currentUser?.displayName)

        binding.btnSignUp.setOnClickListener {
            currentUser?.updateProfile(userProfileChangeRequest {
                displayName = binding.etFullName.text.toString()
            })?.addOnCompleteListener { task ->
               if (task.isSuccessful) {
                   Toast.makeText(this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this, MainActivity::class.java))
                   finish()
               }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

}