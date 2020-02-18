package com.dorukaneskiceri.kotlinartbook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_art.*
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class ArtActivity : AppCompatActivity() {

    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art)
    }

    //Saving SQLite
    fun save(view: View){

        val artName = artText.text.toString()
        val artistName = artistText.text.toString()
        val year = yearText.text.toString().toIntOrNull()

        if(year == null || artName == null || artistName == null){
            Toast.makeText(this,"Please write inputs correctly.",Toast.LENGTH_LONG).show()
        }
        else{
            if(selectedBitmap != null){

                val smallBitmap = makeSmallerBitmap(selectedBitmap!!,300)
                val outputStream = ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
                val byteArray = outputStream.toByteArray()
            }else{
                Toast.makeText(this,"Please make sure to be choose an image.",Toast.LENGTH_LONG).show()
            }
        }
    }

    //Reduce image size
    fun makeSmallerBitmap(image: Bitmap,maximumSize: Int): Bitmap{
        var height = image.height
        var width = image.width

        val bitmapRatio: Double = width.toDouble() / height.toDouble()
        if(bitmapRatio > 1){
            width = maximumSize
            val scaledHeight = height / bitmapRatio
            height = scaledHeight.toInt()
        }else{
            height = maximumSize
            val scaledWidth = width * bitmapRatio
            width = scaledWidth.toInt()
        }

       return Bitmap.createScaledBitmap(image,width,height,true)
    }

    //Selecting image from gallery
    fun selectImage(view: View){

        //Request Permission
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery,2)
        }
    }

    //After permission granted, go to gallery
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

    //Move selected image to program
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selectedImage = data.data

            if(selectedImage != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,selectedImage!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(selectedBitmap)
                }else{
                    selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedImage)
                    imageView.setImageBitmap(selectedBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
