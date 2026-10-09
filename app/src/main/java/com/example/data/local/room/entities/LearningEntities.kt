package com.example.data.local.room.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "learning_levels")
data class LevelEntity(
    @PrimaryKey val id: String,
    val levelNumber: Int,
    val title: String,
    val description: String,
    val colorHex: String = "#58CC02",
    val isUnlocked: Boolean = false,
    val requiredXp: Int = 0
)

@Entity(
    tableName = "lessons",
    indices = [
        Index(value = ["levelId"]),
        Index(value = ["unitId"])
    ]
)
data class LessonEntity(
    @PrimaryKey val id: String,
    val levelId: String,
    val unitId: String,
    val title: String,
    val description: String,
    val orderIndex: Int,
    val xpReward: Int = 15,
    val exerciseCount: Int = 5,
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false,
    val starsEarned: Int = 0,
    val highScore: Int = 0,
    val lastCompletedAt: Long? = null
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val userId: String,
    val xp: Int = 0,
    val streak: Int = 1,
    val hearts: Int = 5,
    val currentLevelId: String = "level_1",
    val completedLessonCount: Int = 0,
    val lastStudyDate: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "lesson_progress",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["lessonId"]),
        Index(value = ["userId", "lessonId"], unique = true)
    ]
)
data class LessonProgressEntity(
    @PrimaryKey val id: String, // "${userId}_${lessonId}"
    val userId: String,
    val lessonId: String,
    val isCompleted: Boolean,
    val score: Int,
    val attemptsCount: Int = 1,
    val completedAt: Long = System.currentTimeMillis()
)
