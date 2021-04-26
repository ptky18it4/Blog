package com.academy.blog

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.academy.blog.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val username = binding.edtUsername.text
        val password = binding.edtPassword.text
        binding.btnSignUp.setOnClickListener {
            Toast.makeText(this, "$username, $password", Toast.LENGTH_LONG).show()
        }
    }
}