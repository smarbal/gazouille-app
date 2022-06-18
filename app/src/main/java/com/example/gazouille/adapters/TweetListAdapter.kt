package com.example.gazouille.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gazouille.R
import com.example.gazouille.listeners.TweetListener
import com.example.gazouille.util.Tweet
import com.example.gazouille.util.getDate
import com.example.gazouille.util.loadUrl
import java.text.DateFormat

class TweetListAdapter(val userId : String, val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetListAdapter.TweetViewHolder>() {

    private var listener: TweetListener? = null // Listen to actions on the tweet

    class TweetViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        private val layout = view.findViewById<ViewGroup>(R.id.tweetLayout)
        private val username = view.findViewById<TextView>(R.id.tweetUsername)
        private val text = view.findViewById<TextView>(R.id.tweetText)
        private val image = view.findViewById<ImageView>(R.id.tweetImage)
        private val date = view.findViewById<TextView>(R.id.tweetDate)
        private val like = view.findViewById<ImageView>(R.id.tweetLike)
        private val likeCount = view.findViewById<TextView>(R.id.tweetLikeCount)
        private val emphasis = view.findViewById<TextView>(R.id.textEmphasis)

        fun bind(userId: String, tweet: Tweet, listener: TweetListener?) { //attach information to the layout
            username.text = tweet.username
            text.text = tweet.text
            if (tweet.imageUrl.isNullOrEmpty()) {
                image.visibility = View.GONE
            }
            else{
                image.visibility = View.VISIBLE
                image.loadUrl(tweet.imageUrl)
            }
            date.text = getDate(tweet.timestamp)
            likeCount.text = tweet.likes?.size.toString()

            if(tweet.emphasis){
                emphasis.visibility = View.VISIBLE
            }
            else{
                emphasis.visibility = View.GONE
            }
            layout.setOnClickListener {
                listener?.onLayoutClick(tweet)
            }
            like.setOnClickListener({
                listener?.onLikeClick(tweet)  // We know if the like was clicked
            })

            if(tweet.likes?.contains(userId) == true) {
                like.setImageDrawable(ContextCompat.getDrawable(like.context, R.drawable.like))
            }
            else {
                like.setImageDrawable(ContextCompat.getDrawable(like.context, R.drawable.like_inactive))
            }
        }

    }

    fun setListener(listener: TweetListener) {
        this.listener = listener
    }

    fun updateTweets(newTweets: List<Tweet>) {
        tweets.clear()
        tweets.addAll(newTweets)
        notifyDataSetChanged()  //notify the adapter that the data has changed
    }
    //create view holder, inflate the layout, and return the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TweetViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
    )

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(userId, tweets[position], listener)
        }


    override fun getItemCount() = tweets.size
}