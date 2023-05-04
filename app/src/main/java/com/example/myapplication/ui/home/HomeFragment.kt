package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceControl.Transaction
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.data.database.User
import com.example.myapplication.data.database.UserDatabase
import com.example.myapplication.data.viewModel.UserViewModel
import com.example.myapplication.data.viewModel.UserViewModelFactory
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.statistics.AddLearnerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val application = requireNotNull(this.activity).application
      val dataSource = UserDatabase.getInstance(application).userDatabaseDao

      val viewModelFactory = UserViewModelFactory(dataSource, application)
      val userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)




    val textView: TextView = binding.textHome
    homeViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }





    return root
  }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}