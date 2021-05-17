package com.academy.blog.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.academy.blog.R
import com.academy.blog.data.ReadComment
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(val activity: Context, val commentList: ArrayList<ReadComment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.adapter_comment_layout, parent, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = commentList[position]
        val comment = commentList[position].comment
        val uid = commentList[position].uid
        val uname = commentList[position].uname
        val uavatar = commentList[position].uavatar
        val time = commentList[position].dateCreate
        if (time != null){
            val sdf = SimpleDateFormat("MM/dd/yyyy ")
            val DateTime = Date(time.toLong())
            val date = sdf.format(DateTime)
            holder.date.text = date
        }

        Glide.with(activity)
            .load(uavatar)
            .into(holder.imgAvt)
        val textComment = uname + " " + comment
        val spannableString = SpannableString(textComment)
        val boldSpan = StyleSpan(Typeface.BOLD)
        if (textComment != null) {
             spannableString.setSpan(boldSpan, 0, uname!!.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        holder.comment.text = spannableString

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvt = itemView.findViewById<ImageView>(R.id.img_uavt)
        var comment = itemView.findViewById<TextView>(R.id.comment)
        var date = itemView.findViewById<TextView>(R.id.date)
    }

}