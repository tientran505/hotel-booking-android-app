package com.example.stayfinder.services.login

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
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private var emailET: EditText? = null
    private var pwET: EditText? = null
    private var confirmPwET: EditText? = null

    private var signUpBtn: AppCompatButton? = null
    private var signInBtn: AppCompatButton? = null
    private var ggLoginBtn: MaterialButton? = null

    private var progressDialog: ProgressDialog? = null

    private val RC_SIGN_IN = 9723;
    private lateinit var mGoogleSignInClient: GoogleSignInClient;
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions;

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        initUI()
        createRequest()
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



        ggLoginBtn?.setOnClickListener {
            goToSignIn()
        }

        signInBtn?.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
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
        signInBtn = findViewById(R.id.goSignInPageBtn)
        ggLoginBtn = findViewById(R.id.logInWithGGBtn)
    }

    private fun goToSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun createRequest(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                progressDialog?.setTitle("Login with gmail")
                progressDialog?.setMessage("Verifying...")
                progressDialog?.show()

                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { it ->
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val authUser = auth.currentUser

                            if (authUser != null) {
                                Log.i("chiplog", " Hello ")
                                val db = Firebase.firestore

                                val user = User(authUser)
                                db.collection("users").document(user.uid).set(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "User data added successfully",
                                            Toast.LENGTH_SHORT).show()
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
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                        progressDialog?.dismiss()
                    }

            } catch (e: ApiException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                Log.e("BUG===========", e.message!!)
            }

        }
    }
}