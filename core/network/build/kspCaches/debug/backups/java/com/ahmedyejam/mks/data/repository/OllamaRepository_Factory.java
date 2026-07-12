package com.ahmedyejam.mks.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class OllamaRepository_Factory implements Factory<OllamaRepository> {
  @Override
  public OllamaRepository get() {
    return newInstance();
  }

  public static OllamaRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OllamaRepository newInstance() {
    return new OllamaRepository();
  }

  private static final class InstanceHolder {
    static final OllamaRepository_Factory INSTANCE = new OllamaRepository_Factory();
  }
}
