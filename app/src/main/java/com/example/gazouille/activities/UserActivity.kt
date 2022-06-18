package com.example.gazouille.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gazouille.R
import com.example.gazouille.adapters.TweetListAdapter
import com.example.gazouille.adapters.UserListAdapter
import com.example.gazouille.listeners.HomeCallback
import com.example.gazouille.listeners.TwitterListenerImpl
import com.example.gazouille.listeners.UserListenerImpl
import com.example.gazouille.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

class UserActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()
    protected var tweetsAdapter: TweetListAdapter? = null
    protected var listener: TwitterListenerImpl? = null
    private var username: String? = null
    protected var callback: HomeCallback? = null
    protected var currentUser: User? = null
    // getIntent() is a method from the started activity



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        username = intent.getStringExtra(DATA_USER_USERNAME) // will return "FirstKeyValue"

        if (username == null) {
            finish()
        }

        tweetsAdapter = TweetListAdapter(username!!, arrayListOf())
        listener = TwitterListenerImpl(tweetsProfile, currentUser, callback)


        tweetsAdapter?.setListener(listener!!)
        tweetsProfile?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tweetsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        //updateList() //load data on launch

        profileRefresh.setOnRefreshListener {
            profileRefresh.isRefreshing = false
            populateInfo()
        }

        populateInfo()


    }



    fun populateInfo(){
        usernameProfile.text = username
        firebaseDB.collection(DATA_TWEETS).whereEqualTo("username", username).get()
            .addOnSuccessListener { documentSnapshot ->
                val posts = documentSnapshot.toObjects(Tweet::class.java)
                if (posts != null) {
                    val sortedTweets = posts.sortedWith(compareByDescending { it.timestamp })
                    tweetsAdapter?.updateTweets(posts)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.localizedMessage}", Toast.LENGTH_LONG).show()
                profileProgressLayout.visibility = View.GONE
            }
    }

    override fun onResume() {
        super.onResume()
        //updateList()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileActivity::class.java)
    }
}