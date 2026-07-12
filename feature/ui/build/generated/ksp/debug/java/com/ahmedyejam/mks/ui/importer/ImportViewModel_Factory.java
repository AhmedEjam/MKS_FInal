package com.ahmedyejam.mks.ui.importer;

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
public final class ImportViewModel_Factory implements Factory<ImportViewModel> {
  private final Provider<QuizRepository> quizRepositoryProvider;

  private ImportViewModel_Factory(Provider<QuizRepository> quizRepositoryProvider) {
    this.quizRepositoryProvider = quizRepositoryProvider;
  }

  @Override
  public ImportViewModel get() {
    return newInstance(quizRepositoryProvider.get());
  }

  public static ImportViewModel_Factory create(Provider<QuizRepository> quizRepositoryProvider) {
    return new ImportViewModel_Factory(quizRepositoryProvider);
  }

  public static ImportViewModel newInstance(QuizRepository quizRepository) {
    return new ImportViewModel(quizRepository);
  }
}
