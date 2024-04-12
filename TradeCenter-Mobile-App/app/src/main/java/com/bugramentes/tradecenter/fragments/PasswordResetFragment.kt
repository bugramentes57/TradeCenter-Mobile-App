package com.bugramentes.tradecenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bugramentes.tradecenter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_giris.*
import kotlinx.android.synthetic.main.fragment_password_reset.*

class PasswordResetFragment : Fragment() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetCancelButton.setOnClickListener {
            val action = PasswordResetFragmentDirections.actionPasswordResetFragmentToGirisFragment()
            Navigation.findNavController(it).navigate(action)
        }
        auth = Firebase.auth

        resetButton.setOnClickListener {
            if(resetEmailText.text.toString() != "") {
                auth.sendPasswordResetEmail(resetEmailText.text.toString()).addOnSuccessListener {
                    Toast.makeText(activity,"Emailinizi Kontrol Edin",Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(activity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(activity,"Email Girin",Toast.LENGTH_SHORT).show()
            }
        }
    }


}