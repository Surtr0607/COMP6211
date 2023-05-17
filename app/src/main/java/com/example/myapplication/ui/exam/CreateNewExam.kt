package com.example.myapplication.ui.exam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.FirebaseUtils
import com.example.myapplication.ui.course.AllCourse
import com.google.android.material.textfield.TextInputEditText

/**
 * A simple [Fragment] subclass.
 * Use the [CreateNewExam.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateNewExam : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    private lateinit var texts: ArrayList<String>
    private val TAG = "FIRESTORE"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_new_exam, container, false)
        val list = root.findViewById<ListView>(R.id.question_List)
        val button = root.findViewById<Button>(R.id.create_exam_button)
        val examname = root.findViewById<TextInputEditText>(R.id.exam_name)
        texts = ArrayList(listOf("email1", "email2", "email"))
        FirebaseUtils().fireStoreDatabase.collection("questions")
            .get()
            .addOnSuccessListener {
                for (que in it){
                    val temp=que.data.get("questionText")
                    val tempstring:String=temp.toString()
                    texts.add(tempstring)

                }

                list.choiceMode = ListView.CHOICE_MODE_MULTIPLE
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, texts)
                list.adapter = adapter
                val selectedItems = ArrayList<String>()

                list.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem = list.getItemAtPosition(position) as String
                    if (list.isItemChecked(position)) {
                        selectedItems.add(selectedItem)
                    } else {
                        selectedItems.remove(selectedItem)
                    }
                }


                button.setOnClickListener {
                    FirebaseUtils().fireStoreDatabase.collection("questions")
                        .get()
                        .addOnSuccessListener {
                            val selectedid=ArrayList<String>()
                            for (document in it){
                                for(item in selectedItems){
                                if(document.data.get("questionText")==item){
                                    val data=document.data.get("questionID").toString()
                                    selectedid.add(data)
                                }
                                }
                            }
                            val name = examname.text.toString()
                            val hashMapexam = hashMapOf<String, Any>(
                                "activityID" to "5",
                                "activityName" to name,
                                "questionList" to selectedid
                            )

                            FirebaseUtils().fireStoreDatabase.collection("activity")
                                .add(hashMapexam)
                                .addOnSuccessListener { it ->
                                    Log.d(TAG, "Added document with ID ${it.id}")
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "Error adding document $exception")
                                }
                            Toast.makeText(activity, "Exam created!", Toast.LENGTH_SHORT).show()
//                    replaceFragment(ExamList())
                        }
                        .addOnFailureListener{
                            Log.w(TAG, "Error adding document $it")
                        }

                }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document $it")
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
         * @return A new instance of fragment CreateNewExam.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateNewExam().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}