package com.example.todolist

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseUserRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun uploadUser(user: User) {
        db.collection("users")
            .document(user.account)
            .set(user)
            .await()
    }
}

