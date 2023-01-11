package com.argumelar.supermediaapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.argumelar.supermediaapp.SuperMediaApplication
import com.argumelar.supermediaapp.databinding.ActivityMainBinding
import com.argumelar.supermediaapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val moduleMainActivity = module {
    factory { MainActivity() }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUser()
        viewModel.user.observe(this) {
            if (it == true){
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            moveLogin()
        }
    }

    fun moveLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}