package com.ahmedyejam.mks.ui.quiz;

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
public final class QuizQuestionsViewModel_Factory implements Factory<QuizQuestionsViewModel> {
  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<StudyRepository> studyRepositoryProvider;

  private QuizQuestionsViewModel_Factory(Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.studyRepositoryProvider = studyRepositoryProvider;
  }

  @Override
  public QuizQuestionsViewModel get() {
    return newInstance(knowledgeRepositoryProvider.get(), assetRepositoryProvider.get(), quizRepositoryProvider.get(), studyRepositoryProvider.get());
  }

  public static QuizQuestionsViewModel_Factory create(
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    return new QuizQuestionsViewModel_Factory(knowledgeRepositoryProvider, assetRepositoryProvider, quizRepositoryProvider, studyRepositoryProvider);
  }

  public static QuizQuestionsViewModel newInstance(KnowledgeRepository knowledgeRepository,
      AssetRepository assetRepository, QuizRepository quizRepository,
      StudyRepository studyRepository) {
    return new QuizQuestionsViewModel(knowledgeRepository, assetRepository, quizRepository, studyRepository);
  }
}
