package com.example.gazouille

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.gazouille.util.DATA_USERS
import com.example.gazouille.util.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.emailET
import kotlinx.android.synthetic.main.activity_signup.emailTIL
import kotlinx.android.synthetic.main.activity_signup.passwordET
import kotlinx.android.synthetic.main.activity_signup.passwordTIL

class SignupActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if (user != null) {
            startActivity(HomeActivity.newIntent(this))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setTextChangeListener(usernameET, usernameTIL)
        setTextChangeListener(emailET, emailTIL)
        setTextChangeListener(passwordET, passwordTIL)

        signupProgressLayout.setOnTouchListener { v, event -> true }
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
    companion object {
        fun newIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }

    fun onSignup(v: View) {

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
        if (usernameET.text.isNullOrEmpty()){
            usernameTIL.error = "Please enter a username"
            usernameTIL.isErrorEnabled = true
            proceed = false
        }
        if (proceed){
            signupProgressLayout.visibility = View.VISIBLE // User only sees this and can't interact with the app
            firebaseAuth.createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val email = emailET.text.toString()
                        val name = usernameET.text.toString()
                        val user = User(email, name, "", arrayListOf())
                        firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
                    }
                    else{
                        Toast.makeText(this@SignupActivity, "Signup error : ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                    signupProgressLayout.visibility = View.GONE
                }
                    // Manage error
                .addOnFailureListener(){ e ->
                    e.printStackTrace()
                    loginProgressLayout.visibility = View.GONE
                    Toast.makeText(this@SignupActivity, "Signup error : ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    fun goToLogin(v: View) {
        startActivity(LoginActivity.newIntent(this))
        finish()
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