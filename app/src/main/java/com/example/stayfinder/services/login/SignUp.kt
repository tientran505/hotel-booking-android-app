package com.example.stayfinder.services.login

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private var emailET: EditText? = null
    private var pwET: EditText? = null
    private var confirmPwET: EditText? = null

    private var signUpBtn: AppCompatButton? = null

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        initUI()

        progressDialog = ProgressDialog(this)

        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)


        signUpBtn?.setOnClickListener {
            val auth: FirebaseAuth = Firebase.auth

            val email = emailET?.text.toString()
            val password = pwET?.text.toString()

            progressDialog?.setTitle("Register Loading")
            progressDialog?.setMessage("Registering...")
            progressDialog?.show()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "createUserWithEmail:success", Toast.LENGTH_LONG).show()
                        val authUser = auth.currentUser

                        if (authUser != null) {
                            val db = Firebase.firestore

                            val user = User(authUser)
                            db.collection("users").document(user.uid).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User data added successfully",
                                    Toast.LENGTH_SHORT).show()


                                    authUser!!.sendEmailVerification().addOnCompleteListener{
                                        if (task.isSuccessful) {
                                            Toast.makeText(this
                                                , "Please check your email for verification"
                                                , Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        else{
                                            Toast.makeText(this
                                                , "Cannot send email verify. Try again in minutes"
                                                , Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error adding user data with" +
                                            " exception: $it",
                                        Toast.LENGTH_SHORT).show()
                                }
                        }
                        Handler().postDelayed(Runnable {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("fragment", "profile")
                            startActivity(intent)
                            finishAffinity()
                        }, 1000)
                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
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
        confirmPwET = findViewById(R.id.inputConfirmPw)
        signUpBtn = findViewById(R.id.signUpBtn)
    }
}