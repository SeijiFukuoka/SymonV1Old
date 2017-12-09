package br.com.symon.data.file

import android.net.Uri
import io.reactivex.Observable


interface FileManager {
    fun getPathFromUri(uri: Uri?) : Observable<String>
    fun saveData() : Observable<Uri>
}