package com.academy.blog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.academy.blog.databinding.ActivityMainBinding
import com.instagram.fragment.GalleryFragment
import com.instagram.fragment.HomeFragment
import com.instagram.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
    }

    private val clickListener : View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.home_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, HomeFragment()).commit()
            }

            R.id.add_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, GalleryFragment()).commit()
            }

            R.id.profile_icon -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, ProfileFragment()).commit()
            }
        }
    }
}