package org.wit.hillfort.helpers


import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import org.wit.hillfort.R
import org.wit.hillfort.views.CAMERA_REQUEST
import org.wit.hillfort.views.mCurrentPhotoPath
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


// Function to show an image picker dialog
fun showImagePicker(parent: Activity, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, R.string.select_hillfort_image.toString())
    parent.startActivityForResult(chooser, id)
}

fun checkImagePersmission(view: Activity): Boolean {
    return (ContextCompat.checkSelfPermission(view, android.Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(view,
        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}

// Camera setup tutorial http://www.kotlincodes.com/kotlin/camera-intent-with-kotlin-android/
fun requestImagePermission(view: Activity) {
    ActivityCompat.requestPermissions(view, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
       CAMERA_REQUEST)
}

fun createFile(activity: Activity): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = absolutePath
    }
}

fun showCameraPicker(parent: Activity, id: Int){
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val file: File = createFile(parent)
    val uri: Uri = FileProvider.getUriForFile(
        parent?.applicationContext!!,
        "com.example.android.fileprovider",
        file
    )
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    parent.startActivityForResult(cameraIntent, id)
}

// Function needed when we want to edit a hillfort
fun readImageFromPath(context: Context, path : String) : Bitmap? {
    var bitmap: Bitmap? = null
    val uri = Uri.parse(path)

    if (uri != null) {
        try {
            val parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.getFileDescriptor()
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
        } catch (e: Exception) {
            error("read image from path error: $e")
        }
    }
    return bitmap
}