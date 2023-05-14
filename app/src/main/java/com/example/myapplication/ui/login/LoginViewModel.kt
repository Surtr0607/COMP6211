package com.example.myapplication.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.RadioButton
import com.example.myapplication.data.LoginRepository
import com.example.myapplication.data.Result

import com.example.myapplication.R
import com.example.myapplication.data.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val auth: FirebaseAuth = Firebase.auth

    fun login(username: String, password: String, role: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)


        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth.currentUser
                    _loginResult.value = LoginResult(success = LoggedInUserView(displayName = username))


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    auth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success")
                                val user = auth.currentUser

                                //add user into the firestore

                                // create a dummy data in activity collection
                                val hashMap = hashMapOf<String, Any>(
                                    "username" to username,
                                    "identity" to role
                                )

                                // use the add() method to create a document inside users collection
                                FirebaseUtils().fireStoreDatabase.collection("users")
                                    .add(hashMap)
                                    .addOnSuccessListener { it ->
                                        Log.d("TAG", "Added document with ID ${it.id}")

                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w("TAG", "Error adding document $exception") }

                                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = username))

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                _loginResult.value = LoginResult(error = R.string.login_failed)
                            }
                        }
                }
            }



//        if (result is Result.Success) {
//            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
//        } else {
//            _loginResult.value = LoginResult(error = R.string.login_failed)
//        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


}