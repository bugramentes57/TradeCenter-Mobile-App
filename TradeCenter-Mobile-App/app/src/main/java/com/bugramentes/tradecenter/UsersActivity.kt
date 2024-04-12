package com.bugramentes.tradecenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
//import android.widget.SearchView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugramentes.tradecenter.adapter.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_users.*
import java.util.*
import kotlin.collections.ArrayList

class UsersActivity : AppCompatActivity() {

    private lateinit var userList: ArrayList<User>
    private lateinit var tempArrayList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        supportActionBar?.title = "Kullanýcýlar"

        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        tempArrayList = ArrayList<User>()
        userAdapter = UserAdapter(this,tempArrayList)

        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = userAdapter

        databaseReference.child("User").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }



                }
                tempArrayList.addAll(userList)
                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UsersActivity,error.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(p0: String?): Boolean {


                tempArrayList.clear()
                val searchText = p0!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){

                    userList.forEach(){

                        if(it.email?.lowercase(Locale.getDefault())!!.contains(searchText)){

                            tempArrayList.add(it)

                        }

                    }

                    userAdapter.notifyDataSetChanged()

                }else{

                    tempArrayList.clear()
                    tempArrayList.addAll(userList)
                    userAdapter.notifyDataSetChanged()

                }


                return false

            }


        })



        return super.onCreateOptionsMenu(menu)
    }
}