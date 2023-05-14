package com.example.myapplication.ui.course

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.myapplication.R
import com.example.myapplication.ui.exam.ExamList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyCourse.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCourse : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ArrayAdapter<String>

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    val listView = view?.findViewById<ListView>(R.id.MycourseList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_course, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 获取当前登录用户的 UID
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // 获取列表视图
        val listView = view.findViewById<ListView>(R.id.MycourseList)

        // 创建一个空的列表项数组和 courseID 数组
        val listItems = arrayListOf<String>()
        val courseIds = arrayListOf<String>()

        // 如果 UID 不为空，则连接 Firestore 并获取当前用户的 course 字段
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // 获取用户的 course 字段的值
                        val courseArray = document.get("course") as ArrayList<String>
                        // 遍历 course 数组并在 Firestore 中查找匹配的数据
                        for (courseId in courseArray) {
                            db.collection("course").whereEqualTo("courseID", courseId)
                                .get()
                                .addOnSuccessListener { querySnapshot ->
                                    if (!querySnapshot.isEmpty) {
                                        // 获取匹配到的数据的 courseName 和 courseID 字段的值
                                        val courseName = querySnapshot.documents[0].get("courseName") as String
                                        val courseId = querySnapshot.documents[0].get("courseID") as String
                                        // 将 courseName 和 courseId 添加到列表项数组和 courseId 数组中
                                        listItems.add(courseName)
                                        courseIds.add(courseId)
                                        // 更新列表视图
                                        listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listItems)
                                    }
                                }
                        }
                    }
                }
        }

        // 设置列表视图的点击事件
        listView.setOnItemClickListener { parent, view, position, id ->
            // 创建一个 Bundle 对象来传递所选列表项的 courseID 字段的值
            val bundle = Bundle()
            bundle.putString("courseId", courseIds[position])
            // 创建一个新的 Fragment 实例并传递 Bundle 对象
            val fragment = ExamList()
            fragment.arguments = bundle
            // 替换当前 Fragment
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyCourse.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyCourse().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}