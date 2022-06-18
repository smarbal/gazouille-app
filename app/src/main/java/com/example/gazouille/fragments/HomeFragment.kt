package com.example.gazouille.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gazouille.R
import com.example.gazouille.adapters.TweetListAdapter
import com.example.gazouille.listeners.HomeCallback
import com.example.gazouille.listeners.TwitterListenerImpl
import com.example.gazouille.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : GazouilleFragment() {
    protected var tweetsAdapter: TweetListAdapter? = null
    protected var listener: TwitterListenerImpl? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_home, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            listener = TwitterListenerImpl(tweetList, currentUser, callback)

            tweetsAdapter = TweetListAdapter(userId!!, arrayListOf())
            tweetsAdapter?.setListener(listener!!)
            tweetList?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = tweetsAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            //updateList() //load data on launch

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                updateList()
            }
        }

    fun updateList() {
            tweetList?.visibility = View.GONE
            currentUser!!.let {
                val tweets = arrayListOf<Tweet>()

                firebaseDB.collection(DATA_TWEETS).get()
                    .addOnSuccessListener { list ->
                        for (document in list.documents) {
                            val tweet = document.toObject(Tweet::class.java)
                            tweet?.let {
                                if (currentUser?.followUsers?.contains(
                                        it.username
                                    )!! && //timestamp is less than 5 minutes ago
                                    it.timestamp > System.currentTimeMillis() - 300000
                                ) {
                                    it.emphasis = true
                                    //set   the emphasis to true if the user is following the user who posted the tweet (the first user in the list)
                                }
                                tweets.add(it)
                            }
                            updateAdapter(tweets)
                            tweetList?.visibility = View.VISIBLE
                        }
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        tweetList?.visibility = View.VISIBLE
                    }
            }




            }


        private fun updateAdapter(tweets: List<Tweet>) {
            val sortedTweets = tweets.sortedWith(compareByDescending { it.timestamp })
            tweetsAdapter?.updateTweets(removeDuplicates(sortedTweets))
        }



    override fun onResume() {
        super.onResume()
        //updateList()
    }
    fun setUser(user: User?) {
        currentUser = user
        listener?.user = user
    }

        private fun removeDuplicates(originalList: List<Tweet>) = originalList.distinctBy { it.tweetId }

    }
