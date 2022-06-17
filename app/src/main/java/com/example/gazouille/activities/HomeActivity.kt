package com.example.gazouille.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gazouille.R
import com.example.gazouille.fragments.HomeFragment
import com.example.gazouille.fragments.MyActivityFragment
import com.example.gazouille.fragments.SearchFragment
import com.example.gazouille.util.User
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var sectionsPagerAdapter: SectionPagerAdapter? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val myActivityFragment = MyActivityFragment()
    private var userId = firebaseAuth.currentUser?.uid
    private var user: User? = null

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
                TODO("Not yet implemented")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }
        })

        logo.setOnClickListener { view ->
                startActivity(ProfileActivity.newIntent(this))
        }

        fab.setOnClickListener {
            startActivity(TweetActivity.newIntent(this, userId, user?.username))
        }
    }




    override fun onResume() {
        super.onResume()
        userId = firebaseAuth.currentUser?.uid
        if(userId == null) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        }
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