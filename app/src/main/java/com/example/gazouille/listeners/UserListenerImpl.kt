package com.example.gazouille.listeners

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.gazouille.activities.LoginActivity
import com.example.gazouille.activities.ProfileActivity
import com.example.gazouille.activities.TweetActivity
import com.example.gazouille.activities.UserActivity
import com.example.gazouille.util.DATA_USERS
import com.example.gazouille.util.DATA_USER_FOLLOW
import com.example.gazouille.util.DATA_USER_USERNAME
import com.example.gazouille.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserListenerImpl(val userList: RecyclerView, var user: User?, val callback: HomeCallback?) : UserListener {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onButtonClick(username: String?) {
        //start intent to profile activity with username as extra
        val intent = Intent(userList.context, UserActivity::class.java)
        intent.putExtra(DATA_USER_USERNAME, username)
        userList.context.startActivity(intent)
    }
     override fun onLayoutClick(owner: String?) {
         owner?.let {
             if (owner != user?.username) {
                 if (user?.followUsers?.contains(owner) == true) {
                     AlertDialog.Builder(userList.context)
                         .setTitle("Unfollow ${owner}?")
                         .setPositiveButton("yes") { dialog, which ->
                             userList.isClickable = false
                             var followedUsers = user?.followUsers
                             if(followedUsers == null) {
                                 followedUsers = arrayListOf()
                             }
                             followedUsers?.remove(owner)
                             firebaseDB.collection(DATA_USERS).document(userId!!).update(
                                 DATA_USER_FOLLOW, followedUsers)
                                 .addOnSuccessListener {
                                     userList.isClickable = true
                                     callback?.onUserUpdated()
                                 }
                                 .addOnFailureListener {
                                     userList.isClickable = true
                                 }
                         }
                         .setNegativeButton("cancel") { dialog, which -> }
                         .show()
                 } else {
                     AlertDialog.Builder(userList.context)
                         .setTitle("Follow ${owner}?")
                         .setPositiveButton("yes") { dialog, which ->
                             userList.isClickable = false
                             var followedUsers = user?.followUsers
                             if(followedUsers == null) {
                                 followedUsers = arrayListOf()
                             }
                             owner?.let {
                                 followedUsers?.add(owner)
                                 firebaseDB.collection(DATA_USERS).document(userId!!)
                                     .update(DATA_USER_FOLLOW, followedUsers)
                                     .addOnSuccessListener {
                                         userList.isClickable = true
                                         callback?.onUserUpdated()
                                     }
                                     .addOnFailureListener {
                                         userList.isClickable = true
                                     }
                             }
                         }
                         .setNegativeButton("cancel") { dialog, which -> }
                         .show()
                 }
             }
         }
    }


}