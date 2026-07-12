package com.ahmedyejam.mks.data.seeder;

import com.ahmedyejam.mks.data.local.MksDatabase;
import com.ahmedyejam.mks.data.repository.BookRepository;
import com.ahmedyejam.mks.data.repository.KnowledgeRepository;
import com.ahmedyejam.mks.data.repository.QuizRepository;
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
public final class MksDatabaseSeeder_Factory implements Factory<MksDatabaseSeeder> {
  private final Provider<MksDatabase> databaseProvider;

  private final Provider<BookRepository> bookRepositoryProvider;

  private final Provider<QuizRepository> quizRepositoryProvider;

  private final Provider<KnowledgeRepository> knowledgeRepositoryProvider;

  private MksDatabaseSeeder_Factory(Provider<MksDatabase> databaseProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider) {
    this.databaseProvider = databaseProvider;
    this.bookRepositoryProvider = bookRepositoryProvider;
    this.quizRepositoryProvider = quizRepositoryProvider;
    this.knowledgeRepositoryProvider = knowledgeRepositoryProvider;
  }

  @Override
  public MksDatabaseSeeder get() {
    return newInstance(databaseProvider.get(), bookRepositoryProvider.get(), quizRepositoryProvider.get(), knowledgeRepositoryProvider.get());
  }

  public static MksDatabaseSeeder_Factory create(Provider<MksDatabase> databaseProvider,
      Provider<BookRepository> bookRepositoryProvider,
      Provider<QuizRepository> quizRepositoryProvider,
      Provider<KnowledgeRepository> knowledgeRepositoryProvider) {
    return new MksDatabaseSeeder_Factory(databaseProvider, bookRepositoryProvider, quizRepositoryProvider, knowledgeRepositoryProvider);
  }

  public static MksDatabaseSeeder newInstance(MksDatabase database, BookRepository bookRepository,
      QuizRepository quizRepository, KnowledgeRepository knowledgeRepository) {
    return new MksDatabaseSeeder(database, bookRepository, quizRepository, knowledgeRepository);
  }
}
