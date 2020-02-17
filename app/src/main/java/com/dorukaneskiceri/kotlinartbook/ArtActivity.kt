package com.dorukaneskiceri.kotlinartbook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_art.*
import java.util.jar.Manifest

class ArtActivity : AppCompatActivity() {

    var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art)
    }

    fun save(view: View){

    }

    fun selectImage(view: View){

        //Request Permission
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selectedImage = data.data

            if(selectedImage != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,selectedImage!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(bitmap)
                }else{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedImage)
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
