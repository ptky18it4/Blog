package com.academy.blog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.academy.blog.data.AccountModel
import com.academy.blog.databinding.EditProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import gun0912.tedimagepicker.builder.TedImagePicker
import java.util.*


class EditProfile : AppCompatActivity() {
    private lateinit var binding: EditProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get instance firebase
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        //
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.btnCancel.setOnClickListener { finish(); }
        binding.btnEditProfile.setOnClickListener {
            binding.btnChooseImg.visibility = VISIBLE
            binding.edtUsername.isEnabled = true
            binding.edtPhoneNumber.isEnabled = true
            binding.edtAddrees.isEnabled = true
            binding.edtBrithday.isEnabled = true
            binding.edtEmail.isEnabled = false
        }
        getInfo()
        binding.btnChooseImg.setOnClickListener { ImagePicker() }

        binding.btnLogout.setOnClickListener {
            mAuth.signOut()
            updateUI(mAuth.currentUser)
        }
        binding.btnUpdate.setOnClickListener {
            updateInfo()
            updateUI(mAuth.currentUser)
        }


    }

    private fun updateInfo() {
        // Create an initial document to update.
        var url: String?= null
        if (filePath == null) return
        val storage = storage.getReference("/Avatar/${mAuth.currentUser.uid}")
        storage.putFile(filePath!!)
            .addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    url = it.toString()
                    val name: String? = binding.edtUsername.text.toString()
                    val phone: String? = binding.edtPhoneNumber.text.toString()
                    val address: String? = binding.edtAddrees.text.toString()
                    val email: String? = binding.edtEmail.text.toString()
                    val df = db.collection("Users").document(mAuth.currentUser.uid)
                    df.update("avatar", url)
                    df.update("name", name)
                    df.update("phone", phone)
                    df.update("email", email)
                    df.update("address", address)

                    binding.btnChooseImg.visibility = GONE
                    binding.edtUsername.isEnabled = false
                    binding.edtPhoneNumber.isEnabled = false
                    binding.edtAddrees.isEnabled = false
                    binding.edtBrithday.isEnabled = false
                    binding.edtEmail.isEnabled = false
                }
            }

    }

    private fun getInfo() {
        val user = mAuth.currentUser
        val name = user.displayName
        val email = user.email
        val photo = user.photoUrl
        val phoneNumber = user.phoneNumber

        binding.edtUsername.setText(name)
        binding.edtEmail.setText(email)
        binding.edtPhoneNumber.setText(phoneNumber)
        if (photo != null) {
            Picasso.get().load(photo).into(binding.imgAvt)
        }

        db.collection("Users").document(mAuth.currentUser.uid)
            .get().addOnSuccessListener { documentSnapshot ->
               if(name== null || photo == null) {
                   val account = documentSnapshot.toObject<AccountModel>()
                   binding.edtUsername.setText(account?.name)
                   binding.edtPhoneNumber.setText(account?.phone)
                   binding.edtPhoneNumber.setText(account?.phone)
                   binding.edtAddrees.setText(account?.address)
                   Picasso.get().load(account?.avatar).into(binding.imgAvt)
               }
            }

    }

    private fun ImagePicker() {
        TedImagePicker.with(this).start { uri ->
            Glide.with(this).load(uri).into(binding.imgAvt)
            filePath = uri
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            val intent = Intent(this@EditProfile, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Sign out success!", Toast.LENGTH_SHORT).show()
        }
    }


}