package com.academy.blog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.academy.blog.databinding.ActivityEditProfileBinding
import com.google.android.material.textfield.TextInputEditText

class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val username = findViewById<TextInputEditText>(R.id.tie_Username)
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //sử lí ựu kiện cho back
        binding.back.setOnClickListener {
            finish();
        }

        // Get info email (id, username, email)

    }
}
