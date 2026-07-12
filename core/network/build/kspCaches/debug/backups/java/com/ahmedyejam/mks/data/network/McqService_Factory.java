package com.ahmedyejam.mks.data.network;

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
public final class McqService_Factory implements Factory<McqService> {
  private final Provider<AiClient> aiClientProvider;

  private McqService_Factory(Provider<AiClient> aiClientProvider) {
    this.aiClientProvider = aiClientProvider;
  }

  @Override
  public McqService get() {
    return newInstance(aiClientProvider.get());
  }

  public static McqService_Factory create(Provider<AiClient> aiClientProvider) {
    return new McqService_Factory(aiClientProvider);
  }

  public static McqService newInstance(AiClient aiClient) {
    return new McqService(aiClient);
  }
}
