package com.example.myapplication.ui.exam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.myapplication.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExamResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExamResult : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var questionId: String
    private lateinit var questionText: String
    private lateinit var userAnswers: BooleanArray
    private lateinit var isCorrectList: BooleanArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            questionId = it.getString("questionId", "")
            questionText = it.getString("questionText", "")
            userAnswers = it.getBooleanArray("userAnswers") ?: BooleanArray(0)
            isCorrectList = it.getBooleanArray("isCorrectList") ?: BooleanArray(0)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set question text and result summary
        // questionTextView = view.findViewById<TextView>(R.id.ExamResultText)
        //questionTextView.text = questionText
        val resultTextView = view.findViewById<TextView>(R.id.ExamResultText)
        val numQuestions = userAnswers.size
        val numCorrect = isCorrectList.count { it }
        resultTextView.text = "You answered $numCorrect out of $numQuestions questions correctly."

        // Set up the ListView adapter
        val listView = view.findViewById<ListView>(R.id.ExamResultList)
        val resultList = mutableListOf<Pair<String, String>>()
        for (i in 0 until numQuestions) {
            val correctOptionString = getOptionString(i, isCorrectList[i])
            val userOptionString = getOptionString(i, userAnswers[i])
            val resultString = if (isCorrectList[i]) "Correct" else "Incorrect"
            resultList.add(Pair("$resultString - Question ${i+1}", "Correct: $correctOptionString, Your answer: $userOptionString"))
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_2, android.R.id.text1, resultList)
        listView.adapter = adapter
    }

    private fun getOptionString(questionIndex: Int, optionValue: Boolean): String {
        val optionLetter = when (questionIndex % 4) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            else -> "D"
        }
        return if (optionValue) optionLetter else "-"
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExamResult.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExamResult().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}