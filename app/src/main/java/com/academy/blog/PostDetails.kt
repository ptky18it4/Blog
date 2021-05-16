package com.academy.blog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.blog.adapter.CommentAdapter
import com.academy.blog.data.NewComment
import com.academy.blog.data.ReadComment
import com.academy.blog.databinding.ActivityPostDetailsBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import java.util.*

class PostDetails : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailsBinding
    private lateinit var idPost: String
    private lateinit var ref: DatabaseReference

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

        reloadComment()

    }

    private fun addComment() {
        if (binding.comment.text.length >0){
            val uid = UUID.randomUUID().toString()
            val database = FirebaseDatabase.getInstance().getReference("/comment/$idPost").push()
            val comment = binding.comment.text.toString()
            var uname = "tay"
            var uavatar =
                "https://firebasestorage.googleapis.com/v0/b/blog-dc4b9.appspot.com/o/postImage%2F0296d86f-989c-4e76-81c6-45b7758e962b?alt=media&token=ee56b701-1f24-4e8b-a5c4-92f16ec0ebbf"
            val dataComment = NewComment(comment, uid, uname, uavatar)
            database.setValue(dataComment)
            database.child("dateCreate").setValue(ServerValue.TIMESTAMP)
            Toast.makeText(this@PostDetails, uname+" "+comment, Toast.LENGTH_SHORT).show()
        }else{
            binding.addComment.isEnabled = false
            Toast.makeText(this@PostDetails, "Hãy nhập bình luận của bạn", Toast.LENGTH_SHORT).show()
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