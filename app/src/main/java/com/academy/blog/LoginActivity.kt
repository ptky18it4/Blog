package com.academy.blog

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.academy.blog.databinding.ActivityLoginBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager


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

        // Call function login facebook
        signInFacebook()
    }

    private fun checkConnect() {
        if (!isConnected()) buildDialog().show()
    }

    private fun buildDialog(): AlertDialog.Builder {
        // Alert build
        val builder = AlertDialog.Builder(this)
        builder.setMessage("No Internet Connection")
        builder.setPositiveButton("Ok") { _, _ ->
            startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS))
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
                signInGoogle()
            }
        }
    }

    private fun isConnected(): Boolean {

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
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun handleLoginWithEmailPassword() {

        // Handler login in application using username and password
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            if (isValidEmail(email) && isValidPassword(password)) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Please check your email or password again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // TODO: 5/14/2021 : validate password
        val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\-_?&])(?=\S+$).{8,}""".toRegex()
        return(PASSWORD_REGEX.matches(password))
    }
    // Google
    private fun signInGoogle() {
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

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignIn", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignIn", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    // Facebook
    private fun signInFacebook() {

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        binding.btnFacebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this@LoginActivity, listOf("email", "public_profile")
            )
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                    }

                    override fun onError(error: FacebookException) {
                    }
                })
        }

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
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
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please sign in to continue!", Toast.LENGTH_SHORT).show()
        }
    }

}