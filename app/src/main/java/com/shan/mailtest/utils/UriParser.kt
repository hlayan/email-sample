package com.shan.mailtest.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.ContextCompat
import com.shan.mailtest.ui.main.showLog

class UriParser(private val context: Context, private val uri: Uri) {

    val fileName
        get() : String? {
            var fileName: String? = null
            val cursor = getCursor(uri)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = cursor.getString(columnIndex)
                cursor.close()
            }
            return fileName
        }

    val filePath
        get() : String? {
            return when {
                isDocument -> getDocumentPath()
                isContent -> {
                    if (isGooglePhotosUri) {
                        uri.lastPathSegment
                    } else {
                        getDataColumn(uri)
                    }
                }
                isFile -> uri.path
                else -> null
            }
        }

    private fun getDocumentPath(): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).toTypedArray()
        return when {
            isExternalStorageDocument -> {
                val isPrimary = "primary".equals(split[0], ignoreCase = true)
                if (isPrimary) {
                    context.getExternalFilesDir(null)
                        .toString() + "/" + split[1]
                } else {
                    val sdCardPath = removableSdCardPath
                        .split("/Android".toRegex())
                        .toTypedArray()[0]

                    sdCardPath + "/" + split[1]
                }
            }
            isDownloadsDocument -> {
                val uriString = Uri.parse("content://downloads/public_downloads")
                val contentUri = ContentUris.withAppendedId(uriString, docId.toLong())
                getDataColumn(contentUri)
            }
            isMediaDocument -> {
                val selectionArgs = arrayOf(split[1])
                val contentUri = getExternalContentUri(split[0]) ?: return null
                getDataColumn(contentUri, "_id=?", selectionArgs)
            }
            else -> null
        }
    }

    private fun getExternalContentUri(value: String): Uri? {
        return when (value) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> null
        }
    }

    private fun getCursor(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): Cursor? {
        return context.contentResolver.query(
            uri, projection, selection, selectionArgs, null
        )
    }

    private fun getDataColumn(
        contentUri: Uri,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): String? {
        var result: String? = null
        val column = "_data"
        val projection = arrayOf(column)
        val cursor = getCursor(contentUri, projection, selection, selectionArgs)

        showLog("$contentUri|${projection.contentToString()}|$selection|${selectionArgs.contentToString()}")
        showLog(cursor?.count.toString())

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(column)
            result = cursor.getString(columnIndex)
            showLog(result)
            cursor.close()
        }
        return result
    }

    private val removableSdCardPath
        get(): String {
            val storage = ContextCompat.getExternalFilesDirs(context, null)
            val boolean = storage.size > 1 && storage[0] != null && storage[1] != null
            return if (boolean) storage[1].toString() else ""
        }

    private val isDocument = DocumentsContract.isDocumentUri(context, uri)

    private val isContent = "content".equals(uri.scheme, ignoreCase = true)

    private val isFile = "file".equals(uri.scheme, ignoreCase = true)

    private val isExternalStorageDocument = "com.android.externalstorage.documents" == uri.authority

    private val isDownloadsDocument = "com.android.providers.downloads.documents" == uri.authority

    private val isMediaDocument = "com.android.providers.media.documents" == uri.authority

    private val isGooglePhotosUri = "com.google.android.apps.photos.content" == uri.authority

}