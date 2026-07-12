package com.ahmedyejam.mks.data.preview;

import com.ahmedyejam.mks.data.local.dao.BookDao;
import com.ahmedyejam.mks.data.local.dao.QuestionDao;
import com.ahmedyejam.mks.data.local.dao.QuizDao;
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
public final class DeletePreviewService_Factory implements Factory<DeletePreviewService> {
  private final Provider<BookDao> bookDaoProvider;

  private final Provider<QuizDao> quizDaoProvider;

  private final Provider<QuestionDao> questionDaoProvider;

  private DeletePreviewService_Factory(Provider<BookDao> bookDaoProvider,
      Provider<QuizDao> quizDaoProvider, Provider<QuestionDao> questionDaoProvider) {
    this.bookDaoProvider = bookDaoProvider;
    this.quizDaoProvider = quizDaoProvider;
    this.questionDaoProvider = questionDaoProvider;
  }

  @Override
  public DeletePreviewService get() {
    return newInstance(bookDaoProvider.get(), quizDaoProvider.get(), questionDaoProvider.get());
  }

  public static DeletePreviewService_Factory create(Provider<BookDao> bookDaoProvider,
      Provider<QuizDao> quizDaoProvider, Provider<QuestionDao> questionDaoProvider) {
    return new DeletePreviewService_Factory(bookDaoProvider, quizDaoProvider, questionDaoProvider);
  }

  public static DeletePreviewService newInstance(BookDao bookDao, QuizDao quizDao,
      QuestionDao questionDao) {
    return new DeletePreviewService(bookDao, quizDao, questionDao);
  }
}
