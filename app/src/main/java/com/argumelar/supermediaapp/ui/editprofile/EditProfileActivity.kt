package com.argumelar.supermediaapp.ui.editprofile

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.argumelar.supermediaapp.Constant
import com.argumelar.supermediaapp.Constant.Companion.IMAGE_DEFAULT
import com.argumelar.supermediaapp.Constant.Companion.PICK_IMAGE
import com.argumelar.supermediaapp.R
import com.argumelar.supermediaapp.SuperMediaApplication
import com.argumelar.supermediaapp.databinding.ActivityEditProfileBinding
import com.argumelar.supermediaapp.ui.home.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import org.koin.dsl.module
import java.io.ByteArrayOutputStream

val moduleEditProfileActivity = module {
    factory { EditProfileActivity() }
}

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var currentUser = Firebase.auth.currentUser
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivPhotoProfile.setOnClickListener {
            alertDialogImage()
        }

        binding.btnSave.setOnClickListener {
            updateProfile()
        }

    }

    override fun onStart() {
        super.onStart()
        checkUser()
    }

    private fun checkUser(){
        if (currentUser != null) {
            binding.etFullName.setText(currentUser?.displayName)
            binding.etEmail.setText(currentUser?.email)
            Picasso.get().load(currentUser?.photoUrl).into(binding.ivPhotoProfile)
        }
    }

    private fun updateProfile() {
        val image = when {
            ::imageUri.isInitialized -> imageUri
            currentUser?.photoUrl == null -> Uri.parse(IMAGE_DEFAULT)
            else -> currentUser?.photoUrl
        }

        val name = binding.etFullName.text.toString().trim()

        if (name.isEmpty()) {
            binding.etFullName.error = "Nama tidak boleh kosong"
            binding.etFullName.requestFocus()
            return
        }

        UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(image)
            .build().also {
                currentUser?.updateProfile(it)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showMessage("Update berhasil")
                        finish()
                    } else {
                        showMessage("${task.exception?.message}")
                    }
                }
            }
    }

    private fun alertDialogImage() {
        val dialog = layoutInflater.inflate(R.layout.dialog_pick_image_profile, null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialog)

        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val camera: TextView = dialog.findViewById(R.id.btn_dari_kamera)
        val gallery: TextView = dialog.findViewById(R.id.btn_dari_galeri)
        val batal: TextView = dialog.findViewById(R.id.btn_batal)
        camera.setOnClickListener {
            intentCamera()
            myDialog.dismiss()
        }
        gallery.setOnClickListener {
            intentGallery()
            myDialog.dismiss()
        }
        batal.setOnClickListener { myDialog.dismiss() }
    }

    private fun intentGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager).also {
                startActivityForResult(intent, Constant.REQUEST_CAMERA)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_CAMERA && resultCode == RESULT_OK) {
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            val selectedImg = data?.data
            val imgBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImg)
            uploadImage(imgBitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        loadImage(true)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val ref =
            FirebaseStorage.getInstance().reference.child("img_profile/${FirebaseAuth.getInstance().currentUser?.uid}")

//        convert BAOS
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val image = byteArrayOutputStream.toByteArray()

//        upload
        ref.putBytes(image)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ref.downloadUrl.addOnCompleteListener { taskUri ->
                        taskUri.result?.let { uri ->
                            imageUri = uri
                            binding.ivPhotoProfile.setImageBitmap(imgBitmap)
                        }
                    }
                    loadImage(false)
                    showMessage("Update Gambar sukses")
                } else {
                    loadImage(false)
                    showMessage("Update Gambar gagal")
                }
            }
    }

    private fun showMessage(msg: String) {
        Snackbar.make(binding.editProfile, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun loadImage(load: Boolean) {
        binding.pbLoadImage.visibility = if (load) View.VISIBLE else View.INVISIBLE
    }
}