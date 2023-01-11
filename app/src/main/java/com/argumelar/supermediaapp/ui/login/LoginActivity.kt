package com.argumelar.supermediaapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.argumelar.supermediaapp.SuperMediaApplication
import com.argumelar.supermediaapp.databinding.ActivityLoginBinding
import com.argumelar.supermediaapp.ui.home.MainActivity
import com.argumelar.supermediaapp.ui.signup.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val moduleLoginActivity = module {
    factory { LoginActivity() }
}

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin?.setOnClickListener {
            val email = binding.etEmail?.text.toString()
            val password = binding.etPassword?.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                binding.warning?.visibility = View.VISIBLE
            }

            if (email.isNotEmpty() && password.isNotEmpty()){
                viewModel.login(email, password)
            }
        }

        binding.btnLoginGoogle?.setOnClickListener {
            Snackbar.make(binding.ll!!, "Login with Google", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUser()
        viewModel.user.observe(this) {
            if (it == true) {
                moveHome()
            }
        }

        viewModel.isLogin.observe(this) {
            if (it == true) {
                moveHome()
            }
        }
        viewModel.createUser.observe(this) {
            if (it == true) {
                moveSignUp()
            }
        }
    }

    private fun moveHome(){
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
    private fun moveSignUp(){
        startActivity(Intent(applicationContext, SignUpActivity::class.java))
        finish()
    }
}