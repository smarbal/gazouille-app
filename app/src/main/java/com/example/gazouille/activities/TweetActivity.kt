package com.example.gazouille.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.gazouille.R
import com.example.gazouille.util.DATA_TWEETS
import com.example.gazouille.util.Tweet
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_tweet.*

class TweetActivity : AppCompatActivity() {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val imageUrl : String? = null
    private var userId : String? = null
    private var userName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)

        userId = intent.getStringExtra(PARAM_USER_ID)
        userName = intent.getStringExtra(PARAM_USER_NAME)

        tweetProgressLayout.setOnTouchListener { v, event -> true }
    }

    fun addImage(view: View) {
        // add image to the firebase database

    }

    fun postTweet(view: View){
        tweetProgressLayout.visibility = View.VISIBLE
        val text :String = tweetText.text.toString()
        val tweetId = firebaseDB.collection(DATA_TWEETS).document()
        val tweet = Tweet(tweetId.id, arrayListOf(userId!!), userName, text, imageUrl, System.currentTimeMillis(), arrayListOf())
        tweetId.set(tweet)
            .addOnCompleteListener{finish()}
            .addOnFailureListener {
                tweetProgressLayout.visibility = View.GONE
                Toast.makeText(this,"Failed to post the tweet", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        val PARAM_USER_ID = "User_id"
        val PARAM_USER_NAME = "Username"
        fun newIntent(context: Context, userId: String?, userName: String?): Intent {
            val intent = Intent(context, TweetActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userId)
            intent.putExtra(PARAM_USER_NAME, userName)
            return intent
        }
    }

}