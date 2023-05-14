package com.example.myapplication.data.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.myapplication.data.database.Course
import com.example.myapplication.data.CourseDatabase

class CourseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CourseRepository
    val allCourses: LiveData<List<Course>>

    init {
        val courseDao = CourseDatabase.getDatabase(application).courseDao()
        repository = CourseRepository(courseDao)
        allCourses = repository.allCourses
    }

    fun insert(course: Course) = viewModelScope.launch {
        repository.insert(course)
    }
}
