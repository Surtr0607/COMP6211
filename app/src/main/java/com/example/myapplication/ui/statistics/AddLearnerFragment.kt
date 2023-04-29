package com.example.myapplication.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddLearnerBinding
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.example.myapplication.ui.notifications.NotificationsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddLearnerFragment : Fragment() {
    private var _binding: FragmentAddLearnerBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentAddLearnerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // binding the listview to adapter and show the content of list value
        val listOfMessage = root.findViewById<ListView>(R.id.studentList)
        val values = arrayOf("QY Zhang", "iphone", "Windows","li zhou")
        val adapter: ArrayAdapter<String> = ArrayAdapter(root.context, android.R.layout.simple_list_item_1, values)
        listOfMessage.adapter = adapter



        // return the view
        return root
    }

}
