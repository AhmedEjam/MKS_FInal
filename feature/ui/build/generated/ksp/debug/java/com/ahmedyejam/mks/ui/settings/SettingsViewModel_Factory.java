package com.ahmedyejam.mks.ui.settings;

import com.ahmedyejam.mks.data.focus.FocusManager;
import com.ahmedyejam.mks.data.network.AiClient;
import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.ExportManager;
import com.ahmedyejam.mks.data.repository.OllamaRepository;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<ExportManager> exportManagerProvider;

  private final Provider<WorkspaceRepository> workspaceRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<OllamaRepository> ollamaRepositoryProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<FocusManager> focusManagerProvider;

  private final Provider<AiClient> aiClientProvider;

  private SettingsViewModel_Factory(Provider<ExportManager> exportManagerProvider,
      Provider<WorkspaceRepository> workspaceRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<OllamaRepository> ollamaRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FocusManager> focusManagerProvider, Provider<AiClient> aiClientProvider) {
    this.exportManagerProvider = exportManagerProvider;
    this.workspaceRepositoryProvider = workspaceRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.ollamaRepositoryProvider = ollamaRepositoryProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.focusManagerProvider = focusManagerProvider;
    this.aiClientProvider = aiClientProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(exportManagerProvider.get(), workspaceRepositoryProvider.get(), quizRepositoryProvider.get(), assetRepositoryProvider.get(), ollamaRepositoryProvider.get(), dataStoreManagerProvider.get(), focusManagerProvider.get(), aiClientProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<ExportManager> exportManagerProvider,
      Provider<WorkspaceRepository> workspaceRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<OllamaRepository> ollamaRepositoryProvider,
      Provider<DataStoreManager> dataStoreManagerProvider,
      Provider<FocusManager> focusManagerProvider, Provider<AiClient> aiClientProvider) {
    return new SettingsViewModel_Factory(exportManagerProvider, workspaceRepositoryProvider, quizRepositoryProvider, assetRepositoryProvider, ollamaRepositoryProvider, dataStoreManagerProvider, focusManagerProvider, aiClientProvider);
  }

  public static SettingsViewModel newInstance(ExportManager exportManager,
      WorkspaceRepository workspaceRepository, QuizRepository quizRepository,
      AssetRepository assetRepository, OllamaRepository ollamaRepository,
      DataStoreManager dataStoreManager, FocusManager focusManager, AiClient aiClient) {
    return new SettingsViewModel(exportManager, workspaceRepository, quizRepository, assetRepository, ollamaRepository, dataStoreManager, focusManager, aiClient);
  }
}
