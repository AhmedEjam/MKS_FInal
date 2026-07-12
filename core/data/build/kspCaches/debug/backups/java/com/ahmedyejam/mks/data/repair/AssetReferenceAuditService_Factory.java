package com.ahmedyejam.mks.data.repair;

import com.ahmedyejam.mks.data.local.dao.AssetReferenceDao;
import com.ahmedyejam.mks.data.local.dao.BookDao;
import com.ahmedyejam.mks.data.local.dao.CourseSlideDao;
import com.ahmedyejam.mks.data.local.dao.FlashcardDao;
import com.ahmedyejam.mks.data.local.dao.QuestionAssetDao;
import com.ahmedyejam.mks.data.local.dao.QuestionDao;
import com.ahmedyejam.mks.data.local.dao.QuizDao;
import com.ahmedyejam.mks.data.local.dao.SourceDocumentDao;
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
public final class AssetReferenceAuditService_Factory implements Factory<AssetReferenceAuditService> {
  private final Provider<AssetReferenceDao> assetReferenceDaoProvider;

  private final Provider<BookDao> bookDaoProvider;

  private final Provider<QuizDao> quizDaoProvider;

  private final Provider<QuestionDao> questionDaoProvider;

  private final Provider<FlashcardDao> flashcardDaoProvider;

  private final Provider<CourseSlideDao> courseSlideDaoProvider;

  private final Provider<SourceDocumentDao> sourceDocumentDaoProvider;

  private final Provider<QuestionAssetDao> questionAssetDaoProvider;

  private AssetReferenceAuditService_Factory(Provider<AssetReferenceDao> assetReferenceDaoProvider,
      Provider<BookDao> bookDaoProvider, Provider<QuizDao> quizDaoProvider,
      Provider<QuestionDao> questionDaoProvider, Provider<FlashcardDao> flashcardDaoProvider,
      Provider<CourseSlideDao> courseSlideDaoProvider,
      Provider<SourceDocumentDao> sourceDocumentDaoProvider,
      Provider<QuestionAssetDao> questionAssetDaoProvider) {
    this.assetReferenceDaoProvider = assetReferenceDaoProvider;
    this.bookDaoProvider = bookDaoProvider;
    this.quizDaoProvider = quizDaoProvider;
    this.questionDaoProvider = questionDaoProvider;
    this.flashcardDaoProvider = flashcardDaoProvider;
    this.courseSlideDaoProvider = courseSlideDaoProvider;
    this.sourceDocumentDaoProvider = sourceDocumentDaoProvider;
    this.questionAssetDaoProvider = questionAssetDaoProvider;
  }

  @Override
  public AssetReferenceAuditService get() {
    return newInstance(assetReferenceDaoProvider.get(), bookDaoProvider.get(), quizDaoProvider.get(), questionDaoProvider.get(), flashcardDaoProvider.get(), courseSlideDaoProvider.get(), sourceDocumentDaoProvider.get(), questionAssetDaoProvider.get());
  }

  public static AssetReferenceAuditService_Factory create(
      Provider<AssetReferenceDao> assetReferenceDaoProvider, Provider<BookDao> bookDaoProvider,
      Provider<QuizDao> quizDaoProvider, Provider<QuestionDao> questionDaoProvider,
      Provider<FlashcardDao> flashcardDaoProvider, Provider<CourseSlideDao> courseSlideDaoProvider,
      Provider<SourceDocumentDao> sourceDocumentDaoProvider,
      Provider<QuestionAssetDao> questionAssetDaoProvider) {
    return new AssetReferenceAuditService_Factory(assetReferenceDaoProvider, bookDaoProvider, quizDaoProvider, questionDaoProvider, flashcardDaoProvider, courseSlideDaoProvider, sourceDocumentDaoProvider, questionAssetDaoProvider);
  }

  public static AssetReferenceAuditService newInstance(AssetReferenceDao assetReferenceDao,
      BookDao bookDao, QuizDao quizDao, QuestionDao questionDao, FlashcardDao flashcardDao,
      CourseSlideDao courseSlideDao, SourceDocumentDao sourceDocumentDao,
      QuestionAssetDao questionAssetDao) {
    return new AssetReferenceAuditService(assetReferenceDao, bookDao, quizDao, questionDao, flashcardDao, courseSlideDao, sourceDocumentDao, questionAssetDao);
  }
}
