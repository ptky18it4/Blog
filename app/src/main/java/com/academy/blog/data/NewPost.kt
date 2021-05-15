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
