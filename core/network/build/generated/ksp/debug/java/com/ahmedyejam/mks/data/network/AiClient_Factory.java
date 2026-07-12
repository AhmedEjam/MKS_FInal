package com.ahmedyejam.mks.data.network;

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
public final class AiClient_Factory implements Factory<AiClient> {
  @Override
  public AiClient get() {
    return newInstance();
  }

  public static AiClient_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AiClient newInstance() {
    return new AiClient();
  }

  private static final class InstanceHolder {
    static final AiClient_Factory INSTANCE = new AiClient_Factory();
  }
}
