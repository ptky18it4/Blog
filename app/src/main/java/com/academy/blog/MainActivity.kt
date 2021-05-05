package com.academy.blog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.instagram.fragment.GalleryFragment
import com.instagram.fragment.HomeFragment
import com.instagram.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val homeBtn = findViewById<ImageView>(R.id.home_icon)
        val galleryBtn = findViewById<ImageView>(R.id.add_icon)
        val profileBtn = findViewById<ImageView>(R.id.profile_icon)

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, HomeFragment()).commit()

        homeBtn.setOnClickListener(clickListener)
        galleryBtn.setOnClickListener(clickListener)
        profileBtn.setOnClickListener(clickListener)
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