package com.example.myapplication.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class FirebaseUtils{
    val fireStoreDatabase = FirebaseFirestore.getInstance()
    private val TAG = "FIRESTORE"

    //Function used for reading all entities in a specified database collection
    fun get(collectionName:String): Task<QuerySnapshot> {
        return fireStoreDatabase.collection(collectionName).get()
    }

    //Search for a specified entity under field value
    fun searchByField(collectionName: String, field: String, value: Any): Task<QuerySnapshot> {
        return fireStoreDatabase.collection(collectionName).whereEqualTo(field, value).get()
    }

    //Add a entity into the database collection
    fun add(collectionName: String, hashMap: HashMap<String, Any>){
        fireStoreDatabase.collection(collectionName)
            .add(hashMap)
            .addOnSuccessListener { it ->
                Log.d(TAG, "Added document with ID ${it.id}")

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error adding document $exception")
            }
    }

    //Remove a specified document
    fun remove(collectionName: String, field: String, value:Any){
        fireStoreDatabase.collection(collectionName).whereEqualTo(field, value)
            .get()
            .addOnSuccessListener { queries ->
                for(query in queries){
                    fireStoreDatabase.collection(collectionName).document(query.id)
                        .delete()
                }
            }
    }


}