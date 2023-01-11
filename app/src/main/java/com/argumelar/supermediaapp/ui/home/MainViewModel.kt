package com.argumelar.supermediaapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.dsl.module

val moduleMainViewModel = module {
    factory { MainViewModel() }
}
class MainViewModel: ViewModel() {

    private val auth = Firebase.auth
    val _currentUser = auth.currentUser

    private val _user = MutableLiveData<Boolean>()
    val user : LiveData<Boolean> = _user

    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name


    fun checkUser(){
        if (_currentUser == null){
            _user.value = true
        }
//        val nama = _currentUser
//        nama.let {
//            Log.i("email", nama?.email.toString())
//            Log.i("email", nama?.displayName.toString())
//        }
    }

    fun logout(){
        auth.signOut()
    }
}