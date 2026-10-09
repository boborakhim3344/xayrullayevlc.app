package com.example.data.repository

import android.content.Context
import com.example.data.local.InitialCurriculum
import com.example.data.local.room.ArabTiliDatabase
import com.example.data.local.room.entities.LessonEntity
import com.example.data.local.room.entities.LessonProgressEntity
import com.example.data.local.room.entities.LevelEntity
import com.example.data.local.room.entities.UserProgressEntity
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LocalLearningRepository(
    private val database: ArabTiliDatabase
) {
    constructor(context: Context) : this(ArabTiliDatabase.getInstance(context))

    private val levelDao = database.levelDao()
    private val lessonDao = database.lessonDao()
    private val userProgressDao = database.userProgressDao()
    private val lessonProgressDao = database.lessonProgressDao()

    // -------------------------------------------------------------
    // LEVELS
    // -------------------------------------------------------------
    fun getAllLevels(): Flow<List<LevelEntity>> = levelDao.getAllLevels()

    fun getLevelById(id: String): Flow<LevelEntity?> = levelDao.getLevelById(id)

    suspend fun unlockLevel(levelId: String) = levelDao.unlockLevel(levelId)

    // -------------------------------------------------------------
    // LESSONS
    // -------------------------------------------------------------
    fun getAllLessons(): Flow<List<LessonEntity>> = lessonDao.getAllLessons()

    fun getLessonsByLevel(levelId: String): Flow<List<LessonEntity>> = lessonDao.getLessonsByLevel(levelId)

    fun getLessonsByUnit(unitId: String): Flow<List<LessonEntity>> = lessonDao.getLessonsByUnit(unitId)

    fun getLessonById(lessonId: String): Flow<LessonEntity?> = lessonDao.getLessonById(lessonId)

    // -------------------------------------------------------------
    // USER PROGRESS
    // -------------------------------------------------------------
    fun observeUserProgress(userId: String): Flow<UserProgressEntity?> =
        userProgressDao.getUserProgress(userId)

    fun observeCompletedLessons(userId: String): Flow<List<LessonProgressEntity>> =
        lessonProgressDao.getCompletedLessonsForUser(userId)

    suspend fun initializeUserProgressIfAbsent(userId: String) {
        val existing = userProgressDao.getUserProgress(userId).firstOrNull()
        if (existing == null) {
            userProgressDao.insertOrUpdateProgress(
                UserProgressEntity(
                    userId = userId,
                    xp = 0,
                    streak = 1,
                    hearts = 5,
                    currentLevelId = "level_1",
                    completedLessonCount = 0,
                    lastStudyDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
            )
        }
    }

    suspend fun completeLesson(
        userId: String,
        lessonId: String,
        xpReward: Int,
        stars: Int = 3,
        score: Int = 100
    ) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // 1. Mark lesson completed in lessons table
        lessonDao.updateLessonCompletion(
            lessonId = lessonId,
            completed = true,
            stars = stars,
            score = score,
            timestamp = now
        )

        // 2. Insert or update record in lesson_progress
        val recordId = "${userId}_${lessonId}"
        val existingProgress = lessonProgressDao.getLessonProgress(userId, lessonId).firstOrNull()
        val attempts = (existingProgress?.attemptsCount ?: 0) + 1

        lessonProgressDao.insertOrUpdateLessonProgress(
            LessonProgressEntity(
                id = recordId,
                userId = userId,
                lessonId = lessonId,
                isCompleted = true,
                score = score,
                attemptsCount = attempts,
                completedAt = now
            )
        )

        // 3. Unlock next lesson in sequence
        val allLessons = lessonDao.getAllLessons().firstOrNull() ?: emptyList()
        val currentIndex = allLessons.indexOfFirst { it.id == lessonId }
        if (currentIndex >= 0 && currentIndex < allLessons.size - 1) {
            val nextLesson = allLessons[currentIndex + 1]
            lessonDao.unlockLesson(nextLesson.id)
        }

        // 4. Update user progress stats
        val progress = userProgressDao.getUserProgress(userId).firstOrNull()
        if (progress != null) {
            val newStreak = if (progress.lastStudyDate == today) {
                progress.streak
            } else {
                progress.streak + 1
            }

            userProgressDao.addXpAndStreak(
                userId = userId,
                xpToAdd = xpReward,
                newStreak = newStreak,
                today = today,
                timestamp = now
            )
        } else {
            userProgressDao.insertOrUpdateProgress(
                UserProgressEntity(
                    userId = userId,
                    xp = xpReward,
                    streak = 1,
                    hearts = 5,
                    currentLevelId = "level_1",
                    completedLessonCount = 1,
                    lastStudyDate = today,
                    updatedAt = now
                )
            )
        }
    }

    suspend fun deductHeart(userId: String): Int = withContext(Dispatchers.IO) {
        val progress = userProgressDao.getUserProgress(userId).firstOrNull()
        val currentHearts = progress?.hearts ?: 5
        val newHearts = (currentHearts - 1).coerceAtLeast(0)
        userProgressDao.updateHearts(userId, newHearts, System.currentTimeMillis())
        newHearts
    }

    suspend fun refillHearts(userId: String) = withContext(Dispatchers.IO) {
        userProgressDao.updateHearts(userId, 5, System.currentTimeMillis())
    }

    suspend fun syncFromCloudProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        userProgressDao.insertOrUpdateProgress(
            UserProgressEntity(
                userId = profile.userId,
                xp = profile.xp,
                streak = profile.streak,
                hearts = profile.hearts,
                completedLessonCount = profile.completedLessons.size,
                lastStudyDate = profile.lastStudyDate,
                updatedAt = System.currentTimeMillis()
            )
        )

        // Unlock completed lessons locally
        profile.completedLessons.forEach { lessonId ->
            lessonDao.updateLessonCompletion(
                lessonId = lessonId,
                completed = true,
                stars = 3,
                score = 100,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    // -------------------------------------------------------------
    // SEED INITIAL CURRICULUM DATA
    // -------------------------------------------------------------
    suspend fun seedInitialCurriculumIfEmpty() = withContext(Dispatchers.IO) {
        val existingLevels = levelDao.getAllLevels().firstOrNull()
        if (existingLevels.isNullOrEmpty()) {
            val levels = listOf(
                LevelEntity(
                    id = "level_1",
                    levelNumber = 1,
                    title = "Boshlang'ich (Mubtadi')",
                    description = "Arab alfaviti, qisqa unlilar va birinchi kundalik iboralar",
                    colorHex = "#58CC02",
                    isUnlocked = true,
                    requiredXp = 0
                ),
                LevelEntity(
                    id = "level_2",
                    levelNumber = 2,
                    title = "O'rta (Mutavassit)",
                    description = "Oila, sonlar, ranglar, taomlar va kundalik muloqot",
                    colorHex = "#1CB0F6",
                    isUnlocked = false,
                    requiredXp = 100
                ),
                LevelEntity(
                    id = "level_3",
                    levelNumber = 3,
                    title = "Yuqori (Mutaqaddim)",
                    description = "Fe'llar, jumlalar tuzish va to'liq matnlar o'qish",
                    colorHex = "#CE82FF",
                    isUnlocked = false,
                    requiredXp = 250
                )
            )
            levelDao.insertLevels(levels)

            val lessonEntities = mutableListOf<LessonEntity>()
            var globalOrder = 0

            InitialCurriculum.UNITS.forEach { unit ->
                val levelId = when (unit.orderIndex) {
                    1, 2, 3 -> "level_1"
                    4, 5 -> "level_2"
                    else -> "level_3"
                }

                unit.lessons.forEachIndexed { index, lesson ->
                    val isFirstLesson = (globalOrder == 0)
                    lessonEntities.add(
                        LessonEntity(
                            id = lesson.id,
                            levelId = levelId,
                            unitId = unit.id,
                            title = lesson.title,
                            description = lesson.description,
                            orderIndex = globalOrder++,
                            xpReward = lesson.xpReward,
                            exerciseCount = lesson.exercises.size,
                            isCompleted = false,
                            isUnlocked = isFirstLesson
                        )
                    )
                }
            }
            lessonDao.insertLessons(lessonEntities)
        }
    }
}
