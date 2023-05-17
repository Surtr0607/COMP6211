package com.example.myapplication.ui.login

import android.app.Activity.RESULT_OK
import android.content.ContentProvider
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.rotationMatrix
import androidx.core.graphics.set
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class DashboardFragment : Fragment() {

    private val requestcode = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val data = it.data?.getStringExtra("data")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root =  inflater.inflate(R.layout.fragment_dashboard, container, false)

        val chooseImageBtn = root.findViewById<Button>(R.id.idBtnChooseImage)
        val imageView = root.findViewById<ImageView>(R.id.avatar)
        var temp = ""

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")

                save_Image(uri)
//                imageView.setImageBitmap(bitmap)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        // on below line adding click listener for our choose image button.
        chooseImageBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }



// Include only one of the following calls to launch(), depending on the types
// of media that you want to let the user choose from.

// Launch the photo picker and let the user choose only images.



        // Launch the photo picker and let the user choose only images/videos of a
        // specific MIME type, such as GIFs.
//        val mimeType = "image/gif"
//        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))





        Log.d("temp", temp)
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("unnamed.jpg")
        val ONE_MEGABYTE: Long = 520 * 520
        imageRef.getBytes(ONE_MEGABYTE)
            .addOnSuccessListener { bytes ->
                // 将字节数组转换为 Bitmap 对象，并将其显示在 ImageView 中
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->
                // 处理下载失败的情况
                Log.e("TAG", "Error downloading image: ${exception.message}", exception)
            }







        return root

    }

    // on below line adding on activity result method this method is called when user picks the image.

    private fun save_Image(uri: Uri) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(uri))

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRef = storageRef.child("unnamed.jpg")
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            Log.d("TAG", "Image uploaded successfully: ${taskSnapshot.metadata?.path}")
        }.addOnFailureListener { e ->
            Log.e("TAG", "Error uploading image: ${e.message}", e)
        }

    }



}