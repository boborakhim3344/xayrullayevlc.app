package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoPurple

@Composable
fun OnboardingScreen(
    onCompleted: (goal: String, minutes: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedGoal by remember { mutableStateOf("Qur'on va namoz o'qish") }
    var selectedMinutes by remember { mutableIntStateOf(10) }

    val goals = listOf(
        Triple("Qur'on va namoz o'qish", "Qur'on tilini tushunish va to'g'ri o'qish", Icons.Default.MenuBook),
        Triple("Sayohat va so'zlashuv", "Arab davlatlarida erkin muloqot qilish", Icons.Default.Public),
        Triple("Grammatika va ilm", "Sarf va nahv qoidalarini o'rganish", Icons.Default.AutoAwesome),
        Triple("Xotirani rivojlantirish", "Miya faoliyati va yangi alifbo o'rganish", Icons.Default.Psychology),
        Triple("Karyera va ish", "Tarjimonlik va xalqaro faoliyat", Icons.Default.Work)
    )

    val timeOptions = listOf(
        Pair(5, "Yengil (5 daqiqa / kun)"),
        Pair(10, "Odatiy (10 daqiqa / kun)"),
        Pair(15, "Jiddiy (15 daqiqa / kun)"),
        Pair(20, "Intensiv (20 daqiqa / kun)")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(3.dp))
                        .background(DuoGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (step >= 2) DuoGreen else DuoGrayBorder)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (step == 1) {
                Text(
                    text = "Arab tilini nima uchun o'rganmoqchisiz?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Darslarni sizning maqsadingizga moslashtiramiz",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                goals.forEach { (title, subtitle, icon) ->
                    val isSelected = selectedGoal == title
                    GoalOptionCard(
                        title = title,
                        subtitle = subtitle,
                        icon = icon,
                        isSelected = isSelected,
                        onClick = { selectedGoal = title }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Text(
                    text = "Kunlik o'rganish maqsadingiz qanday?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Har kuni oz-ozdan o'rganish katta natija beradi!",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                timeOptions.forEach { (minutes, label) ->
                    val isSelected = selectedMinutes == minutes
                    TimeOptionCard(
                        minutes = minutes,
                        label = label,
                        isSelected = isSelected,
                        onClick = { selectedMinutes = minutes }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        DuoButton(
            text = if (step == 1) "Davom etish" else "Boshlash!",
            onClick = {
                if (step == 1) {
                    step = 2
                } else {
                    onCompleted(selectedGoal, selectedMinutes)
                }
            },
            color = DuoButtonColor.GREEN,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GoalOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) DuoGreen else DuoGrayBorder
    val bgColor = if (isSelected) DuoGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) DuoGreen else DuoGrayBorder.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else DuoGreen,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun TimeOptionCard(
    minutes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) DuoGreen else DuoGrayBorder
    val bgColor = if (isSelected) DuoGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = if (isSelected) DuoGreen else Color.Gray,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
