package com.instagram.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.blog.EditProfile
import com.academy.blog.adapter.PostAdapter
import com.academy.blog.data.ReadPost
import com.academy.blog.databinding.FragmentProfileBinding
import com.google.firebase.database.*


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var ref: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as Context

        // recyclerview của id:rcv_MyPost

        binding.rcvMyPost.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rcvMyPost.setHasFixedSize(true)
        val postList = arrayListOf<ReadPost>()
        ref = FirebaseDatabase.getInstance().getReference("/post")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        val data = postSnapshot.getValue(ReadPost::class.java)
                        postList.add(data!!)
                    }
                    binding.rcvMyPost.adapter = PostAdapter(activity, postList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        //chi tiết thông tin cá nhân
        binding.btnEditProfile.setOnClickListener( object :View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(getActivity(), EditProfile::class.java)
                startActivity(intent)
            }

        })

    }
}