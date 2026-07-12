package com.ahmedyejam.mks.ui.flashcard;

import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.BookRepository;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
import com.ahmedyejam.mks.data.repository.QuizRepository;
import com.ahmedyejam.mks.data.repository.StudyRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata
@QualifierMetadata("com.ahmedyejam.mks.di.ApplicationScope")
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
public final class FlashcardDeckViewModel_Factory implements Factory<FlashcardDeckViewModel> {
  private final Provider<CoroutineScope> applicationScopeProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<StudyRepository> studyRepositoryProvider;

  private FlashcardDeckViewModel_Factory(Provider<CoroutineScope> applicationScopeProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    this.applicationScopeProvider = applicationScopeProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.studyRepositoryProvider = studyRepositoryProvider;
  }

  @Override
  public FlashcardDeckViewModel get() {
    return newInstance(applicationScopeProvider.get(), bookRepositoryProvider.get(), knowledgeRepositoryProvider.get(), assetRepositoryProvider.get(), quizRepositoryProvider.get(), studyRepositoryProvider.get());
  }

  public static FlashcardDeckViewModel_Factory create(
      Provider<CoroutineScope> applicationScopeProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    return new FlashcardDeckViewModel_Factory(applicationScopeProvider, bookRepositoryProvider, knowledgeRepositoryProvider, assetRepositoryProvider, quizRepositoryProvider, studyRepositoryProvider);
  }

  public static FlashcardDeckViewModel newInstance(CoroutineScope applicationScope,
      BookRepository bookRepository, KnowledgeRepository knowledgeRepository,
      AssetRepository assetRepository, QuizRepository quizRepository,
      StudyRepository studyRepository) {
    return new FlashcardDeckViewModel(applicationScope, bookRepository, knowledgeRepository, assetRepository, quizRepository, studyRepository);
  }
}
