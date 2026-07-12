package com.ahmedyejam.mks.ui.summary;

import com.ahmedyejam.mks.data.repository.QuizRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class SummaryViewModel_Factory implements Factory<SummaryViewModel> {
  private final Provider<QuizRepository> quizRepositoryProvider;

  private SummaryViewModel_Factory(Provider<QuizRepository> quizRepositoryProvider) {
    this.quizRepositoryProvider = quizRepositoryProvider;
  }

  @Override
  public SummaryViewModel get() {
    return newInstance(quizRepositoryProvider.get());
  }

  public static SummaryViewModel_Factory create(Provider<QuizRepository> quizRepositoryProvider) {
    return new SummaryViewModel_Factory(quizRepositoryProvider);
  }

  public static SummaryViewModel newInstance(QuizRepository quizRepository) {
    return new SummaryViewModel(quizRepository);
  }
}
