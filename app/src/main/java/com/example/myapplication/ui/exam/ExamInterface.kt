package com.example.myapplication.ui.exam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.example.myapplication.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ExamInterface.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamInterface : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var questionList: List<String>
    private var currentQuestionIndex: Int = 0
    private lateinit var questionTextView: TextView
    private lateinit var optionACheckBox: CheckBox
    private lateinit var optionBCheckBox: CheckBox
    private lateinit var optionCCheckBox: CheckBox
    private lateinit var optionDCheckBox: CheckBox
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentQuestionDocument: DocumentSnapshot
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
        val rootView = inflater.inflate(R.layout.fragment_exam_interface, container, false)
        questionTextView = rootView.findViewById(R.id.ExamInterfaceText)
        optionACheckBox = rootView.findViewById(R.id.ExamInterfaceCheckBox)
        optionBCheckBox = rootView.findViewById(R.id.ExamInterfaceCheckBox2)
        optionCCheckBox = rootView.findViewById(R.id.ExamInterfaceCheckBox3)
        optionDCheckBox = rootView.findViewById(R.id.ExamInterfaceCheckBox4)
        firestore = FirebaseFirestore.getInstance()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            questionList = it.getStringArray("questionList")?.toList() ?: emptyList()
            loadQuestion()
        }
        view.findViewById<Button>(R.id.ExamInterfacePreviousButton).setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex -= 1
                loadQuestion()
            }
        }
        view.findViewById<Button>(R.id.ExamInterfaceNextButton).setOnClickListener {
            if (currentQuestionIndex < questionList.size - 1) {
                currentQuestionIndex += 1
                loadQuestion()
            }
        }
        view.findViewById<Button>(R.id.ExamInterfaceFinishButton).setOnClickListener {
            finishQuiz()
        }
    }

    private fun loadQuestion() {
        val questionId = questionList[currentQuestionIndex]
        firestore.collection("questions").document(questionId).get()
            .addOnSuccessListener { documentSnapshot ->
                currentQuestionDocument = documentSnapshot
                val questionText = documentSnapshot.getString("question")
                questionTextView.text = questionText
                optionACheckBox.text = documentSnapshot.getString("optionA")
                optionBCheckBox.text = documentSnapshot.getString("optionB")
                optionCCheckBox.text = documentSnapshot.getString("optionC")
                optionDCheckBox.text = documentSnapshot.getString("optionD")
                optionACheckBox.isChecked = false
                optionBCheckBox.isChecked = false
                optionCCheckBox.isChecked = false
                optionDCheckBox.isChecked = false
            }
    }

    private fun finishQuiz() {
        val userAnswers = listOf(
            optionACheckBox.isChecked,
            optionBCheckBox.isChecked,
            optionCCheckBox.isChecked,
            optionDCheckBox.isChecked
        )
        val correctOptions = currentQuestionDocument.get("correctOption") as List<Long>
        val isCorrectList = mutableListOf<Boolean>()
        for (i in userAnswers.indices) {
            isCorrectList.add(correctOptions.contains(i.toLong()) == userAnswers[i])
        }
        val bundle = Bundle().apply {
            putString("questionId", currentQuestionDocument.id)
            putString("questionText", currentQuestionDocument.getString("question"))
            putBooleanArray("userAnswers", userAnswers.toBooleanArray())
            putBooleanArray("isCorrectList", isCorrectList.toBooleanArray())
        }
        val finishFragment = ExamResult()
        finishFragment.arguments = bundle
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, finishFragment)?.commit()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExamInterface.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExamInterface().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}