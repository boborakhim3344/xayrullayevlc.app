package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InitialCurriculum
import com.example.data.model.ContentPack
import com.example.data.model.LearningUnit
import com.example.data.model.Lesson
import com.example.data.model.UserProfile
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.components.DuoProgressBar
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoYellow
import com.example.ui.theme.DuoYellowDark

@Composable
fun LearnScreen(
    userProfile: UserProfile,
    newPacks: List<ContentPack>,
    onSelectLesson: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedSet = userProfile.completedLessons.toSet()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 15-day pack update banner if available
        if (newPacks.isNotEmpty()) {
            item {
                NewPackBanner(packs = newPacks)
            }
        }

        // Alphabet quick reference card
        item {
            AlphabetIntroBanner()
        }

        // Units and their winding lessons
        InitialCurriculum.UNITS.forEachIndexed { unitIndex, unit ->
            item {
                UnitHeaderBanner(
                    unit = unit,
                    unitNumber = unitIndex + 1,
                    completedCount = unit.lessons.count { completedSet.contains(it.id) }
                )
            }

            itemsIndexed(unit.lessons) { index, lesson ->
                val isCompleted = completedSet.contains(lesson.id)

                // A lesson is unlocked if it is the very first lesson of the app or the previous lesson was completed
                val isUnlocked = if (unitIndex == 0 && index == 0) {
                    true
                } else {
                    val prevLessonId = getPreviousLessonId(unitIndex, index)
                    prevLessonId == null || completedSet.contains(prevLessonId)
                }

                val isCurrent = isUnlocked && !isCompleted

                // Winding S-curve horizontal offsets: 0, 45, 0, -45
                val patternIndex = index % 4
                val xOffsetDp = when (patternIndex) {
                    0 -> 0.dp
                    1 -> 42.dp
                    2 -> 0.dp
                    3 -> (-42).dp
                    else -> 0.dp
                }

                LessonCircleNode(
                    lesson = lesson,
                    isCompleted = isCompleted,
                    isUnlocked = isUnlocked,
                    isCurrent = isCurrent,
                    xOffsetDp = xOffsetDp,
                    onClick = {
                        if (isUnlocked) {
                            onSelectLesson(lesson)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

private fun getPreviousLessonId(unitIndex: Int, lessonIndex: Int): String? {
    if (lessonIndex > 0) {
        return InitialCurriculum.UNITS[unitIndex].lessons[lessonIndex - 1].id
    }
    if (unitIndex > 0) {
        val prevUnitLessons = InitialCurriculum.UNITS[unitIndex - 1].lessons
        return prevUnitLessons.lastOrNull()?.id
    }
    return null
}

@Composable
private fun AlphabetIntroBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DuoBlue.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(DuoBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "أ",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "Arab Alifbosi (28 ta harf)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "O'ngdan chapga yoziladi, harakatlar bilan o'qiladi",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                )
            }
        }
    }
}

@Composable
private fun NewPackBanner(packs: List<ContentPack>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DuoYellow.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = DuoOrange,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Yangi so'zlar to'plami mavjud!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "15 kunlik yangilanish: ${packs.first().title}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun UnitHeaderBanner(unit: LearningUnit, unitNumber: Int, completedCount: Int = 0) {
    val totalLessons = unit.lessons.size
    val unitProgress = if (totalLessons > 0) completedCount.toFloat() / totalLessons.toFloat() else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                when (unitNumber % 3) {
                    1 -> DuoGreen
                    2 -> DuoBlue
                    else -> DuoOrange
                }
            )
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${unitNumber}-BO'LIM",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White.copy(alpha = 0.85f),
                    letterSpacing = 1.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = unit.level,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = unit.title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = unit.description,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Unit Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Darslar: $completedCount / $totalLessons",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = "${(unitProgress * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            DuoProgressBar(
                progress = unitProgress,
                height = 10.dp,
                barColor = Color.White,
                shadowColor = Color(0xFFD4D4D4),
                trackColor = Color.White.copy(alpha = 0.28f),
                testTag = "unit_progress_bar_${unitNumber}"
            )
        }
    }
}

@Composable
private fun LessonCircleNode(
    lesson: Lesson,
    isCompleted: Boolean,
    isUnlocked: Boolean,
    isCurrent: Boolean,
    xOffsetDp: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCurrent) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = xOffsetDp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pulse bubble tag for current lesson
        if (isCurrent) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DuoGreenDark)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "BOSHLASH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        val circleSize = 78.dp
        val (bgColor, shadowColor, iconColor) = when {
            isCompleted -> Triple(DuoYellow, DuoYellowDark, Color.White)
            isCurrent -> Triple(DuoGreen, DuoGreenDark, Color.White)
            else -> Triple(Color(0xFFE5E5E5), Color(0xFFCECECE), Color(0xFFAFAFAF))
        }

        Box(
            modifier = Modifier
                .size(circleSize)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(shadowColor)
                .clickable(enabled = isUnlocked) { onClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(circleSize - 6.dp)
                    .clip(CircleShape)
                    .background(bgColor)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCompleted -> Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Tugallangan",
                        tint = iconColor,
                        modifier = Modifier.size(36.dp)
                    )
                    isCurrent -> Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Joriy dars",
                        tint = iconColor,
                        modifier = Modifier.size(40.dp)
                    )
                    else -> Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Qulflangan",
                        tint = iconColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = lesson.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isUnlocked) MaterialTheme.colorScheme.onBackground else Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
