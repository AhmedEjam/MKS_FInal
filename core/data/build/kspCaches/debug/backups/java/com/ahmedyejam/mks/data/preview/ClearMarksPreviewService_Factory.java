package com.ahmedyejam.mks.data.preview;

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
public final class ClearMarksPreviewService_Factory implements Factory<ClearMarksPreviewService> {
  private final Provider<QuestionDao> questionDaoProvider;

  private ClearMarksPreviewService_Factory(Provider<QuestionDao> questionDaoProvider) {
    this.questionDaoProvider = questionDaoProvider;
  }

  @Override
  public ClearMarksPreviewService get() {
    return newInstance(questionDaoProvider.get());
  }

  public static ClearMarksPreviewService_Factory create(Provider<QuestionDao> questionDaoProvider) {
    return new ClearMarksPreviewService_Factory(questionDaoProvider);
  }

  public static ClearMarksPreviewService newInstance(QuestionDao questionDao) {
    return new ClearMarksPreviewService(questionDao);
  }
}
