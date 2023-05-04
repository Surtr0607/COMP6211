package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.myapplication.data.database.User
import com.example.myapplication.data.database.UserDatabase
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.dashboard.DashboardFragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.ui.statistics.AddLearnerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Load the bottom navigation view
        val navView: BottomNavigationView = binding.navView

        // Initialize the start page after welcome page
        replaceFragment(HomeFragment())


        UserDatabase.getInstance(this).userDatabaseDao.insert(User(2, "Qi", "Zhong"))
//        db.userDatabaseDao.insert(User(2, "Qi", "Zhong"))



        // Set the click event of floating button fab
        val btn: FloatingActionButton = binding.fab
        btn.setOnClickListener{
            Toast.makeText(applicationContext, "Click!", Toast.LENGTH_SHORT).show()
            replaceFragment(AddLearnerFragment())
        }

        // Set the function of bottom navigation bar
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Dashboard -> {
                    replaceFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.MyCourse -> {
                    replaceFragment(DashboardFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.Account -> {
                    replaceFragment(LoginFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        // Bind the navView with navigation bar listener
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)





    }

    // Function used for replace the fragment in the fragment manager
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}