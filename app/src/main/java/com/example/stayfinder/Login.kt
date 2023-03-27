package com.example.stayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private var emailET: EditText? = null
    private var pwET: EditText? = null
    private var signInBtn: AppCompatButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()

        signInBtn?.setOnClickListener {
            val email = emailET?.text.toString()
            val password = pwET?.text.toString()

            val auth: FirebaseAuth = Firebase.auth

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Log.i("userLog", user.toString())

                        Toast.makeText(this, "Authentication success.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initUI() {
        emailET = findViewById(R.id.inputUsernameEt)
        pwET = findViewById(R.id.inputPasswordEt)
        signInBtn = findViewById(R.id.signInBtn)
    }
}