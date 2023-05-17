package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.data.FirebaseUtils
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.course.AddLearner
import com.example.myapplication.ui.course.AllCourse
import com.example.myapplication.ui.exam.CreateNewExam
import com.example.myapplication.ui.exam.CreateQuestion
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.ui.login.RemindFragment
import com.example.myapplication.ui.statistics.AddLearnerFragment
import com.example.myapplication.ui.statistics.StatisticsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "FIRESTORE"
    private val db = FirebaseUtils().fireStoreDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Load the bottom navigation view
        val navView: BottomNavigationView = binding.root.findViewById(R.id.nav_view)

        // Initialize the start page after welcome page
        replaceFragment(AllCourse())

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
                    replaceFragment(RemindFragment())
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




    //Test function and format of data in Firestore
//    private fun uploadData() {
//        // create a dummy data in users collection
//        val hashMap = hashMapOf<String, Any>(
//            "email" to "qz12u22@soton.ac.uk",
//            "firstname" to "James",
//            "lastname" to "Harden",
//            "identity" to "student"
//        )
//
//        // create a dummy data in course collection
//        val hashMap2 = hashMapOf<String, Any>(
//            "courseName" to "English",
//            "description" to "lazy"
//        )
//
//        // create a dummy data in activity collection
//        val hashMap3 = hashMapOf<String, Any>(
//            "courseName" to "English",
//            "description" to "lazy"
//        )
//
//        // use the add() method to create a document inside users collection
//        FirebaseUtils().fireStoreDatabase.collection("users")
//            .add(hashMap)
//            .addOnSuccessListener { it ->
//                Log.d(TAG, "Added document with ID ${it.id}")
//
//
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error adding document $exception")
//            }
//
//
//        FirebaseUtils().fireStoreDatabase.collection("users")
//            .get()
//            .addOnSuccessListener {documents -> for (document in documents){
//                Log.d(TAG, "${document.data.get("firstname")}")
//            }
//
//
//            }
//            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
//    }

}