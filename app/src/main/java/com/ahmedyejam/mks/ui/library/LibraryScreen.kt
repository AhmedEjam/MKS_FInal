package com.ahmedyejam.mks.ui.library

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ahmedyejam.mks.R
import com.ahmedyejam.mks.data.import.model.ImportResult
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity
import com.ahmedyejam.mks.data.local.entity.PromptDeckEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.data.model.CategoryWithMetadata
import com.ahmedyejam.mks.ui.category.CategoryBrowserDialog
import com.ahmedyejam.mks.ui.category.CategoryEditDialog
import com.ahmedyejam.mks.ui.library.components.*
import com.ahmedyejam.mks.ui.quiz.CompilerViewModel
import kotlinx.coroutines.launch

import com.ahmedyejam.mks.ui.import.ImportViewModel
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.GridCells
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    compilerViewModel: CompilerViewModel,
    importViewModel: ImportViewModel,
    sharedUris: List<Uri>? = null,
    onConsumedSharedUris: () -> Unit = {},
    onQuizSelected: (Long) -> Unit,
    onScanSelected: (Long) -> Unit,
    onAdaptiveSelected: (String, String) -> Unit,
    onBrowseQuestions: (Long) -> Unit,
    onCategorySelected: (String) -> Unit,
    onSettingsSelected: () -> Unit,
    onFlashcardDeckSelected: (Long) -> Unit = {},
    onSlideshowSelected: (Long) -> Unit = {},
    onReviewBlueprintSelected: (Long) -> Unit = {},
    onBookSlideshowSelected: (Long) -> Unit = {},
    onBookReviewBlueprintSelected: (Long) -> Unit = {},
    onBookSourcesSelected: (Long) -> Unit = {},
    onBookNotesSelected: (Long) -> Unit = {},
    onBookAiPromptDeckSelected: (Long) -> Unit = {},
    onAiPromptDeckSelected: (Long) -> Unit = {},
    onBookDashboardSelected: (Long) -> Unit = {},
    returnResetSignal: Int = 0,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val dataStoreManager = remember(context) { DataStoreManager(context) }
    val currentThemeMode by dataStoreManager.themeMode.collectAsState(initial = "DAWN")

    val importState by importViewModel.importState.collectAsState()

    val showCompilerDialogState = remember { mutableStateOf(value = false) }
    var showCompilerDialog by showCompilerDialogState
    var pendingImportBookId by remember { mutableStateOf<Long?>(null) }
    var pendingImportQuizId by remember { mutableStateOf<Long?>(null) }
    val completedImportResultState = remember { mutableStateOf<ImportResult?>(null) }
    var completedImportResult by completedImportResultState

    fun handleImportUri(uri: Uri, targetQuizId: Long? = null) {
        val format = importViewModel.detectFormat(uri)
        if ((format == com.ahmedyejam.mks.data.import.model.ImportFormat.XLSX) ||
            (format == com.ahmedyejam.mks.data.import.model.ImportFormat.CSV_TSV)) {
            compilerViewModel.onFileSelected(uri, targetQuizId)
            showCompilerDialog = true
        } else if (format == com.ahmedyejam.mks.data.import.model.ImportFormat.ZIP) {
            importViewModel.getImportPreview(
                uri = uri,
                targetBookId = pendingImportBookId,
                targetQuizId = targetQuizId,
            )
        }
    }

    LaunchedEffect(sharedUris) {
        sharedUris?.firstOrNull()?.let { uri ->
            handleImportUri(uri)
            onConsumedSharedUris()
        }
    }

    LaunchedEffect(returnResetSignal) {
        if (returnResetSignal > 0) {
            viewModel.resetToLibraryRoot()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LibraryUiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val books by viewModel.books.collectAsState(initial = emptyList())
    val quizzes by viewModel.quizzes.collectAsState(initial = emptyList())
    val workspaceId by viewModel.currentWorkspaceId.collectAsState()
    val flashcardDecks by viewModel.flashcardDecks.collectAsState(initial = emptyList())
    val slideshowCourses by viewModel.slideshowCourses.collectAsState(initial = emptyList())
    val noteBlueprints by viewModel.noteBlueprints.collectAsState(initial = emptyList())
    val promptDecks by viewModel.promptDecks.collectAsState(initial = emptyList())
    val recentQuizzes by viewModel.recentQuizzes.collectAsState(initial = emptyList())
    val resumeQuiz by viewModel.resumeQuiz.collectAsState(initial = null)
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val currentBook by viewModel.currentBook.collectAsState(initial = null)
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val selectedBookId by viewModel.selectedBookId.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val libraryViewMode by viewModel.libraryViewMode.collectAsState()
    val bookViewMode by viewModel.bookViewMode.collectAsState()

    val currentViewMode by remember {
        derivedStateOf {
            if ((selectedBookId == -1L) && (selectedCategory == null)) libraryViewMode else bookViewMode
        }
    }

    val showCovers by viewModel.showCovers.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val bookSortBy by viewModel.bookSortBy.collectAsState()

    val showSortDialogState = remember { mutableStateOf(value = false) }
    var showSortDialog by showSortDialogState
    val showQuizSelectionDialogState = remember { mutableStateOf(value = false) }
    var showQuizSelectionDialog by showQuizSelectionDialogState
    val showBookEditDialogState = rememberSaveable { mutableStateOf(value = false) }
    var showBookEditDialog by showBookEditDialogState
    val showQuizEditDialogState = rememberSaveable { mutableStateOf(value = false) }
    var showQuizEditDialog by showQuizEditDialogState
    val showFlashcardDeckDialogState = rememberSaveable { mutableStateOf(value = false) }
    var showFlashcardDeckDialog by showFlashcardDeckDialogState
    val showPromptEditDialogState = rememberSaveable { mutableStateOf(value = false) }
    var showPromptEditDialog by showPromptEditDialogState
    val editingBookState = remember { mutableStateOf<BookEntity?>(null) }
    var editingBook by editingBookState
    val editingQuizState = remember { mutableStateOf<QuizEntity?>(null) }
    var editingQuiz by editingQuizState
    val showDeleteConfirmBookState = remember { mutableStateOf<BookEntity?>(null) }
    var showDeleteConfirmBook by showDeleteConfirmBookState
    val showDeleteConfirmQuizState = remember { mutableStateOf<QuizEntity?>(null) }
    var showDeleteConfirmQuiz by showDeleteConfirmQuizState

    val editingCategoryState = remember { mutableStateOf<CategoryWithMetadata?>(null) }
    var editingCategory by editingCategoryState

    val menuBookState = remember { mutableStateOf<BookEntity?>(null) }
    var menuBook by menuBookState
    val menuQuizState = remember { mutableStateOf<QuizEntity?>(null) }
    var menuQuiz by menuQuizState
    val menuFlashcardDeckState = remember { mutableStateOf<com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity?>(null) }
    var menuFlashcardDeck by menuFlashcardDeckState

    val editingFlashcardDeckState = remember { mutableStateOf<com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity?>(null) }
    var editingFlashcardDeck by editingFlashcardDeckState
    val showDeleteConfirmFlashcardDeckState = remember { mutableStateOf<com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity?>(null) }
    var showDeleteConfirmFlashcardDeck by showDeleteConfirmFlashcardDeckState

    val menuSlideshowState = remember { mutableStateOf<SlideshowCourseEntity?>(null) }
    var menuSlideshow by menuSlideshowState
    val showDeleteConfirmSlideshowState = remember { mutableStateOf<SlideshowCourseEntity?>(null) }
    var showDeleteConfirmSlideshow by showDeleteConfirmSlideshowState

    val menuNoteState = remember { mutableStateOf<NoteBlueprintEntity?>(null) }
    var menuNote by menuNoteState
    val showDeleteConfirmNoteState = remember { mutableStateOf<NoteBlueprintEntity?>(null) }
    var showDeleteConfirmNote by showDeleteConfirmNoteState

    val menuPromptDeckState = remember { mutableStateOf<PromptDeckEntity?>(null) }
    var menuPromptDeck by menuPromptDeckState
    val showDeleteConfirmPromptDeckState = remember { mutableStateOf<PromptDeckEntity?>(null) }
    var showDeleteConfirmPromptDeck by showDeleteConfirmPromptDeckState

    var pendingExportBookId by remember { mutableStateOf<Long?>(null) }
    var pendingExportQuizId by remember { mutableStateOf<Long?>(null) }

    val categoriesExpandedState = rememberSaveable { mutableStateOf(value = false) }
    var categoriesExpanded by categoriesExpandedState
    val showCategoryBrowserState = remember { mutableStateOf(value = false) }
    var showCategoryBrowser by showCategoryBrowserState
    val previewCategories by remember {
        derivedStateOf {
            val pinned = categories.asSequence()
                .filter { it.isPinned }
                .sortedByDescending { it.lastEditedAt }
                .toList()
            val recent = categories.asSequence()
                .filter { !it.isPinned && (it.lastEditedAt > 0) }
                .sortedByDescending { it.lastEditedAt }
                .take(8)
                .toList()
            pinned + recent
        }
    }
    val showOverflowMenuState = remember { mutableStateOf(value = false) }
    var showOverflowMenu by showOverflowMenuState

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let {
            handleImportUri(it, pendingImportQuizId)
        }
    }

    // Handle Import States
    when (val state = importState) {
        is ImportViewModel.ImportState.Review -> {
            ImportReviewDialog(
                preview = state.preview,
                onDismiss = {
                    importViewModel.resetState()
                    pendingImportBookId = null
                    pendingImportQuizId = null
                },
            ) { strategy, allowInsecureHttpImages ->
                importViewModel.importLibrary(
                    uri = state.uri,
                    strategy = strategy,
                    targetBookId = state.targetBookId,
                    targetQuizId = state.targetQuizId,
                    allowInsecureRemoteImages = allowInsecureHttpImages,
                )
            }
        }
        is ImportViewModel.ImportState.Loading -> {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                title = { Text(state.message) },
                text = {
                    LinearProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
            )
        }
        is ImportViewModel.ImportState.Success -> {
            LaunchedEffect(state) {
                val message = if (state.result.warnings.isNotEmpty()) {
                    context.getString(
                        R.string.import_completed_with_warning_count,
                        state.result.warnings.size,
                    )
                } else if (state.result.partiallyImported) {
                    context.getString(
                        R.string.import_completed_with_skipped_count,
                        state.result.skippedRecordsCount,
                    )
                } else {
                    context.getString(R.string.import_successful)
                }
                snackbarHostState.showSnackbar(message)
                completedImportResultState.value = state.result.takeIf { (it.warnings.isNotEmpty()) || (it.skippedRecordsCount > 0) }
                importViewModel.resetState()
            }
        }
        is ImportViewModel.ImportState.Error -> {
            LaunchedEffect(state) {
                snackbarHostState.showSnackbar(context.getString(R.string.import_error, state.message))
                importViewModel.resetState()
            }
        }
        else -> {}
    }

    completedImportResult?.let { result ->
        ImportWarningsDialog(
            result = result,
        ) { completedImportResultState.value = null }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip"),
    ) { uri ->
        uri?.let { exportUri ->
            scope.launch {
                try {
                    val result = context.contentResolver.openOutputStream(exportUri)?.use { stream ->
                        pendingExportBookId?.let { bookId ->
                            viewModel.exportBook(bookId, stream)
                        } ?: pendingExportQuizId?.let { quizId ->
                            viewModel.exportQuiz(quizId, stream)
                        } ?: viewModel.exportAll(stream)
                    }
                    if (result?.success == true) {
                        val message = if (result.failedAssetCount > 0) {
                            context.getString(
                                R.string.export_completed_with_warnings,
                                result.failedAssetCount,
                            )
                        } else {
                            context.getString(R.string.export_successful)
                        }
                        snackbarHostState.showSnackbar(message)
                    } else {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.export_failed,
                                result?.errorMessage ?: context.getString(R.string.unknown_error),
                            ),
                        )
                    }
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar(context.getString(R.string.export_failed, e.message))
                } finally {
                    pendingExportBookId = null
                    pendingExportQuizId = null
                }
            }
        }
    }

    val fabExpandedState = remember { mutableStateOf(value = false) }
    var fabExpanded by fabExpandedState

    BackHandler(enabled = fabExpanded || (selectedBookId != -1L) || (selectedCategory != null) || isSearching) {
        if (fabExpanded) {
            fabExpandedState.value = false
        } else if (isSearching) {
            viewModel.setSearching(searching = false)
        } else {
            viewModel.resetToLibraryRoot()
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val gridState = rememberLazyGridState()

    LaunchedEffect(selectedBookId, selectedCategory, isSearching) {
        gridState.scrollToItem(0)
    }

    LaunchedEffect(selectedBookId, books, currentBook) {
        if ((selectedBookId != -1L) && (books.isNotEmpty()) && (currentBook == null)) {
            viewModel.resetToLibraryRoot()
        }
    }

    val columns by remember {
        derivedStateOf {
            if (currentViewMode == "GRID") GridCells.Fixed(2) else GridCells.Fixed(1)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LibraryTopBar(
                scrollBehavior = scrollBehavior,
                isSearching = isSearching,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                selectedBookId = selectedBookId,
                selectedCategory = selectedCategory,
                currentBookTitle = currentBook?.title,
                onNavigationClick = {
                    if (isSearching) viewModel.setSearching(searching = false)
                    else viewModel.resetToLibraryRoot()
                },
                onSearchClick = { viewModel.setSearching(searching = true) },
                onSettingsClick = onSettingsSelected,
                onSortClick = { showSortDialog = true },
                currentViewMode = currentViewMode,
                onToggleViewMode = {
                    if ((selectedBookId == -1L) && (selectedCategory == null)) {
                        viewModel.toggleLibraryViewMode()
                    } else {
                        viewModel.toggleBookViewMode()
                    }
                },
                showOverflowMenu = showOverflowMenu,
                onOverflowMenuToggle = { showOverflowMenuState.value = it },
                onContactClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://linktr.ee/MKSpace".toUri(),
                    )
                    context.startActivity(intent)
                }
            )
        },
        floatingActionButton = {
            LibraryFabMenu(
                fabExpanded = fabExpanded,
                onFabExpandedChange = { fabExpandedState.value = it },
                selectedBookId = selectedBookId,
                selectedCategory = selectedCategory,
                onAdaptiveSelected = onAdaptiveSelected,
                onFlashcardDeckDialogShow = { showFlashcardDeckDialogState.value = true },
                onBookSlideshowSelected = onBookSlideshowSelected,
                onBookReviewBlueprintSelected = onBookReviewBlueprintSelected,
                onBookSourcesSelected = onBookSourcesSelected,
                onBookNotesSelected = onBookNotesSelected,
                onPromptEditDialogShow = { onBookAiPromptDeckSelected(selectedBookId) },
                onQuizSelectionDialogShow = { showQuizSelectionDialogState.value = true },
                onNewBookClick = {
                    editingBookState.value = null
                    showBookEditDialogState.value = true
                },
                onImportClick = {
                    pendingImportBookId = if (selectedBookId != -1L) selectedBookId else null
                    pendingImportQuizId = null
                    importLauncher.launch(
                        arrayOf(
                            "application/zip",
                            "application/x-zip-compressed",
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                            "application/vnd.ms-excel",
                            "text/csv",
                            "text/comma-separated-values",
                            "text/tab-separated-values",
                        ),
                    )
                },
            ) {
                val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
                if (selectedBookId != -1L) {
                    pendingExportBookId = selectedBookId
                    exportLauncher.launch("${currentBook?.title?.replace(" ", "_")}_$timestamp.mks.zip")
                } else {
                    pendingExportBookId = null
                    exportLauncher.launch("mks_full_library_$timestamp.zip")
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LibraryContentGrid(
                gridState = gridState,
                columns = columns,
                padding = padding,
                selectedBookId = selectedBookId,
                selectedCategory = selectedCategory,
                resumeQuiz = resumeQuiz,
                recentQuizzes = recentQuizzes,
                currentThemeMode = currentThemeMode,
                onQuizClick = { onQuizSelected(it) },
                onLinkClick = { url ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        url.toUri(),
                    )
                    context.startActivity(intent)
                },
                categoriesExpanded = categoriesExpanded,
                previewCategories = previewCategories,
                onCategorySelected = onCategorySelected,
                onCategoryLongClick = { editingCategory = it },
                showCategoryBrowser = { showCategoryBrowser = true },
                books = books,
                quizzes = quizzes,
                flashcardDecks = flashcardDecks,
                slideshowCourses = slideshowCourses,
                noteBlueprints = noteBlueprints,
                promptDecks = promptDecks,
                currentViewMode = currentViewMode,
                showCovers = showCovers,
                onBookClick = { viewModel.selectBook(it.id) },
                onBookLongClick = { menuBook = it },
                onQuizLongClick = { menuQuiz = it },
                onFlashcardDeckSelected = onFlashcardDeckSelected,
                onFlashcardDeckLongClick = { menuFlashcardDeck = it },
                onSlideshowSelected = onSlideshowSelected,
                onSlideshowLongClick = { menuSlideshow = it },
                onReviewBlueprintSelected = onReviewBlueprintSelected,
                onReviewBlueprintLongClick = { menuNote = it },
                onAiPromptDeckSelected = onAiPromptDeckSelected,
                onAiPromptDeckLongClick = { menuPromptDeck = it },
            )

            AnimatedVisibility(
                visible = fabExpanded,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.Black.copy(alpha = 0.32f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            fabExpandedState.value = false
                        },
                )
            }
        }
    }

    // Dialogs management
    menuBook?.let { book ->
        BookOptionsSheet(
            book = book,
            onDismiss = { menuBookState.value = null },
            onPinClick = { viewModel.toggleBookPinned(book) },
            onEditClick = { editingBookState.value = book; showBookEditDialogState.value = true },
            onExportClick = {
                pendingExportBookId = book.id
                pendingExportQuizId = null
                exportLauncher.launch("${book.title.replace(" ", "_")}.mks.zip")
            },
            onImportClick = {
                pendingImportBookId = book.id
                pendingImportQuizId = null
                importLauncher.launch(arrayOf("*/*"))
            },
            onDeleteClick = { showDeleteConfirmBookState.value = book },
        ) { onBookDashboardSelected(book.id) }
    }

    menuQuiz?.let { quiz ->
        QuizOptionsSheet(
            quiz = quiz,
            onDismiss = { menuQuizState.value = null },
            onBrowseQuestionsClick = { onBrowseQuestions(quiz.id) },
            onScanClick = { onScanSelected(quiz.id) },
            onPinClick = { viewModel.toggleQuizPinned(quiz) },
            onEditClick = { editingQuizState.value = quiz; showQuizEditDialogState.value = true },
            onExportClick = {
                pendingExportQuizId = quiz.id
                pendingExportBookId = null
                exportLauncher.launch("${quiz.title.replace(" ", "_")}.mks.zip")
            },
            onImportClick = {
                pendingImportQuizId = quiz.id
                pendingImportBookId = null
                importLauncher.launch(arrayOf("*/*"))
            },
        ) { showDeleteConfirmQuizState.value = quiz }
    }

    menuFlashcardDeck?.let { deck ->
        QuizOptionsSheet(
            quiz = QuizEntity(
                id = deck.id,
                externalId = deck.externalId,
                bookId = deck.bookId,
                title = deck.title,
                description = deck.description ?: "",
                isPinned = deck.isPinned
            ),
            onDismiss = { menuFlashcardDeckState.value = null },
            onBrowseQuestionsClick = { onFlashcardDeckSelected(deck.id) },
            onScanClick = null,
            onPinClick = { viewModel.toggleFlashcardDeckPinned(deck) },
            onEditClick = { editingFlashcardDeckState.value = deck; showFlashcardDeckDialogState.value = true },
            onExportClick = null,
            onImportClick = null,
        ) { showDeleteConfirmFlashcardDeckState.value = deck }
    }

    menuSlideshow?.let { course ->
        QuizOptionsSheet(
            quiz = QuizEntity(
                id = course.id,
                externalId = course.externalId,
                bookId = course.bookId,
                title = course.title,
                description = course.description ?: "",
                isPinned = course.isPinned
            ),
            onDismiss = { menuSlideshowState.value = null },
            onBrowseQuestionsClick = { onSlideshowSelected(course.id) },
            onScanClick = null,
            onPinClick = { viewModel.toggleSlideshowCoursePinned(course) },
            onEditClick = null,
            onExportClick = null,
            onImportClick = null,
        ) { showDeleteConfirmSlideshowState.value = course }
    }

    menuNote?.let { note ->
        QuizOptionsSheet(
            quiz = QuizEntity(
                id = note.id,
                externalId = note.externalId,
                bookId = note.bookId,
                title = note.title,
                description = note.summary ?: "",
                isPinned = false
            ),
            onDismiss = { menuNoteState.value = null },
            onBrowseQuestionsClick = { onReviewBlueprintSelected(note.id) },
            onScanClick = null,
            onPinClick = null,
            onEditClick = null,
            onExportClick = null,
            onImportClick = null,
        ) { showDeleteConfirmNoteState.value = note }
    }

    menuPromptDeck?.let { deck ->
        QuizOptionsSheet(
            quiz = QuizEntity(
                id = deck.id,
                externalId = "",
                bookId = deck.bookId,
                title = deck.title,
                description = deck.description ?: "",
                isPinned = false
            ),
            onDismiss = { menuPromptDeckState.value = null },
            onBrowseQuestionsClick = { onAiPromptDeckSelected(deck.id) },
            onScanClick = null,
            onPinClick = null,
            onEditClick = null,
            onExportClick = null,
            onImportClick = null,
        ) { showDeleteConfirmPromptDeckState.value = deck }
    }

    if (showCategoryBrowser) {
        CategoryBrowserDialog(
            categories = categories,
            onDismiss = { showCategoryBrowserState.value = false },
            onCategorySelected = {
                onCategorySelected(it.name)
                showCategoryBrowserState.value = false
            },
        ) {
            editingCategoryState.value = it
            showCategoryBrowserState.value = false
        }
    }

    editingCategory?.let { category ->
        CategoryEditDialog(
            category = category,
            allCategories = categories,
            books = books,
            onDismiss = { editingCategoryState.value = null },
            onDelete = { viewModel.deleteCategory(category.name); editingCategoryState.value = null },
            onRename = { viewModel.renameCategory(category.name, it); editingCategoryState.value = null },
            onMerge = { viewModel.mergeCategory(category.name, it); editingCategoryState.value = null },
            onGetMergePreview = { viewModel.getMergePreview(category.name, it) },
            onCreateQuiz = { title, bId -> viewModel.createQuizFromCategory(category.name, title, bId); editingCategoryState.value = null },
            onTogglePin = { viewModel.updateCategoryMetadata(category.name, category.emoji, category.color, !category.isPinned) },
            onUpdateEmoji = { viewModel.updateCategoryMetadata(category.name, it, category.color, category.isPinned) },
            onUpdateColor = { viewModel.updateCategoryMetadata(category.name, category.emoji, it, category.isPinned) },
        ) { onCategorySelected(category.name) }
    }

    if (showCompilerDialog) {
        com.ahmedyejam.mks.ui.quiz.CompilerDialog(
            viewModel = compilerViewModel,
            books = books,
            quizzes = quizzes,
        ) {
            showCompilerDialogState.value = false
            pendingImportBookId = null
            pendingImportQuizId = null
        }
    }

    if (showQuizSelectionDialog) {
        CreateQuizDialog(
            quizzes = quizzes,
            categories = categories.map { it.name },
            onDismiss = { showQuizSelectionDialogState.value = false },
        ) { title, description, coverImage, sourceQuizIds, sourceCategories ->
            viewModel.createNewQuiz(selectedBookId, title, description, coverImage, sourceQuizIds, sourceCategories)
            showQuizSelectionDialogState.value = false
        }
    }

    if (showBookEditDialog) {
        EditEntityDialog(
            title = if (editingBook == null) stringResource(R.string.new_book) else stringResource(R.string.edit),
            initialTitle = editingBook?.title ?: "",
            initialDescription = editingBook?.description ?: "",
            initialCoverImage = editingBook?.coverImage,
            titleLabel = stringResource(R.string.book_title_label),
            descriptionLabel = stringResource(R.string.description_label),
            onDismiss = { showBookEditDialogState.value = false },
        ) { title, desc, cover ->
            if (editingBook == null) {
                viewModel.insertBook(
                    BookEntity(
                        workspaceId = workspaceId,
                        externalId = java.util.UUID.randomUUID().toString(),
                        title = title,
                        description = desc,
                    ),
                    coverUri = cover,
                )
            } else {
                editingBook?.let { book ->
                    viewModel.updateBook(
                        book.copy(title = title, description = desc),
                        newCoverUri = cover,
                    )
                }
            }
            showBookEditDialogState.value = false
        }
    }

    if (showQuizEditDialog) {
        EditEntityDialog(
            title = if (editingQuiz == null) stringResource(R.string.new_quiz) else stringResource(R.string.edit),
            initialTitle = editingQuiz?.title ?: "",
            initialDescription = editingQuiz?.description ?: "",
            initialCoverImage = editingQuiz?.coverImage,
            titleLabel = stringResource(R.string.quiz_title_label),
            descriptionLabel = stringResource(R.string.description_label),
            onDismiss = { showQuizEditDialogState.value = false },
        ) { title, desc, cover ->
            if (editingQuiz == null) {
                // Logic to add new quiz if needed
            } else {
                editingQuiz?.let { quiz ->
                    viewModel.updateQuiz(
                        quiz.copy(title = title, description = desc),
                        newCoverUri = cover,
                    )
                }
            }
            showQuizEditDialogState.value = false
        }
    }

    if (showFlashcardDeckDialog && (selectedBookId != -1L || editingFlashcardDeck != null)) {
        EditEntityDialog(
            title = if (editingFlashcardDeck == null) stringResource(R.string.flashcard_deck) else stringResource(R.string.edit),
            initialTitle = editingFlashcardDeck?.title ?: "",
            initialDescription = editingFlashcardDeck?.description ?: "",
            initialCoverImage = editingFlashcardDeck?.coverImage,
            titleLabel = stringResource(R.string.name_label),
            descriptionLabel = stringResource(R.string.description_label),
            allowBlankTitle = true,
            onDismiss = { showFlashcardDeckDialogState.value = false; editingFlashcardDeckState.value = null },
        ) { title, desc, cover ->
            if (editingFlashcardDeck == null) {
                viewModel.createFlashcardDeckFromBook(
                    bookId = selectedBookId,
                    title = title,
                    description = desc,
                    coverUri = cover,
                    onCreated = onFlashcardDeckSelected,
                )
            } else {
                viewModel.updateFlashcardDeck(editingFlashcardDeck!!.copy(title = title, description = desc, coverImage = cover))
            }
            showFlashcardDeckDialogState.value = false
            editingFlashcardDeckState.value = null
        }
    }

    if (showPromptEditDialog && (selectedBookId != -1L)) {
        EditEntityDialog(
            title = stringResource(R.string.new_ai_prompt),
            initialTitle = "",
            initialDescription = "",
            initialCoverImage = null,
            titleLabel = stringResource(R.string.prompt_name_label),
            descriptionLabel = stringResource(R.string.prompt_stem_desc),
            onDismiss = { showPromptEditDialogState.value = false },
        ) { title, stem, _ ->
            viewModel.insertPrompt(selectedBookId, title, stem)
            showPromptEditDialogState.value = false
        }
    }

    showDeleteConfirmBook?.let { book ->
        DeleteConfirmDialog(
            title = stringResource(R.string.delete_book_title),
            message = stringResource(R.string.delete_book_msg, book.title),
            onDismiss = { showDeleteConfirmBookState.value = null },
        ) { viewModel.deleteBook(book); showDeleteConfirmBookState.value = null }
    }

    showDeleteConfirmQuiz?.let { quiz ->
        DeleteConfirmDialog(
            title = stringResource(R.string.delete_quiz_title),
            message = stringResource(R.string.delete_quiz_msg, quiz.title),
            onDismiss = { showDeleteConfirmQuizState.value = null },
        ) { viewModel.deleteQuiz(quiz); showDeleteConfirmQuizState.value = null }
    }

    showDeleteConfirmFlashcardDeck?.let { deck ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmFlashcardDeckState.value = null },
            title = { Text("Delete Flashcard Deck") },
            text = { Text("Are you sure you want to delete '${deck.title}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteFlashcardDeck(deck)
                    showDeleteConfirmFlashcardDeckState.value = null
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmFlashcardDeckState.value = null }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    showDeleteConfirmSlideshow?.let { course ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmSlideshowState.value = null },
            title = { Text("Delete slideshow?") },
            text = { Text("Are you sure you want to delete '${course.title}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteSlideshowCourse(course)
                    showDeleteConfirmSlideshowState.value = null
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmSlideshowState.value = null }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    showDeleteConfirmNote?.let { note ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmNoteState.value = null },
            title = { Text("Delete article?") },
            text = { Text("Are you sure you want to delete '${note.title}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNoteBlueprint(note)
                    showDeleteConfirmNoteState.value = null
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmNoteState.value = null }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    showDeleteConfirmPromptDeck?.let { deck ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmPromptDeckState.value = null },
            title = { Text("Delete prompt deck?") },
            text = { Text("Are you sure you want to delete '${deck.title}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePromptDeck(deck)
                    showDeleteConfirmPromptDeckState.value = null
                }) { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmPromptDeckState.value = null }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }

    if (showSortDialog) {
        val isQuizzes = (selectedBookId != -1L) || (selectedCategory != null)
        SortDialog(
            currentOption = if (isQuizzes) bookSortBy else sortBy,
            onOptionSelected = {
                if (isQuizzes) {
                    viewModel.setBookSortBy(it)
                } else {
                    viewModel.setSortBy(it)
                }
                showSortDialogState.value = false
            },
        ) { showSortDialogState.value = false }
    }
}
