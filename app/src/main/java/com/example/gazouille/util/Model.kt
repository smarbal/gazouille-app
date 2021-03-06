package com.example.gazouille.util

import java.sql.Timestamp

data class User (
    val email: String? = "",
    val username: String? = "",
    val imageUrl: String? = "",
    val followUsers: ArrayList<String>? = arrayListOf()
)

data class Tweet (
    val tweetId : String? = "",
    val userIds : ArrayList<String>? = arrayListOf(),
    val username: String? = "",
    val text : String ? = "",
    val imageUrl: String? = "",
    val timestamp: Long = 0,
    val likes: ArrayList<String>? = arrayListOf(),
    var emphasis: Boolean = false
    ) {
    operator fun set(c: Char, value: Boolean) {

    }
}