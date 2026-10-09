package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.data.model.ContentPack
import com.example.data.model.Lesson
import com.example.data.model.UserProfile
import com.example.data.model.WordItem
import com.example.data.model.WrongWordRecord
import com.example.data.repository.ArabTiliRepository
import com.example.data.repository.LocalLearningRepository
import com.example.ui.components.TopStatsBar
import com.example.ui.theme.DuoGreen
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val icon: ImageVector) {
    LEARN("O'rganish", Icons.Default.School),
    DICTIONARY("Lug'at", Icons.Default.MenuBook),
    PRACTICE("Mashq", Icons.Default.FitnessCenter),
    PROFILE("Profil", Icons.Default.Person)
}

@Composable
fun MainScreen(
    repository: ArabTiliRepository,
    currentUserId: String,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    localLearningRepository: LocalLearningRepository? = null
) {
    val coroutineScope = rememberCoroutineScope()

    var currentTab by remember { mutableStateOf(MainTab.LEARN) }
    var activeLesson by remember { mutableStateOf<Lesson?>(null) }
    var showAdminScreen by remember { mutableStateOf(false) }

    // User Profile Stream
    val userProfileState by repository.observeUserProfile(currentUserId)
        .collectAsState(initial = null)

    val localProgress by (localLearningRepository?.observeUserProgress(currentUserId)
        ?: kotlinx.coroutines.flow.flowOf(null)).collectAsState(initial = null)

    val profile = userProfileState ?: UserProfile(
        userId = currentUserId,
        email = repository.getCurrentUserEmail() ?: "mehmon@arabtili.uz",
        displayName = if (currentUserId == "local_guest") "O'quvchi (Mehmon)" else "Talaba",
        xp = localProgress?.xp ?: 0,
        streak = localProgress?.streak ?: 1,
        hearts = localProgress?.hearts ?: 5
    )

    // Words Stream
    val words by repository.observeWords().collectAsState(initial = emptyList())

    // Favorites Stream
    val favorites by repository.observeFavorites(currentUserId).collectAsState(initial = emptySet())

    // Wrong Words Stream
    val wrongWords by repository.observeWrongWords(currentUserId).collectAsState(initial = emptyList())

    // 15-day Packs Stream
    val packs by repository.observePacks().collectAsState(initial = emptyList())

    val isAdmin = repository.isAdmin()

    // Sync initial words to Firestore if admin
    LaunchedEffect(isAdmin) {
        if (isAdmin) {
            repository.syncInitialWordsToFirestoreIfAdmin()
        }
    }

    // Handle back button on secondary screens
    BackHandler(enabled = activeLesson != null || showAdminScreen) {
        if (activeLesson != null) {
            activeLesson = null
        } else if (showAdminScreen) {
            showAdminScreen = false
        }
    }

    if (activeLesson != null) {
        LessonExerciseScreen(
            lesson = activeLesson!!,
            hearts = profile.hearts,
            onFinishLesson = { xpEarned ->
                coroutineScope.launch {
                    localLearningRepository?.completeLesson(currentUserId, activeLesson!!.id, xpEarned)
                    repository.recordLessonCompleted(activeLesson!!.id, xpEarned)
                    activeLesson = null
                }
            },
            onHeartDeducted = {
                coroutineScope.launch {
                    localLearningRepository?.deductHeart(currentUserId)
                    repository.deductHeart()
                }
            },
            onRefillHearts = {
                coroutineScope.launch {
                    localLearningRepository?.refillHearts(currentUserId)
                    repository.refillHearts()
                }
            },
            onClose = { activeLesson = null }
        )
        return
    }

    if (showAdminScreen && isAdmin) {
        AdminScreen(
            repository = repository,
            words = words,
            packs = packs,
            onBack = { showAdminScreen = false }
        )
        return
    }

    Scaffold(
        topBar = {
            TopStatsBar(
                streak = profile.streak,
                xp = profile.xp,
                hearts = profile.hearts,
                onHeartsClick = {
                    coroutineScope.launch {
                        localLearningRepository?.refillHearts(currentUserId)
                        repository.refillHearts()
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = DuoGreen
            ) {
                MainTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) DuoGreen else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DuoGreen else Color.Gray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = DuoGreen.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (currentTab) {
                MainTab.LEARN -> {
                    LearnScreen(
                        userProfile = profile,
                        newPacks = packs.filter { it.isPublished },
                        onSelectLesson = { lesson -> activeLesson = lesson }
                    )
                }
                MainTab.DICTIONARY -> {
                    DictionaryScreen(
                        words = words,
                        favorites = favorites,
                        onToggleFavorite = { wordId ->
                            coroutineScope.launch { repository.toggleFavorite(currentUserId, wordId) }
                        }
                    )
                }
                MainTab.PRACTICE -> {
                    PracticeScreen(
                        allWords = words,
                        wrongWords = wrongWords,
                        onClearWrongWord = { wordId ->
                            coroutineScope.launch { repository.clearWrongWord(currentUserId, wordId) }
                        }
                    )
                }
                MainTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = profile,
                        isAdmin = isAdmin,
                        onOpenAdmin = { showAdminScreen = true },
                        onSignOut = onSignOut
                    )
                }
            }
        }
    }
}
