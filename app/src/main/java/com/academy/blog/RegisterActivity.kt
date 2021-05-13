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

        handleInfoRegister()
    }

    private fun handleInfoRegister() {
        val username = binding.edtUsername.text.toString()
        val mailAddress = binding.edtEmailAddress.text.toString()
        val password = binding.edtPassword.text.toString()

        binding.btnSignUp.setOnClickListener {
            if (!username.matches(Regex("^[a-z0-9_-]{3,16}\$"))
            ) {
                binding.edtUsername.setError("a-z0-9 and greater than 3-16 characters")
            } else if (!mailAddress.matches(Regex("^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com\$"))
            ) {
                binding.edtEmailAddress.setError("abc12345@gmail.com")
            } else if (!password.matches(Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$"))
            ) {
                binding.edtPassword.setError(("Password must be like aewBSD!@30 and greater than 8 characters"))
            } else if (binding.edtUsername.text.toString()
                    .isEmpty() || binding.edtEmailAddress.text.toString()
                    .isEmpty() || binding.edtPassword.text.toString()
                    .isEmpty() || !binding.reminderSignUp.isChecked
            ) {
                binding.edtUsername.setError("a-z0-9 and greater than 3-16 characters")
                binding.edtEmailAddress.setError("abc@gmail.com")
                binding.edtPassword.setError(("Password must be like aewBSD!@30 and greater than 8 characters"))
                binding.reminderSignUp.setError("Please accept policy...!")
            } else {
                Toast.makeText(
                    this,
                    username + " " + password + " " + mailAddress,
                    Toast.LENGTH_SHORT
                )
            }
        }
    }
}