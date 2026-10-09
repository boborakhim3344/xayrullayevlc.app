package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.UserProfile
import com.example.data.model.WordItem
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ArabTiliRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun createOrUpdateProfile_authenticatedUser_succeeds() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repository = ArabTiliRepository(firestore)

        val profile = UserProfile(
            userId = uid,
            email = ALICE_EMAIL,
            displayName = "Alisa",
            xp = 50,
            streak = 3,
            hearts = 5
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createOrUpdateProfile(profile)
        }
        assertTrue("Profile creation should succeed", result.isSuccess)
    }

    @Test
    fun addWord_asAdmin_succeeds() = runBlocking {
        signInTestUser(ADMIN_EMAIL)
        val repository = ArabTiliRepository(firestore)

        val word = WordItem(
            id = "test_word_1",
            arabic = "سَلَامٌ",
            uzbek = "Tinchlik",
            transliteration = "Salamun",
            category = "Salomlashish",
            level = "Boshlang'ich"
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addWord(word)
        }
        assertTrue("Admin adding word should succeed", result.isSuccess)
    }

    @Test
    fun addWord_asNonAdmin_fails() = runBlocking {
        signInTestUser(BOB_EMAIL)
        val repository = ArabTiliRepository(firestore)

        val word = WordItem(
            id = "test_word_2",
            arabic = "كِتَابٌ",
            uzbek = "Kitob",
            transliteration = "Kitabun",
            category = "Ta'lim",
            level = "Boshlang'ich"
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addWord(word)
        }
        assertTrue("Non-admin adding word should fail", result.isFailure)
    }

    private companion object {
        const val ALICE_EMAIL = "alice@example.com"
        const val BOB_EMAIL = "bob@example.com"
        const val ADMIN_EMAIL = "boborakhim3@gmail.com"
        const val DEFAULT_TIMEOUT_MS = 8000L
    }
}
