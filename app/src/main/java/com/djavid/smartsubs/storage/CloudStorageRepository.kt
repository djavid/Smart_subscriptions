package com.djavid.smartsubs.storage

import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudStorageRepository {

    private val storage = FirebaseStorage.getInstance()

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

}