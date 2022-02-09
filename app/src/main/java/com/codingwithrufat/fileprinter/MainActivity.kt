package com.codingwithrufat.fileprinter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    val RESULT_LOAD_IMAGE = 1
    var selectedImageBitmap: Bitmap? = null
    var folder_path: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wakeupBroadcastReceiverForAnyway()
        createNewFolder()
        clickSelectButton()
        clickPrintButton()

    }

    private fun wakeupBroadcastReceiverForAnyway(){

        val intent = Intent()
        intent.action = "com.codingwithrufat.backgroundservice.PrinterService"
        sendBroadcast(intent)

    }


    private fun createNewFolder(){

        folder_path = File(ROOT_DIR, "Printer")
        if (!folder_path!!.exists()){
            folder_path!!.mkdirs()
        }

    }

    private fun clickPrintButton(){

        button_print.setOnClickListener {
            val path = File(folder_path, "/image${System.currentTimeMillis()}.png")

            try {
                var fileOutputStream = FileOutputStream(path)
                selectedImageBitmap?.compress(Bitmap.CompressFormat.PNG, 10, fileOutputStream)
                fileOutputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun clickSelectButton(){

        button_select.setOnClickListener {

            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(this, galleryIntent, RESULT_LOAD_IMAGE, null)

        }

    }

    /**
     * take an image from internal storage
     * and set it to imageview
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && data?.data != null && resultCode == RESULT_OK){

            val uri = data.data
            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            image.setImageBitmap(selectedImageBitmap) // set image


        }

    }


}