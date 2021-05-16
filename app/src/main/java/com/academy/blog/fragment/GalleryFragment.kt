package com.instagram.fragment

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.academy.blog.data.NewPost
import com.academy.blog.databinding.FragmentGalleryBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import gun0912.tedimagepicker.builder.TedImagePicker
import java.util.*

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    var filePath: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clearImage.visibility = View.GONE

        binding.ChooseImg.setOnClickListener{ PickImage()}
        binding.clearImage.setOnClickListener { ClearImage() }
        binding.btnPost.setOnClickListener { UpLoadData() }
        binding.status.doOnTextChanged { text, start, before, count -> TextWatcherBTN() }

    }
    // textWatcher cho btn Post
    private fun TextWatcherBTN() {
        val string = binding.status.text.toString().trim()
        if (!string.isEmpty()==true){
            binding.btnPost.isEnabled = true
            binding.btnPost.setTextColor(Color.parseColor("#05AFFB"))
        }else{
            binding.btnPost.isEnabled = false
            binding.btnPost.setTextColor(Color.parseColor("#BCBCBC"))
        }
    }

    // upload dữ liệu lên firebase
    private fun UpLoadData() {
        if (filePath == null) return
        val uid = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/postImage/$uid")
        storage.putFile(filePath!!)
            .addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    val name = "minh tay"
                    val photo = it.toString()
                    val logo = it.toString()
                    val like = "150"
                    val id = uid
                    val description = binding.status.text.toString()
//                    val dataPost = dataPost(id, name, logo, photo, like, description )
                    val dataPost = NewPost(id, name, logo, photo, like, description)
                    val database = FirebaseDatabase.getInstance().getReference("/post/$id")
                    database.setValue(dataPost)
                    database.child("dateCreate").setValue(ServerValue.TIMESTAMP)
                }
            }
    }

    // xóa ảnh đã chọn
    private fun ClearImage() {
        binding.image.setImageResource(0)
        binding.ChooseImg.setVisibility(View.VISIBLE)
        binding.clearImage.setVisibility(View.GONE)
        binding.btnPost.isEnabled = false
        binding.btnPost.setTextColor(Color.parseColor("#BCBCBC"))
    }
    private fun  PickImage(){
        activity?.let {
            TedImagePicker.with(it)
                .start { uri ->
                    Glide.with(activity!!).load(uri).into(binding.image)
                    if (uri != null) {
                        binding.clearImage.setVisibility(View.VISIBLE)
                        binding.ChooseImg.setVisibility(View.GONE)
                        filePath = uri
                        binding.btnPost.isEnabled = true
                        binding.btnPost.setTextColor(Color.parseColor("#05AFFB"))
                    }
                }
        }
    }
}

