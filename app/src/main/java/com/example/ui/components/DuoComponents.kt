package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoRedDark
import com.example.ui.theme.DuoYellow
import com.example.ui.theme.DuoYellowDark
import kotlin.random.Random

enum class DuoButtonColor {
    GREEN,
    BLUE,
    RED,
    YELLOW,
    WHITE_OUTLINE,
    GRAY_DISABLED
}

@Composable
fun DuoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: DuoButtonColor = DuoButtonColor.GREEN,
    enabled: Boolean = true,
    height: Dp = 52.dp,
    testTag: String = "duo_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (bgColor, shadowColor, textColor) = when (color) {
        DuoButtonColor.GREEN -> Triple(DuoGreen, DuoGreenDark, Color.White)
        DuoButtonColor.BLUE -> Triple(DuoBlue, DuoBlueDark, Color.White)
        DuoButtonColor.RED -> Triple(DuoRed, DuoRedDark, Color.White)
        DuoButtonColor.YELLOW -> Triple(DuoYellow, DuoYellowDark, Color.Black)
        DuoButtonColor.WHITE_OUTLINE -> Triple(Color.White, DuoGrayBorder, DuoGreen)
        DuoButtonColor.GRAY_DISABLED -> Triple(Color(0xFFE5E5E5), Color(0xFFD4D4D4), Color(0xFFAAAAAA))
    }

    val activeBg = if (enabled) bgColor else Color(0xFFE5E5E5)
    val activeShadow = if (enabled) shadowColor else Color(0xFFD4D4D4)
    val activeText = if (enabled) textColor else Color(0xFFAAAAAA)

    val shadowDepth = 4.dp
    val pressOffset = if (isPressed && enabled) 3.dp else 0.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .testTag(testTag)
            .clip(RoundedCornerShape(16.dp))
            .background(activeShadow)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - shadowDepth)
                .offset { IntOffset(0, pressOffset.roundToPx()) }
                .clip(RoundedCornerShape(16.dp))
                .background(activeBg)
                .then(
                    if (color == DuoButtonColor.WHITE_OUTLINE) {
                        Modifier.border(2.dp, DuoGrayBorder, RoundedCornerShape(16.dp))
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = activeText,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopStatsBar(
    streak: Int,
    xp: Int,
    hearts: Int,
    onHeartsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Streak
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Kunlik seriya",
                    tint = DuoOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$streak",
                    color = DuoOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // XP
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Tajriba bali (XP)",
                    tint = DuoYellow,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$xp XP",
                    color = DuoYellowDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Hearts
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onHeartsClick() }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Jonlar",
                    tint = DuoRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$hearts/5",
                    color = DuoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

data class ConfettiParticle(
    val xRatio: Float,
    val yRatio: Float,
    val size: Float,
    val color: Color,
    val speed: Float
)

@Composable
fun ConfettiOverlay(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    val particles = remember {
        val colors = listOf(DuoGreen, DuoBlue, DuoYellow, DuoOrange, DuoRed, DuoPurple)
        List(70) {
            ConfettiParticle(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat() * -0.5f,
                size = Random.nextFloat() * 14f + 8f,
                color = colors[Random.nextInt(colors.size)],
                speed = Random.nextFloat() * 0.8f + 0.6f
            )
        }
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2800, easing = FastOutSlowInEasing)
        )
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val w = maxWidth.value
        val h = maxHeight.value

        Canvas(modifier = Modifier.fillMaxSize()) {
            val p = progress.value
            particles.forEach { pt ->
                val currentY = ((pt.yRatio + p * pt.speed) % 1.2f) * size.height
                val currentX = (pt.xRatio * size.width) + kotlin.math.sin(p * 10f + pt.xRatio * 20f) * 20f
                drawCircle(
                    color = pt.color,
                    radius = pt.size,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}
