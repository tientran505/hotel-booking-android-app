package com.example.stayfinder.services.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import com.example.stayfinder.AnonymousUser
import com.example.stayfinder.R
import com.example.stayfinder.user.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var signOutBtn: Button? = null
    private var viewProfileBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        signOutBtn = view.findViewById(R.id.signOutBtn)
        viewProfileBtn = view.findViewById(R.id.viewProfileBtn)
        signOutBtn?.setOnClickListener {
            Firebase.auth.signOut()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, ProfileFragment())
                .commit()
        }
        viewProfileBtn?.setOnClickListener{
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser;

        //Check user verify
        if(user!= null && !user.isEmailVerified){ // Login but email haven't verify
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), SignUp::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        signOutBtn?.isVisible = (user != null)

        val childFragment = if (user == null)
        { AnonymousUser() }
        else
        {
            LoginUser(user)

        }

        val transaction = childFragmentManager.beginTransaction()

        transaction.replace(R.id.profile_fragment, childFragment).commit()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}