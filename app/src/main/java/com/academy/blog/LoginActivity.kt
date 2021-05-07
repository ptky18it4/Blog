package com.academy.blog

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.academy.blog.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.txtRegister.setOnClickListener(clickListener)
        binding.txtForgotPassword.setOnClickListener(clickListener)
        binding.switchRememberMe.setOnClickListener(clickListener)
        binding.btnGooglePlus.setOnClickListener(clickListener)
        binding.btnFacebook.setOnClickListener(clickListener)

        // Google
        // Config Google Sign In
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()


        // Call functions login
        handleLoginWithEmailPassword()
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
            R.id.btnGooglePlus -> {
                signIn()
            }
            R.id.btnFacebook -> {
                Toast.makeText(this, "Sign in with Facebook", Toast.LENGTH_LONG).show()
            }
        }
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

    private fun handleLoginWithEmailPassword() {
        // Handler login in application using username and password
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

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignIn", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignIn", "Google sign in failed", e)
                }
            } else {
                Log.w("SignIn", exception.toString())
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignIn", "signInWithCredential:success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }
}