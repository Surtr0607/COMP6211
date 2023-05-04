package com.example.myapplication

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.data.database.User
import com.example.myapplication.data.database.UserDao
import com.example.myapplication.data.database.UserDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.assertEquals

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class UserDatabaseTest {
    private lateinit var userDao: UserDao
    private lateinit var db: UserDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDatabaseDao
        print(db)
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser(){
        val user = User(1, "Qi", "Zhong")
        userDao.insert(user)
        val currentUser = userDao.get(1)
        Log.d("TAG", currentUser?.firstname.toString())
        assertEquals(currentUser?.firstname, "Qi")

    }
}