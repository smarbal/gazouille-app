package com.example.gazouille.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gazouille.R
import com.example.gazouille.fragments.GazouilleFragment
import com.example.gazouille.fragments.HomeFragment
import com.example.gazouille.fragments.MyActivityFragment
import com.example.gazouille.fragments.SearchFragment
import com.example.gazouille.listeners.HomeCallback
import com.example.gazouille.util.DATA_USERS
import com.example.gazouille.util.User
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeCallback {

    private var sectionsPagerAdapter: SectionPagerAdapter? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val myActivityFragment = MyActivityFragment()
    private var userId = firebaseAuth.currentUser?.uid
    private var user: User? = null
    private var currentFragment: GazouilleFragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sectionsPagerAdapter = SectionPagerAdapter(supportFragmentManager)

        container.adapter = sectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs)) //change container content based on tab change
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        //attach tabs to container so that you can swipe in the container and it will change the tab
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {   //Perform operation when new page selected
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {
                        titleBar.visibility = View.VISIBLE
                        titleBar.text = "Home"
                        searchBar.visibility = View.GONE
                        currentFragment = homeFragment
                    }
                    1 -> {
                        titleBar.visibility = View.GONE
                        searchBar.visibility = View.VISIBLE
                        currentFragment = searchFragment
                    }
                    2 -> {

                        titleBar.visibility = View.VISIBLE
                        titleBar.text = "My Activity"
                        searchBar.visibility = View.GONE
                        currentFragment = myActivityFragment
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        logo.setOnClickListener { view ->
                startActivity(ProfileActivity.newIntent(this))
        }

        fab.setOnClickListener {
            startActivity(TweetActivity.newIntent(this, userId, user?.username))
        }
        homeProgressLayout.setOnTouchListener { v, event -> true }

        search.setOnEditorActionListener{v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH){
            searchFragment.searchUser(v?.text.toString())
        }
        true
        }
    }




    override fun onResume() {
        super.onResume()
        userId = firebaseAuth.currentUser?.uid
        if(userId == null) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        }
        else {
            populate()
        }
    }

    fun populate() {
        homeProgressLayout.visibility = View.VISIBLE
        firebaseDB.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                homeProgressLayout.visibility = View.GONE
                user = documentSnapshot.toObject(User::class.java)
                updateFragmentUser()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                finish()
            }
    }
    override fun onUserUpdated(){
        populate()
    }

    override fun onRefresh() {
        homeFragment.updateList()
    }

    fun updateFragmentUser() {
        homeFragment.setUser(user)
         searchFragment.setUser(user)
//        myActivityFragment.setUser(user)
    }
    inner class SectionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> homeFragment
                1 -> searchFragment
                else -> myActivityFragment
            }
        }
        override fun getCount(): Int {
            return 3
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}