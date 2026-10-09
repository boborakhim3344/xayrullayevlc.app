package com.example.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.room.entities.LessonEntity
import com.example.data.local.room.entities.LessonProgressEntity
import com.example.data.local.room.entities.LevelEntity
import com.example.data.local.room.entities.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {
    @Query("SELECT * FROM learning_levels ORDER BY levelNumber ASC")
    fun getAllLevels(): Flow<List<LevelEntity>>

    @Query("SELECT * FROM learning_levels WHERE id = :id LIMIT 1")
    fun getLevelById(id: String): Flow<LevelEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevels(levels: List<LevelEntity>)

    @Update
    suspend fun updateLevel(level: LevelEntity)

    @Query("UPDATE learning_levels SET isUnlocked = 1 WHERE id = :levelId")
    suspend fun unlockLevel(levelId: String)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY orderIndex ASC")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE levelId = :levelId ORDER BY orderIndex ASC")
    fun getLessonsByLevel(levelId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE unitId = :unitId ORDER BY orderIndex ASC")
    fun getLessonsByUnit(unitId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId LIMIT 1")
    fun getLessonById(lessonId: String): Flow<LessonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("""
        UPDATE lessons 
        SET isCompleted = :completed, 
            starsEarned = :stars, 
            highScore = MAX(highScore, :score), 
            lastCompletedAt = :timestamp 
        WHERE id = :lessonId
    """)
    suspend fun updateLessonCompletion(
        lessonId: String,
        completed: Boolean,
        stars: Int,
        score: Int,
        timestamp: Long
    )

    @Query("UPDATE lessons SET isUnlocked = 1 WHERE id = :lessonId")
    suspend fun unlockLesson(lessonId: String)

    @Query("SELECT COUNT(*) FROM lessons WHERE isCompleted = 1")
    fun getCompletedLessonCount(): Flow<Int>
}

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE userId = :userId LIMIT 1")
    fun getUserProgress(userId: String): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserProgressEntity)

    @Query("""
        UPDATE user_progress 
        SET xp = xp + :xpToAdd, 
            streak = :newStreak, 
            lastStudyDate = :today, 
            completedLessonCount = completedLessonCount + 1,
            updatedAt = :timestamp 
        WHERE userId = :userId
    """)
    suspend fun addXpAndStreak(
        userId: String,
        xpToAdd: Int,
        newStreak: Int,
        today: String,
        timestamp: Long
    )

    @Query("UPDATE user_progress SET hearts = :hearts, updatedAt = :timestamp WHERE userId = :userId")
    suspend fun updateHearts(userId: String, hearts: Int, timestamp: Long)
}

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress WHERE userId = :userId AND lessonId = :lessonId LIMIT 1")
    fun getLessonProgress(userId: String, lessonId: String): Flow<LessonProgressEntity?>

    @Query("SELECT * FROM lesson_progress WHERE userId = :userId AND isCompleted = 1")
    fun getCompletedLessonsForUser(userId: String): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLessonProgress(progress: LessonProgressEntity)

    @Query("SELECT COUNT(*) FROM lesson_progress WHERE userId = :userId AND isCompleted = 1")
    fun getCompletedCount(userId: String): Flow<Int>
}
