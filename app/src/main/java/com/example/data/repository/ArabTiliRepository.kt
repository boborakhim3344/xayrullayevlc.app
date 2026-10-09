package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.local.InitialCurriculum
import com.example.data.model.ContentPack
import com.example.data.model.UserProfile
import com.example.data.model.WordItem
import com.example.data.model.WrongWordRecord
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AdminStats(
    val totalUsers: Int = 0,
    val totalWords: Int = 0,
    val totalPacks: Int = 0
)

class ArabTiliRepository(val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    fun isAdmin(): Boolean {
        val email = auth.currentUser?.email
        return email != null && email.equals("boborakhim3@gmail.com", ignoreCase = true)
    }

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("Foydalanuvchi tizimga kirmagan")
    }

    // -------------------------------------------------------------
    // USER PROFILE & PROGRESS
    // -------------------------------------------------------------

    fun observeUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val path = "users/$userId"
        val docRef = db.collection("users").document(userId)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.GET, path)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val profile = snapshot.toObject(UserProfile::class.java)
                trySend(profile)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun createOrUpdateProfile(profile: UserProfile): Result<Unit> {
        return try {
            val uid = profile.userId.ifEmpty { requireUserId() }
            val path = "users/$uid"
            val docRef = db.collection("users").document(uid)
            val data = profile.toMap().toMutableMap().apply {
                put("updatedAt", FieldValue.serverTimestamp())
                if (profile.createdAt == null) {
                    put("createdAt", FieldValue.serverTimestamp())
                }
            }
            docRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, "users/${profile.userId}")
            Result.failure(e)
        }
    }

    suspend fun recordLessonCompleted(lessonId: String, xpEarned: Int): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            val snapshot = docRef.get().await()

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            if (snapshot.exists()) {
                val existingLessons = (snapshot.get("completedLessons") as? List<*>)
                    ?.filterIsInstance<String>()?.toMutableList() ?: mutableListOf()
                if (!existingLessons.contains(lessonId)) {
                    existingLessons.add(lessonId)
                }

                val currentXp = (snapshot.get("xp") as? Number)?.toInt() ?: 0
                val currentStreak = (snapshot.get("streak") as? Number)?.toInt() ?: 1
                val lastDate = snapshot.getString("lastStudyDate") ?: ""

                val newStreak = if (lastDate == today) {
                    currentStreak
                } else {
                    currentStreak + 1
                }

                docRef.update(
                    mapOf(
                        "completedLessons" to existingLessons,
                        "xp" to (currentXp + xpEarned),
                        "streak" to newStreak,
                        "lastStudyDate" to today,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, "users/${getCurrentUserId()}")
            Result.failure(e)
        }
    }

    suspend fun deductHeart(): Result<Int> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            val snapshot = docRef.get().await()
            val currentHearts = (snapshot.get("hearts") as? Number)?.toInt() ?: 5
            val newHearts = (currentHearts - 1).coerceAtLeast(0)
            docRef.update("hearts", newHearts, "updatedAt", FieldValue.serverTimestamp()).await()
            Result.success(newHearts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refillHearts(): Result<Unit> {
        return try {
            val uid = requireUserId()
            val docRef = db.collection("users").document(uid)
            docRef.update("hearts", 5, "updatedAt", FieldValue.serverTimestamp()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // WORDS & VOCABULARY
    // -------------------------------------------------------------

    fun observeWords(): Flow<List<WordItem>> = flow {
        val path = "words"
        val query = db.collection("words")
        emit(InitialCurriculum.INITIAL_WORDS) // Instant emission from local curriculum

        val remoteFlow = query.snapshots().map { snapshot ->
            if (snapshot.isEmpty) {
                InitialCurriculum.INITIAL_WORDS
            } else {
                val list = snapshot.toObjects(WordItem::class.java)
                if (list.size < InitialCurriculum.INITIAL_WORDS.size) {
                    val remoteIds = list.map { it.id }.toSet()
                    val merged = list.toMutableList()
                    InitialCurriculum.INITIAL_WORDS.forEach { local ->
                        if (!remoteIds.contains(local.id)) {
                            merged.add(local)
                        }
                    }
                    merged
                } else {
                    list
                }
            }
        }.catch { error ->
            if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
            emit(InitialCurriculum.INITIAL_WORDS)
        }
        remoteFlow.collect { emit(it) }
    }

    suspend fun syncInitialWordsToFirestoreIfAdmin() {
        if (!isAdmin()) return
        try {
            val snapshot = db.collection("words").limit(5).get().await()
            if (snapshot.isEmpty) {
                val batch = db.batch()
                InitialCurriculum.INITIAL_WORDS.take(50).forEach { word ->
                    val docRef = db.collection("words").document(word.id)
                    batch.set(docRef, word.toMap().toMutableMap().apply {
                        put("createdAt", FieldValue.serverTimestamp())
                    })
                }
                batch.commit().await()

                val pack = InitialCurriculum.INITIAL_PACKS.first()
                db.collection("packs").document(pack.id).set(pack.toMap().toMutableMap().apply {
                    put("createdAt", FieldValue.serverTimestamp())
                }).await()
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, "words_batch_sync")
        }
    }

    suspend fun addWord(word: WordItem): Result<Unit> {
        return try {
            if (!isAdmin()) throw SecurityException("Faqat admin yangi so'z qo'sha oladi")
            val id = word.id.ifEmpty { "word_${System.currentTimeMillis()}" }
            val payload = word.copy(id = id).toMap().toMutableMap().apply {
                put("createdAt", FieldValue.serverTimestamp())
                put("updatedAt", FieldValue.serverTimestamp())
            }
            db.collection("words").document(id).set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, "words")
            Result.failure(e)
        }
    }

    suspend fun updateWord(word: WordItem): Result<Unit> {
        return try {
            if (!isAdmin()) throw SecurityException("Faqat admin so'zni tahrirlay oladi")
            val payload = word.toMap().toMutableMap().apply {
                put("updatedAt", FieldValue.serverTimestamp())
            }
            db.collection("words").document(word.id).set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, "words/${word.id}")
            Result.failure(e)
        }
    }

    suspend fun deleteWord(wordId: String): Result<Unit> {
        return try {
            if (!isAdmin()) throw SecurityException("Faqat admin so'zni o'chira oladi")
            db.collection("words").document(wordId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, "words/$wordId")
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // FAVORITES & WRONG WORDS
    // -------------------------------------------------------------

    fun observeFavorites(userId: String): Flow<Set<String>> = callbackFlow {
        val path = "users/$userId/favorites"
        val listener = db.collection("users").document(userId).collection("favorites")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleFirestoreError(error, OperationType.LIST, path)
                    trySend(emptySet())
                    return@addSnapshotListener
                }
                val set = snapshot?.documents?.mapNotNull { it.id }?.toSet() ?: emptySet()
                trySend(set)
            }
        awaitClose { listener.remove() }
    }

    suspend fun toggleFavorite(userId: String, wordId: String): Result<Boolean> {
        return try {
            val docRef = db.collection("users").document(userId).collection("favorites").document(wordId)
            val doc = docRef.get().await()
            if (doc.exists()) {
                docRef.delete().await()
                Result.success(false)
            } else {
                docRef.set(mapOf("wordId" to wordId, "addedAt" to FieldValue.serverTimestamp())).await()
                Result.success(true)
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, "users/$userId/favorites/$wordId")
            Result.failure(e)
        }
    }

    fun observeWrongWords(userId: String): Flow<List<WrongWordRecord>> = callbackFlow {
        val path = "users/$userId/wrong_words"
        val listener = db.collection("users").document(userId).collection("wrong_words")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleFirestoreError(error, OperationType.LIST, path)
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(WrongWordRecord::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun recordMistake(userId: String, word: WordItem): Result<Unit> {
        return try {
            val docRef = db.collection("users").document(userId).collection("wrong_words").document(word.id)
            val doc = docRef.get().await()
            val count = if (doc.exists()) ((doc.get("mistakeCount") as? Number)?.toInt() ?: 1) + 1 else 1
            val payload = mapOf(
                "wordId" to word.id,
                "arabic" to word.arabic,
                "uzbek" to word.uzbek,
                "transliteration" to word.transliteration,
                "mistakeCount" to count,
                "lastReviewedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, "users/$userId/wrong_words/${word.id}")
            Result.failure(e)
        }
    }

    suspend fun clearWrongWord(userId: String, wordId: String): Result<Unit> {
        return try {
            db.collection("users").document(userId).collection("wrong_words").document(wordId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // CONTENT PACKS (15-day releases)
    // -------------------------------------------------------------

    fun observePacks(): Flow<List<ContentPack>> = flow {
        val path = "packs"
        emit(InitialCurriculum.INITIAL_PACKS)
        val remoteFlow = db.collection("packs").snapshots().map { snapshot ->
            if (snapshot.isEmpty) {
                InitialCurriculum.INITIAL_PACKS
            } else {
                snapshot.toObjects(ContentPack::class.java)
            }
        }.catch { error ->
            if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
            emit(InitialCurriculum.INITIAL_PACKS)
        }
        remoteFlow.collect { emit(it) }
    }

    suspend fun createContentPack(pack: ContentPack): Result<Unit> {
        return try {
            if (!isAdmin()) throw SecurityException("Faqat admin to'plam yarata oladi")
            val id = pack.id.ifEmpty { "pack_${System.currentTimeMillis()}" }
            val payload = pack.copy(id = id).toMap().toMutableMap().apply {
                put("createdAt", FieldValue.serverTimestamp())
                put("releaseDate", FieldValue.serverTimestamp())
            }
            db.collection("packs").document(id).set(payload).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, "packs")
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // ADMIN STATS
    // -------------------------------------------------------------

    suspend fun fetchAdminStats(): Result<AdminStats> {
        return try {
            if (!isAdmin()) throw SecurityException("Ruxsat yo'q")
            val usersCount = db.collection("users").get().await().size()
            val wordsCount = db.collection("words").get().await().size().coerceAtLeast(InitialCurriculum.INITIAL_WORDS.size)
            val packsCount = db.collection("packs").get().await().size().coerceAtLeast(InitialCurriculum.INITIAL_PACKS.size)
            Result.success(AdminStats(usersCount, wordsCount, packsCount))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
