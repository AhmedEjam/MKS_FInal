package com.ahmedyejam.mks.ui.quiz;

import com.ahmedyejam.mks.data.focus.FocusManager;
import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
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
public final class QuizViewModel_Factory implements Factory<QuizViewModel> {
  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<FocusManager> focusManagerProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<StudyRepository> studyRepositoryProvider;

  private QuizViewModel_Factory(Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FocusManager> focusManagerProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.focusManagerProvider = focusManagerProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.studyRepositoryProvider = studyRepositoryProvider;
  }

  @Override
  public QuizViewModel get() {
    return newInstance(dataStoreManagerProvider.get(), focusManagerProvider.get(), knowledgeRepositoryProvider.get(), assetRepositoryProvider.get(), quizRepositoryProvider.get(), studyRepositoryProvider.get());
  }

  public static QuizViewModel_Factory create(Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FocusManager> focusManagerProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    return new QuizViewModel_Factory(dataStoreManagerProvider, focusManagerProvider, knowledgeRepositoryProvider, assetRepositoryProvider, quizRepositoryProvider, studyRepositoryProvider);
  }

  public static QuizViewModel newInstance(DataStoreManager dataStoreManager,
      FocusManager focusManager, KnowledgeRepository knowledgeRepository,
      AssetRepository assetRepository, QuizRepository quizRepository,
      StudyRepository studyRepository) {
    return new QuizViewModel(dataStoreManager, focusManager, knowledgeRepository, assetRepository, quizRepository, studyRepository);
  }
}
