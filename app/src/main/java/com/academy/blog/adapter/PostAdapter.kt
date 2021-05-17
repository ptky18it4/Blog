package com.academy.blog.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.academy.blog.PostDetails
import com.academy.blog.R
import com.academy.blog.data.ReadPost
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter(val activity: Context, val postList: ArrayList<ReadPost>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_post_layout, p0, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val list = postList[p1]
        val id = postList[p1].id
        val userName= postList[p1].name
        val avatar = postList[p1].logo
        val photo = postList[p1].photo
        val likes = postList[p1].likes
        val status = postList[p1].description
        val time = postList[p1].dateCreate.toString()
        val sdf = SimpleDateFormat("MM/dd/yyyy ")
        val DateTime = Date(time.toLong())
        val date = sdf.format(DateTime)



        p0.description?.text = status
        p0.name?.text= userName
        p0.time.text = date

        Glide.with(activity)
            .load(avatar)
            .into(p0.logo)

        Glide.with(activity)
            .load(photo)
            .into(p0.photo)

        // xử lí click photo
        p0.photo.setOnClickListener {
            val intent = Intent(activity, PostDetails::class.java)
            val name = list.name
            intent.putExtra("id", id)
            intent.putExtra("userName", userName)
            intent.putExtra("avatar", avatar)
            intent.putExtra("photo", photo)
            intent.putExtra("likes", likes)
            intent.putExtra("status", status)
            intent.putExtra("date", date)
            activity.startActivities(arrayOf(intent))
        }
        var getlike = false
        val setLike = FirebaseDatabase.getInstance().getReference("/like")
        val uid = "1234"
        setLike.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(id!!).hasChild(uid)) {
                    getlike = true
                    val likeNumber = snapshot.child(id).childrenCount
                    p0.txtlikes.text = likeNumber.toString() +" Like"
                    p0.btnLike.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    val likeNumber = snapshot.child(id).childrenCount
                    p0.txtlikes.text = likeNumber.toString() +" Like"
                    p0.btnLike.setImageResource(R.drawable.ic_big_heart)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        p0.btnLike.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val setLike = FirebaseDatabase.getInstance().getReference("/like/$id")
                if (getlike==false){
                    getlike = true
                    setLike.child(uid).setValue(true)
                }else{
                    getlike = false
                    setLike.child(uid).removeValue()
                }
            }

        })
        }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.brand_name)
        val logo = itemView.findViewById<ImageView>(R.id.logo)
        val photo = itemView.findViewById<ImageView>(R.id.post_img)
        val txtlikes = itemView.findViewById<TextView>(R.id.likes_txt)
        val description = itemView.findViewById<TextView>(R.id.description_txt)
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        val btnLike = itemView.findViewById<ImageView>(R.id.heart)

    }
}