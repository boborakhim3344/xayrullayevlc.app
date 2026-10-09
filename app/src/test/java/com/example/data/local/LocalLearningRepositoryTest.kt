package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.room.ArabTiliDatabase
import com.example.data.local.room.entities.LevelEntity
import com.example.data.repository.LocalLearningRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LocalLearningRepositoryTest {

    private lateinit var database: ArabTiliDatabase
    private lateinit var repository: LocalLearningRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ArabTiliDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = LocalLearningRepository(database)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun seedInitialCurriculumIfEmpty_populatesLevelsAndLessons() = runBlocking {
        repository.seedInitialCurriculumIfEmpty()

        val levels = repository.getAllLevels().first()
        assertTrue("Levels should be seeded", levels.isNotEmpty())
        assertEquals(3, levels.size)

        val lessons = repository.getAllLessons().first()
        assertTrue("Lessons should be seeded", lessons.isNotEmpty())
        assertTrue("First lesson should be unlocked", lessons.first().isUnlocked)
    }

    @Test
    fun completeLesson_marksLessonCompletedAndUpdatesUserProgress() = runBlocking {
        repository.seedInitialCurriculumIfEmpty()

        val userId = "test_user_123"
        val lessonId = "u1_l1"

        repository.completeLesson(
            userId = userId,
            lessonId = lessonId,
            xpReward = 15,
            stars = 3,
            score = 100
        )

        val completedLesson = repository.getLessonById(lessonId).first()
        assertNotNull(completedLesson)
        assertTrue("Lesson should be marked as completed", completedLesson!!.isCompleted)
        assertEquals(3, completedLesson.starsEarned)

        val userProgress = repository.observeUserProgress(userId).first()
        assertNotNull("User progress should be created", userProgress)
        assertEquals(15, userProgress!!.xp)
        assertEquals(1, userProgress.completedLessonCount)
    }

    @Test
    fun deductAndRefillHearts_updatesUserHeartsAccurately() = runBlocking {
        val userId = "test_user_456"
        repository.initializeUserProgressIfAbsent(userId)

        val remainingHearts = repository.deductHeart(userId)
        assertEquals(4, remainingHearts)

        repository.refillHearts(userId)
        val refreshed = repository.observeUserProgress(userId).first()
        assertEquals(5, refreshed?.hearts)
    }
}
