package com.example.gazouille.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gazouille.R
import com.example.gazouille.listeners.TweetListener
import com.example.gazouille.listeners.UserListener
import com.example.gazouille.util.Tweet
import com.example.gazouille.util.User
import com.example.gazouille.util.getDate
import com.example.gazouille.util.loadUrl

class UserListAdapter (val searchedUsers : ArrayList<User>) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private var listener: UserListener? = null // Listen to actions on the user

    class UserViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        private val layout = view.findViewById<ViewGroup>(R.id.userLayout)
        private val username = view.findViewById<TextView>(R.id.userUsername)


        fun bind(user : User, listener: UserListener?) { //attach information to the layout
            username.text = user.username

            layout.setOnClickListener {
                listener?.onLayoutClick(user)
            }
        }

    }

    fun setListener(listener: UserListener) {
        this.listener = listener
    }

    //create view holder, inflate the layout, and return the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(searchedUsers[position], listener)
    }


    override fun getItemCount() = searchedUsers.size
}