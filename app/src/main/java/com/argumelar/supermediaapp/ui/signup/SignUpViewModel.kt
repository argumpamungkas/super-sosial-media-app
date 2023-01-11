package com.argumelar.supermediaapp.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.dsl.module

val moduleSignUpViewModel = module {
    factory { SignUpViewModel() }
}

class SignUpViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val _currentUser = auth.currentUser

    private val _user = MutableLiveData<Boolean>()
    val user: LiveData<Boolean> = _user

    private val _updateProfile = MutableLiveData<Boolean>()
    val updateProfile: LiveData<Boolean> = _updateProfile

    val fullName by lazy { MutableLiveData<String>() }

    init {
        currentDisplayName()
    }

    fun checkUser() {
        viewModelScope.launch {
            if (_currentUser == null) {
                _user.value = false
            }
            val nama = _currentUser
//            nama.let {
//                Log.i("email", nama?.email.toString())
//                Log.i("email", nama?.displayName.toString())
//            }
        }
    }

    fun userUpdateProfile(name: String) {
        viewModelScope.launch {
            _currentUser?.updateProfile(userProfileChangeRequest {
                displayName = name
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _updateProfile.value = true
                } else {
                    _updateProfile.value = false
                    Log.i("Login", task.exception.toString())
                }
            }
        }
    }

    private fun currentDisplayName(){
        fullName.value = _currentUser?.displayName
    }
}