package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoYellow

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    isAdmin: Boolean,
    onOpenAdmin: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(DuoGreen.copy(alpha = 0.12f))
                .border(3.dp, DuoGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_mascot),
                contentDescription = "Avatar",
                modifier = Modifier.size(95.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = userProfile.displayName.ifEmpty { "Foydalanuvchi" },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = userProfile.email,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Cards Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Kunlik seriya",
                value = "${userProfile.streak} kun",
                icon = Icons.Default.LocalFireDepartment,
                iconColor = DuoOrange,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Jami tajriba",
                value = "${userProfile.xp} XP",
                icon = Icons.Default.Star,
                iconColor = DuoYellow,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Darslar",
                value = "${userProfile.completedLessons.size} ta",
                icon = Icons.Default.EmojiEvents,
                iconColor = DuoGreen,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Jonlar",
                value = "${userProfile.hearts}/5",
                icon = Icons.Default.Favorite,
                iconColor = DuoRed,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Reminder Settings Card
        com.example.ui.components.DailyReminderSettingsCard()

        Spacer(modifier = Modifier.height(20.dp))

        // Support Button: opens Instagram @xayrullayevlc
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/xayrullayevlc"))
                    context.startActivity(intent)
                },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DuoRed.copy(alpha = 0.1f)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoRed.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(DuoRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Qo'llab-quvvatlash",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Qo'llab-quvvatlash",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoRed
                    )
                    Text(
                        text = "Instagram: @xayrullayevlc",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        // Admin Panel Button (visible ONLY if boborakhim3@gmail.com)
        if (isAdmin) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onOpenAdmin() },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DuoPurple.copy(alpha = 0.12f)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoPurple.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(DuoPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Admin Panel",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoPurple
                        )
                        Text(
                            text = "So'zlar, to'plamlar va tizim boshqaruvi",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        DuoButton(
            text = "Tizimdan chiqish",
            onClick = onSignOut,
            color = DuoButtonColor.WHITE_OUTLINE,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoGrayBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
