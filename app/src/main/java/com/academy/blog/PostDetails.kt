package com.academy.blog

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.blog.adapter.CommentAdapter
import com.academy.blog.data.NewComment
import com.academy.blog.data.ReadComment
import com.academy.blog.databinding.ActivityPostDetailsBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class PostDetails : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailsBinding
    private lateinit var idPost: String
    private lateinit var ref: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var like = false
    private var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // dữ liệu post
        val intent = intent
        idPost = intent.getStringExtra("id").toString()
        val userName = intent.getStringExtra("userName")
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
        binding.addComment.setOnClickListener { addComment() }
        binding.likes.setOnClickListener { eventLikes()  }
        binding.addComment.setOnClickListener { addComment() }

        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.currentUser!!.getUid()
        val uphotoUrl = mAuth.currentUser!!.photoUrl
        Glide.with(this).load(uphotoUrl).into(binding.avtUserAcc)
        reloadComment()
        reloadLikes()
    }

    private fun eventLikes() {
        val setLike = FirebaseDatabase.getInstance().getReference("/like/$idPost")
        if (like==false){
            like = true
            setLike.child(uid.toString()).setValue(true)
        }else{
            like = false
            setLike.child(uid.toString()).removeValue()
        }
    }

    private fun reloadLikes() {
        val setLike = FirebaseDatabase.getInstance().getReference("/like")
        setLike.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(idPost).hasChild(uid.toString())) {
                    like = true
                    val likeNumber = snapshot.child(idPost).childrenCount
                    binding.likeNumber.text = likeNumber.toString() + " like"
                    binding.likes.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    val likeNumber = snapshot.child(idPost).childrenCount
                    binding.likeNumber.text = likeNumber.toString() + " like"
                    binding.likes.setImageResource(R.drawable.ic_big_heart)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    @SuppressLint("ServiceCast")
    private fun addComment() {
        if (binding.comment.text.length > 0) {
            val database = FirebaseDatabase.getInstance().getReference("/comment/$idPost").push()
            val comment = binding.comment.text.toString()
            var user = mAuth.currentUser
            val uname = user!!.displayName
            val uavatar = user.photoUrl.toString()
            val dataComment = NewComment(comment, uid, uname, uavatar)
            database.setValue(dataComment)
            database.child("dateCreate").setValue(ServerValue.TIMESTAMP)
            Toast.makeText(this@PostDetails, uname + " " + comment, Toast.LENGTH_SHORT).show()
            val view = this.currentFocus
            view?.let { v ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
            }
            binding.comment.text.clear()
        } else {
            binding.addComment.isEnabled = false
            Toast.makeText(this@PostDetails, "Hãy nhập bình luận của bạn", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun reloadComment() {
        binding.rcvComment.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvComment.setHasFixedSize(true)
        ref = FirebaseDatabase.getInstance().getReference("/comment/$idPost")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentList = arrayListOf<ReadComment>()
                if (snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        val data = postSnapshot.getValue(ReadComment::class.java)
                        commentList.add(data!!)
                    }
                    val adapter = CommentAdapter(this@PostDetails, commentList)
                    binding.rcvComment.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PostDetails, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

}