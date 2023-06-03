package ru.mpei.pavlovis.domain

import encryption.MagmaEncryptor
import encryption.MagmaParallelEncryptor
import hashing.StreebogHasher

fun getHashByDefaultMode(fileBytes: ByteArray, length: String?): ByteArray? {
    val hasher = StreebogHasher(fileBytes)
    return when (length) {
        "256" -> {
            hasher.generate256Hash()
        }
        "512" -> {
            hasher.generate512Hash()
        }
        else -> {
            null
        }
    }
}

fun encodeByDefaultMode(fileBytes: ByteArray, threadsParam: String?, password: String): ByteArray? {
    val encodeKey = StreebogHasher(password).generate256Hash()
    return when (threadsParam) {
        "single-threaded" -> {
            MagmaEncryptor(encodeKey).encryptInCodeBook(fileBytes)
        }
        "multi-threaded" -> {
            MagmaParallelEncryptor(encodeKey).encryptInCodeBook(fileBytes)
        }
        else -> null
    }
}

fun decodeByDefaultMode(fileBytes: ByteArray, threadsParam: String?, password: String): ByteArray? {
    val decodeKey = StreebogHasher(password).generate256Hash()
    return when (threadsParam) {
        "single-threaded" -> {
            MagmaEncryptor(decodeKey).decryptInCodeBook(fileBytes)
        }
        "multi-threaded" -> {
            MagmaParallelEncryptor(decodeKey).decryptInCodeBook(fileBytes)
        }
        else -> null
    }
}


fun getHashBySteganographyMode(fileBytes: ByteArray, length: String?): ByteArray? {
    return when (length) {
        "256" -> {
            ByteArray(2)
        }
        "512" -> {
            ByteArray(2)
        }
        else -> {
            null
        }
    }
}

fun encodeBySteganographyMode(fileBytes: ByteArray, threadsParam: String?, password: String): ByteArray? {
    return when (threadsParam) {
        "single-threaded" -> {
            ByteArray(0)
        }
        "multi-threaded" -> {
            ByteArray(0)
        }
        else -> null
    }
}


fun decodeBySteganographyMode(fileBytes: ByteArray, threadsParam: String?, password: String): ByteArray? {
    return when (threadsParam) {
        "single-threaded" -> {
            ByteArray(0)
        }
        "multi-threaded" -> {
            ByteArray(0)
        }
        else -> null
    }
}