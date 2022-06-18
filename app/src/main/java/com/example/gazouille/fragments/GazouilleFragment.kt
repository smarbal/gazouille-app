package com.example.gazouille.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.gazouille.listeners.HomeCallback
import com.example.gazouille.listeners.TwitterListenerImpl
import com.example.gazouille.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class GazouilleFragment : Fragment(){
    protected var callback: HomeCallback? = null
    protected var currentUser: User? = null
    protected val firebaseDB = FirebaseFirestore.getInstance()
    protected val userId = FirebaseAuth.getInstance().currentUser?.uid


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeCallback){
            callback = context
        }
        else {
            throw ClassCastException("$context must implement HomeCallback")
        }
    }

}