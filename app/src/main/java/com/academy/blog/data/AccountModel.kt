package com.academy.blog.data

import com.google.firebase.Timestamp

data class AccountModel(
    val avatar: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    @field:JvmField // use this annotation if your Boolean field is prefixed with 'is'
    val isActive: Boolean? = null,
    val dateOfBirth: Timestamp? = null,
    val createdDate: Timestamp? = null,
    val modifiedDate: Timestamp? = null
)