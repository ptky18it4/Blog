package com.academy.blog.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.blog.EditProfile
import com.academy.blog.adapter.PostAdapter
import com.academy.blog.data.AccountModel
import com.academy.blog.data.Post
import com.academy.blog.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        // get instance firebase
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as Context

        // recyclerview của id:rcv_MyPost
        binding.rcvMyPost.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val postJSON: String =
            activity.assets.open("post.json").bufferedReader().use { it.readText() }
        val mypost = Gson().fromJson(postJSON, Array<Post>::class.java)
        val mypostList = mypost.toList() as ArrayList
        val postAdapter = PostAdapter(activity, mypostList)
        binding.rcvMyPost.adapter = postAdapter

        //chi tiết thông tin cá nhân
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(getActivity(), EditProfile::class.java)
            startActivity(intent)
        }
        getInfo()
    }

    private fun getInfo() {
        val user = mAuth.currentUser
        var name = user.displayName
        var photoUrl = user.photoUrl
        user?.let {
            // Name, email address, and profile photo Url
            binding.tvUserName.text = name
            if (photoUrl != null) {
                Picasso.get().load(photoUrl.toString()).into(binding.imgAvt)
            }
        }
        db.collection("Users").document(mAuth.currentUser.uid)
            .get().addOnSuccessListener { documentSnapshot ->
                val account = documentSnapshot.toObject<AccountModel>()
                if (name == null ||photoUrl == null) {
                    binding.tvUserName.setText(account?.name)
                    Picasso.get().load(account?.avatar).into(binding.imgAvt)
                }
            }

    }
}