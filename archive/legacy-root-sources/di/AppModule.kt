package com.ahmedyejam.mks.di

import android.content.Context
import androidx.room.Room
import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.preferences.DataStoreManager
import com.ahmedyejam.mks.data.import.mapping.LibraryMapper
import com.ahmedyejam.mks.data.import.repository.ImportLibraryManager
import com.ahmedyejam.mks.data.repository.ExportManager
import com.ahmedyejam.mks.data.repository.MksRepository
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuestionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppModule(val context: Context) {
    
    val database: MksDatabase by lazy {
        Room.databaseBuilder(
            context,
            MksDatabase::class.java,
            MksDatabase.DATABASE_NAME
        )
        .addMigrations(
            MksDatabase.MIGRATION_1_2,
            MksDatabase.MIGRATION_2_3,
            MksDatabase.MIGRATION_3_4,
            MksDatabase.MIGRATION_4_5,
            MksDatabase.MIGRATION_5_6,
            MksDatabase.MIGRATION_6_7,
            MksDatabase.MIGRATION_7_8,
            MksDatabase.MIGRATION_8_9,
            MksDatabase.MIGRATION_9_10,
            MksDatabase.MIGRATION_10_11
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed database with sample data on first run
                CoroutineScope(Dispatchers.IO).launch {
                    repository.insertBook(
                        BookEntity(
                            externalId = "book_sample",
                            title = "MKS Sample Book",
                            description = "This is a sample book with some initial content to get you started."
                        )
                    )
                    seedDatabase()
                }
            }
        })
        .build()
    }

    private suspend fun seedDatabase() {
        val book = repository.getAllBooks().first().find { it.externalId == "book_sample" } ?: return
        val bookId = book.id

        val quizId = repository.insertQuiz(
            QuizEntity(
                externalId = "quiz_sample",
                bookId = bookId,
                title = "General Knowledge",
                description = "A basic quiz to test the app features.",
                category = "General"
            )
        )

        repository.insertQuestions(
            listOf(
                QuestionEntity(
                    externalId = "q_sample_1",
                    quizId = quizId,
                    text = "What is the capital of France?",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("London", "Berlin", "Paris", "Madrid"),
                    correctAnswers = listOf(2), // Paris
                    explanation = "Paris is the capital and most populous city of France.",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_2",
                    quizId = quizId,
                    text = "Which of these are programming languages?",
                    type = QuestionType.MULTIPLE_CHOICE,
                    options = listOf("Kotlin", "HTML", "Python", "CSS"),
                    correctAnswers = listOf(0, 2), // Kotlin, Python
                    explanation = "Kotlin and Python are general-purpose programming languages.",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_3",
                    quizId = quizId,
                    text = "What is this famous landmark?",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Eiffel Tower", "Colosseum", "Great Wall", "Machu Picchu"),
                    correctAnswers = listOf(1), // Colosseum
                    explanation = "The Colosseum is an oval amphitheatre in the centre of the city of Rome, Italy.",
                    imagePath = "https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_4",
                    quizId = quizId,
                    text = "Identify the celestial body in the image.",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Mars", "Jupiter", "Saturn", "Venus"),
                    correctAnswers = listOf(2), // Saturn
                    explanation = "Saturn is the sixth planet from the Sun and the second-largest in the Solar System, after Jupiter.",
                    imagePath = "https://images.unsplash.com/photo-1614732414444-096e5f1122d5?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_5",
                    quizId = quizId,
                    text = "What is the primary ingredient in this dish?",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Rice", "Pasta", "Quinoa", "Couscous"),
                    correctAnswers = listOf(1), // Pasta
                    explanation = "This is a classic Italian pasta dish.",
                    imagePath = "https://images.unsplash.com/photo-1473093226795-af9932fe5856?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_6",
                    quizId = quizId,
                    text = "Which animal is shown in this picture?",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Wolf", "Husky", "Fox", "Coyote"),
                    correctAnswers = listOf(1), // Husky
                    explanation = "The Siberian Husky is a medium-sized working dog breed.",
                    imagePath = "https://images.unsplash.com/photo-1517849845537-4d257902454a?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_7",
                    quizId = quizId,
                    text = "Identify the city skyline in this photograph.",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Tokyo", "New York City", "London", "Dubai"),
                    correctAnswers = listOf(1), // New York City
                    explanation = "This is the iconic skyline of Manhattan, New York City.",
                    imagePath = "https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_8",
                    quizId = quizId,
                    text = "Confirm Local Image Loading",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Visible", "Not Visible"),
                    correctAnswers = listOf(0),
                    explanation = "This question tests the app's ability to load a file from the device's public storage. Ensure 'test_image.jpg' exists in your Download folder.",
                    imagePath = "/storage/emulated/0/Download/test_image.jpg",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_9",
                    quizId = quizId,
                    text = "Which planet is known as the 'Red Planet'?",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Venus", "Mars", "Saturn", "Mercury"),
                    correctAnswers = listOf(1), // Mars
                    explanation = "Mars is often called the 'Red Planet' because of iron oxide (rust) on its surface.",
                    imagePath = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_10",
                    quizId = quizId,
                    text = "Identify this musical instrument.",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Cello", "Violin", "Double Bass", "Viola"),
                    correctAnswers = listOf(0), // Cello
                    explanation = "The cello is a bowed string instrument of the violin family.",
                    imagePath = "https://images.unsplash.com/photo-1552422535-c45813c61732?w=800&q=80",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_11",
                    quizId = quizId,
                    text = "The Great Wall of China is visible from the Moon with the naked eye.",
                    type = QuestionType.BOOLEAN,
                    options = listOf("True", "False"),
                    correctAnswers = listOf(1), // False
                    explanation = "The Great Wall is generally not visible from the moon without aid, though it can be seen from low Earth orbit.",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_12",
                    quizId = quizId,
                    text = "This is a very long question text designed to test how the UI handles multiple lines and scrolling behavior when combined with many options. It should wrap correctly and not push the options off the screen in a way that makes them inaccessible. What is the most common gas in Earth's atmosphere?",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Nitrogen", "Oxygen", "Argon", "Carbon Dioxide", "Neon", "Helium", "Methane", "Krypton"),
                    correctAnswers = listOf(0), // Nitrogen
                    explanation = "Nitrogen makes up about 78% of Earth's atmosphere, followed by oxygen at about 21%.",
                    weight = 1
                ),
                QuestionEntity(
                    externalId = "q_sample_13",
                    quizId = quizId,
                    text = "Identify the celestial object in this deep space image.",
                    type = QuestionType.SINGLE_CHOICE,
                    options = listOf("Andromeda Galaxy", "Orion Nebula", "Pillars of Creation", "Black Hole"),
                    correctAnswers = listOf(2), // Pillars of Creation
                    explanation = "The Pillars of Creation is a photograph taken by the Hubble Space Telescope of elephant trunks of interstellar gas and dust in the Eagle Nebula.",
                    imagePath = "https://images.unsplash.com/photo-1614728894747-a83421e2b9c9?w=800&q=80",
                    weight = 1
                )
            )
        )
    }

    val fileManager: com.ahmedyejam.mks.data.local.FileManager by lazy {
        com.ahmedyejam.mks.data.local.FileManager(context)
    }

    val libraryMapper: LibraryMapper by lazy {
        LibraryMapper()
    }

    val exportManager: ExportManager by lazy {
        ExportManager(
            database.bookDao(),
            database.quizDao(),
            database.questionDao(),
            database.sessionDao(),
            database.categoryMetadataDao(),
            fileManager,
            libraryMapper
        )
    }

    val importManager: ImportLibraryManager by lazy {
        ImportLibraryManager(
            context,
            database,
            fileManager
        )
    }

    val repository: MksRepository by lazy {
        MksRepository(
            database.bookDao(),
            database.quizDao(),
            database.questionDao(),
            database.sessionDao(),
            database.categoryMetadataDao(),
            database,
            fileManager,
            exportManager,
            importManager
        )
    }

    val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(context)
    }

    val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * Completely wipes the database and re-seeds it.
     * Use with caution.
     */
    fun resetDatabase() {
        applicationScope.launch(Dispatchers.IO) {
            database.clearAllTables()
            seedDatabase()
        }
    }
}
