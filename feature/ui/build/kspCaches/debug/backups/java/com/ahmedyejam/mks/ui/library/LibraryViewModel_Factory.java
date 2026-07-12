package com.ahmedyejam.mks.ui.library;

import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.BookRepository;
import com.ahmedyejam.mks.data.repository.ExportManager;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
import com.ahmedyejam.mks.data.repository.QuizRepository;
import com.ahmedyejam.mks.data.repository.WorkspaceRepository;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<WorkspaceRepository> workspaceRepositoryProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<ExportManager> exportManagerProvider;

  private LibraryViewModel_Factory(Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<WorkspaceRepository> workspaceRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<ExportManager> exportManagerProvider) {
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.workspaceRepositoryProvider = workspaceRepositoryProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.exportManagerProvider = exportManagerProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(dataStoreManagerProvider.get(), bookRepositoryProvider.get(), workspaceRepositoryProvider.get(), knowledgeRepositoryProvider.get(), assetRepositoryProvider.get(), quizRepositoryProvider.get(), exportManagerProvider.get());
  }

  public static LibraryViewModel_Factory create(Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<WorkspaceRepository> workspaceRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<ExportManager> exportManagerProvider) {
    return new LibraryViewModel_Factory(dataStoreManagerProvider, bookRepositoryProvider, workspaceRepositoryProvider, knowledgeRepositoryProvider, assetRepositoryProvider, quizRepositoryProvider, exportManagerProvider);
  }

  public static LibraryViewModel newInstance(DataStoreManager dataStoreManager,
      BookRepository bookRepository, WorkspaceRepository workspaceRepository,
      KnowledgeRepository knowledgeRepository, AssetRepository assetRepository,
      QuizRepository quizRepository, ExportManager exportManager) {
    return new LibraryViewModel(dataStoreManager, bookRepository, workspaceRepository, knowledgeRepository, assetRepository, quizRepository, exportManager);
  }
}
