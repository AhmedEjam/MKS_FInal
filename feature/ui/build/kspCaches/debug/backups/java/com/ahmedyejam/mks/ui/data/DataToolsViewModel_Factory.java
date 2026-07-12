package com.ahmedyejam.mks.ui.data;

import com.ahmedyejam.mks.data.repository.ExportManager;
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
public final class DataToolsViewModel_Factory implements Factory<DataToolsViewModel> {
  private final Provider<ExportManager> exportManagerProvider;

  private DataToolsViewModel_Factory(Provider<ExportManager> exportManagerProvider) {
    this.exportManagerProvider = exportManagerProvider;
  }

  @Override
  public DataToolsViewModel get() {
    return newInstance(exportManagerProvider.get());
  }

  public static DataToolsViewModel_Factory create(Provider<ExportManager> exportManagerProvider) {
    return new DataToolsViewModel_Factory(exportManagerProvider);
  }

  public static DataToolsViewModel newInstance(ExportManager exportManager) {
    return new DataToolsViewModel(exportManager);
  }
}
