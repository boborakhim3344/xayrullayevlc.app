package com.example.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import org.json.JSONObject

enum class OperationType {
    GET,
    LIST,
    CREATE,
    UPDATE,
    DELETE
}

fun handleFirestoreError(exception: Exception, operation: OperationType, path: String) {
    val json = JSONObject().apply {
        put("timestamp", System.currentTimeMillis())
        put("operation", operation.name)
        put("path", path)
        put("exception", exception.javaClass.simpleName)
        put("message", exception.message ?: "Unknown error")
        if (exception is FirebaseFirestoreException) {
            put("code", exception.code.name)
        }
    }
    Log.e("FirestoreError", json.toString(), exception)
}
