package com.example.stayfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mAuth: FirebaseAuth;
    val RC_SIGN_IN = 9723;
    private lateinit var mGoogleSignInClient: GoogleSignInClient;
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions;

    private lateinit var view:View;
    private lateinit var logInGGBtn: Button;
    private lateinit var switch2SignUpBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mAuth = FirebaseAuth.getInstance()
    }

    fun createRequest(){
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), mGoogleSignInOptions)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log_in, container, false)
        logInGGBtn = view.findViewById(R.id.logInWithGGBtn);
        switch2SignUpBtn = view.findViewById(R.id.goSignUpPageBtn)
        createRequest()

        logInGGBtn!!.setOnClickListener {
            goToSignIn()
        }

        switch2SignUpBtn!!.setOnClickListener {

            var transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction!!.replace(R.id.frameLayout,SignUpFragment());
            transaction.commit()
        }

        return view
    }

    private fun goToSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                .getSignedInAccountFromIntent(data)

            try {
                task.getResult(ApiException::class.java)
                //Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT).show()

                //reload activity

                requireActivity().finish()
                requireActivity().startActivity(requireActivity().intent)

            }
            catch (e: ApiException){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                Log.e("BUG===========",e.message!!)
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
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}