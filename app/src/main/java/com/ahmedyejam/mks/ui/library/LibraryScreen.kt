package com.ahmedyejam.mks.ui.library

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
import com.ahmedyejam.mks.data.local.entity.QuizEntity
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
    returnResetSignal: Int = 0
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val dataStoreManager = remember(context) { DataStoreManager(context) }
    val currentThemeMode by dataStoreManager.themeMode.collectAsState(initial = "DAWN")

    val importState by importViewModel.importState.collectAsState()

    var showCompilerDialog by remember { mutableStateOf(false) }
    var pendingImportBookId by remember { mutableStateOf<Long?>(null) }
    var pendingImportQuizId by remember { mutableStateOf<Long?>(null) }
    var completedImportResult by remember { mutableStateOf<ImportResult?>(null) }

    fun handleImportUri(uri: Uri, targetQuizId: Long? = null) {
        val format = importViewModel.detectFormat(uri)
        if (format == com.ahmedyejam.mks.data.import.model.ImportFormat.XLSX ||
            format == com.ahmedyejam.mks.data.import.model.ImportFormat.CSV_TSV) {
            compilerViewModel.onFileSelected(uri, targetQuizId)
            showCompilerDialog = true
        } else if (format == com.ahmedyejam.mks.data.import.model.ImportFormat.ZIP) {
            importViewModel.getImportPreview(
                uri = uri,
                targetBookId = pendingImportBookId,
                targetQuizId = targetQuizId
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
    val prompts by viewModel.prompts.collectAsState(initial = emptyList())
    val promptDecks by viewModel.promptDecks.collectAsState(initial = emptyList())
    val knowledgeSummary by viewModel.knowledgeSummary.collectAsState(initial = null)
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
            if (selectedBookId == -1L && selectedCategory == null) libraryViewMode else bookViewMode
        }
    }

    val showCovers by viewModel.showCovers.collectAsState()
    val autoHideKnowledgeSummary by viewModel.autoHideKnowledgeSummary.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val bookSortBy by viewModel.bookSortBy.collectAsState()

    var showSortDialog by remember { mutableStateOf(false) }
    var showQuizSelectionDialog by remember { mutableStateOf(false) }
    var showBookEditDialog by rememberSaveable { mutableStateOf(false) }
    var showQuizEditDialog by rememberSaveable { mutableStateOf(false) }
    var showFlashcardDeckDialog by rememberSaveable { mutableStateOf(false) }
    var showPromptEditDialog by rememberSaveable { mutableStateOf(false) }
    var editingBook by remember { mutableStateOf<BookEntity?>(null) }
    var editingQuiz by remember { mutableStateOf<QuizEntity?>(null) }
    var showDeleteConfirmBook by remember { mutableStateOf<BookEntity?>(null) }
    var showDeleteConfirmQuiz by remember { mutableStateOf<QuizEntity?>(null) }

    var editingCategory by remember { mutableStateOf<CategoryWithMetadata?>(null) }

    var menuBook by remember { mutableStateOf<BookEntity?>(null) }
    var menuQuiz by remember { mutableStateOf<QuizEntity?>(null) }

    var pendingExportBookId by remember { mutableStateOf<Long?>(null) }
    var pendingExportQuizId by remember { mutableStateOf<Long?>(null) }

    var categoriesExpanded by rememberSaveable { mutableStateOf(false) }
    var showCategoryBrowser by remember { mutableStateOf(false) }
    val previewCategories by remember {
        derivedStateOf {
            val pinned = categories.filter { it.isPinned }
                .sortedByDescending { it.lastEditedAt }
            val recent = categories.filter { !it.isPinned && it.lastEditedAt > 0 }
                .sortedByDescending { it.lastEditedAt }
                .take(8)
            pinned + recent
        }
    }
    var showOverflowMenu by remember { mutableStateOf(false) }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
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
                onConfirm = { strategy, allowInsecureHttpImages ->
                    importViewModel.importLibrary(
                        uri = state.uri,
                        strategy = strategy,
                        targetBookId = state.targetBookId,
                        targetQuizId = state.targetQuizId,
                        allowInsecureRemoteImages = allowInsecureHttpImages
                    )
                }
            )
        }
        is ImportViewModel.ImportState.Loading -> {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                title = { Text(state.message) },
                text = {
                    LinearProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
        is ImportViewModel.ImportState.Success -> {
            LaunchedEffect(state) {
                val message = if (state.result.warnings.isNotEmpty()) {
                    context.getString(
                        R.string.import_completed_with_warning_count,
                        state.result.warnings.size
                    )
                } else if (state.result.partiallyImported) {
                    context.getString(
                        R.string.import_completed_with_skipped_count,
                        state.result.skippedRecordsCount
                    )
                } else {
                    context.getString(R.string.import_successful)
                }
                snackbarHostState.showSnackbar(message)
                completedImportResult = state.result.takeIf { it.warnings.isNotEmpty() || it.skippedRecordsCount > 0 }
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
            onDismiss = { completedImportResult = null }
        )
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
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
                                result.failedAssetCount
                            )
                        } else {
                            context.getString(R.string.export_successful)
                        }
                        snackbarHostState.showSnackbar(message)
                    } else {
                        snackbarHostState.showSnackbar(
                            context.getString(
                                R.string.export_failed,
                                result?.errorMessage ?: context.getString(R.string.unknown_error)
                            )
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

    var fabExpanded by remember { mutableStateOf(false) }

    BackHandler(enabled = fabExpanded || selectedBookId != -1L || selectedCategory != null || isSearching) {
        if (fabExpanded) {
            fabExpanded = false
        } else if (isSearching) {
            viewModel.setSearching(false)
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
        if (selectedBookId != -1L && books.isNotEmpty() && currentBook == null) {
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
                    if (isSearching) viewModel.setSearching(false)
                    else viewModel.resetToLibraryRoot()
                },
                onSearchClick = { viewModel.setSearching(true) },
                onSettingsClick = onSettingsSelected,
                onSortClick = { showSortDialog = true },
                currentViewMode = currentViewMode,
                onToggleViewMode = {
                    if (selectedBookId == -1L && selectedCategory == null) {
                        viewModel.toggleLibraryViewMode()
                    } else {
                        viewModel.toggleBookViewMode()
                    }
                },
                showOverflowMenu = showOverflowMenu,
                onOverflowMenuToggle = { showOverflowMenu = it }
            )
        },
        floatingActionButton = {
            LibraryFabMenu(
                fabExpanded = fabExpanded,
                onFabExpandedChange = { fabExpanded = it },
                selectedBookId = selectedBookId,
                selectedCategory = selectedCategory,
                onAdaptiveSelected = onAdaptiveSelected,
                onFlashcardDeckDialogShow = { showFlashcardDeckDialog = true },
                onBookSlideshowSelected = onBookSlideshowSelected,
                onBookReviewBlueprintSelected = onBookReviewBlueprintSelected,
                onBookSourcesSelected = onBookSourcesSelected,
                onBookNotesSelected = onBookNotesSelected,
                onPromptEditDialogShow = { onBookAiPromptDeckSelected(selectedBookId) },
                onQuizSelectionDialogShow = { showQuizSelectionDialog = true },
                onNewBookClick = {
                    editingBook = null
                    showBookEditDialog = true
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
                            "text/tab-separated-values"
                        )
                    )
                },
                onExportClick = {
                    val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
                    if (selectedBookId != -1L) {
                        pendingExportBookId = selectedBookId
                        exportLauncher.launch("${currentBook?.title?.replace(" ", "_")}_$timestamp.mks.zip")
                    } else {
                        pendingExportBookId = null
                        exportLauncher.launch("mks_full_library_$timestamp.zip")
                    }
                }
            )
        }
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
                    val intent = android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse(url)
                    )
                    context.startActivity(intent)
                },
                categories = categories,
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
                prompts = prompts,
                promptDecks = promptDecks,
                knowledgeSummary = knowledgeSummary,
                autoHideKnowledgeSummary = autoHideKnowledgeSummary,
                currentViewMode = currentViewMode,
                showCovers = showCovers,
                onBookClick = { viewModel.selectBook(it.id) },
                onBookLongClick = { menuBook = it },
                onQuizLongClick = { menuQuiz = it },
                onFlashcardDeckSelected = onFlashcardDeckSelected,
                onSlideshowSelected = onSlideshowSelected,
                onReviewBlueprintSelected = onReviewBlueprintSelected,
                onAiPromptDeckSelected = onAiPromptDeckSelected
            )

            AnimatedVisibility(
                visible = fabExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color.Black.copy(alpha = 0.32f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            fabExpanded = false
                        }
                )
            }
        }
    }

    // Dialogs management
    menuBook?.let { book ->
        BookOptionsSheet(
            book = book,
            onDismiss = { menuBook = null },
            onPinClick = { viewModel.toggleBookPinned(book) },
            onEditClick = { editingBook = book; showBookEditDialog = true },
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
            onDeleteClick = { showDeleteConfirmBook = book },
            onDashboardClick = { onBookDashboardSelected(book.id) }
        )
    }

    menuQuiz?.let { quiz ->
        QuizOptionsSheet(
            quiz = quiz,
            onDismiss = { menuQuiz = null },
            onBrowseQuestionsClick = { onBrowseQuestions(quiz.id) },
            onScanClick = { onScanSelected(quiz.id) },
            onPinClick = { viewModel.toggleQuizPinned(quiz) },
            onEditClick = { editingQuiz = quiz; showQuizEditDialog = true },
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
            onDeleteClick = { showDeleteConfirmQuiz = quiz }
        )
    }

    if (showCategoryBrowser) {
        CategoryBrowserDialog(
            categories = categories,
            onDismiss = { showCategoryBrowser = false },
            onCategorySelected = {
                onCategorySelected(it.name)
                showCategoryBrowser = false
            },
            onCategoryLongClick = { 
                editingCategory = it
                showCategoryBrowser = false
            }
        )
    }

    editingCategory?.let { category ->
        CategoryEditDialog(
            category = category,
            allCategories = categories,
            books = books,
            onDismiss = { editingCategory = null },
            onDelete = { viewModel.deleteCategory(category.name); editingCategory = null },
            onRename = { viewModel.renameCategory(category.name, it); editingCategory = null },
            onMerge = { viewModel.mergeCategory(category.name, it); editingCategory = null },
            onGetMergePreview = { viewModel.getMergePreview(category.name, it) },
            onCreateQuiz = { title, bookId -> viewModel.createQuizFromCategory(category.name, title, bookId); editingCategory = null },
            onTogglePin = { viewModel.updateCategoryMetadata(category.name, category.emoji, category.color, !category.isPinned) },
            onUpdateEmoji = { viewModel.updateCategoryMetadata(category.name, it, category.color, category.isPinned) },
            onUpdateColor = { viewModel.updateCategoryMetadata(category.name, category.emoji, it, category.isPinned) },
            onViewQuestions = { onCategorySelected(category.name) }
        )
    }

    if (showCompilerDialog) {
        com.ahmedyejam.mks.ui.quiz.CompilerDialog(
            viewModel = compilerViewModel,
            books = books,
            quizzes = quizzes,
            onDismiss = { 
                showCompilerDialog = false
                pendingImportBookId = null
                pendingImportQuizId = null
            }
        )
    }

    if (showQuizSelectionDialog) {
        QuizSelectionDialog(
            quizzes = quizzes,
            onDismiss = { showQuizSelectionDialog = false },
            onConfirm = { selectedQuizIds ->
                viewModel.createCustomQuiz(selectedBookId, selectedQuizIds)
                showQuizSelectionDialog = false
            }
        )
    }

    if (showBookEditDialog) {
        EditEntityDialog(
            title = if (editingBook == null) stringResource(R.string.new_book) else stringResource(R.string.edit),
            initialTitle = editingBook?.title ?: "",
            initialDescription = editingBook?.description ?: "",
            initialCoverImage = editingBook?.coverImage,
            titleLabel = stringResource(R.string.book_title_label),
            descriptionLabel = stringResource(R.string.description_label),
            onDismiss = { showBookEditDialog = false },
            onConfirm = { title, desc, cover ->
                if (editingBook == null) {
                    viewModel.insertBook(
                        BookEntity(
                            workspaceId = workspaceId,
                            externalId = java.util.UUID.randomUUID().toString(),
                            title = title,
                            description = desc
                        ),
                        coverUri = cover
                    )
                } else {
                    editingBook?.let { book ->
                        viewModel.updateBook(
                            book.copy(title = title, description = desc),
                            newCoverUri = cover
                        )
                    }
                }
                showBookEditDialog = false
            }
        )
    }

    if (showQuizEditDialog) {
        EditEntityDialog(
            title = if (editingQuiz == null) stringResource(R.string.new_quiz) else stringResource(R.string.edit),
            initialTitle = editingQuiz?.title ?: "",
            initialDescription = editingQuiz?.description ?: "",
            initialCoverImage = editingQuiz?.coverImage,
            titleLabel = stringResource(R.string.quiz_title_label),
            descriptionLabel = stringResource(R.string.description_label),
            onDismiss = { showQuizEditDialog = false },
            onConfirm = { title, desc, cover ->
                if (editingQuiz == null) {
                    // Logic to add new quiz if needed
                } else {
                    editingQuiz?.let { quiz ->
                        viewModel.updateQuiz(
                            quiz.copy(title = title, description = desc),
                            newCoverUri = cover
                        )
                    }
                }
                showQuizEditDialog = false
            }
        )
    }

    if (showFlashcardDeckDialog && selectedBookId != -1L) {
        EditEntityDialog(
            title = stringResource(R.string.flashcard_deck),
            initialTitle = currentBook?.title?.let { "$it Flashcards" } ?: "",
            initialDescription = currentBook?.description ?: "",
            initialCoverImage = currentBook?.coverImage,
            titleLabel = stringResource(R.string.name_label),
            descriptionLabel = stringResource(R.string.description_label),
            onDismiss = { showFlashcardDeckDialog = false },
            onConfirm = { title, desc, cover ->
                viewModel.createFlashcardDeckFromBook(
                    bookId = selectedBookId,
                    title = title,
                    description = desc,
                    coverUri = cover,
                    onCreated = onFlashcardDeckSelected
                )
                showFlashcardDeckDialog = false
            }
        )
    }

    if (showPromptEditDialog && selectedBookId != -1L) {
        EditEntityDialog(
            title = stringResource(R.string.new_ai_prompt),
            initialTitle = "",
            initialDescription = "",
            initialCoverImage = null,
            titleLabel = stringResource(R.string.prompt_name_label),
            descriptionLabel = stringResource(R.string.prompt_stem_desc),
            onDismiss = { showPromptEditDialog = false },
            onConfirm = { title, stem, _ ->
                viewModel.insertPrompt(selectedBookId, title, stem)
                showPromptEditDialog = false
            }
        )
    }

    showDeleteConfirmBook?.let { book ->
        DeleteConfirmDialog(
            title = stringResource(R.string.delete_book_title),
            message = stringResource(R.string.delete_book_msg, book.title),
            onDismiss = { showDeleteConfirmBook = null },
            onConfirm = { viewModel.deleteBook(book); showDeleteConfirmBook = null }
        )
    }

    showDeleteConfirmQuiz?.let { quiz ->
        DeleteConfirmDialog(
            title = stringResource(R.string.delete_quiz_title),
            message = stringResource(R.string.delete_quiz_msg, quiz.title),
            onDismiss = { showDeleteConfirmQuiz = null },
            onConfirm = { viewModel.deleteQuiz(quiz); showDeleteConfirmQuiz = null }
        )
    }

    if (showSortDialog) {
        val isQuizzes = selectedBookId != -1L || selectedCategory != null
        SortDialog(
            currentOption = if (isQuizzes) bookSortBy else sortBy,
            onOptionSelected = {
                if (isQuizzes) {
                    viewModel.setBookSortBy(it)
                } else {
                    viewModel.setSortBy(it)
                }
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }
}
