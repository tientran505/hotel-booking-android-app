package com.example.stayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private var emailET: EditText? = null
    private var pwET: EditText? = null
    private var confirmPwET: EditText? = null

    private var signUpBtn: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initUI()

        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)

        signUpBtn?.setOnClickListener {
            val auth: FirebaseAuth = Firebase.auth

            val email = emailET?.text.toString()
            val password = pwET?.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast
                            .makeText(this, "createUserWithEmail:success", Toast.LENGTH_LONG).show()
//                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        emailET = findViewById(R.id.inputUsernameEt)
        pwET = findViewById(R.id.inputPasswordEt)
        confirmPwET = findViewById(R.id.inputConfirmPw)
        signUpBtn = findViewById(R.id.signUpBtn)
    }
}