package com.instagram.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.academy.blog.EditProfile
import com.academy.blog.R
import com.academy.blog.adapter.PostAdapter
import com.academy.blog.data.Post
import com.academy.blog.fragment.BottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson


class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, parent, false)

        val activity = activity as Context

        // ánh xạ
        val rcv_mypost = view.findViewById<RecyclerView>(R.id.rcv_MyPost)
        val btn_EditProfile = view.findViewById<Button>(R.id.btn_EditProfile)
        val tv_userName = view.findViewById<TextView>(R.id.tv_UserName)

        // recyclerview của id:rcv_MyPost
        rcv_mypost.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val postJSON: String = activity.assets.open("post.json").bufferedReader().use { it.readText() }
        val mypost = Gson().fromJson(postJSON, Array<Post>::class.java)
        val mypostList = ArrayList<Post>()
        for (j in 0 until mypost.size)
            mypostList.add(Post(mypost[j].id, mypost[j].name, mypost[j].logo, mypost[j].photo, mypost[j].likes, mypost[j].description))
        val postAdapter = PostAdapter(activity, mypostList)
        rcv_mypost.adapter = postAdapter

        //xét click button chỉnh sửa trang cá nhân
        btn_EditProfile.setOnClickListener(View.OnClickListener {
            val intent = Intent(getActivity(),EditProfile::class.java)
            startActivity(intent)
        })

        //xét click UserName -> đăng xuất
        tv_userName.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            fragmentManager?.let { it1 -> bottomSheetFragment.show(it1,"TAG")
            }
        }



        return view

    }

}