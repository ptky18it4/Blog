package com.academy.blog

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.academy.blog.databinding.ActivityLoginBinding


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.txtRegister.setOnClickListener(clickListener)
        binding.txtForgotPassword.setOnClickListener(clickListener)
        binding.switchRememberMe.setOnClickListener(clickListener)

        handleLogin()

    }

    fun checkConnect() {
        if (!isConnected()) buildDialog()?.show()
    }

    fun buildDialog(): AlertDialog.Builder? {
        // Alert build
        val builder = AlertDialog.Builder(this)
        builder.setMessage("No Internet Connection")
        builder.setPositiveButton("Ok") { dialog, which ->
            startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
        return builder
    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->
        // Handle onClickListener of txtRegister, txtForgotPassword, ....
        when (view.id) {
            R.id.txtRegister -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.txtForgotPassword -> {
                Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show()
            }
            R.id.switchRememberMe -> {
                Toast.makeText(this, "Switch remember me!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleLogin() {
        // Handler login in application
        val edtEmail = findViewById<TextView>(R.id.edtUsername)
        val edtPassword = findViewById<TextView>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener(View.OnClickListener {

            if (edtEmail.text.isEmpty() || edtPassword.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Email or password not null !",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edtEmail.text == "ptky.18it4@sict.udn.vn" && edtEmail.text == "0909099900") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Email or password not correct !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun isConnected(): Boolean {

        /*  Check the network connection of the device, if not connected, go to D, otherwise go to the
         *  login screen
         */
        val connectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return mobile != null && mobile.isConnectedOrConnecting || wifi != null && wifi.isConnectedOrConnecting
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        checkConnect()
    }

}