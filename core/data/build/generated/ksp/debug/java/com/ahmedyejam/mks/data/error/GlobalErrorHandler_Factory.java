package com.ahmedyejam.mks.data.error;

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
public final class GlobalErrorHandler_Factory implements Factory<GlobalErrorHandler> {
  @Override
  public GlobalErrorHandler get() {
    return newInstance();
  }

  public static GlobalErrorHandler_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GlobalErrorHandler newInstance() {
    return new GlobalErrorHandler();
  }

  private static final class InstanceHolder {
    static final GlobalErrorHandler_Factory INSTANCE = new GlobalErrorHandler_Factory();
  }
}
