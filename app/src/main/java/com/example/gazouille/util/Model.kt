package com.example.gazouille.util

data class User (
    val email: String? = "",
    val username: String? = "",
    val imageUrl: String? = "",
    val followUsers: ArrayList<User>? = arrayListOf()
)