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
        val button = root.findViewById<Button>(R.id.add_learner_button)
        emails = ArrayList(listOf("email1", "email2", "email"))



        FirebaseUtils().fireStoreDatabase.collection("users")
            .get()
            .addOnSuccessListener {

                for (person in it){
                    if(person.data.get("identity")=="student"){
                        val temp=person.data.get("email")
                        val tempstring:String=temp.toString()
                        emails.add(tempstring)
                    }
                }

                list.choiceMode = ListView.CHOICE_MODE_MULTIPLE
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, emails)
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

                    FirebaseUtils().fireStoreDatabase.collection("course").document("0Rz8veQKjXTkDTdj6BSj").update("student", selectedItems)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }

                    FirebaseUtils().fireStoreDatabase.collection("users")
                        .get()
                        .addOnSuccessListener {
                            for (person in it){
//                                val courselist:Array<String> = person.data.get("course") as Array<String>
                                val courselist:Array<String> = arrayOf("1","2","3")
                                val coursearray = courselist.toMutableList()
                                for(item in selectedItems){
                                    if(person.data.get("email")==item){
                                    coursearray.add("5")

                                    FirebaseUtils().fireStoreDatabase.collection("users").document(person.id).update("course", coursearray)
                                        .addOnSuccessListener {
                                            replaceFragment(MyCourse())
                                        }
                                        .addOnFailureListener {exception ->
                                            Log.w(TAG, "Error adding courelist $exception")
                                        }}
                                }


                            }

                        }
                        .addOnFailureListener {exception ->
                            Log.w(TAG, "Error getting courselist $exception")
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
    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}