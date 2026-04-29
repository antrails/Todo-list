package com.example.todolist

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseMissionRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun uploadMission(mission: Mission) {
        db.collection("users")
            .document(mission.userAccount)
            .collection("missions")
            .document(mission.id.toString())
            .set(mission)
            .await()
    }

    suspend fun deleteMission(mission: Mission) {
        db.collection("users")
            .document(mission.userAccount)
            .collection("missions")
            .document(mission.id.toString())
            .delete()
            .await()
    }

    suspend fun getMissions(account: String): List<Mission> {
        val snapshot = db.collection("users")
            .document(account)
            .collection("missions")
            .get()
            .await()

        return snapshot.toObjects(Mission::class.java)
    }
}