package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.ui.statistics.AddLearnerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

//var btn1 = findViewById<FloatingActionButton>(R.id.fab)
private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.Dashboard, R.id.MyCourse, R.id.Account))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val btn: FloatingActionButton = binding.fab
        btn.setOnClickListener{
            val fm = supportFragmentManager
            val nextFragment = AddLearnerFragment()
            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, nextFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }







    }
//private fun initFirstFragment(){
//    val fragment1 = HomeFragment()
//    val fm: FragmentManager = supportFragmentManager
//    fm.beginTransaction().add(R.id.container, fragment1).commit()
//
//}




}