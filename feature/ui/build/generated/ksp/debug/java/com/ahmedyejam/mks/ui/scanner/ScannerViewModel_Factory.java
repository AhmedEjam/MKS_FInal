package com.ahmedyejam.mks.ui.scanner;

import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
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
public final class ScannerViewModel_Factory implements Factory<ScannerViewModel> {
  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private ScannerViewModel_Factory(Provider<KnowledgeRepository> knowledgeRepositoryProvider) {
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
  }

  @Override
  public ScannerViewModel get() {
    return newInstance(knowledgeRepositoryProvider.get());
  }

  public static ScannerViewModel_Factory create(
      Provider<KnowledgeRepository> knowledgeRepositoryProvider) {
    return new ScannerViewModel_Factory(knowledgeRepositoryProvider);
  }

  public static ScannerViewModel newInstance(KnowledgeRepository knowledgeRepository) {
    return new ScannerViewModel(knowledgeRepository);
  }
}
