package com.example.gazouille.listeners

import com.example.gazouille.util.Tweet

interface TweetListener {
    fun onLayoutClick(tweet: Tweet?)
    fun onLikeClick(tweet: Tweet?)
}