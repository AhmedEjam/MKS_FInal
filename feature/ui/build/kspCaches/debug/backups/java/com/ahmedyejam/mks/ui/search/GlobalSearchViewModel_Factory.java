package com.ahmedyejam.mks.ui.search;

import com.ahmedyejam.mks.data.search.GlobalSearchRepository;
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
public final class GlobalSearchViewModel_Factory implements Factory<GlobalSearchViewModel> {
  private final Provider<GlobalSearchRepository> repositoryProvider;

  private GlobalSearchViewModel_Factory(Provider<GlobalSearchRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GlobalSearchViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static GlobalSearchViewModel_Factory create(
      Provider<GlobalSearchRepository> repositoryProvider) {
    return new GlobalSearchViewModel_Factory(repositoryProvider);
  }

  public static GlobalSearchViewModel newInstance(GlobalSearchRepository repository) {
    return new GlobalSearchViewModel(repository);
  }
}
