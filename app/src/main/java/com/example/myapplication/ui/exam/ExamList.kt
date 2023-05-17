package com.example.myapplication.ui.exam

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.example.myapplication.R
import com.example.myapplication.ui.statistics.StatisticsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        val activityList = arguments?.getSerializable("activityList") as ArrayList<String>

        val view = inflater.inflate(R.layout.fragment_exam_list, container, false)
        val listView = view.findViewById<ListView>(R.id.ExamListListView)

        val db = FirebaseFirestore.getInstance()

        val activityAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1)
        listView.adapter = activityAdapter


        view.findViewById<Button>(R.id.button_result).setOnClickListener {
            replaceFragment(StatisticsFragment())
        }
        for (activityId in activityList) {
            val query = db.collection("activity").whereEqualTo("activityID", activityId)
            query.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val activityName = document.getString("activityName")
                    activityAdapter.add(activityName)
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error querying activity documents: ", exception)
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedActivityId = activityList[position]
            val query = db.collection("activity").whereEqualTo("activityID", selectedActivityId)
            query.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val questionList = document.get("questionList") as List<String>
                    val fragment = ExamInterface()
                    val args = Bundle()
                    args.putStringArrayList("questionList", ArrayList(questionList))
                    fragment.arguments = args
                    val fragmentManager = requireActivity().supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error querying selected activity document: ", exception)
            }
        }

        view.findViewById<Button>(R.id.ExamAddButton).setOnClickListener {
            addExam()
        }
        return view

    }

    private fun addExam(){
        val fragment = CreateNewExam()
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
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

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}