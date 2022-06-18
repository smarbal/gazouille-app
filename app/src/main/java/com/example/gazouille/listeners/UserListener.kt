package com.example.gazouille.listeners

import android.content.Context
import com.example.gazouille.util.User

interface UserListener {
    fun onLayoutClick(userId: String?)
    fun onButtonClick(username: String?)
}