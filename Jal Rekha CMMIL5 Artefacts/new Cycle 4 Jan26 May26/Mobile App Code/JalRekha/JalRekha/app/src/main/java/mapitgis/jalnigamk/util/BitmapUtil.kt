package mapitgis.jalnigamk.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import mapitgis.jalnigam.core.Utility
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object BitmapUtil {

    fun uriToBitmap(context: Context, imageUri: Uri, resize:Boolean = false): Bitmap? {
        val bitmap  =  try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        if (resize) {
            return bitmap?.let { Utility.getResizedBitmap(it)}
        }else{
            return bitmap
        }
    }

    fun drawTextOnBitmap(context: Context, bitmap: Bitmap, text: String): Bitmap {
        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(newBitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.YELLOW
            textSize = 20f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            setShadowLayer(5f, 2f, 2f, Color.BLACK) // Add shadow for better visibility
        }

        val textLines = text.split("\n") // Split text by newline (\n)
        val textBounds = Rect()
        val padding = 20f // Padding from edges
        val lineSpacing = 10f // Space between lines

        // Get text height to properly position lines
        paint.getTextBounds("A", 0, 1, textBounds)
        val lineHeight = textBounds.height()

        // Start from the bottom and move up for each line
        var y = (bitmap.height - padding).toFloat()

        for (i in textLines.indices.reversed()) { // Reverse order to draw bottom-up
            canvas.drawText(textLines[i], padding, y, paint)
            y -= (lineHeight + lineSpacing) // Move up for the next line
        }
        return newBitmap
    }


    fun encodeImageToBase64(bitmap: Bitmap,quality: Int = 70): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeBase64ToImage(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


    fun deleteImage(context: Context,filePath: String?) {
        try {
            filePath?.let {
                if(filePath.startsWith("file:")){
                    val deleted = deleteFileFromAppStorage(filePath)
                    if (deleted) {
                        Log.d("DeleteImage", "Image deleted successfully")
                    } else {
                        Log.d("DeleteImage", "Failed to delete image")
                    }
                }else{
                    val deletedRows = context.contentResolver.delete(Uri.parse(filePath), null, null)
                    if (deletedRows > 0) {
                        Log.d("DeleteImage", "Image deleted successfully")
                    } else {
                        Log.d("DeleteImage", "Failed to delete image")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DeleteImage", "Error deleting image: ${e.message}")
        }
    }


    private fun deleteFileFromAppStorage(filePath: String): Boolean {
        try {
            val file = File(filePath.replace("file://", "")) // Remove "file://" if present
            if (file.exists()) {
                val deleted = file.delete()
                if (deleted) {
                    Log.d("DeleteFile", "File deleted successfully: $filePath")
                } else {
                    Log.d("DeleteFile", "Failed to delete file: $filePath")
                }
                return deleted
            } else {
                Log.d("DeleteFile", "File does not exist: $filePath")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DeleteFile", "Error deleting file: ${e.message}")
        }
        return false
    }



    fun saveBitmapToStorage(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val file: File? = Utility.createImageFile(context)
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos)
            }
            // Get the file URI
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}