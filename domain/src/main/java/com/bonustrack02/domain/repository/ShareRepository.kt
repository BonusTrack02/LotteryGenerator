package com.bonustrack02.domain.repository

interface ShareRepository {
    suspend fun saveImage(fileName: String, imageBytes: ByteArray): String

    suspend fun shareImage(uriString: String)
}