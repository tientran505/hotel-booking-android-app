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
import com.example.stayfinder.SavedList
import com.example.stayfinder.saved_lists
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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.util.concurrent.TimeUnit


class ProfileActivity : AppCompatActivity() {
    lateinit var displaynameET: EditText
//    lateinit var phoneET: EditText
//    lateinit var emailET: EditText
    lateinit var displaynameLayout: View
//    lateinit var verifyImg: ImageView
    lateinit var avarImg: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var editBtn: Button
    lateinit var saveBtn: Button
    lateinit var cancelBtn: Button
    lateinit var displaynameTv: TextView
    lateinit var editImg: ImageView
//    lateinit var emaillayout: View
//    lateinit var channgeEmailBtn:Button
    val db = Firebase.firestore
    val storageRef = Firebase.storage.reference
    private var activityResultLauncher: ActivityResultLauncher<Array<String>>
    private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val auth = Firebase.auth
    var imageUri: Uri? = null
//    lateinit var editTextTextPassword: EditText
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

    fun renderView(){
        displaynameET.setEnabled(false);
//        phoneET.setEnabled(false);
//        emailET.setEnabled(false);
//        editTextTextPassword.setEnabled(false);
        editImg.visibility = View.GONE
        saveBtn.visibility = View.GONE
        editBtn.visibility = View.VISIBLE
        cancelBtn.visibility = View.GONE
//        channgeEmailBtn.visibility = View.GONE
        displaynameTv.setText(user.displayName)
        displaynameET.setText(user.displayName)
//        phoneET.setText(user.phoneNumber)
//        if(user.email == null){
//            emaillayout.visibility = View.GONE
//        }
//        else{
//            emailET.setText(user?.email)
//        }
//        if(user.isEmailVerified == false){
//            verifyImg.visibility= View.GONE
//        }
        Glide.with(this)
            .load(URL(user.photoUrl.toString()))
            .apply(RequestOptions().centerCrop())
            .into(avarImg)
        progressBar.visibility = View.GONE
    }
    fun renderEditView(){
        displaynameET.setEnabled(true);
//        phoneET.setEnabled(true)
//        emailET.setEnabled(true);
//        editTextTextPassword.setEnabled(true)
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
//        channgeEmailBtn.visibility = View.VISIBLE
        val providerData = user.providerData
        val passwordProvider = providerData.find { it.providerId == "password" }
        if (passwordProvider == null) {
            // If the user doesn't have a password set, show an error message
//            emaillayout.visibility = View.GONE
            Toast.makeText(this, "The user does not have a password set.", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initActionBar()
        println(user?.let { User(it) })
        displaynameLayout = findViewById(R.id.displaynameLayout)
        displaynameET = findViewById(R.id.displaynameET)
//        phoneET = findViewById(R.id.phoneET)
//        emailET = findViewById(R.id.emailET)
//        verifyImg = findViewById(R.id.emailVerified)
        avarImg = findViewById(R.id.avarImg)
        editBtn = findViewById(R.id.EditBtn)
        saveBtn = findViewById(R.id.SaveBtn)
        editImg = findViewById(R.id.editImg)
//        emaillayout = findViewById(R.id.emaillayout)
//        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        cancelBtn = findViewById(R.id.cancelBtn)
        displaynameTv = findViewById(R.id.displayname)
        progressBar = findViewById(R.id.savedListPB)
//        channgeEmailBtn = findViewById(R.id.channgeEmailBtn)

        renderView()

        editBtn.setOnClickListener {
            renderEditView()
        }
        saveBtn.setOnClickListener {
            if (imageUri != null) {
                println("imageUri")
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
                                    Glide.with(this)
                                        .load(imageUri!!)
                                        .into(avarImg);
                                } else {
                                    Log.w(TAG, "User profile photo not updated.", task.exception)
                                }
                            }
                    } else {
                    }
                }
            }
            if (user.displayName != displaynameET.text.toString()) {
                println("displayName")
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displaynameET.text.toString())
                    .build()
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User profile profile updated.")
                            displaynameTv.setText(user!!.displayName)
                            displaynameET.setText(user.displayName)
                        } else {
                            Log.w(TAG, "User profile photo not updated.", task.exception)
                        }
                    }
                displaynameTv.setText(user!!.displayName)
                displaynameET.setText(user.displayName)
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


            progressBar.visibility = View.GONE
            renderView()
        }


        cancelBtn.setOnClickListener {
            renderView()
        }
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
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }
    val appPerms = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
}


