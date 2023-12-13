package com.djavid.smartsubs.data.storage

import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudStorageRepository {

    private val storage = FirebaseStorage.getInstance()
    private val iconsCache = mutableListOf<Pair<String, ByteArray>>()

    suspend fun getSubLogoUrl(storageUrl: String): String? {
        return suspendCoroutine { cont ->
            val gsReference = storage.getReferenceFromUrl(storageUrl)

            gsReference.downloadUrl
                .addOnSuccessListener {
                    cont.resume(it.toString())
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }

    suspend fun getSubLogoBytes(storageUrl: String): ByteArray? = suspendCoroutine { cont ->
        val icon = iconsCache.find { it.first == storageUrl }

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

    companion object {
        private const val ONE_MEGABYTE = 1_000_000L
    }


}