package com.ahmedyejam.mks.data.network;

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
public final class PdfTextExtractor_Factory implements Factory<PdfTextExtractor> {
  private final Provider<Context> contextProvider;

  private PdfTextExtractor_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PdfTextExtractor get() {
    return newInstance(contextProvider.get());
  }

  public static PdfTextExtractor_Factory create(Provider<Context> contextProvider) {
    return new PdfTextExtractor_Factory(contextProvider);
  }

  public static PdfTextExtractor newInstance(Context context) {
    return new PdfTextExtractor(context);
  }
}
