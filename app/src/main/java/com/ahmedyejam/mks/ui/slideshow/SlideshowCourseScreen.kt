package com.ahmedyejam.mks.ui.slideshow

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedyejam.mks.data.local.entity.CourseSlideEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity
import com.ahmedyejam.mks.di.AppModule
import com.ahmedyejam.mks.ui.theme.LocalMksDesignTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideshowCourseScreen(
    courseId: Long,
    focusedSlideId: Long? = null,
    appModule: AppModule,
    onBack: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    val vm: SlideshowCourseViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SlideshowCourseViewModel(appModule) as T
            }
        }
    )

    val state by vm.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(focusedSlideId, state.slides) {
        if (focusedSlideId != null && state.slides.isNotEmpty()) {
            val index = state.slides.indexOfFirst { it.id == focusedSlideId }
            if (index != -1) {
                if (tokens.isPlain) {
                    listState.scrollToItem(index + 1)
                } else {
                    listState.animateScrollToItem(index + 1)
                }
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    var showSlideEditor by remember { mutableStateOf<CourseSlideEntity?>(null) }
    var showAddSlide by rememberSaveable { mutableStateOf(false) }
    var showCourseEditor by rememberSaveable { mutableStateOf(false) }
    var showGeneratorDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(courseId) { vm.loadCourseSafe(courseId) }
    LaunchedEffect(state.message, state.error) {
        val text = state.message ?: state.error
        if (text != null) {
            snackbarHostState.showSnackbar(text)
            vm.clearMessage()
        }
    }

    var showOptionsMenu by remember { mutableStateOf(false) }

    state.course?.let { course ->
        if (showCourseEditor) {
            CourseEditorDialog(
                course = course,
                onDismiss = { showCourseEditor = false },
                onSave = { title, desc, cover -> 
                    vm.updateCourse(title, desc, cover)
                    showCourseEditor = false
                }
            )
        }
    }

    if (showAddSlide || showSlideEditor != null) {
        SlideEditorDialog(
            slide = showSlideEditor,
            onDismiss = {
                showAddSlide = false
                showSlideEditor = null
            },
            onSave = { title, body, imagePath ->
                val slide = showSlideEditor
                if (slide == null) vm.addSlide(title, body, imagePath) else vm.updateSlide(slide, title, body, imagePath)
                showAddSlide = false
                showSlideEditor = null
            }
        )
    }

    if (showGeneratorDialog) {
        SlideGeneratorDialog(
            quizzes = state.quizzes,
            categories = state.categories,
            onDismiss = { showGeneratorDialog = false },
            onConfirm = { source, sourceId, clearMarks ->
                when (source) {
                    "ALL" -> vm.generateFromBook(com.ahmedyejam.mks.data.model.SlideGenerationConfig.DEFAULT, clearMarks)
                    "MARKED" -> vm.generateFromMarked(com.ahmedyejam.mks.data.model.SlideGenerationConfig.DEFAULT, clearMarks)
                    "MISSED" -> vm.generateFromMissed(com.ahmedyejam.mks.data.model.SlideGenerationConfig.DEFAULT)
                    "QUIZ" -> sourceId?.toLongOrNull()?.let { vm.generateFromQuiz(it, com.ahmedyejam.mks.data.model.SlideGenerationConfig.DEFAULT, clearMarks) }
                    "CATEGORY" -> sourceId?.let { vm.generateFromCategory(it, com.ahmedyejam.mks.data.model.SlideGenerationConfig.DEFAULT, clearMarks) }
                }
                showGeneratorDialog = false
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (state.selectedSlideIds.isNotEmpty()) {
                TopAppBar(
                    title = { Text("${state.selectedSlideIds.size} selected") },
                    navigationIcon = { IconButton(onClick = { vm.clearSelection() }) { Icon(Icons.Default.Close, contentDescription = "Clear selection") } },
                    actions = {
                        IconButton(onClick = { vm.selectAllSlides() }) {
                            Icon(Icons.Default.SelectAll, contentDescription = "Select All")
                        }
                        IconButton(onClick = { vm.deleteSelectedSlides() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = { Text(state.course?.title ?: "Slideshow Course") },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } },
                    actions = {
                        IconButton(onClick = { showOptionsMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(expanded = showOptionsMenu, onDismissRequest = { showOptionsMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Edit Course") },
                                onClick = { showCourseEditor = true; showOptionsMenu = false }
                            )
                        }
                        IconButton(onClick = { vm.setPresentationMode(!state.isPresentationMode) }) {
                            Icon(if (state.isPresentationMode) Icons.Default.ViewList else Icons.Default.PlayArrow, contentDescription = "Toggle presentation mode")
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.course == null -> Text("Course not found", Modifier.align(Alignment.Center))
                state.isPresentationMode -> SlidePresentationContent(
                    state = state, 
                    viewModel = vm
                )
                else -> SlideshowCourseDetailContent(
                    state = state,
                    onAddSlide = { showAddSlide = true },
                    onEditSlide = { showSlideEditor = it },
                    onDeleteSlide = { vm.deleteSlide(it) },
                    onMoveSlide = { slide, direction -> vm.moveSlide(slide, direction) },
                    onGenerateSlides = { showGeneratorDialog = true },
                    onStartPresentation = { vm.setPresentationMode(true) },
                    onToggleSelect = { vm.toggleSlideSelection(it) }
                )
            }
        }
    }
}

@Composable
private fun SlideshowCourseDetailContent(
    state: SlideshowCourseUiState,
    onAddSlide: () -> Unit,
    onEditSlide: (CourseSlideEntity) -> Unit,
    onDeleteSlide: (CourseSlideEntity) -> Unit,
    onMoveSlide: (CourseSlideEntity, Int) -> Unit,
    onGenerateSlides: () -> Unit,
    onStartPresentation: () -> Unit,
    onToggleSelect: (Long) -> Unit
) {
    val course = state.course ?: return
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            CourseStatsCard(course = course, slideCount = state.slides.size)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onAddSlide, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Add slide")
                }
                OutlinedButton(onClick = onStartPresentation, enabled = state.slides.isNotEmpty(), modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Present")
                }
            }
        }
        item {
            val tokens = LocalMksDesignTokens.current
            Button(
                onClick = onGenerateSlides,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(tokens.cardRadius)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Generate from questions", fontWeight = FontWeight.Bold)
            }
        }
        if (state.slides.isEmpty()) {
            item {
                val tokens = LocalMksDesignTokens.current
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(tokens.cardRadius),
                    elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("No slides yet", fontWeight = FontWeight.SemiBold)
                        Text("Create slides manually or generate them from marked/missed questions.")
                    }
                }
            }
        } else {
            items(state.slides, key = { it.id }) { slide ->
                SlideListItem(
                    slide = slide,
                    isSelected = state.selectedSlideIds.contains(slide.id),
                    isSelectionMode = state.selectedSlideIds.isNotEmpty(),
                    onToggleSelect = { onToggleSelect(slide.id) },
                    onEdit = { onEditSlide(slide) },
                    onDelete = { onDeleteSlide(slide) },
                    onMoveUp = { onMoveSlide(slide, -1) },
                    onMoveDown = { onMoveSlide(slide, 1) }
                )
            }
        }
    }
}

@Composable
private fun CourseStatsCard(course: SlideshowCourseEntity, slideCount: Int) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(course.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            course.description?.takeIf { it.isNotBlank() }?.let { Text(it) }
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text("$slideCount slides") })
                AssistChip(onClick = {}, label = { Text("${course.studiedSlideCount} completed") })
                AssistChip(onClick = {}, label = { Text("${(course.progress * 100).toInt()}% progress") })
            }
            LinearProgressIndicator(progress = { course.progress.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SlideListItem(
    slide: CourseSlideEntity,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onToggleSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    val tokens = LocalMksDesignTokens.current
    Card(
        modifier = Modifier.fillMaxWidth()
            .combinedClickable(
                onClick = { if (isSelectionMode) onToggleSelect() else onEdit() },
                onLongClick = onToggleSelect
            ),
        shape = RoundedCornerShape(tokens.cardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = tokens.cardElevation)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelectionMode) {
                    Checkbox(checked = isSelected, onCheckedChange = { onToggleSelect() })
                } else {
                    Icon(Icons.Default.Slideshow, contentDescription = null)
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(slide.title, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    Text(slide.body, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
                if (!isSelectionMode) {
                    IconButton(onClick = onMoveUp) { Icon(Icons.Default.ArrowUpward, contentDescription = "Move up") }
                    IconButton(onClick = onMoveDown) { Icon(Icons.Default.ArrowDownward, contentDescription = "Move down") }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
                }
            }
        }
    }
}

@Composable
private fun SlidePresentationContent(
    state: SlideshowCourseUiState,
    viewModel: SlideshowCourseViewModel
) {
    val tokens = LocalMksDesignTokens.current
    val slide = state.currentSlide
    if (slide == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No slides to present")
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Slide ${state.currentIndex + 1} / ${state.slides.size}", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(horizontal = 8.dp))
        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            shape = RoundedCornerShape(tokens.cardRadius),
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
                Text(
                    text = slide.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = slide.body,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { viewModel.previousSlide() }) { Text("Previous") }
            Button(onClick = { viewModel.nextSlide() }) { Text("Next") }
        }
    }
}

@Composable
private fun SlideEditorDialog(
    slide: CourseSlideEntity?,
    onDismiss: () -> Unit,
    onSave: (title: String, body: String, imagePath: String?) -> Unit
) {
    var title by rememberSaveable(slide?.id) { mutableStateOf(slide?.title ?: "") }
    var body by rememberSaveable(slide?.id) { mutableStateOf(slide?.body ?: "") }
    var imagePath by rememberSaveable(slide?.id) { mutableStateOf(slide?.imagePath ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (slide == null) "Add Slide" else "Edit Slide") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(body, { body = it }, label = { Text("Body") }, modifier = Modifier.fillMaxWidth(), minLines = 6)
                OutlinedTextField(imagePath, { imagePath = it }, label = { Text("Image URL/Path") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(enabled = title.isNotBlank(), onClick = {
                onSave(
                    title.trim(),
                    body.trim(),
                    imagePath.trim().ifBlank { null }
                )
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun CourseEditorDialog(course: SlideshowCourseEntity, onDismiss: () -> Unit, onSave: (String, String?, String?) -> Unit) {
    var title by rememberSaveable(course.id) { mutableStateOf(course.title) }
    var description by rememberSaveable(course.id) { mutableStateOf(course.description ?: "") }
    var coverImage by rememberSaveable(course.id) { mutableStateOf(course.coverImage ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Course") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(description, { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(coverImage, { coverImage = it }, label = { Text("Cover Image URL/Path") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { Button(enabled = title.isNotBlank(), onClick = { onSave(title.trim(), description.trim().ifBlank { null }, coverImage.trim().ifBlank { null }) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SlideGeneratorDialog(
    quizzes: List<QuizEntity>,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (source: String, sourceId: String?, clearMarks: Boolean) -> Unit
) {
    var source by rememberSaveable { mutableStateOf("ALL") } // ALL, MARKED, MISSED, QUIZ, CATEGORY
    var sourceId by rememberSaveable { mutableStateOf<String?>(null) }
    var clearMarks by rememberSaveable { mutableStateOf(false) }

    var expandedSource by remember { mutableStateOf(false) }
    var expandedSubSource by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Generate slides from questions") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                
                ExposedDropdownMenuBox(
                    expanded = expandedSource,
                    onExpandedChange = { expandedSource = it }
                ) {
                    OutlinedTextField(
                        value = when (source) {
                            "ALL" -> "All Book Questions"
                            "MARKED" -> "Marked Questions"
                            "MISSED" -> "Missed Questions"
                            "QUIZ" -> "Specific Quiz"
                            "CATEGORY" -> "Category"
                            else -> ""
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Source") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSource) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSource,
                        onDismissRequest = { expandedSource = false }
                    ) {
                        DropdownMenuItem(text = { Text("All Book Questions") }, onClick = { source = "ALL"; sourceId = null; expandedSource = false })
                        DropdownMenuItem(text = { Text("Marked Questions") }, onClick = { source = "MARKED"; sourceId = null; expandedSource = false })
                        DropdownMenuItem(text = { Text("Missed Questions") }, onClick = { source = "MISSED"; sourceId = null; expandedSource = false })
                        if (quizzes.isNotEmpty()) {
                            DropdownMenuItem(text = { Text("Specific Quiz") }, onClick = { source = "QUIZ"; sourceId = quizzes.firstOrNull()?.id?.toString(); expandedSource = false })
                        }
                        if (categories.isNotEmpty()) {
                            DropdownMenuItem(text = { Text("Category") }, onClick = { source = "CATEGORY"; sourceId = categories.firstOrNull(); expandedSource = false })
                        }
                    }
                }

                if (source == "QUIZ") {
                    ExposedDropdownMenuBox(
                        expanded = expandedSubSource,
                        onExpandedChange = { expandedSubSource = it }
                    ) {
                        OutlinedTextField(
                            value = quizzes.find { it.id.toString() == sourceId }?.title ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Quiz") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubSource) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSubSource,
                            onDismissRequest = { expandedSubSource = false }
                        ) {
                            quizzes.forEach { quiz ->
                                DropdownMenuItem(text = { Text(quiz.title) }, onClick = { sourceId = quiz.id.toString(); expandedSubSource = false })
                            }
                        }
                    }
                } else if (source == "CATEGORY") {
                    ExposedDropdownMenuBox(
                        expanded = expandedSubSource,
                        onExpandedChange = { expandedSubSource = it }
                    ) {
                        OutlinedTextField(
                            value = sourceId ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSubSource) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSubSource,
                            onDismissRequest = { expandedSubSource = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(text = { Text(cat) }, onClick = { sourceId = cat; expandedSubSource = false })
                            }
                        }
                    }
                }

                if (source != "MISSED") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = clearMarks, onCheckedChange = { clearMarks = it })
                        Text("Clear marks after conversion")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    source,
                    sourceId,
                    clearMarks
                )
            }) { Text("Generate") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
