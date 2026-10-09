package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioHelper
import com.example.data.model.Exercise
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.ui.components.ArabicKeyboardView
import com.example.ui.components.ConfettiOverlay
import com.example.ui.components.DuoButton
import com.example.ui.components.DuoButtonColor
import com.example.ui.components.DuoProgressBar
import com.example.ui.components.LessonCongratulatoryDialog
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGrayBorder
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoOrange
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LessonExerciseScreen(
    lesson: Lesson,
    hearts: Int,
    onFinishLesson: (xpEarned: Int) -> Unit,
    onHeartDeducted: () -> Unit,
    onRefillHearts: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val audioHelper = remember { AudioHelper(context) }

    // Queue of exercises: mistakes will be repeated at the end
    val exerciseQueue = remember { mutableStateListOf<Exercise>().apply { addAll(lesson.exercises) } }
    var currentExerciseIndex by remember { mutableIntStateOf(0) }
    var totalExercisesCompleted by remember { mutableIntStateOf(0) }
    val initialTotalCount = remember { lesson.exercises.size }

    // Current exercise state
    val currentExercise = exerciseQueue.getOrNull(currentExerciseIndex)

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var typedAnswer by remember { mutableStateOf("") }
    val builtSentence = remember { mutableStateListOf<String>() }
    val availableSentenceWords = remember { mutableStateListOf<String>() }

    // Matching pairs state
    val matchedPairs = remember { mutableStateMapOf<String, String>() }
    var selectedLeftPair by remember { mutableStateOf<String?>(null) }
    var selectedRightPair by remember { mutableStateOf<String?>(null) }

    // Feedback state: null = not checked, true = correct, false = wrong
    var isAnswerChecked by remember { mutableStateOf(false) }
    var isAnswerCorrect by remember { mutableStateOf(false) }

    // Lesson Completed state
    var isLessonCompleted by remember { mutableStateOf(false) }

    // Initialize sentence words when exercise changes
    LaunchedEffect(currentExerciseIndex, currentExercise?.id) {
        selectedOption = null
        typedAnswer = ""
        builtSentence.clear()
        availableSentenceWords.clear()
        matchedPairs.clear()
        selectedLeftPair = null
        selectedRightPair = null
        isAnswerChecked = false

        currentExercise?.let { ex ->
            if (ex.sentenceWords.isNotEmpty()) {
                availableSentenceWords.addAll(ex.sentenceWords.shuffled())
            }
            if (ex.type == ExerciseType.LISTENING && ex.arabicText.isNotEmpty()) {
                audioHelper.speakArabic(ex.arabicText)
            }
        }
    }

    if (isLessonCompleted) {
        val calculatedScore = (70 + (hearts * 6)).coerceIn(70, 100)
        val calculatedAccuracy = (60 + (hearts * 8)).coerceIn(60, 100)

        LessonCongratulatoryDialog(
            score = calculatedScore,
            xpEarned = lesson.xpReward,
            heartsLeft = hearts,
            lessonTitle = lesson.title,
            accuracyPercent = calculatedAccuracy,
            onReturnToDashboard = { onFinishLesson(lesson.xpReward) }
        )
        return
    }

    if (currentExercise == null) {
        LaunchedEffect(Unit) { isLessonCompleted = true }
        return
    }

    // Out of hearts state
    if (hearts <= 0) {
        OutOfHeartsDialog(
            onRefill = onRefillHearts,
            onClose = onClose
        )
        return
    }

    val progress = (totalExercisesCompleted.toFloat() / initialTotalCount.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar: Close, Progress bar, Hearts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Chiqish",
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }

            DuoProgressBar(
                progress = progress,
                height = 16.dp,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                testTag = "lesson_progress_bar"
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Jonlar",
                    tint = DuoRed,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$hearts",
                    color = DuoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }

        // Exercise Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentExercise.prompt,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent Arabic word with pronunciation if applicable
            if (currentExercise.arabicText.isNotEmpty() && currentExercise.type != ExerciseType.LETTER_RECOGNITION) {
                ArabicWordBanner(
                    arabicText = currentExercise.arabicText,
                    onAudioClick = { audioHelper.speakArabic(currentExercise.arabicText) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Exercise specific UI
            when (currentExercise.type) {
                ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                ExerciseType.MULTIPLE_CHOICE_UZ_TO_AR,
                ExerciseType.LETTER_RECOGNITION,
                ExerciseType.LISTENING -> {
                    currentExercise.options.forEach { option ->
                        val isSelected = selectedOption == option
                        OptionChoiceCard(
                            text = option,
                            isSelected = isSelected,
                            enabled = !isAnswerChecked,
                            onClick = {
                                if (!isAnswerChecked) {
                                    selectedOption = option
                                    if (currentExercise.type == ExerciseType.MULTIPLE_CHOICE_UZ_TO_AR) {
                                        audioHelper.speakArabic(option)
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                ExerciseType.MATCHING_PAIRS -> {
                    MatchingPairsView(
                        pairs = currentExercise.pairs,
                        matchedPairs = matchedPairs,
                        selectedLeft = selectedLeftPair,
                        selectedRight = selectedRightPair,
                        onLeftSelected = {
                            selectedLeftPair = it
                            audioHelper.speakArabic(it)
                            checkPair(it, selectedRightPair, currentExercise.pairs, matchedPairs) {
                                selectedLeftPair = null
                                selectedRightPair = null
                            }
                        },
                        onRightSelected = {
                            selectedRightPair = it
                            checkPair(selectedLeftPair, it, currentExercise.pairs, matchedPairs) {
                                selectedLeftPair = null
                                selectedRightPair = null
                            }
                        }
                    )
                }

                ExerciseType.SENTENCE_BUILDER -> {
                    SentenceBuilderView(
                        builtWords = builtSentence,
                        availableWords = availableSentenceWords,
                        onWordAdded = { word ->
                            if (!isAnswerChecked) {
                                builtSentence.add(word)
                                availableSentenceWords.remove(word)
                                audioHelper.speakArabic(word)
                            }
                        },
                        onWordRemoved = { word ->
                            if (!isAnswerChecked) {
                                builtSentence.remove(word)
                                availableSentenceWords.add(word)
                            }
                        }
                    )
                }

                ExerciseType.TYPING -> {
                    TypingExerciseView(
                        typedText = typedAnswer,
                        onTextChanged = { typedAnswer = it },
                        onCharTyped = { char -> typedAnswer += char },
                        onBackspace = { if (typedAnswer.isNotEmpty()) typedAnswer = typedAnswer.dropLast(1) }
                    )
                }
            }
        }

        // Bottom Action & Feedback Drawer
        BottomFeedbackDrawer(
            isAnswerChecked = isAnswerChecked,
            isAnswerCorrect = isAnswerCorrect,
            explanation = currentExercise.explanation,
            correctAnswer = currentExercise.correctAnswer,
            onCheck = {
                val correct = verifyAnswer(
                    exercise = currentExercise,
                    selectedOption = selectedOption,
                    typedAnswer = typedAnswer,
                    builtSentence = builtSentence,
                    matchedPairs = matchedPairs
                )
                isAnswerChecked = true
                isAnswerCorrect = correct

                if (correct) {
                    audioHelper.playCorrectSound(coroutineScope)
                    totalExercisesCompleted++
                } else {
                    audioHelper.playWrongSound(coroutineScope)
                    onHeartDeducted()
                    // Re-queue the wrong exercise so it repeats later!
                    exerciseQueue.add(currentExercise)
                }
            },
            onContinue = {
                isAnswerChecked = false
                if (currentExerciseIndex < exerciseQueue.size - 1) {
                    currentExerciseIndex++
                } else {
                    isLessonCompleted = true
                }
            },
            canCheck = when (currentExercise.type) {
                ExerciseType.MATCHING_PAIRS -> matchedPairs.size == currentExercise.pairs.size
                ExerciseType.SENTENCE_BUILDER -> builtSentence.isNotEmpty()
                ExerciseType.TYPING -> typedAnswer.isNotBlank()
                else -> selectedOption != null
            }
        )
    }
}

private fun checkPair(
    left: String?,
    right: String?,
    allPairs: Map<String, String>,
    matched: MutableMap<String, String>,
    resetSelection: () -> Unit
) {
    if (left != null && right != null) {
        if (allPairs[left] == right) {
            matched[left] = right
            resetSelection()
        } else {
            resetSelection()
        }
    }
}

private fun verifyAnswer(
    exercise: Exercise,
    selectedOption: String?,
    typedAnswer: String,
    builtSentence: List<String>,
    matchedPairs: Map<String, String>
): Boolean {
    return when (exercise.type) {
        ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
        ExerciseType.MULTIPLE_CHOICE_UZ_TO_AR,
        ExerciseType.LETTER_RECOGNITION,
        ExerciseType.LISTENING -> {
            selectedOption == exercise.correctAnswer
        }
        ExerciseType.MATCHING_PAIRS -> {
            matchedPairs.size == exercise.pairs.size
        }
        ExerciseType.SENTENCE_BUILDER -> {
            builtSentence.joinToString(" ").trim() == exercise.correctAnswer.trim()
        }
        ExerciseType.TYPING -> {
            typedAnswer.trim().equals(exercise.correctAnswer.trim(), ignoreCase = true)
        }
    }
}

@Composable
private fun ArabicWordBanner(
    arabicText: String,
    onAudioClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, DuoGrayBorder, RoundedCornerShape(20.dp))
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = arabicText,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DuoGreen,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DuoBlue.copy(alpha = 0.15f))
                    .clickable { onAudioClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Ovoz chiqarish",
                    tint = DuoBlue,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun OptionChoiceCard(
    text: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) DuoGreen else DuoGrayBorder,
        label = "border"
    )
    val bgColor = if (isSelected) DuoGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SentenceBuilderView(
    builtWords: List<String>,
    availableWords: List<String>,
    onWordAdded: (String) -> Unit,
    onWordRemoved: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Target Sentence Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, DuoGrayBorder, RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
        ) {
            if (builtWords.isEmpty()) {
                Text(
                    text = "Quyidagi so'zlarni bosib jumlani tuzing...",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    builtWords.forEach { word ->
                        WordChip(text = word, isSelected = true) { onWordRemoved(word) }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Available Words Bank
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableWords.forEach { word ->
                WordChip(text = word, isSelected = false) { onWordAdded(word) }
            }
        }
    }
}

@Composable
private fun WordChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, if (isSelected) DuoGreen else DuoGrayBorder, RoundedCornerShape(12.dp))
            .background(if (isSelected) DuoGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun MatchingPairsView(
    pairs: Map<String, String>,
    matchedPairs: Map<String, String>,
    selectedLeft: String?,
    selectedRight: String?,
    onLeftSelected: (String) -> Unit,
    onRightSelected: (String) -> Unit
) {
    val leftItems = pairs.keys.toList()
    val rightItems = remember { pairs.values.toList().shuffled() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Arabic Words
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            leftItems.forEach { left ->
                val isMatched = matchedPairs.containsKey(left)
                val isSelected = selectedLeft == left
                PairChip(
                    text = left,
                    isSelected = isSelected,
                    isMatched = isMatched,
                    onClick = { if (!isMatched) onLeftSelected(left) }
                )
            }
        }

        // Uzbek Meanings
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rightItems.forEach { right ->
                val isMatched = matchedPairs.containsValue(right)
                val isSelected = selectedRight == right
                PairChip(
                    text = right,
                    isSelected = isSelected,
                    isMatched = isMatched,
                    onClick = { if (!isMatched) onRightSelected(right) }
                )
            }
        }
    }
}

@Composable
private fun PairChip(
    text: String,
    isSelected: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit
) {
    val (bgColor, borderColor) = when {
        isMatched -> Pair(DuoGreen.copy(alpha = 0.2f), DuoGreen)
        isSelected -> Pair(DuoBlue.copy(alpha = 0.2f), DuoBlue)
        else -> Pair(MaterialTheme.colorScheme.surface, DuoGrayBorder)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(2.dp, borderColor, RoundedCornerShape(14.dp))
            .background(bgColor)
            .clickable(enabled = !isMatched) { onClick() }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isMatched) DuoGreen else MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TypingExerciseView(
    typedText: String,
    onTextChanged: (String) -> Unit,
    onCharTyped: (String) -> Unit,
    onBackspace: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = typedText,
            onValueChange = onTextChanged,
            label = { Text("Javobingizni yozing") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Virtual Arabic Keyboard with Harakat
        ArabicKeyboardView(
            onCharTyped = onCharTyped,
            onBackspace = onBackspace
        )
    }
}

@Composable
private fun BottomFeedbackDrawer(
    isAnswerChecked: Boolean,
    isAnswerCorrect: Boolean,
    explanation: String,
    correctAnswer: String,
    onCheck: () -> Unit,
    onContinue: () -> Unit,
    canCheck: Boolean
) {
    val containerBg = when {
        !isAnswerChecked -> MaterialTheme.colorScheme.surface
        isAnswerCorrect -> Color(0xFFD7FFB8)
        else -> Color(0xFFFFDFE0)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = containerBg,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            AnimatedVisibility(visible = isAnswerChecked) {
                Column(modifier = Modifier.padding(bottom = 14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isAnswerCorrect) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isAnswerCorrect) DuoGreen else DuoRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAnswerCorrect) "To'g'ri! Barakalla!" else "To'g'ri javob:",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAnswerCorrect) DuoGreen else DuoRed
                        )
                    }
                    if (!isAnswerCorrect && correctAnswer.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = correctAnswer,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    if (explanation.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = explanation,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            DuoButton(
                text = if (!isAnswerChecked) "Tekshirish" else "Davom etish",
                onClick = { if (!isAnswerChecked) onCheck() else onContinue() },
                color = when {
                    !isAnswerChecked -> if (canCheck) DuoButtonColor.GREEN else DuoButtonColor.GRAY_DISABLED
                    isAnswerCorrect -> DuoButtonColor.GREEN
                    else -> DuoButtonColor.RED
                },
                enabled = isAnswerChecked || canCheck,
                testTag = "check_continue_button"
            )
        }
    }
}

@Composable
private fun LessonCelebrationView(
    xpReward: Int,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ConfettiOverlay()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Celebrating Mascot
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .background(DuoGreen.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_mascot_celebrating),
                        contentDescription = "G'alaba!",
                        modifier = Modifier.size(175.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Dars Yakunlandi!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoGreen,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ajoyib natija! Siz yana bir qadam oldinga siljidingiz.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // XP Reward Card
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DuoYellow.copy(alpha = 0.2f))
                        .border(2.dp, DuoYellow, RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+$xpReward XP",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoYellow
                    )
                }
            }

            DuoButton(
                text = "Davom etish",
                onClick = onContinue,
                color = DuoButtonColor.GREEN,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OutOfHeartsDialog(
    onRefill: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = DuoRed,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Jonlaringiz tugadi!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Darsni davom ettirish uchun jonlarni to'ldiring.",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                DuoButton(
                    text = "Jonlarni to'ldirish (5/5)",
                    onClick = onRefill,
                    color = DuoButtonColor.RED,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                DuoButton(
                    text = "Darsdan chiqish",
                    onClick = onClose,
                    color = DuoButtonColor.WHITE_OUTLINE,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
