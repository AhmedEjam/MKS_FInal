package com.ahmedyejam.mks.ui.booktools;

import com.ahmedyejam.mks.data.local.FileManager;
import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.BookRepository;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
import com.ahmedyejam.mks.data.repository.OllamaRepository;
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
public final class BookToolsViewModel_Factory implements Factory<BookToolsViewModel> {
  private final Provider<OllamaRepository> ollamaRepositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<FileManager> fileManagerProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<StudyRepository> studyRepositoryProvider;

  private BookToolsViewModel_Factory(Provider<OllamaRepository> ollamaRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FileManager> fileManagerProvider, Provider<BookRepository> bookRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    this.ollamaRepositoryProvider = ollamaRepositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.fileManagerProvider = fileManagerProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.studyRepositoryProvider = studyRepositoryProvider;
  }

  @Override
  public BookToolsViewModel get() {
    return newInstance(ollamaRepositoryProvider.get(), dataStoreManagerProvider.get(), fileManagerProvider.get(), bookRepositoryProvider.get(), knowledgeRepositoryProvider.get(), assetRepositoryProvider.get(), quizRepositoryProvider.get(), studyRepositoryProvider.get());
  }

  public static BookToolsViewModel_Factory create(
      Provider<OllamaRepository> ollamaRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FileManager> fileManagerProvider, Provider<BookRepository> bookRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    return new BookToolsViewModel_Factory(ollamaRepositoryProvider, dataStoreManagerProvider, fileManagerProvider, bookRepositoryProvider, knowledgeRepositoryProvider, assetRepositoryProvider, quizRepositoryProvider, studyRepositoryProvider);
  }

  public static BookToolsViewModel newInstance(OllamaRepository ollamaRepository,
      DataStoreManager dataStoreManager, FileManager fileManager, BookRepository bookRepository,
      KnowledgeRepository knowledgeRepository, AssetRepository assetRepository,
      QuizRepository quizRepository, StudyRepository studyRepository) {
    return new BookToolsViewModel(ollamaRepository, dataStoreManager, fileManager, bookRepository, knowledgeRepository, assetRepository, quizRepository, studyRepository);
  }
}
