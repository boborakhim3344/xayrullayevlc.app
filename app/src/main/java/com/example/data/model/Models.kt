package com.example.data.model

import com.google.firebase.Timestamp

enum class ExerciseType {
    MULTIPLE_CHOICE_AR_TO_UZ,
    MULTIPLE_CHOICE_UZ_TO_AR,
    MATCHING_PAIRS,
    LISTENING,
    SENTENCE_BUILDER,
    TYPING,
    LETTER_RECOGNITION
}

data class Exercise(
    val id: String = "",
    val type: ExerciseType = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
    val prompt: String = "",
    val arabicText: String = "",
    val uzbekText: String = "",
    val transliteration: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
    val sentenceWords: List<String> = emptyList(),
    val pairs: Map<String, String> = emptyMap(),
    val explanation: String = ""
)

data class Lesson(
    val id: String = "",
    val unitId: String = "",
    val title: String = "",
    val description: String = "",
    val orderIndex: Int = 0,
    val xpReward: Int = 15,
    val exercises: List<Exercise> = emptyList()
)

data class LearningUnit(
    val id: String = "",
    val orderIndex: Int = 0,
    val title: String = "",
    val description: String = "",
    val level: String = "Boshlang'ich",
    val colorHex: String = "#58CC02",
    val lessons: List<Lesson> = emptyList()
)

data class WordItem(
    val id: String = "",
    val arabic: String = "",
    val uzbek: String = "",
    val transliteration: String = "",
    val category: String = "Umumiy",
    val level: String = "Boshlang'ich",
    val exampleArabic: String = "",
    val exampleUzbek: String = "",
    val packId: String = "pack_1",
    val mediaUrl: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "id" to id,
            "arabic" to arabic,
            "uzbek" to uzbek,
            "transliteration" to transliteration,
            "category" to category,
            "level" to level
        )
        if (exampleArabic.isNotEmpty()) map["exampleArabic"] = exampleArabic
        if (exampleUzbek.isNotEmpty()) map["exampleUzbek"] = exampleUzbek
        if (packId.isNotEmpty()) map["packId"] = packId
        if (mediaUrl.isNotEmpty()) map["mediaUrl"] = mediaUrl
        return map
    }
}

data class ContentPack(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val version: Int = 1,
    val releaseDate: Timestamp? = null,
    val isPublished: Boolean = true,
    val wordCount: Int = 0,
    val createdAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "id" to id,
            "title" to title,
            "version" to version,
            "isPublished" to isPublished,
            "wordCount" to wordCount
        )
        if (description.isNotEmpty()) map["description"] = description
        return map
    }
}

data class UserProfile(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatar: String = "mascot",
    val xp: Int = 0,
    val streak: Int = 1,
    val hearts: Int = 5,
    val lastStudyDate: String = "",
    val completedLessons: List<String> = emptyList(),
    val dailyGoalMinutes: Int = 10,
    val learningReason: String = "Umumiy qiziqish",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "userId" to userId,
            "email" to email,
            "displayName" to displayName,
            "avatar" to avatar,
            "xp" to xp,
            "streak" to streak,
            "hearts" to hearts,
            "dailyGoalMinutes" to dailyGoalMinutes,
            "learningReason" to learningReason,
            "completedLessons" to completedLessons
        )
        if (lastStudyDate.isNotEmpty()) map["lastStudyDate"] = lastStudyDate
        return map
    }
}

data class WrongWordRecord(
    val wordId: String = "",
    val arabic: String = "",
    val uzbek: String = "",
    val transliteration: String = "",
    val mistakeCount: Int = 1,
    val lastReviewedAt: Timestamp? = null
) {
    fun toMap(): Map<String, Any> = mapOf(
        "wordId" to wordId,
        "arabic" to arabic,
        "uzbek" to uzbek,
        "transliteration" to transliteration,
        "mistakeCount" to mistakeCount
    )
}

data class ReadingText(
    val id: String = "",
    val title: String = "",
    val level: String = "Boshlang'ich",
    val arabicParagraphs: List<String> = emptyList(),
    val uzbekTranslation: List<String> = emptyList(),
    val vocabularyNotes: Map<String, String> = emptyMap()
)
