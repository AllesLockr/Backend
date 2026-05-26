package com.alleslocker.backend.persistence.security

import com.alleslocker.backend.application.common.security.CryptionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class AesCryptionService(
    @Value("\${app.security.master-key}") private val base64Key: String
) : CryptionService {
    private val algorithm = "AES/GCM/NoPadding"
    private val tagLengthBit = 128
    private val ivLengthByte = 12 // Standard für AES-GCM
    private val secureRandom = SecureRandom()
    private val secretKey: SecretKeySpec

    init {
        require(base64Key.isNotBlank()) {
            "CRITICAL ERROR: 'master-key' is not set!"
        }

        val decodedKey = try {
            Base64.getDecoder().decode(base64Key)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("CRITICAL ERROR: 'master-key' is not a valid Base64-String!", e)
        }

        require(decodedKey.size == 32) {
            "CRITICAL ERROR: 'master-key' must be 256 Bit (32 Bytes) long! Current length: ${decodedKey.size * 8} Bit."
        }

        this.secretKey = SecretKeySpec(decodedKey, "AES")
    }

    override fun encrypt(plainText: String?): String {
        if (plainText.isNullOrBlank()) throw IllegalArgumentException("plainText can not be null or blank!")

        return try {
            val iv = ByteArray(ivLengthByte)
            secureRandom.nextBytes(iv) // Generiere zufälligen IV

            val cipher = Cipher.getInstance(algorithm)
            val parameterSpec = GCMParameterSpec(tagLengthBit, iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)

            val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

            // IV und Chiffretext in ein einziges Byte-Array zusammenfügen [IV (12 Bytes) + Chiffretext]
            val cipherTextWithIv = ByteBuffer.allocate(iv.size + cipherText.size)
                .put(iv)
                .put(cipherText)
                .array()

            Base64.getEncoder().encodeToString(cipherTextWithIv)
        } catch (e: Exception) {
            throw RuntimeException("Error while encrypting: ", e)
        }
    }

    override fun decrypt(cipherTextWithIvBase64: String?): String {
        if (cipherTextWithIvBase64.isNullOrBlank()) throw IllegalArgumentException("cipherTextWithIvBase64 can not be null or blank!")

        return try {
            val cipherTextWithIv = Base64.getDecoder().decode(cipherTextWithIvBase64)

            // IV und Chiffretext wieder trennen
            val byteBuffer = ByteBuffer.wrap(cipherTextWithIv)
            val iv = ByteArray(ivLengthByte)
            byteBuffer.get(iv) // Die ersten 12 Bytes sind der IV

            val cipherText = ByteArray(byteBuffer.remaining())
            byteBuffer.get(cipherText) // Der Rest ist der eigentliche Chiffretext

            val cipher = Cipher.getInstance(algorithm)
            val parameterSpec = GCMParameterSpec(tagLengthBit, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec)

            val decryptedBytes = cipher.doFinal(cipherText)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            throw RuntimeException(
                "Error while decrypting: ", e
            )
        }
    }
}