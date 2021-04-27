package com.instagram.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.academy.blog.R
import com.academy.blog.databinding.FragmentGalleryBinding
import com.bumptech.glide.Glide
import gun0912.tedimagepicker.builder.TedImagePicker

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as Context

        binding.clearImage.isVisible

        //xét chọn image
        binding.ChooseImg.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                TedImagePicker.with(activity)
                    .start { uri -> showSingleImage(uri) }
            }

            private fun showSingleImage(uri: Uri) {
                Glide.with(activity).load(uri).into(binding.image)
                if (uri!=null){
                    binding.clearImage.setVisibility(View.VISIBLE)
                    binding.ChooseImg.setVisibility(View.GONE)
                }
            }
        })
        binding.clearImage.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                binding.image.setImageResource(0)
                binding.ChooseImg.setVisibility(View.VISIBLE)
                binding.clearImage.setVisibility(View.GONE)
            }
        })
    }
}