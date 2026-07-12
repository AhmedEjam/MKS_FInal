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
public final class OcrService_Factory implements Factory<OcrService> {
  private final Provider<AiClient> aiClientProvider;

  private OcrService_Factory(Provider<AiClient> aiClientProvider) {
    this.aiClientProvider = aiClientProvider;
  }

  @Override
  public OcrService get() {
    return newInstance(aiClientProvider.get());
  }

  public static OcrService_Factory create(Provider<AiClient> aiClientProvider) {
    return new OcrService_Factory(aiClientProvider);
  }

  public static OcrService newInstance(AiClient aiClient) {
    return new OcrService(aiClient);
  }
}
