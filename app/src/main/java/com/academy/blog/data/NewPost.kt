package com.academy.blog.data

data class NewPost(
    var id: String? = null,
    var name: String? = null,
    var logo: String? = null,
    var photo: String? = null,
    var likes: String? = null,
    var description: String? = null
)

data class ReadPost(
    var id: String? = null,
    var name: String? = null,
    var logo: String? = null,
    var photo: String? = null,
    var likes: String? = null,
    var description: String? = null,
    var dateCreate: Long? = null
)

data class NewComment(
    var comment: String? = null,
    var uid: String? = null,
    var uname: String? = null,
    var uavatar: String? = null)
data class ReadComment(
    var comment: String? = null,
    var uid: String? = null,
    var uname: String? = null,
    var uavatar: String? = null,
    var dateCreate : Long? = null)
