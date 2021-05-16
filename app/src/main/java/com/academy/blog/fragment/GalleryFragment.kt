package com.academy.blog.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.academy.blog.databinding.FragmentGalleryBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import gun0912.tedimagepicker.builder.TedImagePicker
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var mAuth: FirebaseAuth
    var filePath: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as Context

        binding.clearImage.visibility = View.GONE

        //xét chọn image
        binding.ChooseImg.setOnClickListener {
            TedImagePicker.with(activity)
                .start { uri ->
                    Glide.with(activity).load(uri).into(binding.image)
                    binding.clearImage.setVisibility(View.VISIBLE)
                    binding.ChooseImg.setVisibility(View.GONE)
                    filePath = uri
                }
        }
        binding.clearImage.setOnClickListener { ClearImage() }
        binding.btnPost.setOnClickListener { UpLoadData() }
        val name = mAuth.currentUser.displayName
        binding.brandName.text = name
        if (mAuth.currentUser.photoUrl != null){
            Picasso.get().load(mAuth.currentUser.photoUrl).into(binding.logo)
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
                    val urlImage = it.toString()
                    val id = uid
                    val status = binding.status.text.toString()
                    val dataPost = dataPost(id, status, urlImage)
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
    }
}

class dataPost(val id: String, val status: String, val urlImage: String)