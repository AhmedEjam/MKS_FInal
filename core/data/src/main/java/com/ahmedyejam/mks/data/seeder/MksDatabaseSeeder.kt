package com.ahmedyejam.mks.data.seeder

import com.ahmedyejam.mks.data.local.MksDatabase
import com.ahmedyejam.mks.data.local.entity.BookEntity
import com.ahmedyejam.mks.data.local.entity.QuestionEntity
import com.ahmedyejam.mks.data.local.entity.QuestionType
import com.ahmedyejam.mks.data.local.entity.QuizEntity
import com.ahmedyejam.mks.data.repository.BookRepository
import com.ahmedyejam.mks.data.repository.KnowledgeRepository
import com.ahmedyejam.mks.data.repository.QuizRepository
import com.ahmedyejam.mks.util.MksLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MksDatabaseSeeder
    @Inject
    constructor(
        private val database: MksDatabase,
        private val bookRepository: BookRepository,
        private val quizRepository: QuizRepository,
        private val knowledgeRepository: KnowledgeRepository,
    ) {
    private val TAG = "MksDatabaseSeeder"

        suspend fun seedDatabase(workspaceId: Long) {
            MksLogger.i(TAG, "Starting database seeding for workspace $workspaceId")
            try {
                val gkBookId = getOrCreateBook(
                    workspaceId = workspaceId,
                    externalId = "book_general_knowledge",
                    title = "General knowledge & معلومات عامة",
                    description = "A collection of general knowledge quizzes and flashcards.",
                    fields = listOf("General Knowledge", "Sample"),
                    iconName = "🌍",
                )
                MksLogger.i(TAG, "General knowledge book ID: $gkBookId")

                val tutorialBookId = getOrCreateBook(
                    workspaceId = workspaceId,
                    externalId = "book_how_to_start",
                    title = "How to start & كيف تبدأ",
                    description = "Tutorials to get you started with the MKS app.",
                    fields = listOf("Tutorial", "Help"),
                    iconName = "🚀",
                )
                MksLogger.i(TAG, "Tutorial book ID: $tutorialBookId")

                // We seed each component independently so a failure in one doesn't block the rest.
                // Each stage is idempotent: it checks for existing data by externalId before creating.
                seedEnglishQuiz(gkBookId)
                seedArabicQuiz(gkBookId)
                seedEnglishTutorial(tutorialBookId)
                seedArabicTutorial(tutorialBookId)
                seedKnowledgeAssets(gkBookId)

                MksLogger.i(TAG, "Database seeding process completed.")
            } catch (e: Exception) {
                MksLogger.e(TAG, "Critical failure during global database seeding", e)
            }
        }

        private suspend fun getOrCreateBook(
            workspaceId: Long,
            externalId: String,
            title: String,
            description: String,
            fields: List<String>,
            iconName: String,
        ): Long {
            val existing = database.bookDao().getBookByExternalId(externalId)
            if (existing != null) {
                MksLogger.i(TAG, "Book '$externalId' already exists with ID ${existing.id}. Reusing.")
                return existing.id
            }
            val id = bookRepository.insertBook(
                BookEntity(
                    workspaceId = workspaceId,
                    externalId = externalId,
                    title = title,
                    description = description,
                    fields = fields,
                    iconName = iconName,
                ),
            )
            MksLogger.i(TAG, "Book '$externalId' created with ID: $id")
            return id
        }

    private suspend fun seedEnglishQuiz(bookId: Long) {
        MksLogger.i(TAG, "[Stage] English General Knowledge Quiz")
        try {
            val externalId = "5cfcd20f-b26d-46a5-86a3-1576bdc83ff7"
            val existing = database.quizDao().getQuizByExternalId(externalId)
            if (existing != null) {
                MksLogger.i(TAG, "English Quiz already exists (ID: ${existing.id}). Skipping.")
                return
            }
            val quizId = quizRepository.insertQuiz(
                    QuizEntity(
                        externalId = externalId,
                        bookId = bookId,
                        title = "Sample quiz",
                        description = "25 General Knowledge Questions",
                        iconName = "🇺🇸",
                        category = "General",
                    )
                )
            quizRepository.insertQuestions(getEnglishQuestions(quizId))
            val verifyCount = database.questionDao().getQuestionsByQuizIdNow(quizId).size
            MksLogger.i(
                TAG,
                "VERIFICATION: English Quiz seeded with $verifyCount questions in the database."
            )
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to seed English Quiz", e)
        }
    }

    private suspend fun seedArabicQuiz(bookId: Long) {
        MksLogger.i(TAG, "[Stage] Arabic General Knowledge Quiz")
        try {
            val externalId = "16a396a8-d068-4305-9d4a-97f2577079d9"
            val existing = database.quizDao().getQuizByExternalId(externalId)
            if (existing != null) {
                MksLogger.i(TAG, "Arabic Quiz already exists (ID: ${existing.id}). Skipping.")
                return
            }
            val quizId = quizRepository.insertQuiz(
                QuizEntity(
                    externalId = externalId,
                    bookId = bookId,
                    title = "نموذج اختبار",
                    description = "٢٥ سؤال في المعلومات العامة",
                    iconName = "🇮🇶",
                    category = "General",
                )
            )
            quizRepository.insertQuestions(getArabicQuestions(quizId))
            val verifyCount = database.questionDao().getQuestionsByQuizIdNow(quizId).size
            MksLogger.i(
                TAG,
                "VERIFICATION: Arabic Quiz seeded with $verifyCount questions in the database."
            )
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to seed Arabic Quiz", e)
        }
    }

    private suspend fun seedEnglishTutorial(bookId: Long) {
        MksLogger.i(TAG, "[Stage] English Tutorial Quiz")
        try {
            val externalId = "how-to-start-en"
            val existing = database.quizDao().getQuizByExternalId(externalId)
            if (existing != null) {
                MksLogger.i(TAG, "English Tutorial already exists (ID: ${existing.id}). Skipping.")
                return
            }
            val quizId = quizRepository.insertQuiz(
                QuizEntity(
                    externalId = externalId,
                    bookId = bookId,
                    title = "How to start",
                    description = "Learn how to use the MKS app",
                    iconName = "🚀",
                    category = "Tutorial",
                )
            )
            quizRepository.insertQuestions(getEnglishTutorialQuestions(quizId))
            val verifyCount = database.questionDao().getQuestionsByQuizIdNow(quizId).size
            MksLogger.i(
                TAG,
                "VERIFICATION: English Tutorial seeded with $verifyCount questions in the database."
            )
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to seed English Tutorial", e)
        }
    }

    private suspend fun seedArabicTutorial(bookId: Long) {
        MksLogger.i(TAG, "[Stage] Arabic Tutorial Quiz")
        try {
            val externalId = "how-to-start-ar"
            val existing = database.quizDao().getQuizByExternalId(externalId)
            if (existing != null) {
                MksLogger.i(TAG, "Arabic Tutorial already exists (ID: ${existing.id}). Skipping.")
                return
            }
            val quizId = quizRepository.insertQuiz(
                QuizEntity(
                    externalId = externalId,
                    bookId = bookId,
                    title = "كيف تبدأ",
                    description = "تعلم كيفية استخدام تطبيق MKS",
                    iconName = "💡",
                    category = "Tutorial",
                )
            )
            quizRepository.insertQuestions(getArabicTutorialQuestions(quizId))
            val verifyCount = database.questionDao().getQuestionsByQuizIdNow(quizId).size
            MksLogger.i(
                TAG,
                "VERIFICATION: Arabic Tutorial seeded with $verifyCount questions in the database."
            )
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to seed Arabic Tutorial", e)
        }
    }

    private suspend fun seedKnowledgeAssets(bookId: Long) {
        MksLogger.i(TAG, "[Stage] Knowledge Bank Assets")
        try {
            val deckExternalId = "sample_flashcard_deck"
            val existingDeck = database.flashcardDeckDao().getFlashcardDeckByExternalId(deckExternalId)
            if (existingDeck != null) {
                MksLogger.i(TAG, "Knowledge assets already seeded (deck ID: ${existingDeck.id}). Skipping.")
                return
            }

            val deckId = knowledgeRepository.insertFlashcardDeck(
                com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity(
                    bookId = bookId,
                    externalId = "sample_flashcard_deck",
                    title = "Basic Concepts Flashcards",
                    description = "Key facts from the General Knowledge quiz."
                )
            )

            knowledgeRepository.insertFlashcards(
                listOf(
                    com.ahmedyejam.mks.data.local.entity.FlashcardEntity(
                        deckId = deckId,
                        externalId = "card_mars",
                        frontText = "Which planet is known as the Red Planet?",
                        backText = "Mars",
                        hint = "4th planet from the sun.",
                        orderIndex = 0
                    ),
                    com.ahmedyejam.mks.data.local.entity.FlashcardEntity(
                        deckId = deckId,
                        externalId = "card_linux",
                        frontText = "Core component of a Linux OS?",
                        backText = "Kernel",
                        hint = "Starts with K.",
                        orderIndex = 1
                    )
                )
            )

            val courseId = knowledgeRepository.insertSlideshowCourse(
                com.ahmedyejam.mks.data.local.entity.SlideshowCourseEntity(
                    bookId = bookId,
                    externalId = "sample_slideshow_course",
                    title = "Introduction to Science & History",
                    description = "A visual guide to the sample questions."
                )
            )

            knowledgeRepository.insertCourseSlide(
                com.ahmedyejam.mks.data.local.entity.CourseSlideEntity(
                    courseId = courseId,
                    externalId = "slide_intro",
                    title = "Welcome to MKS",
                    body = "This slideshow demonstrates how you can learn visually.",
                    orderIndex = 0
                )
            )

            knowledgeRepository.insertNoteBlueprint(
                com.ahmedyejam.mks.data.local.entity.NoteBlueprintEntity(
                    collectionId = knowledgeRepository.getOrCreateDefaultNoteCollection(bookId),
                    externalId = "sample_note_blueprint",
                    title = "Study Note: Solar System",
                    summary = "A brief overview of planetary facts.",
                    body = "The Solar System consists of the Sun and everything that orbits it, including eight planets. Mars is often called the Red Planet due to iron oxide on its surface.",
                    bulletPoints = listOf(
                        "Mars is the 4th planet",
                        "Jupiter is the largest",
                        "Venus is the hottest"
                    ),
                    tags = listOf("Science", "Space"),
                    blueprintMode = com.ahmedyejam.mks.data.local.entity.BlueprintMode.CONCEPT_TEMPLATE
                )
            )

            MksLogger.i(TAG, "Knowledge Assets seeded successfully.")
        } catch (e: Exception) {
            MksLogger.e(TAG, "Failed to seed Knowledge Assets", e)
        }
    }

    private fun getEnglishQuestions(quizId: Long): List<QuestionEntity> = listOf(
        QuestionEntity(
            externalId = "166e05fb-147e-4978-880e-97c4a6de8a88",
            quizId = quizId,
            text = "Which planet is known as the Red Planet?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Jupiter", "Mars", "Venus", "Saturn"),
            correctAnswers = listOf(1),
            explanation = "Its surface is covered in iron oxide.",
            hint = "It's the 4th planet from the sun.",
            categories = listOf("Samples"),
            additionalInfo = "It's the 4th planet from the sun.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "5b30ec00-0f86-4310-9aff-14719c81a11d",
            quizId = quizId,
            text = "What is the core component of a Linux operating system called?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("GUI", "Kernel", "Shell", "Terminal"),
            correctAnswers = listOf(1),
            explanation = "It manages the system's resources completely.",
            hint = "Starts with the letter 'K'.",
            categories = listOf("Samples"),
            additionalInfo = "Starts with the letter 'K'.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "72d2156b-4e74-430e-8ef8-f0bc4ad31727",
            quizId = quizId,
            text = "This ancient blue-glazed brick gate was the main entrance to which city?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Sumer", "Babylon", "Ur", "Nineveh"),
            correctAnswers = listOf(1),
            explanation = "Known as the Ishtar Gate in ancient Iraq.",
            hint = "Near the city of Hillah.",
            categories = listOf("Samples"),
            additionalInfo = "Near the city of Hillah.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "6092f197-f30e-49bb-bb4d-e99bcabc3930",
            quizId = quizId,
            text = "What two power sources are used in a Hybrid Electric Vehicle (HEV)?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Internal combustion & Electric",
                "Solar & Electric",
                "Hydrogen & Diesel",
                "Electric & Wind"
            ),
            correctAnswers = listOf(0),
            explanation = "Uses both fuel and a battery to provide power.",
            hint = "It is a mix of two systems.",
            categories = listOf("Samples"),
            additionalInfo = "It is a mix of two systems.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "e7ffd9c7-4842-49bf-8ce8-e003174eb054",
            quizId = quizId,
            text = "Who wrote the famous dystopian novel '1984'?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Ernest Hemingway", "George Orwell", "Mark Twain", "Charles Dickens"),
            correctAnswers = listOf(1),
            explanation = "A famous novel discussing totalitarianism.",
            hint = "'Big Brother is watching you.'",
            categories = listOf("Samples"),
            additionalInfo = "'Big Brother is watching you.'",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "39e6d2ad-6bb5-4604-bd25-de1bccb33e2c",
            quizId = quizId,
            text = "What display technology is commonly used in modern foldables?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("LCD", "CRT", "OLED", "Plasma"),
            correctAnswers = listOf(2),
            explanation = "Relies on flexible organic layers that can bend.",
            hint = "Emits its own light without a backlight.",
            categories = listOf("Samples"),
            additionalInfo = "Emits its own light without a backlight.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "e2a16599-1806-447d-b553-7fb0a59369c9",
            quizId = quizId,
            text = "How many chambers does a normal human heart have?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Two", "Three", "Four", "Five"),
            correctAnswers = listOf(2),
            explanation = "Consists of two atria and two ventricles.",
            hint = "Two on each side.",
            categories = listOf("Samples"),
            additionalInfo = "Two on each side.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "b4f4fff6-4354-4bae-863e-140ab8078b6b",
            quizId = quizId,
            text = "Which of the following cities is the capital of Japan?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Seoul", "Beijing", "Tokyo", "Bangkok"),
            correctAnswers = listOf(2),
            explanation = "It is the largest metropolitan area in the world.",
            hint = "Hosted the 2020 Summer Olympics.",
            categories = listOf("Samples"),
            additionalInfo = "Hosted the 2020 Summer Olympics.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "a7d6cdc7-d80b-4d4b-9e9f-e6f1ad3e4d2c",
            quizId = quizId,
            text = "'Masgouf' is a traditional dish famously associated with which country?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Egypt", "Iraq", "Lebanon", "Morocco"),
            correctAnswers = listOf(1),
            explanation = "Consists of seasoned, grilled carp.",
            hint = "Famous along Abu Nuwas street.",
            categories = listOf("Samples"),
            additionalInfo = "Famous along Abu Nuwas street.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "45561625-b716-4bd3-a64d-4fb0467b2089",
            quizId = quizId,
            text = "What energy source generates electricity using photovoltaic cells?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Wind", "Geothermal", "Solar", "Hydro"),
            correctAnswers = listOf(2),
            explanation = "Extracts energy directly from sunlight.",
            hint = "Comes from the sky during the day.",
            categories = listOf("Samples"),
            additionalInfo = "Comes from the sky during the day.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "63a59436-48eb-45ae-9244-35762cc5ada5",
            quizId = quizId,
            text = "What does the acronym VPN stand for?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Visual Private",
                "Virtual Public",
                "Virtual Private",
                "Variable Protocol"
            ),
            correctAnswers = listOf(2),
            explanation = "Encrypts internet traffic to protect data.",
            hint = "The first word is 'Virtual'.",
            categories = listOf("Samples"),
            additionalInfo = "The first word is 'Virtual'.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "7374c867-a464-4ca3-8a00-b1e5b43594fb",
            quizId = quizId,
            text = "What is the recommended CPR compression-to-breath ratio for adults?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("15:2", "30:2", "10:1", "50:5"),
            correctAnswers = listOf(1),
            explanation = "The ratio recommended by the AHA.",
            hint = "Starts with 30 compressions.",
            categories = listOf("Samples"),
            additionalInfo = "Starts with 30 compressions.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "09e8e0c4-0f6e-4e49-bb9a-d9f335646fef",
            quizId = quizId,
            text = "Who formulated the laws of motion and universal gravitation?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Albert Einstein", "Isaac Newton", "Nikola Tesla", "Galileo Galilei"),
            correctAnswers = listOf(1),
            explanation = "Known for the famous apple story.",
            hint = "He has 3 fundamental laws of motion.",
            categories = listOf("Samples"),
            additionalInfo = "He has 3 fundamental laws of motion.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "97dbe833-2787-408d-b578-4a3235b14f95",
            quizId = quizId,
            text = "Which programming language is Google's preferred choice for Android?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Java", "Swift", "Kotlin", "C++"),
            correctAnswers = listOf(2),
            explanation = "A modern, expressive, and concise language.",
            hint = "Created by JetBrains.",
            categories = listOf("Samples"),
            additionalInfo = "Created by JetBrains.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "ce4d8b5d-0aea-43e4-b98e-d29c33c94097",
            quizId = quizId,
            text = "Who is credited with inventing the telephone?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Thomas Edison",
                "Alexander Graham Bell",
                "Guglielmo Marconi",
                "James Watt"
            ),
            correctAnswers = listOf(1),
            explanation = "Patented the first practical telephone.",
            hint = "Alexander Graham...",
            categories = listOf("Samples"),
            additionalInfo = "Alexander Graham...",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "da55b633-7bb1-4bf8-a085-8c2eeb8a0b0f",
            quizId = quizId,
            text = "Which of the following is a fast-acting IV anesthetic?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Paracetamol", "Propofol", "Aspirin", "Ibuprofen"),
            correctAnswers = listOf(1),
            explanation = "Widely used for the induction of anesthesia.",
            hint = "Nicknamed the 'Milk of Amnesia'.",
            categories = listOf("Samples"),
            additionalInfo = "Nicknamed the 'Milk of Amnesia'.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "3aa7d740-58a6-424c-9eed-13513f59396c",
            quizId = quizId,
            text = "Who painted the famous Mona Lisa?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Vincent van Gogh",
                "Pablo Picasso",
                "Leonardo da Vinci",
                "Claude Monet"
            ),
            correctAnswers = listOf(2),
            explanation = "An Italian Renaissance polymath.",
            hint = "Leonardo...",
            categories = listOf("Samples"),
            additionalInfo = "Leonardo...",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "863fdd73-6352-4e73-8a8e-b21bb957aaa7",
            quizId = quizId,
            text = "What is the chemical symbol for Gold?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Ag", "Au", "Fe", "Pb"),
            correctAnswers = listOf(1),
            explanation = "Derived from the Latin word 'Aurum'.",
            hint = "Located in Group 11 of the periodic table.",
            categories = listOf("Samples"),
            additionalInfo = "Located in Group 11 of the periodic table.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "9bb082be-386e-43c7-8a74-56032e0f3c72",
            quizId = quizId,
            text = "In what year did the Berlin Wall fall?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("1985", "1989", "1991", "1995"),
            correctAnswers = listOf(1),
            explanation = "This event marked the beginning of the end of the Cold War.",
            hint = "Late eighties.",
            categories = listOf("Samples"),
            additionalInfo = "Late eighties.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "ad38d7bf-2579-43d7-86b0-a9a87216a0ea",
            quizId = quizId,
            text = "The scale used to measure the magnitude of earthquakes is called:",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Fujita Scale", "Richter Scale", "Kelvin Scale", "Beaufort Scale"),
            correctAnswers = listOf(1),
            explanation = "It is a logarithmic scale.",
            hint = "Named after Charles F.",
            categories = listOf("Samples"),
            additionalInfo = "Named after Charles F.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "c73b95c4-e6e3-48b7-9696-332732ab7526",
            quizId = quizId,
            text = "What was the first decentralized cryptocurrency introduced?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Ethereum", "Ripple", "Bitcoin", "Litecoin"),
            correctAnswers = listOf(2),
            explanation = "Created by an unknown entity named Satoshi.",
            hint = "Launched in the year 2009.",
            categories = listOf("Samples"),
            additionalInfo = "Launched in the year 2009.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "d31afbc9-6cdc-4e1f-a855-502b8f506f78",
            quizId = quizId,
            text = "Who was the legendary king of Uruk that searched for immortality?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Zeus", "Odin", "Gilgamesh", "Enkidu"),
            correctAnswers = listOf(2),
            explanation = "Central figure of the world's oldest epic.",
            hint = "His friend was named Enkidu.",
            categories = listOf("Samples"),
            additionalInfo = "His friend was named Enkidu.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "365abd50-636c-42e8-9ef7-6ba3d7ae225d",
            quizId = quizId,
            text = "Which gas is most responsible for the greenhouse effect?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Oxygen", "Nitrogen", "Carbon Dioxide", "Helium"),
            correctAnswers = listOf(2),
            explanation = "Emitted primarily from burning fossil fuels.",
            hint = "The gas we exhale.",
            categories = listOf("Samples"),
            additionalInfo = "The gas we exhale.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "b530119b-8ef9-4197-824b-0e2c7a48055e",
            quizId = quizId,
            text = "Which country has won the most FIFA Men's World Cup titles?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Germany", "Italy", "Brazil", "Argentina"),
            correctAnswers = listOf(2),
            explanation = "Crowned champions 5 times.",
            hint = "The Samba team from South America.",
            categories = listOf("Samples"),
            additionalInfo = "The Samba team from South America.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "214f1f35-98e6-445c-9759-63200122c1ae",
            quizId = quizId,
            text = "Which famous director helmed the sci-fi movie 'Inception'?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Steven Spielberg",
                "Quentin Tarantino",
                "Christopher Nolan",
                "James Cameron"
            ),
            correctAnswers = listOf(2),
            explanation = "A British director known for complex plots.",
            hint = "Also directed Interstellar.",
            categories = listOf("Samples"),
            additionalInfo = "Also directed Interstellar.",
            weight = 1,
        ),
    )

    private fun getArabicQuestions(quizId: Long): List<QuestionEntity> = listOf(
        QuestionEntity(
            externalId = "2737dea7-193f-485e-b657-e172adb5b083",
            quizId = quizId,
            text = "أي كوكب يُعرف بالكوكب الأحمر؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("المشتري", "المريخ", "الزهرة", "زحل"),
            correctAnswers = listOf(1),
            explanation = "سطحه مغطى بأكسيد الحديد.",
            hint = "هو الكوكب الرابع من الشمس.",
            categories = listOf("Samples"),
            additionalInfo = "هو الكوكب الرابع من الشمس.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "f802b2d4-4d28-48b0-99cc-b29f35d374ea",
            quizId = quizId,
            text = "ماذا يسمى المكون الأساسي لنظام تشغيل لينكس؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "الواجهة الرسومية",
                "النواة (Kernel)",
                "الصدفة (Shell)",
                "المحطة (Terminal)"
            ),
            correctAnswers = listOf(1),
            explanation = "هو الجزء الذي يدير موارد النظام بالكامل.",
            hint = "يبدأ بحرف K بالإنجليزية.",
            categories = listOf("Samples"),
            additionalInfo = "يبدأ بحرف K بالإنجليزية.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "48ae7af0-335f-4066-bdff-dc138d607b1a",
            quizId = quizId,
            text = "هذه البوابة الأثرية ذات الطوب الأزرق المزجج لأي مدينة كانت؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("سومر", "بابل", "أور", "نينوى"),
            correctAnswers = listOf(1),
            explanation = "بوابة عشتار في العراق القديم.",
            hint = "بالقرب من مدينة الحلة.",
            categories = listOf("Samples"),
            additionalInfo = "بالقرب من مدينة الحلة.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "470ce5b1-1b47-4694-abd8-633d655d7bd3",
            quizId = quizId,
            text = "ما هما مصدرا الطاقة في السيارات الهجينة (HEV)؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "محرك احتراق ومحرك كهربائي",
                "طاقة شمسية وكهرباء",
                "هيدروجين وديزل",
                "كهرباء وطاقة رياح"
            ),
            correctAnswers = listOf(0),
            explanation = "تستخدم الوقود والبطارية معاً لتوفير الطاقة.",
            hint = "هي مزيج من نظامين.",
            categories = listOf("Samples"),
            additionalInfo = "هي مزيج من نظامين.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "7f1a77cc-d925-4b24-9ad4-4bd94d2a9991",
            quizId = quizId,
            text = "من هو مؤلف رواية '1984'؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("إرنست همينغوي", "جورج أورويل", "مارك توين", "تشارلز ديكنز"),
            correctAnswers = listOf(1),
            explanation = "رواية ديستوبيا شهيرة تناقش الشمولية.",
            hint = "'الأخ الأكبر يراقبك'.",
            categories = listOf("Samples"),
            additionalInfo = "'الأخ الأكبر يراقبك'.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "88614d81-6e9c-40a9-8092-69eb71f9c494",
            quizId = quizId,
            text = "ما نوع تقنية الشاشات الشائعة في الهواتف القابلة للطي؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("LCD", "CRT", "OLED", "Plasma"),
            correctAnswers = listOf(2),
            explanation = "تعتمد على طبقات عضوية مرنة قابلة للانحناء.",
            hint = "تبعث ضوءها الخاص بدون إضاءة خلفية.",
            categories = listOf("Samples"),
            additionalInfo = "تبعث ضوءها الخاص بدون إضاءة خلفية.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "27b2a711-00ac-4c89-a676-89573757b86d",
            quizId = quizId,
            text = "كم عدد الحجرات في قلب الإنسان الطبيعي؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("2", "3", "4", "5"),
            correctAnswers = listOf(2),
            explanation = "يتكون من أذينين وبطينين.",
            hint = "اثنان في كل جانب.",
            categories = listOf("Samples"),
            additionalInfo = "اثنان في كل جانب.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "e7c82e2a-987e-4717-85e3-0f6a14e69adc",
            quizId = quizId,
            text = "أي مدينة مما يلي هي عاصمة اليابان؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("سيول", "بكين", "طوكيو", "بانكوك"),
            correctAnswers = listOf(2),
            explanation = "تعد أكبر منطقة حضرية في العالم من حيث عدد السكان.",
            hint = "استضافت أولمبياد 2020.",
            categories = listOf("Samples"),
            additionalInfo = "استضافت أولمبياد 2020.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "2b04394d-f678-441f-9333-df1ad25fb10c",
            quizId = quizId,
            text = "طبق 'المسكوف' يشتهر به أي بلد؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("مصر", "العراق", "لبنان", "المغرب"),
            correctAnswers = listOf(1),
            explanation = "عبارة عن سمك شبوط مشوي على الحطب.",
            hint = "مشهور في شارع أبو نواس.",
            categories = listOf("Samples"),
            additionalInfo = "مشهور في شارع أبو نواس.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "4726b55f-bfd2-40bb-8001-8a79f7e4b382",
            quizId = quizId,
            text = "ما هو مصدر الطاقة الذي يولد الكهرباء باستخدام الخلايا الكهروضوئية؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "طاقة الرياح",
                "الطاقة الحرارية الأرضية",
                "الطاقة الشمسية",
                "الطاقة المائية"
            ),
            correctAnswers = listOf(2),
            explanation = "تستخرج الطاقة من ضوء الشمس المباشر.",
            hint = "تأتي من السماء نهاراً.",
            categories = listOf("Samples"),
            additionalInfo = "تأتي من السماء نهاراً.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "d5869586-70b3-45f8-a0c1-145499c6d9b7",
            quizId = quizId,
            text = "إلى ماذا يرمز اختصار VPN؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "شبكة بصرية خاصة",
                "شبكة عامة افتراضية",
                "شبكة خاصة افتراضية",
                "شبكة بروتوكول متغير"
            ),
            correctAnswers = listOf(2),
            explanation = "تقوم بتشفير حركة الإنترنت لحماية البيانات.",
            hint = "تبدأ بكلمة 'افتراضية'.",
            categories = listOf("Samples"),
            additionalInfo = "تبدأ بكلمة 'افتراضية'.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "cb0c9b04-9508-49f9-89c0-4b7ced06cc88",
            quizId = quizId,
            text = "ما هي نسبة ضغطات الصدر إلى الأنفاس أثناء الإنعاش (CPR) للبالغين؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("15:2", "30:2", "10:1", "50:5"),
            correctAnswers = listOf(1),
            explanation = "النسبة الموصى بها من جمعية القلب الأمريكية.",
            hint = "30 ضغطة.",
            categories = listOf("Samples"),
            additionalInfo = "30 ضغطة.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "3858d7c5-6adc-4b1b-a493-b2b42d4cb75c",
            quizId = quizId,
            text = "من هو العالم الذي صاغ قوانين الحركة والجاذبية؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("ألبرت أينشتاين", "إسحاق نيوتن", "نيكولا تسلا", "غاليليو غاليلي"),
            correctAnswers = listOf(1),
            explanation = "صاحب قصة التفاحة الشهيرة.",
            hint = "له 3 قوانين أساسية للحركة.",
            categories = listOf("Samples"),
            additionalInfo = "له 3 قوانين أساسية للحركة.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "a73adf7d-92d2-4b85-883d-468001a34b5f",
            quizId = quizId,
            text = "ما هي لغة البرمجة المفضلة لدى جوجل لتطوير تطبيقات أندرويد؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("جافا", "سويفت", "كوتلن", "سي بلس بلس"),
            correctAnswers = listOf(2),
            explanation = "لغة حديثة وتعبيرية وتختصر الأكواد.",
            hint = "أنشأتها شركة JetBrains.",
            categories = listOf("Samples"),
            additionalInfo = "أنشأتها شركة JetBrains.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "c4afa4b8-79ee-46a7-86db-8a12e32cd2aa",
            quizId = quizId,
            text = "لمن يُنسب فضل اختراع الهاتف؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("توماس إديسون", "ألكسندر جراهام بيل", "غولييلمو ماركوني", "جيمس واط"),
            correctAnswers = listOf(1),
            explanation = "سجل براءة اختراع أول هاتف عملي.",
            hint = "ألكسندر جراهام...",
            categories = listOf("Samples"),
            additionalInfo = "ألكسندر جراهام...",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "23aeb9f1-dca8-48b4-bcfb-f28f624ad561",
            quizId = quizId,
            text = "أي مما يلي يُستخدم كمخدر وريدي سريع المفعول للحث على التخدير العام؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("براسيتامول", "بروبوفول", "أسبرين", "آيبوبروفين"),
            correctAnswers = listOf(1),
            explanation = "يُستخدم بشكل واسع لبدء التخدير.",
            hint = "يُلقب بـ 'حليب النسيان'.",
            categories = listOf("Samples"),
            additionalInfo = "يُلقب بـ 'حليب النسيان'.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "9f928f6a-9375-4a1f-9c4d-c7fbfb3490cc",
            quizId = quizId,
            text = "من رسم لوحة الموناليزا الشهيرة؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("فينسنت فان جوخ", "بابلو بيكاسو", "ليوناردو دا فينشي", "كلود مونيه"),
            correctAnswers = listOf(2),
            explanation = "عالم وفنان إيطالي من عصر النهضة.",
            hint = "ليوناردو...",
            categories = listOf("Samples"),
            additionalInfo = "ليوناردو...",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "f00b6c5d-b6b4-406f-8812-3cd014b40524",
            quizId = quizId,
            text = "ما هو الرمز الكيميائي للذهب؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("Ag", "Au", "Fe", "Pb"),
            correctAnswers = listOf(1),
            explanation = "مشتق من الكلمة اللاتينية Aurum.",
            hint = "يقع في المجموعة 11 من الجدول الدوري.",
            categories = listOf("Samples"),
            additionalInfo = "يقع في المجموعة 11 من الجدول الدوري.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "21c91db8-8a8e-4b43-a648-4daa31663320",
            quizId = quizId,
            text = "في أي عام سقط جدار برلين؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("1985", "1989", "1991", "1995"),
            correctAnswers = listOf(1),
            explanation = "مثل هذا الحدث بداية نهاية الحرب الباردة.",
            hint = "في أواخر الثمانينيات.",
            categories = listOf("Samples"),
            additionalInfo = "في أواخر الثمانينيات.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "9f99ce34-85c9-40dd-8de5-e82500472356",
            quizId = quizId,
            text = "ماذا يسمى المقياس المستخدم لقياس قوة الزلازل؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("مقياس فوجيتا", "مقياس ريختر", "مقياس كلفن", "مقياس بوفورت"),
            correctAnswers = listOf(1),
            explanation = "هو مقياس لوغاريتمي يسجل قوة الهزات.",
            hint = "سُمي على اسم تشارلز إف...",
            categories = listOf("Samples"),
            additionalInfo = "سُمي على اسم تشارلز إف...",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "35a59768-7702-4f51-adb0-ace138178257",
            quizId = quizId,
            text = "ما هي أول عملة مشفرة لامركزية تم إطلاقها؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("إيثيريوم", "ريبل", "بيتكوين", "لايتكوين"),
            correctAnswers = listOf(2),
            explanation = "أنشأها شخص (أو مجموعة) مجهول باسم ساتوشي.",
            hint = "انطلقت في عام 2009.",
            categories = listOf("Samples"),
            additionalInfo = "انطلقت في عام 2009.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "f11aea64-b13b-4b85-afc5-7032a0668bda",
            quizId = quizId,
            text = "من هو الملك الأسطوري لأوروك الذي بحث عن الخلود؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("زيوس", "أودين", "جلجامش", "إنكيدو"),
            correctAnswers = listOf(2),
            explanation = "تدور حوله أقدم ملحمة أدبية في العالم.",
            hint = "صديقه اسمه إنكيدو.",
            categories = listOf("Samples"),
            additionalInfo = "صديقه اسمه إنكيدو.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "f853a4fd-268f-4c2c-a91a-0d4345e8eaf7",
            quizId = quizId,
            text = "ما هو الغاز الأكثر مسؤولية عن ظاهرة الاحتباس الحراري؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("الأكسجين", "النيتروجين", "ثاني أكسيد الكربون", "الهيليوم"),
            correctAnswers = listOf(2),
            explanation = "ينبعث بشكل رئيسي من حرق الوقود الأحفوري.",
            hint = "الغاز الذي نطلقه عند الزفير.",
            categories = listOf("Samples"),
            additionalInfo = "الغاز الذي نطلقه عند الزفير.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "4c775cae-691f-4d35-8f08-08294c796237",
            quizId = quizId,
            text = "ما هو البلد الذي فاز بأكبر عدد من ألقاب كأس العالم؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("ألمانيا", "إيطاليا", "البرازيل", "الأرجنتين"),
            correctAnswers = listOf(2),
            explanation = "توج باللقب 5 مرات.",
            hint = "فريق السامبا من أمريكا الجنوبية.",
            categories = listOf("Samples"),
            additionalInfo = "فريق السامبا من أمريكا الجنوبية.",
            weight = 1,
        ),
        QuestionEntity(
            externalId = "c62bef3f-2c65-4e31-922f-ab24b78b6250",
            quizId = quizId,
            text = "من هو مخرج فيلم الخيال العلمي 'Inception'؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf("ستيفن سبيلبرغ", "كوينتن تارانتينو", "كريستوفر نولان", "جيمس كاميرون"),
            correctAnswers = listOf(2),
            explanation = "مخرج بريطاني معروف بحبكاته الزمنية المعقدة.",
            hint = "أخرج أيضاً فيلم Interstellar.",
            categories = listOf("Samples"),
            additionalInfo = "أخرج أيضاً فيلم Interstellar.",
            weight = 1,
        ),
    )

    private fun getEnglishTutorialQuestions(quizId: Long): List<QuestionEntity> = listOf(
        QuestionEntity(
            externalId = "tut-en-1",
            quizId = quizId,
            text = "What is MKS?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "A personal mobile knowledge management and smart study application",
                "A specialized mobile file manager and cloud backup server",
                "An automated web development IDE and compiler",
                "A music player and audio streaming system"
            ),
            correctAnswers = listOf(0),
            explanation = "MKS stands for Mobile Knowledge System.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-2",
            quizId = quizId,
            text = "What does 'Rapid Mode' do?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Instantly advances to the next question upon choosing an answer without requiring confirmation",
                "Filters and displays only the most difficult and complex questions in the quiz",
                "Deletes the current study session history to save database storage",
                "Accelerates the TTS (Text-to-Speech) audio narration speed automatically"
            ),
            correctAnswers = listOf(0),
            explanation = "Rapid mode auto-advances to the next question after you answer.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-3",
            quizId = quizId,
            text = "How can you create a new Book or Quiz?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Click the floating action button (+) in the library to create manually or import files",
                "Shake the device on the home screen to auto-generate a random book",
                "Send a voice command directly to the active AI background agent",
                "Export existing app data store variables to an external configuration script"
            ),
            correctAnswers = listOf(0),
            explanation = "You can create a book manually or import one by clicking the floating action button (+) in the Library.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-4",
            quizId = quizId,
            text = "What is the Knowledge Dashboard?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "A comprehensive hub inside a book to manage its quizzes, flashcard decks, slideshow courses, note blueprints, and AI prompt decks",
                "A built-in web browser for downloading external study materials",
                "An analytical calculator for tracking user study statistics and trends",
                "A background manager for controlling active background downloads and updates"
            ),
            correctAnswers = listOf(0),
            explanation = "The Knowledge Dashboard provides access to all study materials connected to a specific book.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-6",
            quizId = quizId,
            text = "How can you get additional Books for the app?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Request them directly from the developer through the Contact Us section",
                "Purchase them from the official in-app marketplace store",
                "Scan and import them from the device's default System Settings",
                "Download them through a public subscription cloud channel"
            ),
            correctAnswers = listOf(0),
            explanation = "You can get additional books by navigating to the 'Contact Us' section and requesting them. This is a temporary setup as the app is still under active development.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-7",
            quizId = quizId,
            text = "How do you import a Book into MKS?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Use the Import option in the library, or share a supported file to MKS from an external app using Android Share",
                "Copy the raw database file manually into the system's root partition directory",
                "Write custom compiler scripts and inject them via the Settings screen",
                "Transfer the files wirelessly via local Bluetooth network scanning"
            ),
            correctAnswers = listOf(0),
            explanation = "You can import supported files (like XLSX or CSV) via the Library's Import tool, or by sharing the file from another app and choosing MKS as the destination.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-8",
            quizId = quizId,
            text = "How can you change the app's theme or appearance?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Navigate to the Settings screen and adjust the Appearance/Theme options",
                "Perform a long-press gesture on the app icon and configure system preferences",
                "Adjust the device's system font and brightness settings",
                "It is set permanently at installation and cannot be altered"
            ),
            correctAnswers = listOf(0),
            explanation = "You can customize colors, fonts, and layout density by heading to the Settings screen and choosing Appearance/Theme.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-9",
            quizId = quizId,
            text = "How do you request an explanation or study assist from AI?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Use the Explanation button during study, or run templates in the Prompts tab, connected to cloud APIs or a local Ollama server",
                "Speak directly into the device microphone from the Library home screen",
                "Compose a formal help ticket to the customer support team",
                "Execute manual SQL commands within the integrated database viewer"
            ),
            correctAnswers = listOf(0),
            explanation = "MKS allows you to generate explanations during quizzes or draft custom materials from the Prompts tab, using either cloud APIs or a local Ollama server.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-10",
            quizId = quizId,
            text = "What kinds of learning formats (knowledge structures) are supported in MKS?",
            type = QuestionType.MULTIPLE_CHOICE,
            options = listOf(
                "Interactive Quizzes and Tests",
                "Spaced-Repetition Flashcard Decks",
                "Progressive Slideshow Courses",
                "Summary Note Blueprints and Articles",
                "Raw System Log Files"
            ),
            correctAnswers = listOf(0, 1, 2, 3),
            explanation = "MKS organizes knowledge into quizzes, flashcard decks, progressive slides, and note blueprints.",
            categories = listOf("How to start"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-en-11",
            quizId = quizId,
            text = "How can you make the most of each learning format in the app?",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "Use its specialized player interface (such as note autoscroll/TTS) or convert it to another format (like transforming a quiz into flashcards)",
                "Read it only as plain text without using interactive features",
                "Export and print it to physical paper for traditional studying",
                "Share it with other users via external messaging apps"
            ),
            correctAnswers = listOf(0),
            explanation = "You can study materials through specialized players (like the blueprint reader with autoscroll/TTS) or convert them (e.g., turning quiz questions into flashcards).",
            categories = listOf("How to start"),
            weight = 1,
        )
    )

    private fun getArabicTutorialQuestions(quizId: Long): List<QuestionEntity> = listOf(
        QuestionEntity(
            externalId = "tut-ar-1",
            quizId = quizId,
            text = "ما هو تطبيق MKS؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "تطبيق شخصي لإدارة المعرفة والمذاكرة الذكية عبر الهاتف",
                "نظام لإدارة الملفات والنسخ الاحتياطي السحابي المتخصص",
                "بيئة تطوير متكاملة ومترجم لكود الويب",
                "مشغل وسائط ونظام بث صوتي رقمي"
            ),
            correctAnswers = listOf(0),
            explanation = "MKS اختصار لنظام معرفة الجوال.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-2",
            quizId = quizId,
            text = "ماذا يفعل 'الوضع السريع' (Rapid Mode)؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "ينقلك فوراً للسؤال التالي عند اختيار الإجابة دون الحاجة لتأكيد يدوي",
                "يقوم بفلترة وعرض الأسئلة الأكثر صعوبة وتعقيداً فقط",
                "يحذف سجل الجلسة الحالية لتوفير مساحة في قاعدة البيانات",
                "يضاعف سرعة نطق القارئ الصوتي التلقائي للنصوص"
            ),
            correctAnswers = listOf(0),
            explanation = "الوضع السريع ينقلك تلقائياً للسؤال التالي بمجرد الإجابة.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-3",
            quizId = quizId,
            text = "كيف يمكنك إنشاء كتاب أو اختبار جديد؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "الضغط على زر الإضافة العائم (+) في المكتبة لإنشائه يدوياً أو استيراد الملفات",
                "هز الهاتف في الواجهة الرئيسية لتوليد كتاب عشوائي تلقائياً",
                "إرسال أمر صوتي مباشر إلى مساعد الذكاء الاصطناعي في الخلفية",
                "تصدير متغيرات تفضيلات التطبيق إلى ملف إعداد خارجي"
            ),
            correctAnswers = listOf(0),
            explanation = "يمكنك إنشاء كتاب جديد أو استيراده من خلال زر الإضافة العائم في المكتبة.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-4",
            quizId = quizId,
            text = "ما هي لوحة المعرفة (Knowledge Dashboard)؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "مركز متكامل داخل الكتاب لإدارة الكوزات، والبطاقات المراجعة، والشرائح التعليمية، ومقالات الملاحظات، وبرومبتات الذكاء الاصطناعي",
                "متصفح ويب مدمج لتحميل مصادر الدراسة الخارجية",
                "آلة حاسبة تحليلية لتتبع إحصائيات ومعدلات المذاكرة اليومية",
                "لوحة إدارية للتحكم بالتحميلات والتحديثات التي تجري في الخلفية"
            ),
            correctAnswers = listOf(0),
            explanation = "تحتوي لوحة المعرفة على جميع أدوات التعلم الخاصة بالكتاب.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-6",
            quizId = quizId,
            text = "كيف يمكنك الحصول على كتب إضافية للتطبيق؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "طلبها مباشرة من المطور من خلال الانتقال إلى قسم 'اتصل بنا'",
                "شراؤها مباشرة من المتجر الداخلي المخصص داخل التطبيق",
                "فحص واستيراد الكتب من إعدادات النظام الافتراضية للهاتف",
                "تحميلها من خلال الاشتراك في قناة سحابية عامة"
            ),
            correctAnswers = listOf(0),
            explanation = "يمكنك الحصول على الكتب من خلال خانة 'اتصل بنا' وطلبها مباشرة من المطور. هذه الحالة مؤقتة نظراً لأن التطبيق ما زال في مرحلة التطوير.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-7",
            quizId = quizId,
            text = "كيف تستورد كتاباً أو ملفاً إلى داخل تطبيق MKS؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "باستخدام خيار الاستيراد في المكتبة، أو مشاركة ملف مدعوم إلى MKS من تطبيق خارجي عبر نظام مشاركة أندرويد",
                "نسخ ملف قاعدة البيانات الخام يدوياً إلى مسار النظام الجذري للهاتف",
                "كتابة سكريبتات مترجم مخصصة وحقنها عبر شاشة الإعدادات",
                "نقل الملفات لاسلكياً عبر فحص شبكة البلوتوث المحلية"
            ),
            correctAnswers = listOf(0),
            explanation = "يمكنك الاستيراد إما من داخل التطبيق عبر خيار الاستيراد بالمكتبة، أو بمشاركة ملف مدعوم (مثل XLSX/CSV) من أي تطبيق خارجي واختيار MKS عبر نظام مشاركة أندرويد.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-8",
            quizId = quizId,
            text = "كيف يمكنك تغيير مظهر التطبيق (الثيم)؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "الانتقال إلى شاشة الإعدادات وتخصيص خيارات المظهر والثيم المناسب",
                "الضغط المطول على أيقونة التطبيق وتعديل تفضيلات نظام التشغيل",
                "تغيير إعدادات خطوط وسطوع نظام أندرويد الافتراضي",
                "المظهر ثابت منذ تنصيب التطبيق ولا يمكن تغييره بأي شكل"
            ),
            correctAnswers = listOf(0),
            explanation = "تغيير المظهر (الثيم) متاح بسهولة عبر الانتقال إلى شاشة الإعدادات ثم خيارات المظهر لتخصيص الألوان والخطوط.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-9",
            quizId = quizId,
            text = "كيف يمكنك طلب شرح أو معالجة شيء من الذكاء الاصطناعي؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "باستخدام زر الشرح أثناء الدراسة، أو تشغيل قوالب البرومبتات، بالاتصال بنماذج سحابية أو خادم أولاما المحلي",
                "التحدث مباشرة في ميكروفون الهاتف من شاشة المكتبة الرئيسية",
                "كتابة تذكرة دعم فني رسمية إلى فريق خدمة العملاء",
                "تشغيل أوامر قواعد بيانات SQL يدوياً في نافذة الفحص المدمجة"
            ),
            correctAnswers = listOf(0),
            explanation = "يمكنك التفاعل مع الذكاء الاصطناعي عبر زر الشرح داخل الكوزات أو من خلال لوحة البرومبتات، مع دعم الربط بنماذج سحابية أو خادم محلي يعمل على Ollama.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-10",
            quizId = quizId,
            text = "ما هو تشكيل المواد المعرفية في التطبيق؟",
            type = QuestionType.MULTIPLE_CHOICE,
            options = listOf(
                "الاختبارات والكوزات التفاعلية",
                "بطاقات المراجعة الذكية بالتكرار المتباعد",
                "الشرائح والدروس التعليمية التتابعية",
                "ملخصات المواد ومقالات الملاحظات (Blueprints)",
                "ملفات سجلات النظام الخام"
            ),
            correctAnswers = listOf(0, 1, 2, 3),
            explanation = "ينظم تطبيق MKS المواد المعرفية في هيئة اختبارات (Quizzes)، بطاقات مراجعة، شرائح عرض، وملاحظات أو مقالات (Blueprints).",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        ),
        QuestionEntity(
            externalId = "tut-ar-11",
            quizId = quizId,
            text = "كيف يمكن الاستفادة من كل مادة معرفية على التطبيق؟",
            type = QuestionType.SINGLE_CHOICE,
            options = listOf(
                "استخدام واجهة المشغل الخاصة بها (مثل القراءة الصوتي والتمرير التلقائي للمقالات) أو تحويلها لتشكيل آخر (كتحويل كوز إلى بطاقات مراجعة)",
                "اقتصار قراءتها كنصوص جافة دون استخدام أي ميزات تفاعلية",
                "تصديرها وطباعتها ورقياً للدراسة بالطريقة التقليدية",
                "مشاركتها مع مستخدمين آخرين عبر تطبيقات المراسلة الخارجية"
            ),
            correctAnswers = listOf(0),
            explanation = "يمكنك دراسة كل مادة عبر المشغل المخصص لها (كقارئ المقالات وخاصية القراءة التلقائية) أو بتحويلها مثل تحويل الكوزات إلى بطاقات مراجعة لتنويع أساليب الدراسة.",
            categories = listOf("كيف تبدأ"),
            weight = 1,
        )
    )
    }
