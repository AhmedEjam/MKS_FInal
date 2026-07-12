package com.ahmedyejam.mks.data.repository;

import com.ahmedyejam.mks.data.network.McqService;
import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AiMcqRepository_Factory implements Factory<AiMcqRepository> {
  private final Provider<McqService> mcqServiceProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private AiMcqRepository_Factory(Provider<McqService> mcqServiceProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    this.mcqServiceProvider = mcqServiceProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public AiMcqRepository get() {
    return newInstance(mcqServiceProvider.get(), quizRepositoryProvider.get(), dataStoreManagerProvider.get());
  }

  public static AiMcqRepository_Factory create(Provider<McqService> mcqServiceProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new AiMcqRepository_Factory(mcqServiceProvider, quizRepositoryProvider, dataStoreManagerProvider);
  }

  public static AiMcqRepository newInstance(McqService mcqService, QuizRepository quizRepository,
      DataStoreManager dataStoreManager) {
    return new AiMcqRepository(mcqService, quizRepository, dataStoreManager);
  }
}
