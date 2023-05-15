package com.example.myapplication.ui.course

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.FirebaseUtils


class AddLearner : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private lateinit var emails: ArrayList<String>
    private val TAG = "FIRESTORE"
//    private lateinit var user: User
    data class User(
        val course: List<String>,
        val email: String,
        val firstname: String,
        val identity: String,
        val lastname: String)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_add_learner, container, false)
        val list = root.findViewById<ListView>(R.id.student_List)
        val search = root.findViewById<SearchView>(R.id.search_Learner)
        val button = root.findViewById<Button>(R.id.add_learner_button)

        class MultiSelectAdapter(context: Context, items: List<String>) :
            ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, items) {

            private val selectedItems = ArrayList<String>()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val checkBox = view.findViewById<CheckBox>(android.R.id.checkbox)

                // 设置列表项的选中状态
                checkBox.isChecked = selectedItems.contains(getItem(position))

                // 监听复选框的点击事件
                checkBox.setOnClickListener {
                    val selectedItem = getItem(position)
                    if (checkBox.isChecked) {
                        if (selectedItem != null) {
                            selectedItems.add(selectedItem)
                        }
                    } else {
                        selectedItems.remove(selectedItem)
                    }
                }

                return view
            }

            // 获取选中的项
            fun getSelectedItems(): List<String> {
                return selectedItems
            }
        }
        emails = ArrayList(listOf("email1", "email2", "email")) // 使用构造函数初始化列表
        emails.add("crying")

        FirebaseUtils().fireStoreDatabase.collection("user")
            .get()
            .addOnSuccessListener {
                for (person in it){
//                if(person.data.get("identity")=="student"){
////                    val temp=person.data.get("email")
//                    emails.add("temp as String")
//                    }
                    emails.add("temp as String")
            }






//                for (person in user){
//                    if(person.getString("identity")=="student"){
//                        val aaa=person.getString("email")
//                        if (aaa != null){
//                            emails.add(aaa)}
//                    }
//                }
            }

            .addOnFailureListener {
                Log.w(TAG, "Error adding document $it")
            }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, emails)
        list.adapter = adapter

//       val adapter = MultiSelectAdapter(requireContext(), emails)
//       list.adapter = adapter
        val selectedItems = ArrayList<String>()
        selectedItems.add("zhongqi")
        selectedItems.add("Sarah")

        button.setOnClickListener {
//            selectedItems.clear()
//            selectedItems.addAll(adapter.getSelectedItems())
            FirebaseUtils().fireStoreDatabase.collection("course").document("0Rz8veQKjXTkDTdj6BSj").update("student", selectedItems)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
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
            AddLearner().apply {
                arguments = Bundle().apply {

                }
            }
    }
}