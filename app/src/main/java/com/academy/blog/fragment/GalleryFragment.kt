package com.academy.blog.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.academy.blog.EditProfile
import com.academy.blog.MainActivity
import com.academy.blog.data.NewPost
import com.academy.blog.databinding.FragmentGalleryBinding
import com.academy.blog.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.instagram.fragment.HomeFragment
import com.squareup.picasso.Picasso
import gun0912.tedimagepicker.builder.TedImagePicker
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var mAuth: FirebaseAuth
    var filePath: Uri? = null
    var uname : String? = null
    var uid :String? = null
    var uavatar : String? = null
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

        binding.clearImage.visibility = View.GONE

        //xét chọn image
        binding.ChooseImg.setOnClickListener {
            activity?.let { it1 ->
                TedImagePicker.with(it1)
                    .start { uri ->
                        Glide.with(activity!!).load(uri).into(binding.image)
                        binding.clearImage.setVisibility(View.VISIBLE)
                        binding.ChooseImg.setVisibility(View.GONE)
                        filePath = uri
                    }
            }
        }
        binding.clearImage.setOnClickListener { ClearImage() }
        binding.btnPost.setOnClickListener { UpLoadData() }
        uname = mAuth.currentUser!!.displayName
        uavatar = mAuth.currentUser!!.photoUrl.toString()
        binding.brandName.text = uname
        if (mAuth.currentUser!!.photoUrl != null) {
            Picasso.get().load(uavatar).into(binding.logo)
            binding.ChooseImg.setOnClickListener { PickImage() }
            binding.clearImage.setOnClickListener { ClearImage() }
            binding.btnPost.setOnClickListener { UpLoadData() }
            binding.status.doOnTextChanged { text, start, before, count -> TextWatcherBTN() }

        }

    }
    // textWatcher cho btn Post
    private fun TextWatcherBTN() {
        val string = binding.status.text.toString().trim()
        if (!string.isEmpty() == true) {
            binding.btnPost.isEnabled = true
            binding.btnPost.setTextColor(Color.parseColor("#05AFFB"))
        } else {
            binding.btnPost.isEnabled = false
            binding.btnPost.setTextColor(Color.parseColor("#BCBCBC"))
        }
    }

    // upload dữ liệu lên firebase
    private fun UpLoadData() {
        if (filePath == null) return
        val p_id = UUID.randomUUID().toString()
        val storage = FirebaseStorage.getInstance().getReference("/postImage/$p_id")
        storage.putFile(filePath!!)
            .addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {


                    val p_photo = it.toString()
                    val id = p_id
                    val description = binding.status.text.toString()
                    val dataPost = NewPost(id, uname, uavatar, p_photo, description,uid)
                    val database = FirebaseDatabase.getInstance().getReference("/post/$id")
                    database.setValue(dataPost).addOnSuccessListener {
                        Toast.makeText(activity,"Finished creating new posts",Toast.LENGTH_SHORT)
                        database.child("dateCreate").setValue(ServerValue.TIMESTAMP)
                        val intent = Intent(getActivity(), MainActivity::class.java)
                        startActivity(intent)
                    }

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

    private fun PickImage() {
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


