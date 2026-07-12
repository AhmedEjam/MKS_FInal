package com.ahmedyejam.mks.ui.quiz;

import android.content.Context;
import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
import com.ahmedyejam.mks.data.repository.QuizRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class CompilerViewModel_Factory implements Factory<CompilerViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private CompilerViewModel_Factory(Provider<Context> contextProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    this.contextProvider = contextProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
  }

  @Override
  public CompilerViewModel get() {
    return newInstance(contextProvider.get(), knowledgeRepositoryProvider.get(), quizRepositoryProvider.get(), dataStoreManagerProvider.get());
  }

  public static CompilerViewModel_Factory create(Provider<Context> contextProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider) {
    return new CompilerViewModel_Factory(contextProvider, knowledgeRepositoryProvider, quizRepositoryProvider, dataStoreManagerProvider);
  }

  public static CompilerViewModel newInstance(Context context,
      KnowledgeRepository knowledgeRepository, QuizRepository quizRepository,
      DataStoreManager dataStoreManager) {
    return new CompilerViewModel(context, knowledgeRepository, quizRepository, dataStoreManager);
  }
}
