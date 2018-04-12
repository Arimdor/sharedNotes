package com.arimdor.sharednotes.util

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraUtil {

    @Throws(IOException::class)
    fun createImageFile(storageDir: File): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "NOTE_" + timeStamp + "_"
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        return image
    }
}