package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoYellow
import kotlinx.coroutines.delay

/**
 * Animated congratulatory dialog displayed when a user finishes a lesson.
 * Displays score, accuracy, stars, XP reward, and a prominent button to return to the dashboard.
 */
@Composable
fun LessonCongratulatoryDialog(
    score: Int,
    xpEarned: Int,
    heartsLeft: Int,
    lessonTitle: String = "Dars yakunlandi",
    accuracyPercent: Int = 100,
    onReturnToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Entrance scale animation
    var visible by remember { androidx.compose.runtime.mutableStateOf(true) }

    val calculatedStars = when {
        accuracyPercent >= 90 -> 3
        accuracyPercent >= 60 -> 2
        else -> 1
    }

    // Animated score count-up
    var displayedScore by remember { mutableIntStateOf(score) }
    var displayedStars by remember { mutableIntStateOf(calculatedStars) }

    LaunchedEffect(score) {
        // Count up score smoothly
        val step = (score / 20).coerceAtLeast(1)
        displayedScore = 0
        for (s in 0..score step step) {
            displayedScore = s
            delay(15)
        }
        displayedScore = score

        // Reveal stars in sequence
        displayedStars = 0
        for (star in 1..calculatedStars) {
            delay(150)
            displayedStars = star
        }
    }

    Dialog(
        onDismissRequest = onReturnToDashboard,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Confetti falling behind dialog
            ConfettiOverlay()

            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = tween(300))
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .testTag("congratulatory_dialog"),
                    shadowElevation = 16.dp,
                    border = androidx.compose.foundation.BorderStroke(2.dp, DuoGreen.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Celebrating Mascot with glowing circle
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(DuoGreen.copy(alpha = 0.12f))
                                .border(3.dp, DuoGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_mascot_celebrating),
                                contentDescription = "Tabriklaymiz!",
                                modifier = Modifier.size(82.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Title
                        Text(
                            text = "Ajoyib Natija!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoGreen,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = lessonTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Animated Stars Rating
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 1..3) {
                                val isEarned = i <= displayedStars
                                Icon(
                                    imageVector = if (isEarned) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = if (isEarned) DuoYellow else Color.LightGray,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .scale(if (isEarned) 1.15f else 1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Score & Stats Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCardPill(
                                title = "Ball",
                                value = "$displayedScore",
                                icon = Icons.Default.EmojiEvents,
                                iconColor = DuoYellow,
                                modifier = Modifier.weight(1f),
                                testTag = "score_stat_value"
                            )
                            StatCardPill(
                                title = "Aniqlik",
                                value = "$accuracyPercent%",
                                icon = Icons.Default.Speed,
                                iconColor = DuoGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatCardPill(
                                title = "Tajriba",
                                value = "+$xpEarned XP",
                                icon = Icons.Default.Star,
                                iconColor = DuoOrange,
                                modifier = Modifier.weight(1f)
                            )
                            StatCardPill(
                                title = "Jonlar",
                                value = "$heartsLeft / 5",
                                icon = Icons.Default.Favorite,
                                iconColor = DuoRed,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Return to Dashboard Button
                        DuoButton(
                            text = "Bosh sahifaga qaytish",
                            onClick = onReturnToDashboard,
                            color = DuoButtonColor.GREEN,
                            modifier = Modifier.fillMaxWidth(),
                            height = 50.dp,
                            testTag = "return_to_dashboard_button"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCardPill(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoGrayBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier
            )
        }
    }
}
