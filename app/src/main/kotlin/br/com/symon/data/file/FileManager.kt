package br.com.symon.data.file

import android.net.Uri
import io.reactivex.Observable
import okhttp3.MultipartBody


interface FileManager {
    fun createMultipartFromUri(uri: Uri?): Observable<MultipartBody.Part>
}