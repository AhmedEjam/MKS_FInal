package com.ahmedyejam.mks.ui.session;

import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.QuizRepository;
import com.ahmedyejam.mks.data.repository.StudyRepository;
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
public final class SessionViewModel_Factory implements Factory<SessionViewModel> {
  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<StudyRepository> studyRepositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private SessionViewModel_Factory(Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.studyRepositoryProvider = studyRepositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public SessionViewModel get() {
    return newInstance(quizRepositoryProvider.get(), studyRepositoryProvider.get(), dataStoreManagerProvider.get());
  }

  public static SessionViewModel_Factory create(Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new SessionViewModel_Factory(quizRepositoryProvider, studyRepositoryProvider, dataStoreManagerProvider);
  }

  public static SessionViewModel newInstance(QuizRepository quizRepository,
      StudyRepository studyRepository, DataStoreManager dataStoreManager) {
    return new SessionViewModel(quizRepository, studyRepository, dataStoreManager);
  }
}
