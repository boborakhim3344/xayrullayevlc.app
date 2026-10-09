package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.audio.AudioHelper
import com.example.data.model.WordItem
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoRed

@Composable
fun DictionaryScreen(
    words: List<WordItem>,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val audioHelper = remember { AudioHelper(context) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Barchasi") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    var detailWord by remember { mutableStateOf<WordItem?>(null) }

    val categories = remember(words) {
        listOf("Barchasi") + words.map { it.category }.distinct()
    }

    val filteredWords = remember(words, searchQuery, selectedCategory, showOnlyFavorites, favorites) {
        words.filter { word ->
            val matchesCategory = selectedCategory == "Barchasi" || word.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    word.arabic.contains(searchQuery, ignoreCase = true) ||
                    word.uzbek.contains(searchQuery, ignoreCase = true) ||
                    word.transliteration.contains(searchQuery, ignoreCase = true)
            val matchesFavorites = !showOnlyFavorites || favorites.contains(word.id)
            matchesCategory && matchesSearch && matchesFavorites
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Lug'atdan qidirish (arabcha, o'zbekcha)...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Qidirish", tint = DuoGreen)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tozalash")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Categories & Favorites toggle
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            item {
                FilterChip(
                    selected = showOnlyFavorites,
                    onClick = { showOnlyFavorites = !showOnlyFavorites },
                    label = { Text("Sevimlilar (${favorites.size})") },
                    leadingIcon = {
                        Icon(
                            imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (showOnlyFavorites) DuoRed else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DuoRed.copy(alpha = 0.15f),
                        selectedLabelColor = DuoRed
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category && !showOnlyFavorites,
                    onClick = {
                        selectedCategory = category
                        showOnlyFavorites = false
                    },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DuoGreen,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${filteredWords.size} ta so'z topildi",
            fontSize = 13.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Word List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredWords, key = { it.id }) { word ->
                val isFav = favorites.contains(word.id)
                WordCardItem(
                    word = word,
                    isFavorite = isFav,
                    onAudioClick = { audioHelper.speakArabic(word.arabic) },
                    onFavoriteClick = { onToggleFavorite(word.id) },
                    onClick = { detailWord = word }
                )
            }
        }
    }

    // Word Detail Dialog
    detailWord?.let { word ->
        WordDetailDialog(
            word = word,
            isFavorite = favorites.contains(word.id),
            onAudioClick = { audioHelper.speakArabic(word.arabic) },
            onExampleAudioClick = { audioHelper.speakArabic(word.exampleArabic) },
            onFavoriteClick = { onToggleFavorite(word.id) },
            onDismiss = { detailWord = null }
        )
    }
}

@Composable
private fun WordCardItem(
    word: WordItem,
    isFavorite: Boolean,
    onAudioClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = word.arabic,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoGreen
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(DuoBlue.copy(alpha = 0.12f))
                            .clickable { onAudioClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Tinglash",
                            tint = DuoBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = word.uzbek,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = word.transliteration,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Sevimli",
                    tint = if (isFavorite) DuoRed else Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun WordDetailDialog(
    word: WordItem,
    isFavorite: Boolean,
    onAudioClick: () -> Unit,
    onExampleAudioClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(DuoGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = word.category,
                            color = DuoGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) DuoRed else Color.Gray
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Yopish")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Arabic with Audio
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = word.arabic,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoGreen
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DuoBlue.copy(alpha = 0.15f))
                            .clickable { onAudioClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Ovoz",
                            tint = DuoBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = word.uzbek,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "O'qilishi: ${word.transliteration}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                if (word.exampleArabic.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DuoBlue.copy(alpha = 0.08f))
                            .border(1.dp, DuoBlue.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Misol jumla:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuoBlue
                                )
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(DuoBlue.copy(alpha = 0.15f))
                                        .clickable { onExampleAudioClick() },
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
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = word.exampleArabic,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = word.exampleUzbek,
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
