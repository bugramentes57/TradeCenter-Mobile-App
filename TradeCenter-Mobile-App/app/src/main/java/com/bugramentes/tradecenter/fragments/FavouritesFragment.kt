package com.bugramentes.tradecenter.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugramentes.tradecenter.FavDetailsActivity
import com.bugramentes.tradecenter.R
import com.bugramentes.tradecenter.adapter.FavouritesRecyclerAdapter
import com.bugramentes.tradecenter.adapter.HomeRecycleAdapter
import com.bugramentes.tradecenter.model.Urun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var urunArrayList : ArrayList<Urun>
    private lateinit var favAdapter : FavouritesRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

        urunArrayList = ArrayList<Urun>()

        getData()

        favouritesRecycler.layoutManager = LinearLayoutManager(activity)
        favAdapter = FavouritesRecyclerAdapter(urunArrayList)
        favouritesRecycler.adapter = favAdapter

        favAdapter.setOnItemClickListener(object : FavouritesRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity,FavDetailsActivity::class.java)

                //putExtra Data
                intent.putExtra("docId", urunArrayList[position].docId)
                intent.putExtra("urunBilgi", urunArrayList[position].urunBilgi)
                intent.putExtra("downloadUrl", urunArrayList[position].downloadUrl)
                intent.putExtra("email", urunArrayList[position].email)
                startActivity(intent)
            }

        })

    }

    private fun getData(){
        db.collection(auth.currentUser!!.email!!).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if(error != null){
                Toast.makeText(activity,error.localizedMessage, Toast.LENGTH_LONG).show()
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
                            val docId = document.id

                            val urun = Urun(userEmail,urunBilgi,downloadUrl,docId)

                            urunArrayList.add(urun)



                        }

                        favAdapter.notifyDataSetChanged()



                    }
                }
            }

        }
    }


}