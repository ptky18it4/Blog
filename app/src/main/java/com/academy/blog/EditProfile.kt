package com.academy.blog

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager import androidx.core.content.ContextCompat
import com.academy.blog.databinding.EditProfileBinding
import com.bumptech.glide.Glide
import gun0912.tedimagepicker.builder.TedImagePicker

class EditProfile : AppCompatActivity() {
    private lateinit var binding : EditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //sử lí ựu kiện cho back
        binding.back.setOnClickListener {
            finish();
        }
        binding.btnEditAvtar.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                TedImagePicker.with(this@EditProfile)
                    .start { uri -> Glide.with(this@EditProfile).load(uri).into(binding.imgAvt)
                    getUri(uri)}
            }
        })
    }

    private fun getUri(uri: Uri) {
        binding.tieUserName.setText(uri.toString())
    }
}