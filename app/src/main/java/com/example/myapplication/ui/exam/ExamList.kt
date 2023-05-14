package com.example.myapplication.ui.exam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExamList.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var courseId: String
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // 获取列表视图
        val listView = view?.findViewById<ListView>(R.id.ExamListListView)

        // 创建一个空的列表项数组
        val listItems = arrayListOf<String>()

        // 如果 courseId 不为空，则连接 Firestore 并获取匹配的 activity 数据
        if (courseId.isNotBlank()) {
            db.collection("activity").whereEqualTo("courseID", courseId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // 遍历匹配的数据并将 activityName 添加到列表项数组中
                        for (document in querySnapshot.documents) {
                            val activityName = document.getString("activityName") ?: ""
                            listItems.add(activityName)
                        }
                        // 更新列表视图
                        listView?.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listItems)

                        // 为列表视图中的每个列表项添加点击事件监听器
                        listView?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                            // 获取要传递给下一个 Fragment 的数据
                            val questionList = querySnapshot.documents[position].get("questionList") as ArrayList<String>
                            // 创建要传递给下一个 Fragment 的 Bundle 对象，并将数据存储到该对象中
                            val bundle = Bundle()
                            bundle.putStringArrayList("questionList", questionList)
                            // 创建下一个 Fragment 实例，并将 Bundle 对象附加到该实例中
                            val nextFragment = ExamInterface()
                            nextFragment.arguments = bundle
                            // 使用 FragmentManager 启动下一个 Fragment
                            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, nextFragment)?.commit()
                        }
                    }
                }
        }


        return inflater.inflate(R.layout.fragment_exam_list, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExamList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExamList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}