package com.ahmedyejam.mks.ui.booktools;

import android.content.Context;
import com.ahmedyejam.mks.data.network.AiClient;
import com.ahmedyejam.mks.data.network.OcrService;
import com.ahmedyejam.mks.data.network.PdfRendererService;
import com.ahmedyejam.mks.data.network.PdfTextExtractor;
import com.ahmedyejam.mks.data.preferences.DataStoreManager;
import com.ahmedyejam.mks.data.repository.AssetRepository;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
import com.ahmedyejam.mks.data.repository.OllamaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class PdfExtractionViewModel_Factory implements Factory<PdfExtractionViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<AssetRepository> assetRepositoryProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private final Provider<PdfRendererService> pdfRendererServiceProvider;

  private final Provider<PdfTextExtractor> pdfTextExtractorProvider;

  private final Provider<OcrService> ocrServiceProvider;

  private final Provider<DataStoreManager> dataStoreManagerProvider;

  private final Provider<AiClient> aiClientProvider;

  private final Provider<OllamaRepository> ollamaRepositoryProvider;

  private PdfExtractionViewModel_Factory(Provider<Context> contextProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<PdfRendererService> pdfRendererServiceProvider,
      Provider<PdfTextExtractor> pdfTextExtractorProvider, Provider<OcrService> ocrServiceProvider,
      Provider<DataStoreManager> dataStoreManagerProvider, Provider<AiClient> aiClientProvider,
      Provider<OllamaRepository> ollamaRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.assetRepositoryProvider = assetRepositoryProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
    this.pdfRendererServiceProvider = pdfRendererServiceProvider;
    this.pdfTextExtractorProvider = pdfTextExtractorProvider;
    this.ocrServiceProvider = ocrServiceProvider;
    this.dataStoreManagerProvider = dataStoreManagerProvider;
    this.aiClientProvider = aiClientProvider;
    this.ollamaRepositoryProvider = ollamaRepositoryProvider;
  }

  @Override
  public PdfExtractionViewModel get() {
    return newInstance(contextProvider.get(), assetRepositoryProvider.get(), knowledgeRepositoryProvider.get(), pdfRendererServiceProvider.get(), pdfTextExtractorProvider.get(), ocrServiceProvider.get(), dataStoreManagerProvider.get(), aiClientProvider.get(), ollamaRepositoryProvider.get());
  }

  public static PdfExtractionViewModel_Factory create(Provider<Context> contextProvider,
      Provider<AssetRepository> assetRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider,
      Provider<PdfRendererService> pdfRendererServiceProvider,
      Provider<PdfTextExtractor> pdfTextExtractorProvider, Provider<OcrService> ocrServiceProvider,
      Provider<DataStoreManager> dataStoreManagerProvider, Provider<AiClient> aiClientProvider,
      Provider<OllamaRepository> ollamaRepositoryProvider) {
    return new PdfExtractionViewModel_Factory(contextProvider, assetRepositoryProvider, knowledgeRepositoryProvider, pdfRendererServiceProvider, pdfTextExtractorProvider, ocrServiceProvider, dataStoreManagerProvider, aiClientProvider, ollamaRepositoryProvider);
  }

  public static PdfExtractionViewModel newInstance(Context context, AssetRepository assetRepository,
      KnowledgeRepository knowledgeRepository, PdfRendererService pdfRendererService,
      PdfTextExtractor pdfTextExtractor, OcrService ocrService, DataStoreManager dataStoreManager,
      AiClient aiClient, OllamaRepository ollamaRepository) {
    return new PdfExtractionViewModel(context, assetRepository, knowledgeRepository, pdfRendererService, pdfTextExtractor, ocrService, dataStoreManager, aiClient, ollamaRepository);
  }
}
