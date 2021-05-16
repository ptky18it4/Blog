package com.academy.blog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.academy.blog.databinding.ActivityMainBinding
import com.academy.blog.fragment.GalleryFragment
import com.academy.blog.fragment.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.instagram.fragment.HomeFragment
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, HomeFragment()).commit()

        binding.homeIcon.setOnClickListener(clickListener)
        binding.addIcon.setOnClickListener(clickListener)
        binding.profileIcon.setOnClickListener(clickListener)
        binding.cameraImg.setOnClickListener(clickListener)
        getInfo()
    }
    private fun getInfo() {
        val user = mAuth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val photoUrl = profile.photoUrl

                if (photoUrl != null) {
                    Picasso.get().load(photoUrl).into(binding.profileIcon)
                }
            }
        }
    }
    private val clickListener : View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.home_icon -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, HomeFragment()).commit()
            }

            R.id.add_icon -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, GalleryFragment()).commit()
            }

            R.id.profile_icon -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
            }

            R.id.camera_img -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame, GalleryFragment()).commit()
            }
        }
    }
}