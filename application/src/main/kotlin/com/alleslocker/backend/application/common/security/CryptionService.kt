package com.alleslocker.backend.application.common.security

interface CryptionService {
    fun encrypt(plainText: String?): String
    fun decrypt(cipherTextWithIvBase64: String?): String
}