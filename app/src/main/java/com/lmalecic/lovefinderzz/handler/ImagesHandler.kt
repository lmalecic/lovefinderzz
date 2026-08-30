package com.lmalecic.lovefinderzz.handler

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

fun getGifEnabledLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            if ( SDK_INT >= 28 ) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
}

object ImageGallerySaver {

    suspend fun save(
        context: Context,
        imageUrl: String,
        baseFileName: String
    ): Uri = withContext(Dispatchers.IO) {
        val connection = URL(imageUrl).openConnection() as HttpURLConnection

        connection.connectTimeout = 15_000
        connection.readTimeout = 30_000
        connection.instanceFollowRedirects = true

        try {
            connection.connect()

            if (connection.responseCode !in 200..299) {
                throw IOException("Image request failed: HTTP ${connection.responseCode}")
            }

            val mimeType = connection.contentType
                ?.substringBefore(";")
                ?.takeIf { it.startsWith("image/") }
                ?: "image/jpeg"

            val extension = MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(mimeType)

            val fileName = "${sanitizeFileName(baseFileName)}-${System.currentTimeMillis()}.$extension"

            connection.getInputStream().buffered().use { input ->
                if (SDK_INT >= Build.VERSION_CODES.Q) {
                    saveWithMediaStore(
                        context = context,
                        fileName = fileName,
                        mimeType = mimeType,
                        writeImage = { output -> input.copyTo(output) }
                    )
                } else {
                    saveLegacy(
                        context = context,
                        fileName = fileName,
                        mimeType = mimeType,
                        writeImage = { output -> input.copyTo(output) }
                    )
                }
            }
        } finally {
            connection.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveWithMediaStore(
        context: Context,
        fileName: String,
        mimeType: String,
        writeImage: (OutputStream) -> Unit
    ): Uri {
        val resolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Lovefinderzz")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val uri = resolver.insert(collection, values) ?: throw IOException("Could not create MediaStore entry")

        try {
            resolver.openOutputStream(uri, "w")?.use(writeImage) ?: throw IOException("Could not open image output stream")

            val finishedValues = ContentValues().apply {
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }

            resolver.update(uri, finishedValues, null, null)

            return uri
        } catch (error: Throwable) {
            resolver.delete(uri, null, null)
            throw error
        }
    }

    private fun saveLegacy(
        context: Context,
        fileName: String,
        mimeType: String,
        writeImage: (OutputStream) -> Unit
    ): Uri {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val albumDir = File(picturesDir, "Lovefinderzz")

        if (!albumDir.exists() && !albumDir.mkdirs()) {
            throw IOException("Could not create gallery album")
        }

        val imageFile = File(albumDir, fileName)

        try {
            imageFile.outputStream()
                .buffered()
                .use(writeImage)
        } catch (error: Throwable) {
            imageFile.delete()
            throw error
        }

        MediaScannerConnection.scanFile(context, arrayOf(imageFile.absolutePath), arrayOf(mimeType), null)

        return Uri.fromFile(imageFile)
    }

    private fun sanitizeFileName(value: String): String {
        return value.replace(Regex("""[^\p{L}\p{N}._-]+"""), "_")
            .trim('_')
            .ifBlank { "character" }
            .take(80)
    }
}