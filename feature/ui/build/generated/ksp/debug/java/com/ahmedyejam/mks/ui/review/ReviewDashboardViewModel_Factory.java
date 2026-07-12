package com.ahmedyejam.mks.ui.review;

import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.BookRepository;
import com.ahmedyejam.mks.data.repository.StudyRepository;
import com.ahmedyejam.mks.data.review.ReviewRepository;
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
public final class ReviewDashboardViewModel_Factory implements Factory<ReviewDashboardViewModel> {
  private final Provider<ReviewRepository> repositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<StudyRepository> studyRepositoryProvider;

  private ReviewDashboardViewModel_Factory(Provider<ReviewRepository> repositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.studyRepositoryProvider = studyRepositoryProvider;
  }

  @Override
  public ReviewDashboardViewModel get() {
    return newInstance(repositoryProvider.get(), dataStoreManagerProvider.get(), bookRepositoryProvider.get(), assetRepositoryProvider.get(), studyRepositoryProvider.get());
  }

  public static ReviewDashboardViewModel_Factory create(
      Provider<ReviewRepository> repositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<StudyRepository> studyRepositoryProvider) {
    return new ReviewDashboardViewModel_Factory(repositoryProvider, dataStoreManagerProvider, bookRepositoryProvider, assetRepositoryProvider, studyRepositoryProvider);
  }

  public static ReviewDashboardViewModel newInstance(ReviewRepository repository,
      DataStoreManager dataStoreManager, BookRepository bookRepository,
      AssetRepository assetRepository, StudyRepository studyRepository) {
    return new ReviewDashboardViewModel(repository, dataStoreManager, bookRepository, assetRepository, studyRepository);
  }
}
