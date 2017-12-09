package br.com.symon.data.repository

import android.net.Uri
import br.com.symon.base.BaseRepository
import br.com.symon.data.file.FileManagerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(private val fileManagerImpl: FileManagerImpl) : BaseRepository() {
    fun getPathFromUri(uri: Uri?) = call(fileManagerImpl.getPathFromUri(uri))
    fun setPathUri() = call(fileManagerImpl.saveData())
}