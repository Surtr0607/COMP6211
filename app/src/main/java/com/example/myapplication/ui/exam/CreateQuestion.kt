package com.example.myapplication.ui.exam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.FirebaseUtils
import com.example.myapplication.ui.course.AllCourse
import com.google.android.material.textfield.TextInputEditText

/**
 * A simple [Fragment] subclass.
 * Use the [CreateQuestion.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateQuestion : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val root = inflater.inflate(R.layout.fragment_create_new_question, container, false)
        val checkBox1: CheckBox = root.findViewById(R.id.option_a_chosen)
        val checkBox2: CheckBox = root.findViewById(R.id.option_b_chosen)
        val checkBox3: CheckBox = root.findViewById(R.id.option_c_chosen)
        val checkBox4: CheckBox = root.findViewById(R.id.option_d_chosen)
        val finish = root.findViewById<Button>(R.id.finish_exam)

        val question = root.findViewById<TextInputEditText>(R.id.question)
        val optiona = root.findViewById<TextInputEditText>(R.id.option_a)
        val optionb = root.findViewById<TextInputEditText>(R.id.option_b)
        val optionc = root.findViewById<TextInputEditText>(R.id.option_c)
        val optiond = root.findViewById<TextInputEditText>(R.id.option_d)

        val selectedStrings = ArrayList<String>()

        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val selectedString = when (buttonView) {
                checkBox1 -> "1"
                checkBox2 -> "2"
                checkBox3 -> "3"
                checkBox4 -> "4"
                else -> ""
            }

            if (isChecked) {
                // 复选框被选中
                selectedStrings.add(selectedString)
            } else {
                // 复选框未被选中
                selectedStrings.remove(selectedString)
            }
        }
        checkBox1.setOnCheckedChangeListener(checkedChangeListener)
        checkBox2.setOnCheckedChangeListener(checkedChangeListener)
        checkBox3.setOnCheckedChangeListener(checkedChangeListener)
        checkBox4.setOnCheckedChangeListener(checkedChangeListener)

        finish.setOnClickListener {
            val name = question.text.toString()
            val questionid:String="6"
            val optionatext = optiona.text.toString()
            val optionbtext = optionb.text.toString()
            val optionctext = optionc.text.toString()
            val optiondtext = optiond.text.toString()
            val hashMapquestion = hashMapOf<String, Any>(
                "correctOption" to selectedStrings,
                "optionA" to optionatext,
                "optionB" to optionbtext,
                "optionC" to optionctext,
                "optionD" to optiondtext,
                "questionID" to questionid,
                "questionText" to name
            )

            FirebaseUtils().fireStoreDatabase.collection("questions")
                .add(hashMapquestion)
                .addOnSuccessListener { it ->
                    Log.d(TAG, "Added document with ID ${it.id}")
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error adding document $exception")
                }
            Toast.makeText(activity, "Question created!", Toast.LENGTH_SHORT).show()
            replaceFragment(CreateNewExam())

        }
        return root
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}