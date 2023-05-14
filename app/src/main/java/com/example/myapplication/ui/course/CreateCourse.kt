package com.example.myapplication.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R

class CreateCourseFragment : Fragment() {
    private lateinit var courseViewModel: CourseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courseViewModel = ViewModelProvider(this).get(CourseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_course, container, false)

        val nameEditText = root.findViewById<EditText>(R.id.name_edit_text)
        val descEditText = root.findViewById<EditText>(R.id.desc_edit_text)

        val button = root.findViewById<Button>(R.id.save_button)
        button.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descEditText.text.toString()
            val course = Course(name = name, description = description)
            courseViewModel.insert(course)
            //
            Toast.makeText(activity, "Course created!", Toast.LENGTH_SHORT).show()
        }

        return root
    }
}
