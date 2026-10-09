package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioHelper
import com.example.data.local.InitialCurriculum
import com.example.data.model.ReadingText
import com.example.data.model.WordItem
import com.example.data.model.WrongWordRecord
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoYellow
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    allWords: List<WordItem>,
    wrongWords: List<WrongWordRecord>,
    onClearWrongWord: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Kartochkalar", "Takrorlash", "O'qish")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = DuoGreen
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> FlashcardsPracticeTab(words = allWords)
            1 -> WrongWordsReviewTab(wrongWords = wrongWords, onClear = onClearWrongWord)
            2 -> ReadingPracticeTab(readingTexts = InitialCurriculum.READING_TEXTS)
        }
    }
}

@Composable
private fun FlashcardsPracticeTab(words: List<WordItem>) {
    val context = LocalContext.current
    val audioHelper = remember { AudioHelper(context) }

    val flashcardList = remember(words) { words.shuffled() }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val currentWord = flashcardList.getOrNull(currentIndex)

    if (currentWord == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("So'zlar topilmadi")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentIndex + 1} / ${flashcardList.size}",
                fontSize = 15.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                currentIndex = 0
                isFlipped = false
            }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Qaytadan boshlash")
            }
        }

        // 3D Flip Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(2.dp, if (isFlipped) DuoBlue else DuoGreen, RoundedCornerShape(24.dp))
                .clickable { isFlipped = !isFlipped },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isFlipped) {
                    // Front side: Arabic word
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentWord.arabic,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoGreen,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(DuoBlue.copy(alpha = 0.15f))
                                .clickable { audioHelper.speakArabic(currentWord.arabic) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Eshitish",
                                tint = DuoBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Tarjimasini ko'rish uchun bosing",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    // Back side: Uzbek translation & transliteration
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentWord.uzbek,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "O'qilishi: ${currentWord.transliteration}",
                            fontSize = 16.sp,
                            color = DuoBlue,
                            fontWeight = FontWeight.Medium
                        )
                        if (currentWord.exampleUzbek.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "${currentWord.exampleArabic}\n(${currentWord.exampleUzbek})",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DuoButton(
                text = "Yana takrorlash",
                onClick = {
                    isFlipped = false
                    if (currentIndex < flashcardList.size - 1) currentIndex++ else currentIndex = 0
                },
                color = DuoButtonColor.RED,
                modifier = Modifier.weight(1f)
            )

            DuoButton(
                text = "Bildim! ✓",
                onClick = {
                    isFlipped = false
                    if (currentIndex < flashcardList.size - 1) currentIndex++ else currentIndex = 0
                },
                color = DuoButtonColor.GREEN,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WrongWordsReviewTab(
    wrongWords: List<WrongWordRecord>,
    onClear: (String) -> Unit
) {
    val context = LocalContext.current
    val audioHelper = remember { AudioHelper(context) }

    if (wrongWords.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = DuoGreen,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Xatolar yo'q!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Darslarda xato qilgan so'zlaringiz avtomatik tarzda shu yerga tushadi va ularni qayta mustahkamlashingiz mumkin.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Mustahkamlash uchun ${wrongWords.size} ta so'z:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DuoOrange
            )
        }

        items(wrongWords, key = { it.wordId }) { wrong ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = wrong.arabic,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuoRed
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(DuoBlue.copy(alpha = 0.15f))
                                    .clickable { audioHelper.speakArabic(wrong.arabic) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = DuoBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = wrong.uzbek,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Xatolar soni: ${wrong.mistakeCount} marta",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    IconButton(onClick = { onClear(wrong.wordId) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "O'zlashtirildi",
                            tint = DuoGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadingPracticeTab(readingTexts: List<ReadingText>) {
    val context = LocalContext.current
    val audioHelper = remember { AudioHelper(context) }
    var selectedSentenceIndex by remember { mutableStateOf<Int?>(null) }
    var activeStory by remember { mutableStateOf(readingTexts.first()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Harakatli Matnlarni O'qish",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Jumlalarni bosib ularning tarjimasini ko'ring va talaffuzini tinglang!",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        items(readingTexts) { story ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = story.title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoGreen
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    story.arabicParagraphs.forEachIndexed { index, arabicSentence ->
                        val isSelected = activeStory.id == story.id && selectedSentenceIndex == index

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) DuoGreen.copy(alpha = 0.12f) else Color.Transparent)
                                .clickable {
                                    activeStory = story
                                    selectedSentenceIndex = if (isSelected) null else index
                                    audioHelper.speakArabic(arabicSentence)
                                }
                                .padding(vertical = 8.dp, horizontal = 6.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = arabicSentence,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) DuoGreen else MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(DuoBlue.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = null,
                                            tint = DuoBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = isSelected) {
                                    Column(modifier = Modifier.padding(top = 6.dp)) {
                                        Text(
                                            text = story.uzbekTranslation.getOrElse(index) { "" },
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = DuoBlue
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
