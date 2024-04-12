package com.bugramentes.tradecenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugramentes.tradecenter.adapter.HomeRecycleAdapter
import com.bugramentes.tradecenter.adapter.MyStuffRecyclerAdapter
import com.bugramentes.tradecenter.model.Urun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_stuff.*
import kotlinx.android.synthetic.main.fragment_home.*

class MyStuffActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var urunArrayList : ArrayList<Urun>
    private lateinit var myStuffAdapter : MyStuffRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_stuff)

        auth = Firebase.auth
        db = Firebase.firestore

        urunArrayList = ArrayList<Urun>()

        getData()

        myStuffRecycler.layoutManager = LinearLayoutManager(this)
        myStuffAdapter = MyStuffRecyclerAdapter(urunArrayList)
        myStuffRecycler.adapter = myStuffAdapter

        myStuffAdapter.setOnItemClickListener(object : MyStuffRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val docId = urunArrayList[position].docId
                db.collection("Urunler").document(docId)
                    .delete().addOnSuccessListener {

                    }.addOnFailureListener {
                        Toast.makeText(this@MyStuffActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
            }

        })

    }
    private fun getData(){
        db.collection("Urunler").whereEqualTo("userEmail",auth.currentUser!!.email.toString()).addSnapshotListener { value, error ->

            if(error != null){
                Toast.makeText(this,error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(value != null) {
                    if(!value.isEmpty){

                        val documents = value.documents

                        urunArrayList.clear()

                        for(document in documents){
                            //casting
                            val urunBilgi = document.get("urunBilgi") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val docId = document.id.toString()

                            val urun = Urun(userEmail,urunBilgi,downloadUrl,docId)
                            urunArrayList.add(urun)

                        }

                        myStuffAdapter.notifyDataSetChanged()

                    }
                }
            }

        }

}   }