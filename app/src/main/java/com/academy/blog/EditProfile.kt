package com.academy.blog

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.academy.blog.databinding.EditProfileBinding
import com.bumptech.glide.Glide
import gun0912.tedimagepicker.builder.TedImagePicker


class EditProfile : AppCompatActivity() {
    private lateinit var binding: EditProfileBinding
    var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.edtEmail.isEnabled = true
        }
        binding.btnChooseImg.setOnClickListener { ImagePicker() }


    }

    private fun ImagePicker() {
        TedImagePicker.with(this).start { uri ->
            Glide.with(this).load(uri).into(binding.imgAvt)
            filePath = uri
            binding.edtUsername.setText(filePath.toString())

        }
    }

}