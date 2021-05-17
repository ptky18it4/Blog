package com.academy.blog

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.academy.blog.data.AccountModel
import com.academy.blog.databinding.ActivityRegisterBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage.*
import java.util.*


@Suppress("UNREACHABLE_CODE")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        binding.btnSignUp.setOnClickListener {
            handleRegisterWithUsernameEmailPassword()
        }
    }

    private fun handleRegisterWithUsernameEmailPassword() {


        // Handler login in application using username and password
        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (isValidEmail(email) && isValidPassword(password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // add information account to firebase fire store
                            saveToFireStore(mAuth.currentUser!!.uid, email)

                            val user = mAuth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please check your email or password again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveToFireStore(id: String, email: String) {
        val account = AccountModel(
            null,
            null,
            null,
            email,
            null,
            isActive = true,
            dateOfBirth = Timestamp.now(),
            createdDate = Timestamp.now(),
            modifiedDate = null
        )
        db.collection("Users").document(id).set(account)
            .addOnSuccessListener {
                Toast.makeText(
                    this@RegisterActivity,
                    "Data saved!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this@RegisterActivity,
                    "Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Sign out success!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // TODO: 5/14/2021 : validate password
        val password_regex =
            """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\-_?&])(?=\S+$).{8,}""".toRegex()
        return (password_regex.matches(password))
    }
}