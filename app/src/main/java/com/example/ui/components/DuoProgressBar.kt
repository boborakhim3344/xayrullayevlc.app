package com.example.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark

/**
 * A tactile, 3D Duolingo-style Progress Bar for tracking user progress through a lesson sequence.
 * Features a glossy top highlight, spring animation physics, and optional step markers.
 */
@Composable
fun DuoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    barColor: Color = DuoGreen,
    shadowColor: Color = DuoGreenDark,
    trackColor: Color = DuoGrayBorder,
    currentStep: Int? = null,
    totalSteps: Int? = null,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    testTag: String = "duo_progress_bar"
) {
    val clampedTarget = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedTarget,
        animationSpec = animationSpec,
        label = "duo_progress_animation"
    )

    Column(modifier = modifier) {
        if (currentStep != null && totalSteps != null && totalSteps > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$currentStep / $totalSteps",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .testTag(testTag)
                .clip(RoundedCornerShape(height / 2))
                .background(trackColor)
        ) {
            val totalWidth = maxWidth
            val progressWidth = totalWidth * animatedProgress

            if (animatedProgress > 0f) {
                // Active fill bar
                Box(
                    modifier = Modifier
                        .width(progressWidth)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(height / 2))
                        .background(barColor)
                ) {
                    // Glossy specular highlight (top reflection)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height * 0.35f)
                            .padding(horizontal = 4.dp, vertical = 1.5.dp)
                            .clip(RoundedCornerShape(height / 4))
                            .background(Color.White.copy(alpha = 0.38f))
                    )

                    // Bottom 3D shadow rim
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height * 0.2f)
                            .align(Alignment.BottomCenter)
                            .background(shadowColor.copy(alpha = 0.4f))
                    )
                }

                // Sparkle / leading tip cap
                if (animatedProgress in 0.05f..0.98f) {
                    Box(
                        modifier = Modifier
                            .size(height * 0.6f)
                            .align(Alignment.CenterStart)
                            .padding(start = (progressWidth - (height * 0.8f)).coerceAtLeast(0.dp))
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}
