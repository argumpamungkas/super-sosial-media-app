package com.argumelar.supermediaapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.dsl.module

val moduleLoginViewModel = module {
    factory { LoginViewModel() }
}

class LoginViewModel() : ViewModel() {
    private val auth = Firebase.auth
    private val _currentUser = auth.currentUser

    private val _user = MutableLiveData<Boolean>()
    val user: LiveData<Boolean> = _user

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _createUser = MutableLiveData<Boolean>()
    val createUser: LiveData<Boolean> = _createUser

    fun checkUser() {
        if (_currentUser != null) {
            _user.value = true
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        _isLogin.value = true
                    } else {
                        signUp(email, password)
                        _isLogin.value = false
                    }
                }
        }
    }

    private fun signUp(email: String, password: String){
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() {task ->
                    if (task.isSuccessful){
                        _createUser.value = true
                    } else {
                        Log.d("LoginViewModel", "${task.exception}")
                    }
                }
        }
    }

}

