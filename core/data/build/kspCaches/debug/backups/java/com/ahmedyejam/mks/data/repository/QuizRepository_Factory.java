package com.ahmedyejam.mks.data.repository;

import com.ahmedyejam.mks.data.importer.repository.ImportLibraryManager;
import com.ahmedyejam.mks.data.local.FileManager;
import com.ahmedyejam.mks.data.local.dao.AnnotationDao;
import com.ahmedyejam.mks.data.local.dao.AssetReferenceDao;
import com.ahmedyejam.mks.data.local.dao.BookDao;
import com.ahmedyejam.mks.data.local.dao.CategoryMetadataDao;
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao;
import com.ahmedyejam.mks.data.local.dao.FlashcardDao;
import com.ahmedyejam.mks.data.local.dao.FlashcardDeckDao;
import com.ahmedyejam.mks.data.local.dao.KnowledgeStudySessionDao;
import com.ahmedyejam.mks.data.local.dao.LearningSessionDao;
import com.ahmedyejam.mks.data.local.dao.MistakeLogDao;
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao;
import com.ahmedyejam.mks.data.local.dao.NoteCollectionDao;
import com.ahmedyejam.mks.data.local.dao.PromptCardDao;
import com.ahmedyejam.mks.data.local.dao.PromptDao;
import com.ahmedyejam.mks.data.local.dao.PromptDeckDao;
import com.ahmedyejam.mks.data.local.dao.PromptRunDao;
import com.ahmedyejam.mks.data.local.dao.QuestionAssetDao;
import com.ahmedyejam.mks.data.local.dao.QuestionCategoryDao;
import com.ahmedyejam.mks.data.local.dao.QuestionDao;
import com.ahmedyejam.mks.data.local.dao.QuizDao;
import com.ahmedyejam.mks.data.local.dao.SessionDao;
import com.ahmedyejam.mks.data.local.dao.SlideshowCourseDao;
import com.ahmedyejam.mks.data.local.dao.SourceDocumentDao;
import com.ahmedyejam.mks.data.local.dao.StudySessionDao;
import com.ahmedyejam.mks.data.local.dao.WorkspaceDao;
import com.ahmedyejam.mks.data.preview.CategoryMergePreviewService;
import com.ahmedyejam.mks.data.preview.ClearMarksPreviewService;
import com.ahmedyejam.mks.data.preview.DeletePreviewService;
import com.ahmedyejam.mks.data.repair.AssetReferenceAuditService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class QuizRepository_Factory implements Factory<QuizRepository> {
  private final Provider<WorkspaceDao> workspaceDaoProvider;

  private final Provider<BookDao> bookDaoProvider;

  private final Provider<QuizDao> quizDaoProvider;

  private final Provider<QuestionDao> questionDaoProvider;

  private final Provider<SessionDao> sessionDaoProvider;

  private final Provider<CategoryMetadataDao> categoryMetadataDaoProvider;

  private final Provider<FileManager> fileManagerProvider;

  private final Provider<ExportManager> exportManagerProvider;

  private final Provider<ImportLibraryManager> importManagerProvider;

  private final Provider<FlashcardDeckDao> flashcardDeckDaoProvider;

  private final Provider<FlashcardDao> flashcardDaoProvider;

  private final Provider<LearningSessionDao> learningSessionDaoProvider;

  private final Provider<SlideshowCourseDao> slideshowCourseDaoProvider;

  private final Provider<CourseSlideDao> courseSlideDaoProvider;

  private final Provider<NoteCollectionDao> noteCollectionDaoProvider;

  private final Provider<NoteBlueprintDao> noteBlueprintDaoProvider;

  private final Provider<PromptDao> promptDaoProvider;

  private final Provider<StudySessionDao> studySessionDaoProvider;

  private final Provider<KnowledgeStudySessionDao> knowledgeStudySessionDaoProvider;

  private final Provider<QuestionCategoryDao> questionCategoryDaoProvider;

  private final Provider<AssetReferenceDao> assetReferenceDaoProvider;

  private final Provider<QuestionAssetDao> questionAssetDaoProvider;

  private final Provider<SourceDocumentDao> sourceDocumentDaoProvider;

  private final Provider<PromptDeckDao> promptDeckDaoProvider;

  private final Provider<PromptCardDao> promptCardDaoProvider;

  private final Provider<PromptRunDao> promptRunDaoProvider;

  private final Provider<MistakeLogDao> mistakeLogDaoProvider;

  private final Provider<AnnotationDao> annotationDaoProvider;

  private final Provider<DeletePreviewService> deletePreviewServiceProvider;

  private final Provider<CategoryMergePreviewService> categoryMergePreviewServiceProvider;

  private final Provider<ClearMarksPreviewService> clearMarksPreviewServiceProvider;

  private final Provider<AssetReferenceAuditService> assetReferenceAuditServiceProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private QuizRepository_Factory(Provider<WorkspaceDao> workspaceDaoProvider,
      Provider<BookDao> bookDaoProvider, Provider<QuizDao> quizDaoProvider,
      Provider<QuestionDao> questionDaoProvider, Provider<SessionDao> sessionDaoProvider,
      Provider<CategoryMetadataDao> categoryMetadataDaoProvider,
      Provider<FileManager> fileManagerProvider, Provider<ExportManager> exportManagerProvider,
      Provider<ImportLibraryManager> importManagerProvider,
      Provider<FlashcardDeckDao> flashcardDeckDaoProvider,
      Provider<FlashcardDao> flashcardDaoProvider,
      Provider<LearningSessionDao> learningSessionDaoProvider,
      Provider<SlideshowCourseDao> slideshowCourseDaoProvider,
      Provider<CourseSlideDao> courseSlideDaoProvider,
      Provider<NoteCollectionDao> noteCollectionDaoProvider,
      Provider<NoteBlueprintDao> noteBlueprintDaoProvider, Provider<PromptDao> promptDaoProvider,
      Provider<StudySessionDao> studySessionDaoProvider,
      Provider<KnowledgeStudySessionDao> knowledgeStudySessionDaoProvider,
      Provider<QuestionCategoryDao> questionCategoryDaoProvider,
      Provider<AssetReferenceDao> assetReferenceDaoProvider,
      Provider<QuestionAssetDao> questionAssetDaoProvider,
      Provider<SourceDocumentDao> sourceDocumentDaoProvider,
      Provider<PromptDeckDao> promptDeckDaoProvider, Provider<PromptCardDao> promptCardDaoProvider,
      Provider<PromptRunDao> promptRunDaoProvider, Provider<MistakeLogDao> mistakeLogDaoProvider,
      Provider<AnnotationDao> annotationDaoProvider,
      Provider<DeletePreviewService> deletePreviewServiceProvider,
      Provider<CategoryMergePreviewService> categoryMergePreviewServiceProvider,
      Provider<ClearMarksPreviewService> clearMarksPreviewServiceProvider,
      Provider<AssetReferenceAuditService> assetReferenceAuditServiceProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider) {
    this.workspaceDaoProvider = workspaceDaoProvider;
    this.bookDaoProvider = bookDaoProvider;
    this.quizDaoProvider = quizDaoProvider;
    this.questionDaoProvider = questionDaoProvider;
    this.sessionDaoProvider = sessionDaoProvider;
    this.categoryMetadataDaoProvider = categoryMetadataDaoProvider;
    this.fileManagerProvider = fileManagerProvider;
    this.exportManagerProvider = exportManagerProvider;
    this.importManagerProvider = importManagerProvider;
    this.flashcardDeckDaoProvider = flashcardDeckDaoProvider;
    this.flashcardDaoProvider = flashcardDaoProvider;
    this.learningSessionDaoProvider = learningSessionDaoProvider;
    this.slideshowCourseDaoProvider = slideshowCourseDaoProvider;
    this.courseSlideDaoProvider = courseSlideDaoProvider;
    this.noteCollectionDaoProvider = noteCollectionDaoProvider;
    this.noteBlueprintDaoProvider = noteBlueprintDaoProvider;
    this.promptDaoProvider = promptDaoProvider;
    this.studySessionDaoProvider = studySessionDaoProvider;
    this.knowledgeStudySessionDaoProvider = knowledgeStudySessionDaoProvider;
    this.questionCategoryDaoProvider = questionCategoryDaoProvider;
    this.assetReferenceDaoProvider = assetReferenceDaoProvider;
    this.questionAssetDaoProvider = questionAssetDaoProvider;
    this.sourceDocumentDaoProvider = sourceDocumentDaoProvider;
    this.promptDeckDaoProvider = promptDeckDaoProvider;
    this.promptCardDaoProvider = promptCardDaoProvider;
    this.promptRunDaoProvider = promptRunDaoProvider;
    this.mistakeLogDaoProvider = mistakeLogDaoProvider;
    this.annotationDaoProvider = annotationDaoProvider;
    this.deletePreviewServiceProvider = deletePreviewServiceProvider;
    this.categoryMergePreviewServiceProvider = categoryMergePreviewServiceProvider;
    this.clearMarksPreviewServiceProvider = clearMarksPreviewServiceProvider;
    this.assetReferenceAuditServiceProvider = assetReferenceAuditServiceProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
  }

  @Override
  public QuizRepository get() {
    return newInstance(workspaceDaoProvider.get(), bookDaoProvider.get(), quizDaoProvider.get(), questionDaoProvider.get(), sessionDaoProvider.get(), categoryMetadataDaoProvider.get(), fileManagerProvider.get(), exportManagerProvider.get(), importManagerProvider.get(), flashcardDeckDaoProvider.get(), flashcardDaoProvider.get(), learningSessionDaoProvider.get(), slideshowCourseDaoProvider.get(), courseSlideDaoProvider.get(), noteCollectionDaoProvider.get(), noteBlueprintDaoProvider.get(), promptDaoProvider.get(), studySessionDaoProvider.get(), knowledgeStudySessionDaoProvider.get(), questionCategoryDaoProvider.get(), assetReferenceDaoProvider.get(), questionAssetDaoProvider.get(), sourceDocumentDaoProvider.get(), promptDeckDaoProvider.get(), promptCardDaoProvider.get(), promptRunDaoProvider.get(), mistakeLogDaoProvider.get(), annotationDaoProvider.get(), deletePreviewServiceProvider.get(), categoryMergePreviewServiceProvider.get(), clearMarksPreviewServiceProvider.get(), assetReferenceAuditServiceProvider.get(), bookRepositoryProvider, assetRepositoryProvider);
  }

  public static QuizRepository_Factory create(Provider<WorkspaceDao> workspaceDaoProvider,
      Provider<BookDao> bookDaoProvider, Provider<QuizDao> quizDaoProvider,
      Provider<QuestionDao> questionDaoProvider, Provider<SessionDao> sessionDaoProvider,
      Provider<CategoryMetadataDao> categoryMetadataDaoProvider,
      Provider<FileManager> fileManagerProvider, Provider<ExportManager> exportManagerProvider,
      Provider<ImportLibraryManager> importManagerProvider,
      Provider<FlashcardDeckDao> flashcardDeckDaoProvider,
      Provider<FlashcardDao> flashcardDaoProvider,
      Provider<LearningSessionDao> learningSessionDaoProvider,
      Provider<SlideshowCourseDao> slideshowCourseDaoProvider,
      Provider<CourseSlideDao> courseSlideDaoProvider,
      Provider<NoteCollectionDao> noteCollectionDaoProvider,
      Provider<NoteBlueprintDao> noteBlueprintDaoProvider, Provider<PromptDao> promptDaoProvider,
      Provider<StudySessionDao> studySessionDaoProvider,
      Provider<KnowledgeStudySessionDao> knowledgeStudySessionDaoProvider,
      Provider<QuestionCategoryDao> questionCategoryDaoProvider,
      Provider<AssetReferenceDao> assetReferenceDaoProvider,
      Provider<QuestionAssetDao> questionAssetDaoProvider,
      Provider<SourceDocumentDao> sourceDocumentDaoProvider,
      Provider<PromptDeckDao> promptDeckDaoProvider, Provider<PromptCardDao> promptCardDaoProvider,
      Provider<PromptRunDao> promptRunDaoProvider, Provider<MistakeLogDao> mistakeLogDaoProvider,
      Provider<AnnotationDao> annotationDaoProvider,
      Provider<DeletePreviewService> deletePreviewServiceProvider,
      Provider<CategoryMergePreviewService> categoryMergePreviewServiceProvider,
      Provider<ClearMarksPreviewService> clearMarksPreviewServiceProvider,
      Provider<AssetReferenceAuditService> assetReferenceAuditServiceProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider) {
    return new QuizRepository_Factory(workspaceDaoProvider, bookDaoProvider, quizDaoProvider, questionDaoProvider, sessionDaoProvider, categoryMetadataDaoProvider, fileManagerProvider, exportManagerProvider, importManagerProvider, flashcardDeckDaoProvider, flashcardDaoProvider, learningSessionDaoProvider, slideshowCourseDaoProvider, courseSlideDaoProvider, noteCollectionDaoProvider, noteBlueprintDaoProvider, promptDaoProvider, studySessionDaoProvider, knowledgeStudySessionDaoProvider, questionCategoryDaoProvider, assetReferenceDaoProvider, questionAssetDaoProvider, sourceDocumentDaoProvider, promptDeckDaoProvider, promptCardDaoProvider, promptRunDaoProvider, mistakeLogDaoProvider, annotationDaoProvider, deletePreviewServiceProvider, categoryMergePreviewServiceProvider, clearMarksPreviewServiceProvider, assetReferenceAuditServiceProvider, bookRepositoryProvider, assetRepositoryProvider);
  }

  public static QuizRepository newInstance(WorkspaceDao workspaceDao, BookDao bookDao,
      QuizDao quizDao, QuestionDao questionDao, SessionDao sessionDao,
      CategoryMetadataDao categoryMetadataDao, FileManager fileManager, ExportManager exportManager,
      ImportLibraryManager importManager, FlashcardDeckDao flashcardDeckDao,
      FlashcardDao flashcardDao, LearningSessionDao learningSessionDao,
      SlideshowCourseDao slideshowCourseDao, CourseSlideDao courseSlideDao,
      NoteCollectionDao noteCollectionDao, NoteBlueprintDao noteBlueprintDao, PromptDao promptDao,
      StudySessionDao studySessionDao, KnowledgeStudySessionDao knowledgeStudySessionDao,
      QuestionCategoryDao questionCategoryDao, AssetReferenceDao assetReferenceDao,
      QuestionAssetDao questionAssetDao, SourceDocumentDao sourceDocumentDao,
      PromptDeckDao promptDeckDao, PromptCardDao promptCardDao, PromptRunDao promptRunDao,
      MistakeLogDao mistakeLogDao, AnnotationDao annotationDao,
      DeletePreviewService deletePreviewService,
      CategoryMergePreviewService categoryMergePreviewService,
      ClearMarksPreviewService clearMarksPreviewService,
      AssetReferenceAuditService assetReferenceAuditService,
      javax.inject.Provider<BookRepository> bookRepositoryProvider,
      javax.inject.Provider<AssetRepository> assetRepositoryProvider) {
    return new QuizRepository(workspaceDao, bookDao, quizDao, questionDao, sessionDao, categoryMetadataDao, fileManager, exportManager, importManager, flashcardDeckDao, flashcardDao, learningSessionDao, slideshowCourseDao, courseSlideDao, noteCollectionDao, noteBlueprintDao, promptDao, studySessionDao, knowledgeStudySessionDao, questionCategoryDao, assetReferenceDao, questionAssetDao, sourceDocumentDao, promptDeckDao, promptCardDao, promptRunDao, mistakeLogDao, annotationDao, deletePreviewService, categoryMergePreviewService, clearMarksPreviewService, assetReferenceAuditService, bookRepositoryProvider, assetRepositoryProvider);
  }
}
