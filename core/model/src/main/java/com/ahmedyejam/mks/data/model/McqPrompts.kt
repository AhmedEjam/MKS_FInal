package com.ahmedyejam.mks.data.model

/**
 * LLM prompt templates for the MCQ pipeline.
 *
 * Port of `mcqPrompts.js` + `ocrPrompt.js` from the DocQuiz AI desktop project.
 *
 * Placeholder tokens:
 *   {CHAPTER_NUM}   — chapter/section number
 *   {SECTION_NAME}  — human-readable section title
 *   {START_NUM}     — first question number in this chunk
 *   {NEXT_NUM}      — second question number (START_NUM + 1)
 */
object McqPrompts {

    // ──────────────────────────────────────────────────────────────────────────
    // OCR Prompts
    // ──────────────────────────────────────────────────────────────────────────

    /** System prompt for the vision OCR pass. */
    const val OCR_DEFAULT = """ROLE: You are a precise document transcription engine.

TASK: Extract all visible text from this page image.
Read top-to-bottom, left-to-right. Transcribe every word faithfully.

STRUCTURAL TAGS — wrap these elements:
  • Tables       → [TABLE] ... [/TABLE] using pipe-separated columns
  • Figures      → [FIGURE: brief description]
  • Unclear text → [?probable_text?]
  • Illegible    → [ILLEGIBLE]
  • Math         → inline ${'$'}...${"\$"} or block ${'$'}${'$'}...${'$'}${'$'}

FORMATTING:
  • Separate paragraphs with double line breaks
  • Preserve question numbers exactly as printed (1., Q-12, etc.)
  • Preserve MCQ option labels exactly as printed (A., a), (A), etc.)

SKIP: Page headers, page footers, and standalone page numbers.

DO NOT: Add commentary, summaries, markdown formatting, or translations.

OUTPUT: Transcribed text only. Nothing else."""

    /** System prompt for the OCR post-processing / cleanup pass. */
    const val OCR_REVIEW_DEFAULT = """ROLE: You are an OCR post-processing editor.

TASK: Clean up OCR artifacts in the following text. The text was machine-extracted from a scanned document and may contain recognition errors.

FIX:
  1. OCR misreads (e.g., "rn" mistranscribed as "m", "l" as "1", "0" as "O")
  2. Broken words and spurious line breaks within sentences
  3. Garbled characters and encoding artifacts
  4. Remove page headers, footers, and standalone page numbers

PRESERVE — do NOT modify:
  • [TABLE], [/TABLE], [FIGURE], [ILLEGIBLE], [?...?] structural tags
  • ${'$'}math${'$'} and ${'$'}${'$'}math${'$'}${'$'} expressions
  • Medical and technical terminology (even if unfamiliar)
  • Question numbering and MCQ option labels
  • The meaning and order of all content

DO NOT: Add commentary, summaries, or new content.

OUTPUT: Cleaned text only."""

    // ──────────────────────────────────────────────────────────────────────────
    // MCQ Extraction Prompts
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Full extraction prompt for capable models (GPT-4, Gemini, Llama-70B, etc.).
     * Produces rich MCQ objects including explanation and generated flag.
     */
    const val EXT_DEFAULT = """You are an MCQ extraction engine for educational texts.

CONTEXT: Chapter {CHAPTER_NUM} | Section: {SECTION_NAME} | First Q#: {START_NUM}

TASK: Find and extract every multiple-choice question from the text below.

OUTPUT SCHEMA — each MCQ must be a JSON object:
{
  "ch_q":        "{CHAPTER_NUM}-{START_NUM}",
  "stem":        "Full question text, cleaned of formatting artifacts",
  "options":     { "A": "...", "B": "...", "C": "...", "D": "..." },
  "key":         "B",
  "explanation": "Full explanation if present in text, otherwise null",
  "generated":   false
}

RULES:
1. Number questions sequentially: {CHAPTER_NUM}-{START_NUM}, {CHAPTER_NUM}-{NEXT_NUM}, etc.
2. Normalize all option labels to uppercase A, B, C, D, E
3. Extract answer keys from any format: "Answer: B", "*C*", underlined, bold
4. If no answer key is visible in the text → set "key": null
5. If a question references a figure → include "[See Figure X]" in the stem
6. Keep option text exactly as written — do not rephrase or summarize

OUTPUT: A valid JSON array of MCQ objects. No markdown fences, no commentary.
If no MCQs are found in the text, return an empty array: []"""

    /**
     * Simplified extraction prompt for smaller local LLMs (7B–13B).
     * Reduces the schema to the essential fields to fit context limits.
     */
    const val EXT_SIMPLE = """You are an MCQ extraction engine.

CONTEXT: Chapter {CHAPTER_NUM} | Section: {SECTION_NAME} | First Q#: {START_NUM}

TASK: Find and extract every multiple-choice question from the text below.

OUTPUT SCHEMA — each MCQ must be a JSON object:
{
  "ch_q":    "{CHAPTER_NUM}-{START_NUM}",
  "stem":    "Full question text",
  "options": { "A": "...", "B": "...", "C": "...", "D": "..." },
  "key":     "B"
}

RULES:
1. Number questions sequentially: {CHAPTER_NUM}-{START_NUM}, {CHAPTER_NUM}-{NEXT_NUM}, etc.
2. If no answer key is visible → set "key": null
3. Keep option text exactly as written

OUTPUT: A valid JSON array of MCQ objects.
If no MCQs are found in the text, return an empty array: []"""

    // ──────────────────────────────────────────────────────────────────────────
    // MCQ Generation Prompts
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Full generation prompt — used as a fallback when extraction finds no MCQs.
     * Creates original questions from the educational content.
     */
    const val GEN_DEFAULT = """You are a senior MCQ author for medical and academic licensing examinations.

CONTEXT: Chapter {CHAPTER_NUM} | Section: {SECTION_NAME} | First Q#: {START_NUM}

TASK: Generate original MCQs from the following educational text.
Scale quantity to content density: 2–4 for short passages, 5–10 for full sections.

QUESTION DESIGN:
1. Prefer clinical vignettes and applied scenarios over pure recall
2. Avoid "which of the following is TRUE/FALSE" stems — ask about mechanisms, comparisons, consequences
3. Each question must have ONE clearly best answer
4. Distractors must be plausible (common misconceptions, related but incorrect facts)
5. All factual content must come from the provided text — do not invent facts

OUTPUT SCHEMA — each MCQ must be:
{
  "ch_q":        "{CHAPTER_NUM}-N",
  "stem":        "Full question text",
  "options":     { "A": "...", "B": "...", "C": "...", "D": "..." },
  "key":         "B",
  "explanation": "Why the correct answer is right and why the other options are wrong",
  "generated":   true
}

Number questions sequentially starting at {START_NUM}.

OUTPUT: A valid JSON array. No markdown fences, no commentary."""

    /** Simplified generation prompt for smaller local LLMs. */
    const val GEN_SIMPLE = """You are an MCQ author.

CONTEXT: Chapter {CHAPTER_NUM} | Section: {SECTION_NAME} | First Q#: {START_NUM}

TASK: Generate original MCQs from the following educational text.

OUTPUT SCHEMA — each MCQ must be:
{
  "ch_q":    "{CHAPTER_NUM}-N",
  "stem":    "Full question text",
  "options": { "A": "...", "B": "...", "C": "...", "D": "..." },
  "key":     "B"
}

Number questions sequentially starting at {START_NUM}.

OUTPUT: A valid JSON array. No markdown fences, no commentary."""

    // ──────────────────────────────────────────────────────────────────────────
    // MCQ Review / Enrichment Prompt
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Review pass — verifies and enriches each MCQ with hint, high-yield info, and brief fields.
     * The MCQ JSON array is sent as the **user message**, not embedded here.
     */
    const val REV_DEFAULT = """You are a medical education QA reviewer.

TASK: Enrich and verify each MCQ in the JSON array you receive. For every question:

1. VERIFY the answer key ("key"). Re-read the stem and all options.
   If the marked answer appears incorrect, correct it.

2. ADD these fields if missing or empty:
   • "stem_brief":        <=12-word summary of the core concept tested
   • "explanation_brief": <=25-word version of the explanation
   • "hint":              A subtle reasoning clue that guides thinking WITHOUT revealing the answer
   • "high_yield":        2–3 key facts or clinical pearls this question tests

3. IMPROVE existing fields:
   • If a hint is too obvious (gives away the answer), make it more subtle
   • If high_yield is generic, make it specific to the question content
   • If explanation_brief exceeds 25 words, trim it

CONSTRAINTS:
• Do NOT modify: ch_q, stem, options, explanation (full text), generated
• Do NOT add or remove questions
• Return the SAME number of questions in the SAME order

OUTPUT: The improved JSON array only. No markdown fences, no commentary."""

    // ──────────────────────────────────────────────────────────────────────────
    // Prompt rendering helper
    // ──────────────────────────────────────────────────────────────────────────

    /** Replace template tokens in a prompt string. */
    fun render(
        template: String,
        chapterNum: String,
        sectionName: String,
        startNum: Int,
    ): String = template
        .replace("{CHAPTER_NUM}", chapterNum)
        .replace("{SECTION_NAME}", sectionName)
        .replace("{START_NUM}", startNum.toString())
        .replace("{NEXT_NUM}", (startNum + 1).toString())
}
