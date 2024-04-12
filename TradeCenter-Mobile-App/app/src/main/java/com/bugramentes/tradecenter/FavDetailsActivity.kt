package com.bugramentes.tradecenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bugramentes.tradecenter.fragments.FavouritesFragment
import com.bugramentes.tradecenter.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_fav_details.*

class FavDetailsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_details)

        auth = Firebase.auth
        db = Firebase.firestore


        val docId = intent.getStringExtra("docId")
        val urunBilgi = intent.getStringExtra("urunBilgi")
        val downloadUrl = intent.getStringExtra("downloadUrl").toString()
        val email = intent.getStringExtra("email").toString()

        Picasso.get().load(downloadUrl).into(favImage)
        favText.text = urunBilgi
        favEmail.text = email

    }
    fun delFav(view:View){

        val docId = intent.getStringExtra("docId")
        db.collection(auth.currentUser!!.email!!).document(docId.toString()).delete().addOnSuccessListener {

            val intent = Intent(this@FavDetailsActivity,MainActivity::class.java)
            startActivity(intent)

        }.addOnFailureListener {
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }
}