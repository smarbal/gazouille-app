package com.example.gazouille

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null) {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginProgressLayout.setOnTouchListener { v, event -> true } // Intercepts all touches on the progress layout and handles it
    }
    fun setTextChangeListener(et: EditText, til: TextInputLayout) {
        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                til.isErrorEnabled = false
            }
        })
    }


    fun onLogin(v: View){
       var proceed = true
        if (emailET.text.isNullOrEmpty()){
            emailTIL.error = "Please enter an email"
            emailTIL.isErrorEnabled = true
            proceed = false
        }
        if (passwordET.text.isNullOrEmpty()){
            passwordTIL.error = "Please enter a password"
            passwordTIL.isErrorEnabled = true
            proceed = false
        }
        if (proceed){
            loginProgressLayout.visibility = View.VISIBLE // User only sees this and can't interact with the app
            firebaseAuth.signInWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        loginProgressLayout.visibility = View.GONE
                        finish()
                    }
                    else{
                        loginProgressLayout.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login error : ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener(){ e ->
                    e.printStackTrace()
                    loginProgressLayout.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Login error : ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun goToSignup(v: View){

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }
    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }
}