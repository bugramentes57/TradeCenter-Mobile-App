package com.bugramentes.tradecenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bugramentes.tradecenter.adapter.FavouritesRecyclerAdapter
import com.bugramentes.tradecenter.model.Urun
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_upload.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var urunArrayList: ArrayList<Urun>
    var isInMyFavourites = false
    var duplicateDel = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
        urunArrayList = ArrayList()

        val docId = intent.getStringExtra("docId")
        val urunBilgi = intent.getStringExtra("urunBilgi")
        val downloadUrl = intent.getStringExtra("downloadUrl").toString()
        val email = intent.getStringExtra("email").toString()


        Picasso.get().load(downloadUrl).into(detailsImage)
        detailsText.text = urunBilgi
        detailsEmail.text = email




    }
    fun addFav(view : View){
        val downloadUrl = intent.getStringExtra("downloadUrl").toString()
        val urunBilgi = intent.getStringExtra("urunBilgi")
        val docId = intent.getStringExtra("docId")

        urunArrayList.clear()

        val favMap = hashMapOf<String, Any>()
        favMap.put("downloadUrl",downloadUrl)
        favMap.put("userEmail",auth.currentUser!!.email!!)
        favMap.put("urunBilgi",urunBilgi.toString())
        favMap.put("date", Timestamp.now())
        favMap.put("docId",docId.toString())

        firestore.collection(auth.currentUser!!.email!!).addSnapshotListener { value, error ->

            if(error!=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{

                if(value != null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        for (document in documents){

                            val urunBilgi = document.get("urunBilgi") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val docId = document.id

                            val urun = Urun(userEmail,urunBilgi,downloadUrl,docId)
                            urunArrayList.add(urun)

                        }
                        for (urun in urunArrayList){
                            if(favMap["docId"] == urun.docId){
                                isInMyFavourites = true
                                if(isInMyFavourites){
                                    break
                                }

                            }
                        }
                        for (document in documents){
                            if(favMap["docId"] == document.get("docId")){
                                firestore.collection(auth.currentUser!!.email!!).document(document.id).delete()

                            }
                        }

                    }
                }

            }

        }
        if(isInMyFavourites){
            Toast.makeText(this,"Bu Ürün Favorilerinizde Mevcut",Toast.LENGTH_LONG).show()

        }else{
            firestore.collection(auth.currentUser!!.email.toString()).add(favMap).addOnSuccessListener {

            }.addOnFailureListener {
                Toast.makeText(this@DetailsActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }









    }

}