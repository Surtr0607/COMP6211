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


    private lateinit var checkBoxLayout: LinearLayout
    private lateinit var addButton: Button


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

        checkBoxLayout = view.findViewById(R.id.check_box_layout)
        addButton = view.findViewById(R.id.AllCoursesButton)

        // 查询 course 集合中的所有文档，创建多个 CheckBox 并添加到 LinearLayout 中
        val db = FirebaseFirestore.getInstance()
        val courseRef = db.collection("course")

        courseRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val courseName = document.getString("courseName")
                val courseID = document.getString("courseID")
                val checkBox = CheckBox(requireContext())
                checkBox.text = courseName
                checkBox.tag = courseID
                checkBoxLayout.addView(checkBox)
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }

        // 添加点击事件到按钮
        addButton.setOnClickListener {
            // 获取选中的 CheckBox 的 courseID
            val selectedCourseIDs = mutableListOf<String>()
            for (i in 0 until checkBoxLayout.childCount) {
                val child = checkBoxLayout.getChildAt(i)
                if (child is CheckBox && child.isChecked) {
                    val courseID = child.tag as? String
                    courseID?.let { selectedCourseIDs.add(it) }
                }
            }
            // 更新当前登录用户的数据到 Firestore 中
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { uid ->
                val userRef = db.collection("users").document(uid)
                userRef.get().addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.toObject(UserData::class.java)
                    userData?.let {
                        val courses = it.course.toMutableList()
                        courses.addAll(selectedCourseIDs)
                        userRef.update("course", courses)
                    }
                }
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