package com.bonustrack02.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.bonustrack02.domain.repository.ShareRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import androidx.core.net.toUri

class ShareRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ShareRepository {
    override suspend fun saveImage(
        fileName: String,
        imageBytes: ByteArray
    ): String {
        return withContext(Dispatchers.IO) {
            val cachePath = File(context.cacheDir, "images")
            if (!cachePath.exists()) cachePath.mkdirs()

            cachePath.listFiles()?.forEach { it.delete() }

            val file = File(cachePath, "$fileName.png").also {
                it.writeBytes(imageBytes)
            }

            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            contentUri.toString()
        }
    }

    override suspend fun shareImage(uriString: String) {
        withContext(Dispatchers.Main) {
            val uri = uriString.toUri()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(intent, "로또 번호 공유하기").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(chooser)
        }
    }
}