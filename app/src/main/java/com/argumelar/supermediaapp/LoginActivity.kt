package com.argumelar.supermediaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.argumelar.supermediaapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        binding.btnLogin?.setOnClickListener {
            loginUsernamePassword()
        }
        val currenUser = auth.currentUser
        if (currenUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun loginUsernamePassword() {
        val email = binding.etEmail?.text.toString()
        val password = binding.etPassword?.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    val user = auth.currentUser
//                        updateUI(user)
                } else {
                    this.sighUp(email, password)

                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                        updateUI(null)
                }
            }
    }

    private fun sighUp(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    startActivity(Intent(this, SignUpActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, task.exception?.message.toString(),
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }
}