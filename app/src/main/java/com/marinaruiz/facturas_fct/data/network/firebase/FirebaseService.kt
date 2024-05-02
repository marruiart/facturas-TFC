package com.marinaruiz.facturas_fct.data.network.firebase

import com.google.android.gms.tasks.Task

class FirebaseService(val client: FirebaseClient = FirebaseClient.getInstance()) {

    companion object {

        private var _INSTANCE: FirebaseService? = null

        fun getInstance(): FirebaseService {
            return _INSTANCE ?: FirebaseService()
        }
    }

    fun logout() {
        client.auth.signOut()
    }

    fun resetPassword(email: String): Task<Void> {
        return client.auth.sendPasswordResetEmail(email)
    }

}