package com.bugramentes.tradecenter.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bugramentes.tradecenter.FeedActivity
import com.bugramentes.tradecenter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_giris.*


class GirisFragment : Fragment() {

    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_giris, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpText.setOnClickListener {
            val action = GirisFragmentDirections.actionGirisFragmentToKayitFragment()
            Navigation.findNavController(it).navigate(action)
        }

        goToResetText.setOnClickListener {
            val action = GirisFragmentDirections.actionGirisFragmentToPasswordResetFragment()
            Navigation.findNavController(it).navigate(action)
        }

        val auth = Firebase.auth

        girisButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){

                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                    val intent = Intent(activity,FeedActivity::class.java)
                    startActivity(intent)
                    activity?.finish()

                }.addOnFailureListener {
                    Toast.makeText(activity,it.localizedMessage,Toast.LENGTH_LONG)
                }

            }else{
                Toast.makeText(activity,"Email ve Parolanızı Giriniz",Toast.LENGTH_LONG)
            }
        }

    }


}