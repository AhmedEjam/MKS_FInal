package com.ahmedyejam.mks.data.review;

import com.ahmedyejam.mks.data.local.dao.CourseSlideDao;
import com.ahmedyejam.mks.data.local.dao.FlashcardDao;
import com.ahmedyejam.mks.data.local.dao.MistakeLogDao;
import com.ahmedyejam.mks.data.local.dao.NoteBlueprintDao;
import com.ahmedyejam.mks.data.local.dao.QuestionDao;
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
public final class ReviewRepository_Factory implements Factory<ReviewRepository> {
  private final Provider<FlashcardDao> flashcardDaoProvider;

  private final Provider<NoteBlueprintDao> noteBlueprintDaoProvider;

  private final Provider<MistakeLogDao> mistakeLogDaoProvider;

  private final Provider<QuestionDao> questionDaoProvider;

  private final Provider<CourseSlideDao> courseSlideDaoProvider;

  private ReviewRepository_Factory(Provider<FlashcardDao> flashcardDaoProvider,
      Provider<NoteBlueprintDao> noteBlueprintDaoProvider,
      Provider<MistakeLogDao> mistakeLogDaoProvider, Provider<QuestionDao> questionDaoProvider,
      Provider<CourseSlideDao> courseSlideDaoProvider) {
    this.flashcardDaoProvider = flashcardDaoProvider;
    this.noteBlueprintDaoProvider = noteBlueprintDaoProvider;
    this.mistakeLogDaoProvider = mistakeLogDaoProvider;
    this.questionDaoProvider = questionDaoProvider;
    this.courseSlideDaoProvider = courseSlideDaoProvider;
  }

  @Override
  public ReviewRepository get() {
    return newInstance(flashcardDaoProvider.get(), noteBlueprintDaoProvider.get(), mistakeLogDaoProvider.get(), questionDaoProvider.get(), courseSlideDaoProvider.get());
  }

  public static ReviewRepository_Factory create(Provider<FlashcardDao> flashcardDaoProvider,
      Provider<NoteBlueprintDao> noteBlueprintDaoProvider,
      Provider<MistakeLogDao> mistakeLogDaoProvider, Provider<QuestionDao> questionDaoProvider,
      Provider<CourseSlideDao> courseSlideDaoProvider) {
    return new ReviewRepository_Factory(flashcardDaoProvider, noteBlueprintDaoProvider, mistakeLogDaoProvider, questionDaoProvider, courseSlideDaoProvider);
  }

  public static ReviewRepository newInstance(FlashcardDao flashcardDao,
      NoteBlueprintDao noteBlueprintDao, MistakeLogDao mistakeLogDao, QuestionDao questionDao,
      CourseSlideDao courseSlideDao) {
    return new ReviewRepository(flashcardDao, noteBlueprintDao, mistakeLogDao, questionDao, courseSlideDao);
  }
}
