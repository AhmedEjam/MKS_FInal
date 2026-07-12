package com.ahmedyejam.mks.ui.booktools;

import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AiMcqRepository;
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
public final class AiMcqGeneratorViewModel_Factory implements Factory<AiMcqGeneratorViewModel> {
  private final Provider<AiMcqRepository> aiMcqRepositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private AiMcqGeneratorViewModel_Factory(Provider<AiMcqRepository> aiMcqRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    this.aiMcqRepositoryProvider = aiMcqRepositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public AiMcqGeneratorViewModel get() {
    return newInstance(aiMcqRepositoryProvider.get(), dataStoreManagerProvider.get());
  }

  public static AiMcqGeneratorViewModel_Factory create(
      Provider<AiMcqRepository> aiMcqRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new AiMcqGeneratorViewModel_Factory(aiMcqRepositoryProvider, dataStoreManagerProvider);
  }

  public static AiMcqGeneratorViewModel newInstance(AiMcqRepository aiMcqRepository,
      DataStoreManager dataStoreManager) {
    return new AiMcqGeneratorViewModel(aiMcqRepository, dataStoreManager);
  }
}
