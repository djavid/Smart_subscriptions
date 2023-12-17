package com.djavid.smartsubs.data.storage

import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudStorageRepository {

    private val storage = FirebaseStorage.getInstance()
    private val iconsCache = mutableListOf<Pair<String, ByteArray>>()
    private val urlCache = hashMapOf<String, String>()

    suspend fun getSubLogoUrl(storageUrl: String): String? {
        return urlCache[storageUrl] ?: getDownloadUrl(storageUrl)
    }

    suspend fun getSubLogoBytes(storageUrl: String): ByteArray? = suspendCoroutine { cont ->
        val icon = iconsCache.toMutableList().find { it.first == storageUrl }

        if (icon != null) {
            cont.resume(icon.second)
        } else {
            val gsReference = storage.getReferenceFromUrl(storageUrl)

            gsReference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    iconsCache.add(storageUrl to bytes)
                    cont.resume(bytes)
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }

    private suspend fun getDownloadUrl(storageUrl: String): String? = suspendCoroutine { cont ->
        val gsReference = storage.getReferenceFromUrl(storageUrl)

        gsReference.downloadUrl
            .addOnSuccessListener {
                urlCache[storageUrl] = it.toString()
                cont.resume(it.toString())
            }
            .addOnFailureListener {
                cont.resume(null)
            }
    }

    companion object {
        private const val ONE_MEGABYTE = 1_000_000L
    }
}