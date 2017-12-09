package br.com.symon.data.file

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import br.com.symon.BuildConfig
import io.reactivex.Observable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileManagerImpl @Inject constructor(val context: Context) : FileManager {
    override fun saveData(): Observable<Uri> =
            Observable.create<Uri> { emitter ->
            emitter.onNext(createFileUri())
            emitter.onComplete()
        }

    override fun getPathFromUri(uri: Uri?): Observable<String> =
            Observable.create<String> { emitter ->
                emitter.onNext(getPath(uri))
                emitter.onComplete()
            }

    private fun createFileUri(): Uri {
        val dir = File(Environment.getExternalStorageDirectory(), context.packageName)
        dir.mkdirs()

        val picName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("pt", "BR")).format(Date())
        val newFile = File(dir, "SYMON_$picName.png")

        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", newFile)
    }

    private fun isExternalStorageDocument(uri: Uri?): Boolean =
            "com.android.externalstorage.documents" == uri?.authority

    private fun isDownloadsDocument(uri: Uri?): Boolean =
            "com.android.providers.downloads.documents" == uri?.authority

    private fun isMediaDocument(uri: Uri?): Boolean =
            "com.android.providers.media.documents" == uri?.authority

    private fun isGooglePhotosUri(uri: Uri?): Boolean =
            "com.google.android.apps.photos.content" == uri?.authority

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                              selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    @SuppressLint("NewApi")
    private fun getPath(uri: Uri?): String {
        var path: String? = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    path = "${Environment.getExternalStorageDirectory()}  /  $split[1]"
                }

            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                path = getDataColumn(context, contentUri, null, null)

            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null

                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                path = getDataColumn(context, contentUri, selection, selectionArgs)
            }

        } else if ("content".equals(uri?.scheme, ignoreCase = true)) {
            path = if (isGooglePhotosUri(uri))
                uri?.lastPathSegment
            else
                getDataColumn(context, uri, null, null)

        } else if ("file".equals(uri?.scheme, ignoreCase = true)) {
            path = uri?.path
        }

        return if (path == null) "" else path
    }
}