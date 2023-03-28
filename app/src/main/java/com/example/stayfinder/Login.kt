package com.example.stayfinder

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

class Login : AppCompatActivity() {
    private var emailET: EditText? = null
    private var pwET: EditText? = null
    private var signInBtn: AppCompatButton? = null

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()

        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)

        progressDialog = ProgressDialog(this)

        signInBtn?.setOnClickListener {
            val email = emailET?.text.toString()
            val password = pwET?.text.toString()

            val auth: FirebaseAuth = Firebase.auth

            progressDialog?.setTitle("Please wait")
            progressDialog?.setMessage("Verifying...")
            progressDialog?.show()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Log.i("userLog", user.toString())

                        Toast.makeText(this, "Login successful.",
                            Toast.LENGTH_SHORT).show()

                        Handler().postDelayed(Runnable {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("fragment", "profile")
                            startActivity(intent)
                            finishAffinity()
                        }, 2000)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                    progressDialog?.dismiss()
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
        signInBtn = findViewById(R.id.signInBtn)
    }
}