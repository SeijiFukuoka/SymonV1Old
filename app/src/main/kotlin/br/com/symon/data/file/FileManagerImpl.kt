package br.com.symon.data.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.mlsdev.rximagepicker.RxImageConverters
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileManagerImpl @Inject constructor(val context: Context) : FileManager {

    override fun createMultipartFromUri(uri: Uri?): Observable<MultipartBody.Part> =
            Observable.create<MultipartBody.Part> { emitter ->
                var part: MultipartBody.Part

                RxImageConverters.uriToFile(context,
                        uri,
                        createTempFile(suffix = "_${System.currentTimeMillis()}.jpeg", directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)))
                        .subscribe {
                            part = MultipartBody.Part.createFormData(
                                    "resource",
                                    it.name,
                                    RequestBody.create(MediaType.parse("image/jpeg"), it))

                            emitter.onNext(part)
                            emitter.onComplete()
                        }
            }
}