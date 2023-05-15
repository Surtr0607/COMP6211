package com.example.myapplication.ui.course

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.FirebaseUtils
import com.google.android.material.textfield.TextInputEditText

/**
 * A simple [Fragment] subclass.
 * Use the [CreateNewCourse.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateNewCourse : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    private val TAG = "FIRESTORE"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_new_course, container, false)
        val button = root.findViewById<Button>(R.id.save_button)
        val nameEditText = root.findViewById<TextInputEditText>(R.id.name_edit_text)
        val descEditText = root.findViewById<TextInputEditText>(R.id.desc_edit_text)

        button.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descEditText.text.toString()
            val hashMap = hashMapOf<String, Any>(
                "courseName" to name,
                "description" to description)

            FirebaseUtils().fireStoreDatabase.collection("course")
                .add(hashMap)
                .addOnSuccessListener { it ->
                    Log.d(TAG, "Added document with ID ${it.id}")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error adding document $exception")
                }
            Toast.makeText(activity, "Course created!", Toast.LENGTH_SHORT).show()
        }
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateNewCourse().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
//