package com.example.myapplication.ui.course

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllCourse.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllCourse : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var courses: List<Course>

    data class Course(
        val courseID: String = "",
        val courseName: String = ""
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*
                val view = inflater.inflate(R.layout.fragment_all_course, container, false)
                // 获取 LinearLayout 视图实例
                val checkboxLayout = view.findViewById<LinearLayout>(R.id.check_box_layout)
                // 获取 Firestore 实例
                val firestore = Firebase.firestore

                // 从 Firestore 中读取所有课程的名称
                firestore.collection("course")
                    .get()
                    .addOnSuccessListener { documents ->

                        // 遍历每个文档，创建一个复选框，并将其添加到 LinearLayout 中
                        for (document in documents) {
                            val courseName = document.getString("courseName")
                            if (courseName != null) {
                                val checkbox = CheckBox(requireContext())
                                checkbox.text = courseName
                                checkboxLayout.addView(checkbox)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // 如果读取过程中发生错误，则在控制台中打印错误消息
                        Log.e(TAG, "Error getting documents.", exception)
                    }
        */

        val view = inflater.inflate(R.layout.fragment_all_course, container, false)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val checkboxGroup = view.findViewById<LinearLayout>(R.id.check_box_layout)
        val addButton = view.findViewById<Button>(R.id.AllCoursesButton)

        // 获取所有课程数据
        firestore.collection("course")
            .get()
            .addOnSuccessListener { result ->
                courses = result.toObjects(Course::class.java)

                // 创建复选框并显示课程名称
                for (course in courses) {
                    val checkbox = CheckBox(context)
                    checkbox.text = course.courseName
                    checkboxGroup.addView(checkbox)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to obtain course data", Toast.LENGTH_SHORT).show()
            }

        addButton.setOnClickListener {
            val selectedCourses = mutableListOf<String>()

            // 获取选中的课程ID
            for (i in 0 until checkboxGroup.childCount) {
                val checkbox = checkboxGroup.getChildAt(i) as CheckBox
                if (checkbox.isChecked) {
                    selectedCourses.add(courses[i].courseID)
                }
            }

            // 添加课程ID到当前用户的course字段
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userRef = firestore.collection("users").document(currentUser.uid)
                userRef.update("course", selectedCourses)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Successfully added courses", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(context, "Failed to add a course", Toast.LENGTH_SHORT).show()

                        print(selectedCourses)
                    }
            } else {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }
    data class UserData(val course: List<String>)

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllCourse.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllCourse().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}