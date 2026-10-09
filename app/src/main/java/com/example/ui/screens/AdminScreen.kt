package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ContentPack
import com.example.data.model.WordItem
import com.example.data.repository.AdminStats
import com.example.data.repository.ArabTiliRepository
import com.example.ui.components.ArabicKeyboardView
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    repository: ArabTiliRepository,
    words: List<WordItem>,
    packs: List<ContentPack>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Yangi So'z", "So'zlar Ro'yxati", "15-kunlik To'plam", "Statistika")

    var stats by remember { mutableStateOf(AdminStats()) }

    LaunchedEffect(Unit) {
        val res = repository.fetchAdminStats()
        if (res.isSuccess) {
            stats = res.getOrNull() ?: AdminStats()
        }
    }

    // New Word Form State
    var arabicText by remember { mutableStateOf("") }
    var uzbekText by remember { mutableStateOf("") }
    var transliterationText by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf("Salomlashish") }
    var levelText by remember { mutableStateOf("Boshlang'ich") }
    var exampleArabic by remember { mutableStateOf("") }
    var exampleUzbek by remember { mutableStateOf("") }
    var editingWordId by remember { mutableStateOf<String?>(null) }

    // Active field for virtual Arabic keyboard input
    var activeArabicField by remember { mutableStateOf("word") } // "word" or "example"

    // New Pack Form State
    var packTitle by remember { mutableStateOf("") }
    var packDescription by remember { mutableStateOf("") }
    var packVersion by remember { mutableIntStateOf(2) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Admin Panel (boborakhim3)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Orqaga")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DuoPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = DuoPurple
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // ADD / EDIT WORD FORM
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (editingWordId != null) "So'zni tahrirlash" else "Yangi so'z qo'shish",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoPurple
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Arabic Word Input
                        OutlinedTextField(
                            value = arabicText,
                            onValueChange = { arabicText = it },
                            label = { Text("Arabcha so'z (harakatlari bilan)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Uzbek Translation
                        OutlinedTextField(
                            value = uzbekText,
                            onValueChange = { uzbekText = it },
                            label = { Text("O'zbekcha tarjimasi") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Transliteration
                        OutlinedTextField(
                            value = transliterationText,
                            onValueChange = { transliterationText = it },
                            label = { Text("O'qilishi (Transliteratsiya)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = categoryText,
                                onValueChange = { categoryText = it },
                                label = { Text("Kategoriya") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = levelText,
                                onValueChange = { levelText = it },
                                label = { Text("Daraja") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Example Sentence
                        OutlinedTextField(
                            value = exampleArabic,
                            onValueChange = { exampleArabic = it },
                            label = { Text("Misol jumla (Arabcha harakatli)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = exampleUzbek,
                            onValueChange = { exampleUzbek = it },
                            label = { Text("Misol jumla (O'zbekcha tarjimasi)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Virtual Arabic Keyboard with Harakat
                        Text(
                            text = "Arabcha Harakatli Klaviatura:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        ArabicKeyboardView(
                            onCharTyped = { char ->
                                if (activeArabicField == "word") {
                                    arabicText += char
                                } else {
                                    exampleArabic += char
                                }
                            },
                            onBackspace = {
                                if (activeArabicField == "word") {
                                    if (arabicText.isNotEmpty()) arabicText = arabicText.dropLast(1)
                                } else {
                                    if (exampleArabic.isNotEmpty()) exampleArabic = exampleArabic.dropLast(1)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        DuoButton(
                            text = if (editingWordId != null) "Saqlash" else "So'zni Bazaga Qo'shish",
                            onClick = {
                                if (arabicText.isBlank() || uzbekText.isBlank()) {
                                    Toast.makeText(context, "Arabcha va o'zbekcha maydonlarni to'ldiring", Toast.LENGTH_SHORT).show()
                                    return@DuoButton
                                }
                                coroutineScope.launch {
                                    val word = WordItem(
                                        id = editingWordId ?: "word_${System.currentTimeMillis()}",
                                        arabic = arabicText.trim(),
                                        uzbek = uzbekText.trim(),
                                        transliteration = transliterationText.trim(),
                                        category = categoryText.trim(),
                                        level = levelText.trim(),
                                        exampleArabic = exampleArabic.trim(),
                                        exampleUzbek = exampleUzbek.trim()
                                    )
                                    val result = if (editingWordId != null) {
                                        repository.updateWord(word)
                                    } else {
                                        repository.addWord(word)
                                    }
                                    if (result.isSuccess) {
                                        Toast.makeText(context, "So'z muvaffaqiyatli saqlandi!", Toast.LENGTH_SHORT).show()
                                        arabicText = ""
                                        uzbekText = ""
                                        transliterationText = ""
                                        exampleArabic = ""
                                        exampleUzbek = ""
                                        editingWordId = null
                                    } else {
                                        Toast.makeText(context, "Xatolik: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            color = DuoButtonColor.GREEN,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

                1 -> {
                    // WORDS LIST
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(words, key = { it.id }) { word ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DuoGrayBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = word.arabic,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DuoGreen
                                        )
                                        Text(
                                            text = word.uzbek,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        Text(
                                            text = "${word.category} • ${word.transliteration}",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }

                                    Row {
                                        IconButton(onClick = {
                                            arabicText = word.arabic
                                            uzbekText = word.uzbek
                                            transliterationText = word.transliteration
                                            categoryText = word.category
                                            levelText = word.level
                                            exampleArabic = word.exampleArabic
                                            exampleUzbek = word.exampleUzbek
                                            editingWordId = word.id
                                            selectedTab = 0
                                        }) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Tahrirlash", tint = DuoBlue)
                                        }

                                        IconButton(onClick = {
                                            coroutineScope.launch {
                                                val res = repository.deleteWord(word.id)
                                                if (res.isSuccess) {
                                                    Toast.makeText(context, "So'z o'chirildi", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }) {
                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "O'chirish", tint = DuoRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // 15-DAY CONTENT PACKS
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "15-kunlik Yangi To'plam Chiqarish",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoPurple
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = packTitle,
                            onValueChange = { packTitle = it },
                            label = { Text("To'plam nomi (masalan: 2-to'plam: Kundalik hayot)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = packDescription,
                            onValueChange = { packDescription = it },
                            label = { Text("To'plam tavsifi") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        DuoButton(
                            text = "To'plamni E'lon Qilish (Nashr etish)",
                            onClick = {
                                if (packTitle.isBlank()) {
                                    Toast.makeText(context, "To'plam nomini kiriting", Toast.LENGTH_SHORT).show()
                                    return@DuoButton
                                }
                                coroutineScope.launch {
                                    val newPack = ContentPack(
                                        id = "pack_${System.currentTimeMillis()}",
                                        title = packTitle.trim(),
                                        description = packDescription.trim(),
                                        version = packVersion++,
                                        isPublished = true,
                                        wordCount = 20
                                    )
                                    val res = repository.createContentPack(newPack)
                                    if (res.isSuccess) {
                                        Toast.makeText(context, "Yangi to'plam chiqarildi!", Toast.LENGTH_SHORT).show()
                                        packTitle = ""
                                        packDescription = ""
                                    }
                                }
                            },
                            color = DuoButtonColor.GREEN,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Mavjud to'plamlar:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        packs.forEach { pack ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = pack.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = pack.description,
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // STATS TAB
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Ilova Statistikasi",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DuoPurple
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DuoGreen.copy(alpha = 0.15f))
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.ViewList, contentDescription = null, tint = DuoGreen, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("Jami so'zlar soni", fontSize = 13.sp, color = Color.Gray)
                                    Text("${stats.totalWords} ta", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DuoGreen)
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DuoBlue.copy(alpha = 0.15f))
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.People, contentDescription = null, tint = DuoBlue, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("Ro'yxatdan o'tgan o'quvchilar", fontSize = 13.sp, color = Color.Gray)
                                    Text("${stats.totalUsers} nafar", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DuoBlue)
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DuoPurple.copy(alpha = 0.15f))
                        ) {
                            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = DuoPurple, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("15-kunlik to'plamlar", fontSize = 13.sp, color = Color.Gray)
                                    Text("${stats.totalPacks} ta", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DuoPurple)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
