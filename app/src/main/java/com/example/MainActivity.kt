package com.example

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.data.model.UserProfile
import com.example.data.repository.ArabTiliRepository
import com.example.data.repository.LocalLearningRepository
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.MainScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.MyApplicationTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val repository = remember { ArabTiliRepository(context) }
    val localLearningRepository = remember { LocalLearningRepository(context) }

    // Seed initial Room curriculum if empty on launch
    LaunchedEffect(Unit) {
        localLearningRepository.seedInitialCurriculumIfEmpty()
    }

    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    // Attempt silent auto-sign-in on app start
    LaunchedEffect(Unit) {
        if (currentUser == null) {
            attemptAutoSignIn(
                context = context,
                credentialManager = credentialManager,
                scope = coroutineScope,
                onSuccess = { currentUser = Firebase.auth.currentUser }
            )
        }
    }

    var isGuestSession by remember { mutableStateOf(false) }
    var guestOnboarded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        val user = currentUser
        if (user == null && !isGuestSession) {
            AuthScreen(
                onAuthSuccess = { currentUser = Firebase.auth.currentUser },
                onStartAsGuest = { isGuestSession = true }
            )
        } else if (user == null && isGuestSession) {
            if (!guestOnboarded) {
                OnboardingScreen(
                    onCompleted = { goal, minutes ->
                        coroutineScope.launch {
                            localLearningRepository.initializeUserProgressIfAbsent("local_guest")
                            guestOnboarded = true
                        }
                    }
                )
            } else {
                MainScreen(
                    repository = repository,
                    currentUserId = "local_guest",
                    localLearningRepository = localLearningRepository,
                    onSignOut = {
                        isGuestSession = false
                        guestOnboarded = false
                    }
                )
            }
        } else if (user != null) {
            val userProfileState by repository.observeUserProfile(user.uid)
                .collectAsState(initial = "loading")

            when (val profileResult = userProfileState) {
                "loading" -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DuoGreen)
                    }
                }
                null -> {
                    // New user -> show Onboarding to set learning goal & daily time
                    OnboardingScreen(
                        onCompleted = { goal, minutes ->
                            coroutineScope.launch {
                                val newProfile = UserProfile(
                                    userId = user.uid,
                                    email = user.email ?: "",
                                    displayName = user.displayName ?: "O'quvchi",
                                    avatar = "mascot",
                                    xp = 0,
                                    streak = 1,
                                    hearts = 5,
                                    dailyGoalMinutes = minutes,
                                    learningReason = goal
                                )
                                repository.createOrUpdateProfile(newProfile)
                            }
                        }
                    )
                }
                is UserProfile -> {
                    LaunchedEffect(profileResult) {
                        localLearningRepository.syncFromCloudProfile(profileResult)
                    }

                    MainScreen(
                        repository = repository,
                        currentUserId = user.uid,
                        localLearningRepository = localLearningRepository,
                        onSignOut = {
                            signOut(
                                credentialManager = credentialManager,
                                scope = coroutineScope,
                                onSignOutComplete = { currentUser = null }
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun attemptAutoSignIn(
    context: Context,
    credentialManager: CredentialManager,
    scope: CoroutineScope,
    onSuccess: () -> Unit
) {
    if (Firebase.auth.currentUser != null) {
        onSuccess()
        return
    }

    val clientId = try {
        context.getString(R.string.default_web_client_id)
    } catch (_: Exception) {
        return
    }

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(clientId)
        .setAutoSelectEnabled(true)
        .build()

    val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

    scope.launch {
        try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                Firebase.auth.signInWithCredential(authCredential).await()
                onSuccess()
            }
        } catch (_: Exception) {
            // Silent check failed; user will use the interactive button
        }
    }
}

private fun signOut(
    credentialManager: CredentialManager,
    scope: CoroutineScope,
    onSignOutComplete: () -> Unit
) {
    Firebase.auth.signOut()
    scope.launch {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.e("Auth", "Failed to clear credential state", e)
        } finally {
            onSignOutComplete()
        }
    }
}
