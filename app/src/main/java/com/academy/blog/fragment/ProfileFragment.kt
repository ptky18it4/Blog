package com.instagram.fragment

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
import com.academy.blog.data.Post
import com.academy.blog.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        // get instance firebase
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as Context

        // recyclerview của id:rcv_MyPost
        binding.rcvMyPost.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val postJSON: String = activity.assets.open("post.json").bufferedReader().use { it.readText() }
        val mypost = Gson().fromJson(postJSON, Array<Post>::class.java)
        val mypostList = mypost.toList() as ArrayList
        val postAdapter = PostAdapter(activity, mypostList)
        binding.rcvMyPost.adapter = postAdapter

        //chi tiết thông tin cá nhân
        binding.btnEditProfile.setOnClickListener( object :View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(getActivity(), EditProfile::class.java)
                startActivity(intent)
            }

        })
        binding.tvUserName.text = mAuth.currentUser.displayName
    }
}