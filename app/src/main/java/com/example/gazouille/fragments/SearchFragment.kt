package com.example.gazouille.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gazouille.R
import com.example.gazouille.adapters.UserListAdapter
import com.example.gazouille.listeners.UserListener
import com.example.gazouille.listeners.UserListenerImpl
import com.example.gazouille.util.DATA_TWEETS
import com.example.gazouille.util.DATA_USERS
import com.example.gazouille.util.DATA_USER_USERNAME
import com.example.gazouille.util.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : GazouilleFragment() {

    protected var usersAdapter : UserListAdapter? = null
    protected var listener : UserListenerImpl? = null
    private var searchedUser : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = UserListenerImpl(userList, currentUser, callback)
        usersAdapter = UserListAdapter(arrayListOf())
        usersAdapter?.setListener(listener!!)


        userList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = usersAdapter
        }}

    fun searchUser(term:String){
        searchedUser = term
        updateList()

    }

    fun updateList() {
        userList?.visibility = View.GONE
        searchedUser?.let {
            firebaseDB.collection(DATA_USERS).whereGreaterThanOrEqualTo(DATA_USER_USERNAME,
                searchedUser!!
            ).get()
                .addOnSuccessListener { result ->
                    userList?.visibility = View.VISIBLE
                    val users : ArrayList<User> = arrayListOf<User>()
                    for (document in result) {
                        val user = document.toObject(User::class.java)
                        user?.let{users.add(user)}
                    }
                    usersAdapter?.updateUsers(users)
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: ${exception.message}")
                }
        }
    }

    fun setUser(user: User?) {
        currentUser = user
        listener?.user = user
    }

}