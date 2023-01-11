package com.argumelar.supermediaapp

import android.app.Application
import android.util.Log
import com.argumelar.supermediaapp.ui.home.moduleMainActivity
import com.argumelar.supermediaapp.ui.home.moduleMainViewModel
import com.argumelar.supermediaapp.ui.login.moduleLoginActivity
import com.argumelar.supermediaapp.ui.login.moduleLoginViewModel
import com.argumelar.supermediaapp.ui.signup.moduleSignUpActivity
import com.argumelar.supermediaapp.ui.signup.moduleSignUpViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class SuperMediaApplication: Application() {

    val firebaseAuth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var firebaseApp: FirebaseApp

    override fun onCreate() {
        super.onCreate()
        Log.i("SuperMediaApplication", "base Application")
        firebaseApp = FirebaseApp.initializeApp(this)!!

        startKoin {
            androidLogger()
            androidContext(this@SuperMediaApplication)
            modules(
                moduleMainActivity,
                moduleMainViewModel,
                moduleLoginActivity,
                moduleLoginViewModel,
                moduleSignUpActivity,
                moduleSignUpViewModel
            )
        }
    }

    object Auth: SuperMediaApplication()

}
