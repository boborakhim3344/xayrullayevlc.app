package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen

@Composable
fun ArabicKeyboardView(
    onCharTyped: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Harakat Row (Diacritics)
            val harakat = listOf(
                "َ" to "Fatha",
                "ِ" to "Kasra",
                "ُ" to "Damma",
                "ْ" to "Sukun",
                "ً" to "Fathatan",
                "ٍ" to "Kasratan",
                "ٌ" to "Dammatan",
                "ّ" to "Shadda",
                "ٓ" to "Madd"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                harakat.forEach { (mark, _) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DuoGreen.copy(alpha = 0.15f))
                            .clickable { onCharTyped(mark) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ـ$mark",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoGreen,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Arabic Letters Row 1
            val row1 = listOf("ض", "ص", "ث", "ق", "ف", "غ", "ع", "ه", "خ", "ح", "ج", "د")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row1.forEach { letter ->
                    KeyButton(text = letter, modifier = Modifier.weight(1f)) { onCharTyped(letter) }
                }
            }

            // Arabic Letters Row 2
            val row2 = listOf("ش", "س", "ي", "ب", "ل", "ا", "ت", "ن", "م", "ك", "ط", "ذ")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row2.forEach { letter ->
                    KeyButton(text = letter, modifier = Modifier.weight(1f)) { onCharTyped(letter) }
                }
            }

            // Arabic Letters Row 3
            val row3 = listOf("ئ", "ء", "ؤ", "ر", "لا", "ى", "ة", "و", "ز", "ظ")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row3.forEach { letter ->
                    KeyButton(text = letter, modifier = Modifier.weight(1f)) { onCharTyped(letter) }
                }
            }

            // Control Row: Space and Backspace
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spacebar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(DuoGrayBorder)
                        .clickable { onCharTyped(" ") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bo'sh joy (Space)",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Backspace
                Box(
                    modifier = Modifier
                        .size(width = 64.dp, height = 44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFFE5E5))
                        .clickable { onBackspace() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = "O'chirish",
                        tint = Color.Red,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
