package com.example.myapplication.ui.exam

import android.os.Bundle
import android.util.Log
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
    private lateinit var activityList: ArrayList<String>

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
        val view = inflater.inflate(R.layout.fragment_exam_list, container, false)

        // Assuming you have a ListView in your fragment layout with id 'activityListView'
        val listView: ListView = view.findViewById(R.id.ExamListListView)

        // Retrieve the activityList passed from the previous fragment
        activityList = arguments?.getStringArrayList("activityList") ?: ArrayList()

        // Create an ArrayAdapter to populate the ListView with activity names
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            ArrayList<String>()
        )

        // Set the adapter for the ListView
        listView.adapter = adapter

        // Set item click listener for the ListView
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val activityId = activityList[position]
            val questionList = getQuestionListFromFirestore(activityId)

            // Navigate to another fragment and pass the questionList
            val fragment = ExamInterface()
            val bundle = Bundle()
            bundle.putStringArray("questionList", questionList.toTypedArray())
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace "fragment_container" with the ID of the container in your layout file
                .addToBackStack(null)
                .commit()
        }

        // Fetch activity names from Firestore and update the adapter
        fetchActivityNames(adapter)

        return view
    }
    private fun fetchActivityNames(adapter: ArrayAdapter<String>) {
        val firestore = FirebaseFirestore.getInstance()

        for (activityId in activityList) {
            firestore.collection("activity").document(activityId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val activityName = documentSnapshot.getString("activityName")
                    activityName?.let { name ->
                        adapter.add(name)
                        adapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { exception ->
                    // 打印获取活动名称失败的错误信息
                    Log.e("YourFragment", "Failed to fetch activity names", exception)
                }
        }
    }

    private fun getQuestionListFromFirestore(activityId: String): ArrayList<String> {
        val firestore = FirebaseFirestore.getInstance()
        val questionList: ArrayList<String> = ArrayList()

        firestore.collection("activity").document(activityId).get()
            .addOnSuccessListener { documentSnapshot ->
                val questions = documentSnapshot.get("questionList") as? ArrayList<String>
                questions?.let { questionList.addAll(it) }
            }
            .addOnFailureListener { exception ->
                // 打印获取问题列表失败的错误信息
                Log.e("YourFragment", "Failed to fetch question list", exception)
            }

        return questionList
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