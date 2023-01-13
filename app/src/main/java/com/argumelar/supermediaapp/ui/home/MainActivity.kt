package com.argumelar.supermediaapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.argumelar.supermediaapp.R
import com.argumelar.supermediaapp.SuperMediaApplication
import com.argumelar.supermediaapp.databinding.ActivityMainBinding
import com.argumelar.supermediaapp.ui.editprofile.EditProfileActivity
import com.argumelar.supermediaapp.ui.login.LoginActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val moduleMainActivity = module {
    factory { MainActivity() }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    private var currentUser = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("ini", "MAIN create")

        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("ini", "start")
        viewModel.checkUser()
        viewModel.user.observe(this) {
            if (it == true){
                moveLogin()
            }
        }

        currentUser.let {
            val displayName = currentUser?.displayName
            val email = currentUser?.email
            val photoProfile = currentUser?.photoUrl

            binding.tvDisplayName.text = displayName
            binding.tvEmail.text = email
            Picasso.get().load(photoProfile).into(binding.ivPhotoProfile)

            Log.i("current","nama: $displayName\nemail: $email\nphoto: $photoProfile")
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            moveLogin()
        }
    }

    private fun moveLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        Log.i("ini", "MAIN resume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ini", "MAIN pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ini", "MAIN destroy")
    }
}