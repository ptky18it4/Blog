package com.academy.blog

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.core.text.bold
import com.academy.blog.data.ReadPost
import com.academy.blog.databinding.ActivityPostDetailsBinding
import com.bumptech.glide.Glide

class PostDetails : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // dữ liệu post
        val intent = intent
        val id = intent.getStringExtra("id")
        val userName=intent.getStringExtra("userName")
        val avatar = intent.getStringExtra("avatar")
        val photo = intent.getStringExtra("photo")
        val likes = intent.getStringExtra("likes")
        val status = intent.getStringExtra("status")
        val date = intent.getStringExtra("date")

        // đưa dữ liệu lên giao diện
        binding.userName.text = userName
        binding.date.text = date
        binding.status.text = status
        Glide.with(this).load(avatar).into(binding.avtUserPost)
        Glide.with(this).load(photo).into(binding.photo)

        // xử lí button back
        binding.btnBack.setOnClickListener { finish() }


        /*val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        if (id != null) {
            spannableString.setSpan(boldSpan, 0, id.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            val underlineSpan = UnderlineSpan()
            spannableString.setSpan(underlineSpan,id.length,text.length,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }*/

    }


}