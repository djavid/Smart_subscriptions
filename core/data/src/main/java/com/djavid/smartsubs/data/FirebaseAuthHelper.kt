package com.djavid.smartsubs.data

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.djavid.smartsubs.analytics.CrashlyticsLogger
import com.djavid.smartsubs.data.storage.SharedRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthHelper(
    private val prefs: SharedRepository,
    private val coroutineScope: CoroutineScope
) : DefaultLifecycleObserver, CoroutineScope by coroutineScope {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        checkUserAuth()
    }

    fun getUid(): String? = auth.currentUser?.uid

    private fun checkUserAuth() {
        if (auth.currentUser == null) {
            signInAnonymously()
        } else if (prefs.googleAuthToken == null) {
            launch(Dispatchers.IO) { prefs.googleAuthToken = loadToken() }
        }
    }

    private fun signInAnonymously() {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    launch(Dispatchers.IO) { prefs.googleAuthToken = loadToken() }
                } else {
                    CrashlyticsLogger.logException(task.exception)
                }
            }
    }

    private suspend fun loadToken(): String? = suspendCoroutine { cont ->
        auth.currentUser?.let { user ->
            user.getIdToken(true)
                .addOnSuccessListener {
                    cont.resume(it.token)
                }
                .addOnFailureListener {
                    CrashlyticsLogger.logException(it)
                    cont.resume(null)
                }
        }
    }

}