package com.ahmedyejam.mks.ui

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmedyejam.mks.di.AppModule
import com.ahmedyejam.mks.ui.booktools.AiPromptDeckListScreen
import com.ahmedyejam.mks.ui.booktools.AiPromptDeckScreen
import com.ahmedyejam.mks.ui.booktools.BookNotesScreen
import com.ahmedyejam.mks.ui.booktools.BookToolsViewModel
import com.ahmedyejam.mks.ui.booktools.ReviewBlueprintListScreen
import com.ahmedyejam.mks.ui.booktools.ReviewBlueprintScreen
import com.ahmedyejam.mks.ui.booktools.SlideshowCourseListScreen
import com.ahmedyejam.mks.ui.booktools.SourceDocumentListScreen
import com.ahmedyejam.mks.ui.category.CategoryQuestionsScreen
import com.ahmedyejam.mks.ui.category.CategoryQuestionsViewModel
import com.ahmedyejam.mks.ui.common.InvalidRouteScreen
import com.ahmedyejam.mks.ui.data.DataToolsScreen
import com.ahmedyejam.mks.ui.data.DataToolsViewModel
import com.ahmedyejam.mks.ui.flashcard.FlashcardDeckScreen
import com.ahmedyejam.mks.ui.import.ImportViewModel
import com.ahmedyejam.mks.ui.library.LibraryScreen
import com.ahmedyejam.mks.ui.library.LibraryViewModel
import com.ahmedyejam.mks.ui.navigation.requireNonBlankStringArg
import com.ahmedyejam.mks.ui.navigation.requirePositiveLongArg
import com.ahmedyejam.mks.ui.quiz.CompilerViewModel
import com.ahmedyejam.mks.ui.quiz.QuizDetailTabsScreen
import com.ahmedyejam.mks.ui.quiz.QuizPlayerScreen
import com.ahmedyejam.mks.ui.quiz.QuizQuestionsScreen
import com.ahmedyejam.mks.ui.quiz.QuizQuestionsViewModel
import com.ahmedyejam.mks.ui.quiz.QuizViewModel
import com.ahmedyejam.mks.ui.review.ReviewDashboardScreen
import com.ahmedyejam.mks.ui.review.ReviewDashboardViewModel
import com.ahmedyejam.mks.ui.scanner.ScannerScreen
import com.ahmedyejam.mks.ui.scanner.ScannerViewModel
import com.ahmedyejam.mks.ui.search.GlobalSearchScreen
import com.ahmedyejam.mks.ui.search.GlobalSearchViewModel
import com.ahmedyejam.mks.ui.session.SessionViewModel
import com.ahmedyejam.mks.ui.settings.SettingsScreen
import com.ahmedyejam.mks.ui.slideshow.SlideshowCourseScreen
import com.ahmedyejam.mks.ui.summary.SummaryScreen
import com.ahmedyejam.mks.ui.summary.SummaryViewModel
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens
import com.ahmedyejam.mks.ui.welcome.WelcomeScreen
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
@Composable
fun MksNavHost(
    navController: NavHostController,
    appModule: AppModule,
    showWelcomeOnStartup: Boolean,
    sharedUris: List<Uri>? = null,
    onConsumedSharedUris: () -> Unit = {},
) {
    val currentThemeMode by appModule.dataStoreManager.themeMode.collectAsState(initial = "DAWN")
    val scope = rememberCoroutineScope()
    val startDestination = if (showWelcomeOnStartup) MksRoutes.WELCOME else MksRoutes.LIBRARY
    var libraryResetSignal by remember { mutableIntStateOf(0) }

    fun returnToLibraryRoot() {
        navController.navigate(MksRoutes.LIBRARY) {
            popUpTo(MksRoutes.LIBRARY) { inclusive = true }
            launchSingleTop = true
        }
    }

    val tokens = LocalMksDesignTokens.current
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { if (tokens.isPlain) EnterTransition.None else fadeIn() + slideInHorizontally { it } },
        exitTransition = { if (tokens.isPlain) ExitTransition.None else fadeOut() + slideOutHorizontally { -it } },
        popEnterTransition = { if (tokens.isPlain) EnterTransition.None else fadeIn() + slideInHorizontally { -it } },
        popExitTransition = { if (tokens.isPlain) ExitTransition.None else fadeOut() + slideOutHorizontally { it } },
    ) {
        composable(MksRoutes.WELCOME) {
            // Get initial language for WelcomeScreen without observing it here to avoid NavHost restarts
            val currentLanguage by appModule.dataStoreManager.language.collectAsState(initial = "en")

            WelcomeScreen(
                currentLanguage = currentLanguage,
                themeMode = currentThemeMode,
                onLanguageChanged = { lang ->
                    scope.launch { appModule.dataStoreManager.setLanguage(lang) }
                },
            ) {
                scope.launch { appModule.dataStoreManager.setShowWelcomeOnStartup(enabled = false) }
                navController.navigate(MksRoutes.LIBRARY) {
                    popUpTo(MksRoutes.WELCOME) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable(MksRoutes.LIBRARY) {
            val libraryViewModel: LibraryViewModel = hiltViewModel()
            
            LaunchedEffect(quizId) {
                questionsViewModel.loadQuiz(quizId)
            }
            
            val questionsState by questionsViewModel.uiState.collectAsState()

            QuizDetailTabsScreen(
                quizId = quizId,
                quizTitle = questionsState.quiz?.title,
                sessionViewModel = sessionViewModel,
                questionsViewModel = questionsViewModel,
                dataStoreManager = appModule.dataStoreManager,
                onSessionSelected = { sessionId, isCompleted ->
                    if (isCompleted) {
                        navController.navigate("summary/$sessionId")
                    } else {
                        navController.navigate("quiz/$quizId?sessionId=$sessionId")
                    }
                },
                onBack = {
                    navController.navigate("library") {
                        popUpTo("library") { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            route = "scanner/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val quizId = backStackEntry.requirePositiveLongArg("quizId")
            if (quizId == null) {
                InvalidRouteScreen { navController.popBackStack() }
                return@composable
            }
            val scannerViewModel: ScannerViewModel = hiltViewModel()
            
            ScannerScreen(
                quizId = quizId,
                viewModel = scannerViewModel,
            ) { navController.popBackStack() }
        }

        composable(
            route = "adaptive/{type}/{id}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val type = backStackEntry.requireNonBlankStringArg("type")
            val id = backStackEntry.requireNonBlankStringArg("id")
            if ((type == null) || (id == null)) {
                InvalidRouteScreen { navController.popBackStack() }
                return@composable
            }
            val quizViewModel: QuizViewModel = hiltViewModel()
            
            LaunchedEffect(type, id) {
                quizViewModel.startAdaptiveTraining(type, id)
            }
            
            QuizPlayerScreen(
                viewModel = quizViewModel,
                onQuizFinished = { sId, _, _ ->
                    if (sId != -1L) {
                        navController.navigate("summary/$sId") {
                            popUpTo("library") { inclusive = false }
                        }
                    } else {
                        navController.navigate("library") {
                            popUpTo("library") { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = "summary/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val sessionId = backStackEntry.requirePositiveLongArg("sessionId")
            if (sessionId == null) {
                InvalidRouteScreen { navController.popBackStack() }
                return@composable
            }
            val summaryViewModel: SummaryViewModel = hiltViewModel()

            SummaryScreen(
                viewModel = summaryViewModel,
                sessionId = sessionId,
                onHomeClick = {
                    navController.navigate("library") {
                        popUpTo("library") { inclusive = true }
                    }
                },
                onRetryClick = { navController.popBackStack() },
            )
        }
    }
}
