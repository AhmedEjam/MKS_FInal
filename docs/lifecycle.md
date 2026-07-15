# MKS User Lifecycle & Management Plan

This document outlines the complete, highly detailed user journey and lifecycle within the MKS application. It captures every interactable element, button, dialog, and configuration option the user faces throughout the application.

## Table of Contents

1. [Discovery & Installation](#1-discovery--installation)
2. [First Launch & Workspace Management](#2-first-launch--workspace-management)
3. [Main Interface: The Books Library](#3-main-interface-the-books-library)
4. [Inside the Book: Knowledge Dashboard](#4-inside-the-book-knowledge-dashboard)
5. [Interactive Testing: The Quiz Player](#5-interactive-testing-the-quiz-player)
6. [Spaced Repetition: Flashcard Decks](#6-spaced-repetition-flashcard-decks)
7. [Progressive Learning: Slideshows & Blueprints](#7-progressive-learning-slideshows--blueprints)
8. [AI Integration: Prompt Decks](#8-ai-integration-prompt-decks)
9. [Global Tools & Cross-Book Features](#9-global-tools--cross-book-features)
10. [Settings & Preferences](#10-settings--preferences)

---

## 1. Discovery & Installation

### 1.1 Discovery (Play Store)
- **Current State:** The app is found by the user in the Google Play Store.
- **Interactables:** 
  - Reading the app description and feature list.
  - Viewing screenshots and promotional media.
  - Tapping the "Install" button.

### 1.2 Installation & Unpacking
- **Current State:** The app downloads and installs on the user's device. 
- **Details:**
  - Supports Android 11+ (legacy and modern devices).
  - Optimized footprint and download size.

### 1.3 Device Integration
- **Current State:** The app icon appears on the device launcher.
- **Interactables:**
  - Tapping the icon to launch the app.
  - *(Future Project: Redesign the app icon for a more premium look).*

---

## 2. First Launch & Workspace Management

- **Current State:** A basic welcome screen appears, which disables itself after the first startup.
- **Planned Upgrades & Interactables:**
  - **Language Selector:** Dropdown to choose between English and Arabic (RTL).
  - **Workspace Selector / Creator:** 
    - A dropdown to pick an existing workspace.
    - A button to "Create New Workspace" instead of standard accounts.
  - **Security Dialog:** If a workspace is protected, a modal dialog appears requiring a password before entry.
  - **Onboarding Guide:** Swipeable tutorial pages explaining app features.
  - **"Explore Features" Toggle/Button:** Opens an embedded or linked YouTube playlist tutorial.
  - **"Enter Workspace" Button:** Proceeds to the main library.

---

## 3. Main Interface: The Books Library

- **Current State:** The central hub displaying available books.
- **Interactables & UI Elements:**
  - **Dynamic Banners (Top Carousel):** Loops every few seconds. 
    - *Contact Card:* Tapping opens a Linktree URL.
    - *Resume Card:* Tapping jumps straight into the last opened session.
  - **Top App Bar Options (Three-dots menu):**
    - *Sort/Find:* Opens sorting parameters and a search bar.
    - *Change View:* Toggles between Grid, List, and the planned "Big Cards/Tiles" display.
    - *Trash Bin:* Opens the deleted items recovery view.
    - *Workspace Switcher:* Allows jumping to a different workspace.
  - **Categories Panel:** Horizontal scrollable row of `CategoryChip`s. Tapping expands to show all tags.
  - **Floating Action Button (FAB):** Expands to reveal options:
    - *New Book:* Opens a dialog for Title, Description, and Cover Image.
    - *Import:* Opens the device file picker to select XLSX, CSV, JSON, or ZIP files.
    - *Export:* Opens a dialog to select formats.
    - *Adaptive Quiz:* Immediately launches an AI-curated quiz based on weak spots.
  - **Book Cards:**
    - Tapping a card opens the book.
    - Tapping the 'Options' menu on a card: Edit metadata, Pin to top, Delete, Import into, or Export this specific book.

---

## 4. Inside the Book: Knowledge Dashboard

- **Current State:** Opening a book takes the user to the `BookKnowledgeDashboardScreen`.
- **Interactables & UI Elements:**
  - **Top Bar:** Displays the Book Title and a "Back" button.
  - **Scrollable Tabs:** Swiping horizontally switches between: *Dashboard, Slides, Quizzes, Notes, Mistakes, Flashcards, Prompts, Sources.*
  - **Dashboard Tab:**
    - *Summary Card:* Displays a linear progress bar (Completion %), and big numeric stats for: Due cards, Weak questions, Marked questions, and Open mistakes.
    - *Magic Actions Section (Horizontal scrollable chips):*
      - "Draft Note from Marked" / "Cards from Marked": Auto-generates notes/flashcards from marked questions.
      - "Note from Mistakes" / "Cards from Mistakes": Auto-generates study material from wrong answers.
      - "Generate from Questions": Compiles a slideshow course out of the question bank.
  - **FAB (+ Add):** The action button changes context based on the active tab:
    - *Quizzes Tab:* Opens `CreateQuizDialog` (Inputs: Title, Description, Cover Image, Category Filters).
    - *Slides Tab:* Opens `EntityEditDialog` (Inputs: Course Title, Description).
    - *Flashcards Tab:* Opens `EntityEditDialog` (Inputs: Deck Name, Description, Image).
    - *Notes Tab:* Opens `ArticleCreateDialog` (Options: Blank draft, Draft from marked, Draft from missed).
    - *Prompts Tab:* Opens `EntityEditDialog` (Inputs: Deck Title, Description).
    - *Sources Tab:* Opens `SourceDocumentDialog` (Inputs: Title, Type dropdown, Details text).

---

## 5. Interactive Testing: The Quiz Player

- **Current State:** The `QuizPlayerScreen` where the actual testing happens.
- **Interactables & UI Elements:**
  - **Top Bar:** 
    - Question Counter (e.g., 5/20).
    - Bookmark Icon: Tapping marks the question for later review.
    - Bolt Icon: Toggles Focus Mode (hides distractions).
    - Timer: Shows elapsed/remaining time.
    - Score & Streak indicators.
  - **Question Area:**
    - Text body of the question.
    - Embedded Image: Tapping the image opens a fullscreen `ZoomableImageDialog`.
    - "Assets" Button: If the question has attachments, tapping this opens `QuestionAssetsReadOnlyDialog`.
  - **Options List:**
    - *Tap:* Selects an option (Single or Multiple Choice).
    - *Double Tap:* Enables Elimination Mode (strikes through the option to visually rule it out).
    - *Swipe Left/Right on Screen:* Navigates to the Previous/Next question.
  - **Post-Answer Elements (Appear after checking answer):**
    - *Explanation Card:* Green for "Excellent!", Red for "Not quite right", displaying the rationale text.
    - *Categories Section:* Displays `CategoryChip`s. Users can tap to toggle tags, long-press to edit/rename a category, or type in a text field and hit the (+) icon to create a new category.
    - *Notes Field:* A multiline text field to type a permanent personal note for this question.
    - *Hint Toggle:* Tapping the lightbulb icon reveals hidden hint text.
  - **Draggable Bottom Sheet:**
    - Swiping up on the handle reveals settings.
    - Contains the primary "Check Answer" / "Next" / "Finish" buttons.
    - "Drop Question" button: Opens a warning dialog to permanently drop the question from the quiz.

---

## 6. Spaced Repetition: Flashcard Decks

- **Current State:** `FlashcardDeckScreen` for rote memorization.
- **Interactables & UI Elements:**
  - **Deck Overview:** Displays total cards, cards due today, and accuracy.
  - **Study Button:** Tapping starts the review session.
  - **During Review (Card Player):**
    - Tapping the center of the screen flips the card to reveal the back.
    - Bottom Bar options appear after flipping: "Hard" (Review sooner), "Good" (Normal interval), "Easy" (Review later).
    - If a flashcard is linked to a quiz question, a "View Source Question" button is accessible to read the original context.

---

## 7. Progressive Learning: Slideshows & Blueprints

- **Current State:** Screens for reading and structured visual learning.
- **Interactables & UI Elements:**
  - **SlideshowCourseScreen:**
    - *Navigation:* "Next Slide" / "Previous Slide" buttons.
    - *Image Area:* Displays the slide image, tapping zooms in.
    - *Notes Area:* Scrollable text displaying the teacher's notes or bullet points for the slide.
    - *Completion Checkbox:* Tapping marks the individual slide as understood.
  - **ReviewBlueprintScreen:**
    - *Markdown Viewer:* Renders rich text, bolding, lists, and headers.
    - *Interaction:* Users scroll through the document.
    - *Action:* Tapping the "Mark as Reviewed" button increments the review counter and updates the last-studied timestamp.

---

## 8. AI Integration: Prompt Decks

- **Current State:** The `AiPromptDeckScreen` for interacting with AI agents.
- **Interactables & UI Elements:**
  - **Deck List:** Users tap a specific prompt template (e.g., "Explain like I'm 5").
  - **Execution Interface:**
    - *Variable Inputs:* Text fields for the user to fill in the blank variables required by the prompt.
    - *Run Button:* Triggers the AI request.
  - **Output Area:**
    - Displays the generated markdown text.
    - *Copy Button:* Copies the output to clipboard.
    - *Save as Note Button:* Instantly converts the AI output into a new Note Blueprint in the book.
    - *History Tab:* Allows the user to scroll through past `PromptRunEntity` executions.

---

## 9. Global Tools & Cross-Book Features

- **Current State:** Features spanning the entire app ecosystem.
- **Interactables & UI Elements:**
  - **Global Search Screen:** 
    - *Search Bar:* Typing queries the entire database.
    - *Filter Chips:* Toggle searches between "Books", "Quizzes", "Questions", and "Notes".
    - *Results List:* Tapping a result teleports the user directly into that specific entity across the app.
  - **Review Dashboard (Mistake Log):** 
    - A scrollable queue of all questions answered incorrectly globally.
    - Each item shows the question stem and the user's wrong answer.
    - *Actions:* Users can tap to review the question again, or tap the "Mark as Fixed" button to remove it from the mistake queue.
  - **Data Tools Screen:**
    - Buttons to "Export Workspace to ZIP" or "Import from ZIP".
    - Shows progress bars and success/failure dialogs during I/O operations.

---

## 10. Settings & Preferences

- **Current State:** The global `SettingsScreen`.
- **Interactables & UI Elements:**
  - **Language Dropdown:** Switches app language (English / Arabic). UI instantly reacts (RTL switch).
  - **Theme Toggles:** Segmented buttons for "System Default", "Light Mode", "Dark Mode".
  - **Sliders:** 
    - *Font Scale:* Drag slider to increase/decrease global text size.
    - *UI Density:* Drag slider to compress or expand padding.
  - **Danger Zone:**
    - *Clear Cache Button:* Deletes temporary images, opens a confirmation dialog.
    - *Reset Database Button:* Completely wipes the database and re-seeds it. Requires typing "CONFIRM" in a dialog to proceed.
