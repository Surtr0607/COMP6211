package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.data.FirebaseUtils
import com.example.myapplication.databinding.ActivityEducatorBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.course.AllCourse
import com.example.myapplication.ui.course.MyCourse
import com.example.myapplication.ui.login.DashboardFragment
import com.example.myapplication.ui.statistics.AddLearnerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EducatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEducatorBinding
    private val TAG = "FIRESTORE"
    private val db = FirebaseUtils().fireStoreDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityEducatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Load the bottom navigation view
        val navView: BottomNavigationView = binding.root.findViewById(R.id.nav_view)

        // Initialize the start page after welcome page
        replaceFragment(DashboardFragment())

        // Set the click event of floating button fab
        val btn: FloatingActionButton = binding.fab
        btn.setOnClickListener{
            Toast.makeText(applicationContext, "Click!", Toast.LENGTH_SHORT).show()
            replaceFragment(AddLearnerFragment())
        }

        // Set the function of bottom navigation bar
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Allcourses -> {
                    replaceFragment(AllCourse())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.MyCourse -> {
                    replaceFragment(MyCourse())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.Account -> {
                    replaceFragment(DashboardFragment())
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