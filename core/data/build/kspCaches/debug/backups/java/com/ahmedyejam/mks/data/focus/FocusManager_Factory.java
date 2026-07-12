package com.ahmedyejam.mks.data.focus;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FocusManager_Factory implements Factory<FocusManager> {
  private final Provider<Context> contextProvider;

  private FocusManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FocusManager get() {
    return newInstance(contextProvider.get());
  }

  public static FocusManager_Factory create(Provider<Context> contextProvider) {
    return new FocusManager_Factory(contextProvider);
  }

  public static FocusManager newInstance(Context context) {
    return new FocusManager(context);
  }
}
