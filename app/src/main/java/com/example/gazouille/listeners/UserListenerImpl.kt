package com.example.gazouille.listeners

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gazouille.activities.LoginActivity
import com.example.gazouille.activities.ProfileActivity
import com.example.gazouille.activities.TweetActivity

class UserListenerImpl(val userList: RecyclerView) : UserListener {
     override fun onLayoutClick(userId: String?) {

//         val intent = Intent(context, ProfileActivity::class.java)
//        context?.startActivity(intent)
    }


}