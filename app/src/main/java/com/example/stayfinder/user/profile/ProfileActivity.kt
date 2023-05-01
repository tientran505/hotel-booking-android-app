package com.example.stayfinder.user.profile

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.MainActivity
import com.example.stayfinder.R
import com.example.stayfinder.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URL
import java.util.concurrent.TimeUnit


class ProfileActivity : AppCompatActivity() {
    lateinit var displaynameET: EditText
    private var progressDialog: ProgressDialog? = null
    lateinit var phoneET: EditText
    lateinit var emailET: EditText
    private val RC_SIGN_IN = 9723;
    lateinit var displaynameLayout: View
    lateinit var verifyImg: ImageView
    lateinit var avarImg: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var editBtn: Button
    lateinit var saveBtn: Button
    lateinit var cancelBtn: Button
    lateinit var displaynameTv: TextView
    lateinit var editImg: ImageView
    val db = Firebase.firestore
    val storageRef = Firebase.storage.reference
    private var activityResultLauncher: ActivityResultLauncher<Array<String>>
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val auth = Firebase.auth
    var imageUri: Uri? = null
    private lateinit var mGoogleSignInClient: GoogleSignInClient;
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions;
    init {
        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if (allAreGranted) {
                pickImageFromGallery()
            }
        }
    }

    val PICK_IMAGE = 1

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }
    val appPerms = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    private fun goToSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun createRequest(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initActionBar()
        println(user)
        displaynameLayout = findViewById(R.id.displaynameLayout)
        displaynameET = findViewById(R.id.displaynameET)
        phoneET = findViewById(R.id.phoneET)
        emailET = findViewById(R.id.emailET)
        verifyImg = findViewById(R.id.emailVerified)
        avarImg = findViewById(R.id.avarImg)
        editBtn = findViewById(R.id.EditBtn)
        saveBtn = findViewById(R.id.SaveBtn)
        editImg = findViewById(R.id.editImg)
        cancelBtn = findViewById(R.id.cancelBtn)
        displaynameTv = findViewById(R.id.displayname)
        progressBar = findViewById(R.id.savedListPB)
//        phoneBtn = findViewById(R.id.phoneBtn)
        displaynameET.setEnabled(false);
        phoneET.setEnabled(false);

        progressBar.visibility = View.GONE
        editImg.visibility = View.GONE
        saveBtn.visibility = View.GONE
        displaynameTv.setText(user!!.displayName)
        cancelBtn.visibility = View.GONE
        displaynameET.setText(user.displayName)
        emailET.setText(user.email)
        phoneET.setText(user.phoneNumber)
//        phoneBtn.visibility=View.GONE
        progressDialog = ProgressDialog(this)
        createRequest()
        if(user.isEmailVerified == false){
            verifyImg.visibility= View.GONE
        }
        Glide.with(this)
            .load(URL(user.photoUrl.toString()))
            .apply(RequestOptions().centerCrop())
            .into(avarImg)

        editBtn.setOnClickListener{
//            phoneBtn.visibility=View.VISIBLE
            displaynameET.setEnabled(true);
            phoneET.setEnabled(true);

            editImg.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            cancelBtn.visibility = View.VISIBLE
            displaynameTv.visibility = View.GONE
            editBtn.visibility = View.GONE
            editImg.setOnClickListener{
                activityResultLauncher.launch(appPerms)
            }
            avarImg.setOnClickListener{
                activityResultLauncher.launch(appPerms)
            }
        }
        saveBtn.setOnClickListener{
//            phoneBtn.visibility=View.GONE
            progressBar.visibility = View.VISIBLE
            displaynameET.setEnabled(false);
            phoneET.setEnabled(false);
            editImg.visibility = View.GONE
            saveBtn.visibility = View.GONE
            editBtn.visibility = View.GONE
            cancelBtn.visibility = View.GONE
            displaynameTv.visibility = View.VISIBLE
            if(imageUri != null) {
                val riversRef = storageRef.child("user_avatar/${user.uid}" + "avatar.jpg")
                var uploadTask = riversRef.putFile(imageUri!!)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    riversRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUrl)
                            .build()
                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User profile photo updated.")
                                } else {
                                    Log.w(TAG, "User profile photo not updated.", task.exception)
                                }
                            }
                    } else {
                    }
                }
            }
            else if(user.displayName != displaynameET.text.toString()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displaynameET.text.toString())
                    .build()
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User profile profile updated.")
                        } else {
                            Log.w(TAG, "User profile photo not updated.", task.exception)
                        }
                    }
            }
            else if(user.email != emailET.text.toString()){
                user!!.updateEmail(emailET.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User email address updated: {${user.email}")
                        } else {
                            Log.w(TAG, "User email address not updated.", task.exception)
                        }
                    }
            }
            else if(user.phoneNumber != phoneET.text.toString()){
                val credential = PhoneAuthProvider.getCredential(phoneET.text.toString(), "12342456") // Replace with the verification code received by the user
                user.updatePhoneNumber(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Phone number updated successfully")
                        } else {
                            Log.w(TAG, "Phone number update failed", task.exception)
                        }
                    }
                db.collection("users").document(user.uid)
                    .set(User(user))
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "DocumentSnapshot successfully written!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            }
            editBtn.visibility=View.VISIBLE
            progressBar.visibility = View.GONE
            phoneET.setText(user.phoneNumber)
            displaynameTv.setText(user.displayName)
        }
        cancelBtn.setOnClickListener{
//            phoneBtn.visibility=View.GONE
            displaynameET.setEnabled(false);
            phoneET.setEnabled(false);
            editImg.visibility = View.GONE
            saveBtn.visibility = View.GONE
            editBtn.visibility = View.GONE
            cancelBtn.visibility = View.GONE
            displaynameTv.visibility = View.VISIBLE
            displaynameTv.setText(user.displayName)
            displaynameET.setText(user.displayName)
            emailET.setText(user.email)
//            phoneTv.setText(user.phoneNumber)
            if(user.isEmailVerified == false){
                verifyImg.visibility= View.GONE
            }
            else{
                verifyImg.visibility= View.VISIBLE
            }
            Glide.with(this)
                .load(URL(user.photoUrl.toString()))
                .apply(RequestOptions().centerCrop())
                .into(avarImg)
        }
        editBtn.visibility=View.VISIBLE
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    fun enableUserManuallyInputCode(verificationId: String) {
        // Show a dialog or view that allows the user to manually input the verification code.
        // This could be a simple EditText field or a custom UI component.
        val verificationCodeEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Enter Verification Code")
            .setMessage("Please enter the verification code sent to your phone.")
            .setView(verificationCodeEditText)
            .setPositiveButton("Submit") { _, _ ->
                val verificationCode = verificationCodeEditText.text.toString()
                // Call the PhoneAuthProvider.verifyPhoneNumber() method again with the entered code.
                val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
                signInWithPhoneAuthCredential(credential)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancellation
            }
            .create()
        dialog.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            Glide.with(this)
                .load(imageUri!!)
                .into(avarImg);
            return
        }
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)

                progressDialog?.setTitle("Change gmail")
                progressDialog?.setMessage("Verifying...")
                progressDialog?.show()

                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { it ->
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val authUser = auth.currentUser

                            if (authUser != null) {
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

    private fun initActionBar() {
        val menu = supportActionBar
        menu?.setDisplayHomeAsUpEnabled(true)
        menu?.setHomeButtonEnabled(true)
        menu?.title = "Profile" // title of activity
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}


