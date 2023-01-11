package com.argumelar.supermediaapp.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.argumelar.supermediaapp.databinding.ActivitySignUpBinding
import com.argumelar.supermediaapp.ui.home.MainActivity
import com.argumelar.supermediaapp.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.dsl.module

val moduleSignUpActivity = module {
    factory { SignUpActivity() }
}
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

       viewModel.fullName.observe(this){
           binding.etFullName.setText(it)
       }

        binding.btnSignUp.setOnClickListener {

            val name = binding.etFullName.text.toString().trim()

            if (name.isEmpty()){
                binding.etFullName.error = "Field kosong, Harap isi"
            } else {
                viewModel.userUpdateProfile(name)
                Log.i("this", name.toString())
            }
        }

        viewModel.updateProfile.observe(this){
            if (it == true){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUser()
        viewModel.user.observe(this) {
            if (it == false){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

}