package com.ahmedyejam.mks.data.preview;

import com.ahmedyejam.mks.data.local.dao.QuestionCategoryDao;
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
public final class CategoryMergePreviewService_Factory implements Factory<CategoryMergePreviewService> {
  private final Provider<QuestionCategoryDao> questionCategoryDaoProvider;

  private CategoryMergePreviewService_Factory(
      Provider<QuestionCategoryDao> questionCategoryDaoProvider) {
    this.questionCategoryDaoProvider = questionCategoryDaoProvider;
  }

  @Override
  public CategoryMergePreviewService get() {
    return newInstance(questionCategoryDaoProvider.get());
  }

  public static CategoryMergePreviewService_Factory create(
      Provider<QuestionCategoryDao> questionCategoryDaoProvider) {
    return new CategoryMergePreviewService_Factory(questionCategoryDaoProvider);
  }

  public static CategoryMergePreviewService newInstance(QuestionCategoryDao questionCategoryDao) {
    return new CategoryMergePreviewService(questionCategoryDao);
  }
}
