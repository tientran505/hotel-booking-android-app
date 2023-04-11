package com.example.stayfinder.services.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.stayfinder.R
import com.google.firebase.auth.UserProfileChangeRequest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginUser.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginUser() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var nameTV: TextView? = null
    private var nameUser: String? = null

    private lateinit var avatarImg: ShapeableImageView
    private var user: FirebaseUser? = null

    private lateinit var imageUri: Uri

    constructor(name: String) : this() {
        this.nameUser = name
    }

    constructor(user: FirebaseUser): this() {
        this.user = user
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    val appPerms = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>
    init {
        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }

            if(allAreGranted) {
                pickImageFromGallery()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login_user, container, false)

        nameTV = view.findViewById(R.id.nameTV)

        avatarImg = view.findViewById(R.id.avtImgv)




        if (this.user?.photoUrl != null) {
//            Picasso.with(this.context).load(this.user?.photoUrl).into(avatarImg)
//            avatarImg?.setImageURI(this.user?.photoUrl)
            Glide.with(this)
                .load(this.user?.photoUrl)
                .apply(RequestOptions().centerCrop())
                .into(avatarImg)
        }

        avatarImg.setOnClickListener {
            activityResultLauncher.launch(appPerms)
        }

        nameTV?.text = if (this.user?.displayName != null && this.user?.displayName != "")
        { this.user?.displayName }
        else {this.user?.email}


        return view
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun uploadImg() {
        val storageRef = Firebase.storage.reference
        val imgRef = storageRef.child("imgs")
        val imageRef = imgRef.child("${user?.uid}-avatar.jpg")

        val uploadTask = imageRef.putFile(imageUri)

        uploadTask
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                imageRef.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val imgUrl = it.result.toString()
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(imgUrl))
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(requireContext(), "Profile picture updated successfully"
                                        , Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    Toast.makeText(requireContext(), "Failed to update profile picture"
                                        , Toast.LENGTH_SHORT).show()
                                }
                            }

                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error uploading image", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            imageUri = data?.data!!

            Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions().centerCrop())
                .into(avatarImg)

            uploadImg()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "Permission denied to read external storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginUser.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginUser().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val REQUEST_CODE = 1752
        private const val PERMISSION_REQUEST_CODE = 241
    }
}