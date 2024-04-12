package com.bugramentes.tradecenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bugramentes.tradecenter.fragments.FavouritesFragment
import com.bugramentes.tradecenter.fragments.HomeFragment
import com.bugramentes.tradecenter.fragments.MessagesFragment
import com.bugramentes.tradecenter.model.Urun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var urunArrayList : ArrayList<Urun>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        auth = Firebase.auth
        db = Firebase.firestore

        val homeFragment = HomeFragment()
        val favouritesFragment = FavouritesFragment()
        val messagesFragment = MessagesFragment()

        makeCurrentFragment(homeFragment)

        bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){

                R.id.menuHome -> makeCurrentFragment(homeFragment)
                R.id.menuFavourites -> makeCurrentFragment((favouritesFragment))
                R.id.menuMessages -> {val intent = Intent(this@FeedActivity,UsersActivity::class.java)
                startActivity(intent)}


            }
            true

        }




    }


    private fun makeCurrentFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper,fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.log_out_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logOut){
            auth.signOut()
            val intent = Intent(this@FeedActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else if(item.itemId == R.id.addStuff){
            val intent = Intent(this@FeedActivity,UploadActivity::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.myStuff){
            val intent = Intent(this@FeedActivity,MyStuffActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}