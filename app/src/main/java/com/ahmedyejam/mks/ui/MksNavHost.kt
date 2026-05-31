package com.ahmedyejam.mks.ui

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens
import com.ahmedyejam.mks.ui.category.CategoryQuestionsScreen
import com.ahmedyejam.mks.ui.category.CategoryQuestionsViewModel
import com.ahmedyejam.mks.ui.booktools.AiPromptDeckScreen
import com.ahmedyejam.mks.ui.booktools.BookNotesScreen
import com.ahmedyejam.mks.ui.booktools.BookToolsViewModel
import com.ahmedyejam.mks.ui.booktools.ReviewBlueprintScreen
import com.ahmedyejam.mks.ui.booktools.SlideshowCourseScreen
import com.ahmedyejam.mks.ui.booktools.AiPromptDeckListScreen
import com.ahmedyejam.mks.ui.booktools.ReviewBlueprintListScreen
import com.ahmedyejam.mks.ui.booktools.SourceDocumentListScreen
import com.ahmedyejam.mks.ui.booktools.SlideshowCourseListScreen
import com.ahmedyejam.mks.ui.import.ImportViewModel
import com.ahmedyejam.mks.ui.library.LibraryScreen
import com.ahmedyejam.mks.ui.library.LibraryViewModel
import com.ahmedyejam.mks.ui.quiz.CompilerViewModel
import com.ahmedyejam.mks.ui.flashcard.FlashcardDeckScreen
import com.ahmedyejam.mks.ui.quiz.QuizPlayerScreen
import com.ahmedyejam.mks.ui.quiz.QuizQuestionsScreen
import com.ahmedyejam.mks.ui.quiz.QuizQuestionsViewModel
import com.ahmedyejam.mks.ui.quiz.QuizViewModel
import com.ahmedyejam.mks.ui.scanner.ScannerScreen
import com.ahmedyejam.mks.ui.scanner.ScannerViewModel
import com.ahmedyejam.mks.ui.session.SessionManagementScreen
import com.ahmedyejam.mks.ui.session.SessionViewModel
import com.ahmedyejam.mks.ui.settings.SettingsScreen
import com.ahmedyejam.mks.ui.search.GlobalSearchScreen
import com.ahmedyejam.mks.ui.search.GlobalSearchViewModel
import com.ahmedyejam.mks.ui.review.ReviewDashboardScreen
import com.ahmedyejam.mks.ui.review.ReviewDashboardViewModel
import com.ahmedyejam.mks.ui.data.DataToolsScreen
import com.ahmedyejam.mks.ui.data.DataToolsViewModel
import com.ahmedyejam.mks.ui.summary.SummaryScreen
import com.ahmedyejam.mks.ui.summary.SummaryViewModel
import com.ahmedyejam.mks.ui.welcome.WelcomeScreen
import com.ahmedyejam.mks.ui.common.InvalidRouteScreen
import com.ahmedyejam.mks.ui.navigation.MksRouteBuilders
import com.ahmedyejam.mks.ui.navigation.requireNonBlankStringArg
import com.ahmedyejam.mks.ui.navigation.requirePositiveLongArg
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
@Composable
fun MksNavHost(
    navController: NavHostController,
    appModule: AppModule,
    showWelcomeOnStartup: Boolean,
    sharedUris: List<Uri>? = null,
    onConsumedSharedUris: () -> Unit = {}
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
        popExitTransition = { if (tokens.isPlain) ExitTransition.None else fadeOut() + slideOutHorizontally { it } }
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
                scope.launch { appModule.dataStoreManager.setShowWelcomeOnStartup(false) }
                navController.navigate(MksRoutes.LIBRARY) {
                    popUpTo(MksRoutes.WELCOME) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable(MksRoutes.LIBRARY) {
            val libraryViewModel: LibraryViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LibraryViewModel(appModule.repository, appModule.dataStoreManager) as T
                    }
                }
            )
            val compilerViewModel: CompilerViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CompilerViewModel(appModule.context, appModule.repository) as T
                    }
                }
            )
            val importViewModel: ImportViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ImportViewModel(appModule.repository) as T
                    }
                }
            )

            LibraryScreen(
                viewModel = libraryViewModel,
                compilerViewModel = compilerViewModel,
                importViewModel = importViewModel,
                returnResetSignal = libraryResetSignal,
                sharedUris = sharedUris,
                onConsumedSharedUris = onConsumedSharedUris,
                onQuizSelected = { quizId -> navController.navigate(MksRoutes.sessions(quizId)) },
                onScanSelected = { quizId -> navController.navigate(MksRoutes.scanner(quizId)) },
                onAdaptiveSelected = { type, id -> navController.navigate(MksRoutes.adaptive(type, id)) },
                onBrowseQuestions = { quizId -> navController.navigate(MksRoutes.quizQuestions(quizId)) },
                onCategorySelected = { category -> navController.navigate(MksRoutes.category(category)) },
                onSettingsSelected = { navController.navigate(MksRoutes.SETTINGS) },
                onFlashcardDeckSelected = { deckId -> navController.navigate(MksRoutes.flashcards(deckId)) },
                onSlideshowSelected = { courseId -> navController.navigate(MksRoutes.slideshow(courseId)) },
                onReviewBlueprintSelected = { noteId -> navController.navigate(MksRoutes.blueprint(noteId)) },
                onBookSlideshowSelected = { bookId -> navController.navigate(MksRoutes.bookSlideshows(bookId)) },
                onBookReviewBlueprintSelected = { bookId -> navController.navigate(MksRoutes.bookBlueprints(bookId)) },
                onBookSourcesSelected = { bookId -> navController.navigate(MksRoutes.bookSources(bookId)) },
                onBookNotesSelected = { bookId -> navController.navigate(MksRoutes.bookNotes(bookId)) },
                onBookAiPromptDeckSelected = { bookId -> navController.navigate(MksRoutes.bookPrompts(bookId)) },
                onAiPromptDeckSelected = { promptId -> navController.navigate(MksRoutes.promptDeck(promptId)) },
                onBookDashboardSelected = { bookId -> navController.navigate(MksRoutes.bookDashboard(bookId)) }
            )
        }

        composable(
            route = "quiz_questions/{quizId}?questionId={questionId}",
            arguments = listOf(
                navArgument("quizId") { type = NavType.LongType },
                navArgument("questionId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.requirePositiveLongArg("quizId")
            if (quizId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val questionIdInput = backStackEntry.arguments?.getLong("questionId") ?: -1L
            val questionId = if (questionIdInput == -1L) null else questionIdInput

            val viewModel: QuizQuestionsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return QuizQuestionsViewModel(appModule.repository) as T
                    }
                }
            )

            LaunchedEffect(quizId) {
                viewModel.loadQuiz(quizId)
            }

            QuizQuestionsScreen(
                viewModel = viewModel,
                focusedQuestionId = questionId,
            ) { navController.popBackStack() }
        }

        composable(
            route = "flashcards/{deckId}?cardId={cardId}",
            arguments = listOf(
                navArgument("deckId") { type = NavType.LongType },
                navArgument("cardId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.requirePositiveLongArg("deckId")
            if (deckId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val cardIdInput = backStackEntry.arguments?.getLong("cardId") ?: -1L
            val cardId = if (cardIdInput == -1L) null else cardIdInput
            FlashcardDeckScreen(deckId = deckId, focusedCardId = cardId, appModule = appModule, onBack = { navController.popBackStack() })
        }

        composable(
            route = "book_dashboard/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.requirePositiveLongArg("bookId")
            if (bookId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            com.ahmedyejam.mks.ui.booktools.BookKnowledgeDashboardScreen(
                bookId = bookId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenQuizzes = { 
                    // Special case: just go back to library and select this book?
                    // Or we stay in dashboard.
                    navController.popBackStack() 
                },
                onOpenFlashcards = { navController.navigate(MksRoutes.bookNotes(bookId)) }, // Wait, flashcards list?
                onOpenSlideshows = { navController.navigate(MksRoutes.bookSlideshows(bookId)) },
                onOpenNotes = { navController.navigate(MksRoutes.bookBlueprints(bookId)) },
                onOpenPrompts = { navController.navigate(MksRoutes.bookPrompts(bookId)) },
                onOpenSources = { navController.navigate(MksRoutes.bookSources(bookId)) }
            )
        }
        composable(
            route = "book_slideshows/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.requirePositiveLongArg("bookId")
            if (bookId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            SlideshowCourseListScreen(
                bookId = bookId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenCourse = { courseId -> navController.navigate("slideshow/$courseId") }
            )
        }

        composable(
            route = "book_blueprints/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.requirePositiveLongArg("bookId")
            if (bookId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            ReviewBlueprintListScreen(
                bookId = bookId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenNote = { noteId -> navController.navigate("blueprint/$noteId") }
            )
        }


        composable(
            route = "book_sources/{bookId}?sourceId={sourceId}",
            arguments = listOf(
                navArgument("bookId") { type = NavType.LongType },
                navArgument("sourceId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.requirePositiveLongArg("bookId")
            if (bookId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val sourceIdInput = backStackEntry.arguments?.getLong("sourceId") ?: -1L
            val sourceId = if (sourceIdInput == -1L) null else sourceIdInput
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            SourceDocumentListScreen(
                bookId = bookId,
                focusedSourceId = sourceId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "book_prompts/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.requirePositiveLongArg("bookId")
            if (bookId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            AiPromptDeckListScreen(
                bookId = bookId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenPrompt = { promptId -> navController.navigate("prompt_deck/$promptId") }
            )
        }

        composable(
            route = "slideshow/{courseId}?slideId={slideId}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.LongType },
                navArgument("slideId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.requirePositiveLongArg("courseId")
            if (courseId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val slideIdInput = backStackEntry.arguments?.getLong("slideId") ?: -1L
            val slideId = if (slideIdInput == -1L) null else slideIdInput
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            SlideshowCourseScreen(courseId = courseId, focusedSlideId = slideId, viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(
            route = "blueprint/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val noteId = backStackEntry.requirePositiveLongArg("noteId")
            if (noteId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            ReviewBlueprintScreen(noteId = noteId, viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(
            route = "book_notes/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.requirePositiveLongArg("bookId")
            if (bookId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            BookNotesScreen(bookId = bookId, viewModel = viewModel, onBack = { navController.popBackStack() })
        }

        composable(
            route = "prompt_deck/{promptId}?cardId={cardId}&runId={runId}",
            arguments = listOf(
                navArgument("promptId") { type = NavType.LongType },
                navArgument("cardId") { type = NavType.LongType; defaultValue = -1L },
                navArgument("runId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val promptId = backStackEntry.requirePositiveLongArg("promptId")
            if (promptId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val cardIdInput = backStackEntry.arguments?.getLong("cardId") ?: -1L
            val cardId = if (cardIdInput == -1L) null else cardIdInput
            val runIdInput = backStackEntry.arguments?.getLong("runId") ?: -1L
            val runId = if (runIdInput == -1L) null else runIdInput

            val viewModel: BookToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return BookToolsViewModel(appModule.repository) as T
                    }
                }
            )
            AiPromptDeckScreen(promptId = promptId, focusedCardId = cardId, focusedRunId = runId, viewModel = viewModel, onBack = { navController.popBackStack() })
        }


        composable("global_search") {
            val searchViewModel: GlobalSearchViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return GlobalSearchViewModel(appModule.globalSearchRepository) as T
                    }
                }
            )
            GlobalSearchScreen(
                viewModel = searchViewModel,
                onBack = { navController.popBackStack() },
                onOpenRoute = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = "review_dashboard?mistakeId={mistakeId}",
            arguments = listOf(navArgument("mistakeId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val mistakeIdInput = backStackEntry.arguments?.getLong("mistakeId") ?: -1L
            val mistakeId = if (mistakeIdInput == -1L) null else mistakeIdInput
            val reviewViewModel: ReviewDashboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ReviewDashboardViewModel(appModule.reviewRepository) as T
                    }
                }
            )
            ReviewDashboardScreen(
                viewModel = reviewViewModel,
                focusedMistakeId = mistakeId,
                onBack = { navController.popBackStack() },
                onOpenRoute = { route -> navController.navigate(route) }
            )
        }

        composable("data_tools") {
            val dataToolsViewModel: DataToolsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return DataToolsViewModel(appModule.fullImportExportService) as T
                    }
                }
            )
            DataToolsScreen(
                viewModel = dataToolsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                appModule = appModule,
                onBack = { returnToLibraryRoot() },
                onGlobalSearch = { navController.navigate("global_search") },
                onReviewDashboard = { navController.navigate("review_dashboard") },
                onDataTools = { navController.navigate("data_tools") }
            )
        }

        composable(
            route = "category/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.requireNonBlankStringArg("category")
            if (category == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val viewModel: CategoryQuestionsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CategoryQuestionsViewModel(appModule.repository) as T
                    }
                }
            )

            LaunchedEffect(category) {
                viewModel.loadCategory(category)
            }

            CategoryQuestionsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onStartQuiz = { categoryName ->
                    navController.navigate("adaptive/CATEGORY/${Uri.encode(categoryName)}")
                }
            )
        }

        composable(
            route = "quiz/{quizId}?sessionId={sessionId}",
            arguments = listOf(
                navArgument("quizId") { type = NavType.LongType },
                navArgument("sessionId") { 
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.requirePositiveLongArg("quizId")
            if (quizId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val sessionIdInput = backStackEntry.arguments?.getLong("sessionId") ?: -1L
            val sessionId = if (sessionIdInput == -1L) null else sessionIdInput
            
            val quizViewModel: QuizViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return QuizViewModel(appModule.repository, appModule.dataStoreManager, appModule.focusManager) as T
                    }
                }
            )
            
            LaunchedEffect(quizId, sessionId) {
                quizViewModel.startQuiz(quizId, sessionId)
            }
            
            QuizPlayerScreen(
                viewModel = quizViewModel,
                onQuizFinished = { sId, _, _ ->
                    navController.navigate("summary/$sId") {
                        popUpTo("library") { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() },
                onViewCategoryQuestions = { category ->
                    navController.navigate("category/${Uri.encode(category)}")
                }
            )
        }

        composable(
            route = "sessions/{quizId}",
            arguments = listOf(navArgument("quizId") { type = NavType.LongType })
        ) { backStackEntry ->
            val quizId = backStackEntry.requirePositiveLongArg("quizId")
            if (quizId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val sessionViewModel: SessionViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SessionViewModel(appModule.repository) as T
                    }
                }
            )
            
            SessionManagementScreen(
                quizId = quizId,
                viewModel = sessionViewModel,
                dataStoreManager = appModule.dataStoreManager,
                onSessionSelected = { sessionId, isCompleted ->
                    if (isCompleted) {
                        navController.navigate("summary/$sessionId")
                    } else {
                        navController.navigate("quiz/$quizId?sessionId=$sessionId")
                    }
                },
                onNavigateBack = {
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
            arguments = listOf(navArgument("quizId") { type = NavType.LongType })
        ) { backStackEntry ->
            val quizId = backStackEntry.requirePositiveLongArg("quizId")
            if (quizId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val scannerViewModel: ScannerViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ScannerViewModel(appModule.repository) as T
                    }
                }
            )
            
            ScannerScreen(
                quizId = quizId,
                viewModel = scannerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "adaptive/{type}/{id}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val type = backStackEntry.requireNonBlankStringArg("type")
            val id = backStackEntry.requireNonBlankStringArg("id")
            if (type == null || id == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val quizViewModel: QuizViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return QuizViewModel(appModule.repository, appModule.dataStoreManager, appModule.focusManager) as T
                    }
                }
            )
            
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
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "summary/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.requirePositiveLongArg("sessionId")
            if (sessionId == null) {
                InvalidRouteScreen(onBack = { navController.popBackStack() })
                return@composable
            }
            val summaryViewModel: SummaryViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SummaryViewModel(appModule.repository) as T
                    }
                }
            )

            SummaryScreen(
                viewModel = summaryViewModel,
                sessionId = sessionId,
                onHomeClick = {
                    navController.navigate("library") {
                        popUpTo("library") { inclusive = true }
                    }
                },
                onReviewClick = { quizId, _ ->
                    navController.navigate("sessions/$quizId")
                },
                onRetryClick = { navController.popBackStack() }
            )
        }
    }
}
