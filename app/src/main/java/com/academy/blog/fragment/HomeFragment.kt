package com.instagram.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.academy.blog.R
import com.academy.blog.adapter.PostAdapter
import com.academy.blog.adapter.StatusAdapter
import com.academy.blog.data.InstaStatus
import com.academy.blog.data.Post
import com.academy.blog.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // get instance firebase
        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as Context

        binding.instaStatusList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.postList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val statusJSON: String =
            activity.assets.open("status.json").bufferedReader().use { it.readText() }
        val postJSON: String =
            activity.assets.open("post.json").bufferedReader().use { it.readText() }

        val status = Gson().fromJson(statusJSON, Array<InstaStatus>::class.java)
        val post = Gson().fromJson(postJSON, Array<Post>::class.java)

        val statusList = status.toList() as ArrayList
        val postList = post.toList() as ArrayList


        val statusAdapter = StatusAdapter(activity, statusList)
        binding.instaStatusList.adapter = statusAdapter

        val postAdapter = PostAdapter(activity, postList)
        binding.postList .adapter = postAdapter


    }

}
