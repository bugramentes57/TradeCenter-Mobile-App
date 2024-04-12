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
import com.bugramentes.tradecenter.MainActivity
import com.bugramentes.tradecenter.R
import com.bugramentes.tradecenter.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_kayit.*


class KayitFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kayit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alreadySignInText.setOnClickListener {
            val action = KayitFragmentDirections.actionKayitFragmentToGirisFragment()
            Navigation.findNavController(it).navigate(action)
        }

        auth = Firebase.auth

        kayitButton.setOnClickListener {
            val email = emailText.text.toString()
            val password = passwordText.text.toString()
            val confirmPassword = confirmPasswordText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(password == confirmPassword){

                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                        addUserToDataBase(email,auth.currentUser?.uid!!)
                        val intent = Intent(activity,FeedActivity::class.java)
                        startActivity(intent)
                        activity?.finish()

                    }.addOnFailureListener {
                        Toast.makeText(activity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }

                }else{
                    Toast.makeText(activity,"Parolanız eşleşmiyor",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(activity,"Boş Dlanları Doldurunuz",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun addUserToDataBase(email: String, uid: String) {

        databaseReference = FirebaseDatabase.getInstance().getReference()


        databaseReference.child("User").child(uid).setValue(User(email,uid))

    }
}