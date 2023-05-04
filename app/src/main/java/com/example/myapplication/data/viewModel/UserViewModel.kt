package com.example.myapplication.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.database.UserDao

class UserViewModel(val database : UserDao, application: Application): AndroidViewModel(application){

}