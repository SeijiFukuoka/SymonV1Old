package br.com.symon.data.repository

import android.net.Uri
import br.com.symon.base.BaseRepository
import br.com.symon.data.file.FileManagerImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(private val fileManagerImpl: FileManagerImpl) : BaseRepository() {
    fun createMultipartFromUri(uri: Uri?) = call(fileManagerImpl.createMultipartFromUri(uri))
}