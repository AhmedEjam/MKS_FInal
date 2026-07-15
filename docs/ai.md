# MKS AI System Architecture & Documentation

> **Last updated:** 2026-07-11 | 17 providers | 3 pipelines | 2 network stacks | 7 AI DataStore keys | 11 prompt templates

This document provides a comprehensive, source-verified overview of **every** AI and LLM integration within the MKS project — from frontend UI screens, through intermediate orchestration pipelines, down to the network transport layer and persistent configuration store. Each section includes file links, architecture context, and actionable improvement recommendations.

---

## Table of Contents

1. [High-Level Architecture](#1-high-level-architecture)
2. [Network Layer — `core/network`](#2-network-layer-corenetwork)
3. [Intermediate Pipelines — Services](#3-intermediate-pipelines-services)
4. [PDF & Image Pre-Processing](#4-pdf--image-pre-processing)
5. [Models & Configurations — `core/model`](#5-models--configurations-coremodel)
6. [Prompt Engineering Catalog](#6-prompt-engineering-catalog)
7. [Data & Repository Layer — `core/data`](#7-data--repository-layer-coredata)
8. [Persistent AI Settings — DataStore](#8-persistent-ai-settings-datastore)
9. [Frontend UI & ViewModels — `feature/ui`](#9-frontend-ui--viewmodels-featureui)
10. [On-Device ML Kit Integration](#10-on-device-ml-kit-integration)
11. [Dependency Injection Setup](#11-dependency-injection-setup)
12. [Error Handling & Resilience Audit](#12-error-handling--resilience-audit)
13. [Security Considerations](#13-security-considerations)
14. [Recommendations & Improvement Ideas](#14-recommendations--improvement-ideas)

---

## 1. High-Level Architecture

The AI system in MKS is built on **two parallel network stacks** and **one on-device ML pipeline**, designed to handle different types of AI interactions:

| Stack | Transport | Primary Use |
|---|---|---|
| **Unified OpenAI-Compatible** (`AiClient`) | HTTP — `/v1/chat/completions` | MCQ extraction/generation, VLM OCR, text refinement |
| **Ollama Native** (`OllamaRepository`) | HTTP — `/api/generate` (streaming) | Prompt Deck token-by-token generation |
| **On-Device ML Kit** (`TextRecognition`) | Local — no network | Camera OCR (Scanner feature) |

### End-to-End Data Flow Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                           FRONTEND (Compose Screens)                        │
│  ┌──────────────┐  ┌─────────────────┐  ┌───────────┐  ┌────────────────┐  │
│  │AiMcqGenerator│  │PdfExtraction    │  │Scanner    │  │BookTools       │  │
│  │   Screen     │  │   Screen        │  │  Screen   │  │ (Prompt Deck)  │  │
│  └──────┬───────┘  └───────┬─────────┘  └─────┬─────┘  └──────┬─────────┘  │
│         │                  │                  │               │             │
│  ┌──────▼───────┐  ┌───────▼─────────┐  ┌────▼──────┐ ┌──────▼─────────┐   │
│  │AiMcqGenerator│  │PdfExtraction    │  │Scanner    │ │BookTools       │   │
│  │  ViewModel   │  │  ViewModel      │  │ ViewModel │ │ ViewModel      │   │
│  └──────┬───────┘  └───────┬─────────┘  └─────┬─────┘ └──────┬─────────┘   │
│         │                  │                  │               │             │
├─────────┼──────────────────┼──────────────────┼───────────────┼─────────────┤
│         │       DATA LAYER (Repositories & Services)         │             │
│  ┌──────▼───────┐  ┌───────▼─────────┐  ┌────▼──────┐ ┌──────▼─────────┐   │
│  │AiMcqRepository│ │   OcrService    │  │ ML Kit    │ │OllamaRepository│   │
│  └──────┬───────┘  └───────┬─────────┘  │TextRecogn.│ └──────┬─────────┘   │
│         │                  │            └───────────┘        │             │
│  ┌──────▼───────┐          │                                 │             │
│  │  McqService  │          │                                 │             │
│  └──────┬───────┘          │                                 │             │
│         │                  │                                 │             │
├─────────┼──────────────────┼─────────────────────────────────┼─────────────┤
│         │        NETWORK LAYER                               │             │
│         ▼                  ▼                                 ▼             │
│  ┌─────────────────────────────────┐    ┌──────────────────────────────┐   │
│  │          AiClient               │    │    OllamaRepository          │   │
│  │  (OpenAI-compat /v1/chat/...)   │    │  (Native /api/generate)      │   │
│  └──────────────┬──────────────────┘    └─────────────┬────────────────┘   │
│                 │                                     │                    │
├─────────────────┼─────────────────────────────────────┼────────────────────┤
│                 ▼                                     ▼                    │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                   17 AI Providers (LLM Backends)                     │  │
│  │  Ollama · Gemini · Groq · OpenRouter · Qwen · DeepSeek · Mistral   │  │
│  │  Together · Cohere · Cerebras · HuggingFace · Anthropic · Perplexity│  │
│  │  Fireworks · NVIDIA NIM · Zenmux · Custom OpenAI-Compatible         │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Network Layer (`core/network`)

### 2.1 `AiClient` — Primary Unified Client

- **File:** [AiClient.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/AiClient.kt) (275 lines)
- **Annotation:** `@Singleton` + `@Inject constructor()`
- **Purpose:** Unified OkHttp client targeting **any** provider that speaks the OpenAI `/v1/chat/completions` API.

#### Public API Surface

| Method | Purpose | Temperature | Response Format |
|---|---|---|---|
| `chatComplete()` | Text-only system+user chat | Configurable (default 0.1) | Plain text or JSON mode |
| `generateWithImage()` | Vision model with base64 images | Fixed 0.0 | Plain text |
| `listModels()` | `/v1/models` endpoint query | N/A | Model ID list |

#### Internal Mechanics

- **OkHttp Timeouts:** Connect 30s, Read 180s (large models need 2–3 min), Write 30s.
- **Retry Strategy:** Exponential backoff — 3 attempts, delays of 1s → 2s → 4s.
- **Fast-fail:** Non-retriable HTTP codes (400, 401, 403, 404, 422) throw immediately without retry.
- **Provider-specific JSON mode:**
  - Ollama/Ollama-server → `"format": "json"` (top-level).
  - All other providers → `"response_format": {"type": "json_object"}`.
- **Ollama context window:** Forces `num_ctx: 32768` via `options` block when `providerId.startsWith("ollama")`.
- **Auth injection:** Dynamic Bearer token; supports pre-formatted schemes (e.g., `"Bearer xyz"` or raw tokens).
- **Token usage logging:** Logs `prompt_tokens` and `completion_tokens` from the `usage` response object.

#### Key Observations

- The client uses **synchronous** `execute()` inside `withContext(Dispatchers.IO)` — not async callbacks.
- Error body is read with `response.body?.string()` which consumes the stream — correct, but the 300-char truncation in `AiHttpError` means large error messages are clipped.
- `Thread.sleep(backoffMs)` is used for backoff inside a coroutine dispatcher — **blocks the IO thread** instead of using `delay()`.

---

### 2.2 `OllamaRepository` — Native Streaming Client

- **File:** [OllamaRepository.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/repository/OllamaRepository.kt) (260 lines)
- **Annotation:** `@Singleton` + `@Inject constructor()`
- **Purpose:** Specialized client for Ollama's native `/api/generate` endpoint with **line-by-line streaming**.

#### Public API Surface

| Method | Purpose | Streaming |
|---|---|---|
| `generateCompletionStream()` | Token-by-token generation | Yes — `Flow<String>` |
| `listModels()` | `/api/tags` query | No |
| `testConnection()` | Connectivity + model availability check | No |

#### Internal Mechanics

- **Dual OkHttp Clients:**
  - `client` — long timeouts (Connect 30s, Read 120s, Write 30s) for generation.
  - `quickClient` — short timeouts (Connect 10s, Read 15s, Write 10s) for metadata queries.
- **Streaming protocol:** Reads NDJSON line-by-line via `BufferedReader`, parses each chunk with Moshi `OllamaResponseJsonAdapter`, emits `response` field tokens.
- **URL normalization:** Auto-prefixes `http://`, strips trailing `/v1` to ensure correct Ollama native API paths.
- **Error handling:** Parses Ollama error JSON (`{"error": "..."}`) both from HTTP error responses and from mid-stream error chunks.
- **Auth support:** Same `applyAuth()` pattern as `AiClient` — supports bare tokens and pre-formatted schemes.
- **Cancellation-safe:** Calls `currentCoroutineContext().ensureActive()` inside the streaming loop.

#### Key Observations

- `testConnection()` is a **never-throws** method — always returns a structured `OllamaConnectionResult` with `success`, `message`, and `models`.
- Model matching supports both exact match and base-name match (e.g., `"llama3:latest"` matches `"llama3"`).
- The streaming flow uses `flowOn(Dispatchers.IO)` — correct, but the consumer (`BookToolsViewModel`) accumulates via string concatenation (`accumulatedResponse += chunk`) which is O(n²) for large responses.

---

### 2.3 `RemoteAssetFetcher` — Image Download Client

- **File:** [RemoteAssetFetcher.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/RemoteAssetFetcher.kt) (67 lines)
- **Purpose:** Downloads remote images for asset references. **Not AI-related per se**, but part of the network module and used in the import pipeline alongside AI-extracted content.
- **Policy:** Governed by [RemoteAssetPolicy.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/RemoteAssetPolicy.kt) — controls max download size, content-type validation, and plain HTTP consent.

---

## 3. Intermediate Pipelines (Services)

### 3.1 `McqService` — 3-Pass MCQ Pipeline

- **File:** [McqService.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/McqService.kt) (248 lines)
- **Annotation:** `@Singleton` + `@Inject constructor(aiClient: AiClient)`
- **Purpose:** Orchestrates the full extract → generate → review MCQ pipeline.

#### Pipeline Stages

```
Input Text
    │
    ▼
┌─────────────────────────┐
│  1. TEXT CHUNKING        │   chunkText() — max 6000 chars per chunk
│  Split on page dividers  │   Prefers "--- PAGE N ---" breaks
│  or paragraph boundaries │   Falls back to \n\n splits
└──────────┬──────────────┘
           │  For each chunk:
           ▼
┌─────────────────────────┐
│  2. EXTRACTION (T=0.1)  │   System: EXT_DEFAULT or EXT_SIMPLE
│  Find existing MCQs     │   JSON mode enabled
│  in the text             │   Parses via ParsedMcq.parseJsonArray()
└──────────┬──────────────┘
           │  If empty:
           ▼
┌─────────────────────────┐
│  3. GENERATION (T=0.5)  │   System: GEN_DEFAULT or GEN_SIMPLE
│  Create original MCQs   │   JSON mode enabled
│  from the content        │   Clinical vignette style
└──────────┬──────────────┘
           │  If questions found & review enabled:
           ▼
┌─────────────────────────┐
│  4. REVIEW (T=0.1)      │   System: REV_DEFAULT
│  Verify answer keys     │   Enriches: stem_brief, explanation_brief,
│  Add hint, high_yield   │   hint, high_yield fields
└──────────┬──────────────┘
           │
           ▼
┌─────────────────────────┐
│  5. DEDUPLICATION       │   Jaccard similarity on stem word tokens
│  Threshold: 0.85        │   Renumbers sequentially after removing dupes
└─────────────────────────┘
```

#### Configuration (`McqRunConfig`)

| Field | Default | Purpose |
|---|---|---|
| `provider` | — | `AiProviderConfig` with model, URL, key |
| `chapterNum` | `"1"` | Chapter label for `ch_q` numbering |
| `sectionName` | `"Section"` | Human-readable section title for prompts |
| `extractionPrompt` | `EXT_DEFAULT` | System prompt for extraction pass |
| `generationPrompt` | `GEN_DEFAULT` | System prompt for generation fallback |
| `reviewPrompt` | `REV_DEFAULT` | System prompt for review pass (`null` = skip) |
| `reviewEnabled` | `true` | Master toggle for the review pass |
| `chunkSizeChars` | `6000` | ~1500 tokens per chunk |

#### Key Observations

- Each pass is **independently fault-tolerant** — `runCatching` + `getOrElse` ensures a single chunk failure doesn't abort the entire pipeline.
- Progress callbacks (`onProgress`) are invoked after each chunk completes.
- `chunkText()` has a smart fallback: page dividers → paragraph boundaries → raw substring.
- Deduplication uses lowercase ASCII tokenization — **Arabic/non-Latin text stems will be stripped entirely**, causing false-negative deduplication (Arabic questions will never deduplicate against each other).

---

### 3.2 `OcrService` — Vision-Language Pipeline

- **File:** [OcrService.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/OcrService.kt) (121 lines)
- **Annotation:** `@Singleton` + `@Inject constructor(aiClient: AiClient)`
- **Purpose:** Orchestrates VLM OCR on page images.

#### Pipeline Stages

| Stage | Method | Temperature | Input | Output |
|---|---|---|---|---|
| 1. Vision Extraction | `AiClient.generateWithImage()` | 0.0 | Base64 image | Raw OCR text |
| 2. Text Cleanup (Optional) | `AiClient.chatComplete()` | 0.0 | Raw OCR text | Cleaned text |
| 3. Combine | Internal | — | Per-page text | Combined text with `--- PAGE N ---` dividers |

#### Public API

| Method | Purpose | Uses Vision? |
|---|---|---|
| `processPages()` | Full OCR pipeline on image list | Yes |
| `refineRawText()` | Text-only cleanup of raw PDF text | No |

#### Key Observations

- Images are processed **sequentially** (one page at a time) — no batch processing or parallel page OCR.
- The cleanup pass uses the **same model** as the vision pass — there's no separate "fast text model" configuration.
- Failed pages emit `"[OCR FAILED ON PAGE X: message]"` inline — errors are embedded in the output text, not thrown.
- `refineRawText()` is also used by `PdfExtractionViewModel.reviewBlock()` for post-processing raw PDF text extractions.

---

## 4. PDF & Image Pre-Processing

### 4.1 `PdfRendererService` — Page-to-Bitmap Converter

- **File:** [PdfRendererService.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/PdfRendererService.kt) (74 lines)
- **Engine:** Android `PdfRenderer` (native API).
- **Default DPI:** 300 for OCR rendering, 150 for UI thumbnails.
- **Output:** `Bitmap` objects (ARGB_8888, white background).

### 4.2 `PdfTextExtractor` — Raw Text Layer Extraction

- **File:** [PdfTextExtractor.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/PdfTextExtractor.kt) (51 lines)
- **Engine:** PdfBox (`com.tom_roush.pdfbox`) — **no AI involved**.
- **Memory strategy:** `MemoryUsageSetting.setupTempFileOnly()` — avoids OOM on large PDFs by using temp files.
- **Note:** This is the **non-AI path** — used when the user selects "Extract Text" instead of "AI Vision OCR" in the PDF extraction screen.

---

## 5. Models & Configurations (`core/model`)

### 5.1 AI Provider Registry (`AiProviderConfig.kt`)

- **File:** [AiProviderConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/AiProviderConfig.kt) (183 lines)

**Runtime Config** (`AiProviderConfig`) — 4 fields passed through the entire AI pipeline:

| Field | Type | Description |
|---|---|---|
| `providerId` | `String` | Matches `AI_PROVIDERS` list (e.g., `"ollama"`, `"groq"`) |
| `baseUrl` | `String` | API endpoint without trailing slash |
| `apiKey` | `String` | Bearer token (blank for local Ollama) |
| `model` | `String` | Model name for this specific call |

**17 Pre-configured Providers** (`AI_PROVIDERS` list):

| # | Provider | Default Chat Model | Default Vision Model | Requires Key |
|---|---|---|---|---|
| 1 | Ollama (Local) | `gemma4:12b` | `glm-ocr-strict:latest` | No |
| 2 | Google Gemini | `gemini-2.5-flash` | `gemini-2.5-flash` | Yes |
| 3 | Groq (LPU) | `llama-3.3-70b-versatile` | `llama-4-scout-17b-16e-instruct` | Yes |
| 4 | OpenRouter | `google/gemini-flash-1.5` | `google/gemini-flash-1.5` | Yes |
| 5 | Alibaba Qwen | `qwen-plus` | `qwen-vl-max` | Yes |
| 6 | DeepSeek | `deepseek-chat` | — | Yes |
| 7 | Mistral AI | `mistral-large-latest` | `pixtral-large-latest` | Yes |
| 8 | Together AI | `meta-llama/Llama-3-70b-chat-hf` | `meta-llama/Llama-4-Scout-17B-16E-Instruct-Turbo` | Yes |
| 9 | Cohere | `command-r-plus` | — | Yes |
| 10 | Cerebras | `llama3.1-70b` | — | Yes |
| 11 | Hugging Face | `meta-llama/Meta-Llama-3-8B-Instruct` | — | Yes |
| 12 | Anthropic (Claude) | `claude-3-5-haiku-20241022` | `claude-3-5-sonnet-20241022` | Yes |
| 13 | Perplexity | `llama-3.1-sonar-large-128k-online` | — | Yes |
| 14 | Fireworks AI | `accounts/fireworks/models/llama-v3p1-70b-instruct` | — | Yes |
| 15 | NVIDIA NIM | `meta/llama-3.1-70b-instruct` | `nvidia/llama-3.2-90b-vision-instruct` | Yes |
| 16 | Zenmux | `deepseek/deepseek-chat` | — | Yes |
| 17 | Custom OpenAI-Compatible | `gpt-4o` | `gpt-4o` | Yes |

### 5.2 Domain Model (`ParsedMcq.kt`)

- **File:** [ParsedMcq.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/ParsedMcq.kt) (154 lines)
- **10 fields:** `chQ`, `stem`, `stemBrief`, `options` (A–E map), `key`, `explanation`, `explanationBrief`, `hint`, `highYield`, `generated`.

**JSON Parsing Resilience:**
- Strips markdown fences (`\`\`\`json`, `\`\`\``) from LLM output.
- Accepts both bare arrays `[...]` and wrapped objects `{"questions":[...]}`, `{"mcqs":[...]}`, `{"data":[...]}`.
- Tolerates missing fields — `fromJson()` returns `null` only if `stem` is blank.
- `key` normalization: uppercased, filters out `"NULL"` strings.

**Entity Conversion (`toQuestionEntity()`):**
- Maps options A–E in order, prepending letter labels.
- Resolves correct answer index from the key letter.
- Maps `hint` → `QuestionEntity.hint`, `highYield` → `QuestionEntity.additionalInfo`.
- Generates unique `externalId` using `"ai_{ch_q}_{nanoTime}"`.

### 5.3 Knowledge Bank Generation Configs

These configs control how **quiz questions are transformed** into other knowledge bank asset types. They are **not AI-driven** — they are mapping configurations for deterministic field-to-field transformations:

| Config | File | Purpose |
|---|---|---|
| `FlashcardGenerationConfig` | [FlashcardGenerationConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/FlashcardGenerationConfig.kt) | Maps question fields → flashcard front/back |
| `SlideGenerationConfig` | [SlideGenerationConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/SlideGenerationConfig.kt) | Maps question fields → slide title/body/notes |
| `ArticleGenerationConfig` | [ArticleGenerationConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/ArticleGenerationConfig.kt) | Maps question fields → note blueprint body |

### 5.4 Ollama Data Models (`OllamaModels.kt`)

- **File:** [OllamaModels.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/OllamaModels.kt) (26 lines)
- **`OllamaRequest`:** `model`, `prompt`, `system`, `stream`, `options`, `images` — Moshi-serialized.
- **`OllamaResponse`:** `model`, `createdAt`, `response`, `done` — Moshi-deserialized line-by-line from NDJSON stream.

---

## 6. Prompt Engineering Catalog

- **File:** [McqPrompts.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/McqPrompts.kt) (231 lines)

### Template Tokens

| Token | Replaced With | Example |
|---|---|---|
| `{CHAPTER_NUM}` | Chapter/section number | `"2"` |
| `{SECTION_NAME}` | Section title | `"Cardiac Physiology"` |
| `{START_NUM}` | First question number in chunk | `"7"` |
| `{NEXT_NUM}` | `START_NUM + 1` | `"8"` |

### Prompt Inventory (11 Prompts)

| ID | Category | Temperature | Key Directives |
|---|---|---|---|
| `OCR_DEFAULT` | Vision OCR | 0.0 | Strict transcription, structural tags `[TABLE]`, `[FIGURE]`, `[ILLEGIBLE]`, math notation |
| `OCR_REVIEW_DEFAULT` | Text Cleanup | 0.0 | Fix OCR misreads (rn→m, l→1, 0→O), preserve structural tags and math |
| `EXT_DEFAULT` | MCQ Extraction (Full) | 0.1 | Rich 10-field JSON schema, normalize options A–E, extract answer keys |
| `EXT_SIMPLE` | MCQ Extraction (7B–13B) | 0.1 | Minimal 4-field schema (`ch_q`, `stem`, `options`, `key`) |
| `GEN_DEFAULT` | MCQ Generation (Full) | 0.5 | Clinical vignettes, applied scenarios, plausible distractors, full explanation |
| `GEN_SIMPLE` | MCQ Generation (7B–13B) | 0.5 | Minimal schema for small models |
| `REV_DEFAULT` | MCQ Review/Enrichment | 0.1 | Verify answer keys, add `stem_brief` (≤12 words), `explanation_brief` (≤25 words), `hint`, `high_yield` |
| *(Inline)* | Prompt Deck System | — | Hardcoded in `BookToolsViewModel` L744: "You are an expert educational AI assistant..." |
| *(Inline)* | Test Call | — | Hardcoded in `SettingsViewModel` L131 & `PdfExtractionViewModel` L308: "You are a helpful test assistant." |
| *(N/A)* | Scanner OCR | — | Uses ML Kit — no LLM prompt needed |
| *(N/A)* | PdfBox Extraction | — | Uses PdfBox — no LLM prompt needed |

### Prompt Design Observations

- All prompts end with explicit output formatting directives ("No markdown fences, no commentary").
- The `GEN_DEFAULT` prompt specifically guides against "which of the following is TRUE/FALSE" stems.
- The `REV_DEFAULT` prompt has **strict constraints** — it must NOT modify `ch_q`, `stem`, `options`, `explanation`, or `generated`.
- The Prompt Deck system prompt (in `BookToolsViewModel`) is **hardcoded inline** — not centralized in `McqPrompts`.

---

## 7. Data & Repository Layer (`core/data`)

### 7.1 `AiMcqRepository` — Pipeline-to-Persistence Bridge

- **File:** [AiMcqRepository.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/data/src/main/java/com/ahmedyejam/mks/data/repository/AiMcqRepository.kt) (159 lines)
- **Annotation:** `@Singleton` + `@Inject constructor(mcqService, quizRepository, dataStoreManager)`

#### Workflow (`generateAndSave()`)

```
1. Read AI settings from DataStoreManager (7 preferences)
2. Construct AiProviderConfig + McqRunConfig
3. Invoke McqService.processMCQ() with progress callback
4. Create QuizEntity (title, description, question count)
5. Convert ParsedMcq → QuestionEntity list (via toQuestionEntity())
6. Batch-insert via QuizRepository.insertQuestions()
7. Emit AiMcqProgress.Done(count, quizId)
```

#### Progress State Machine

```
Idle → Processing(chunk, totalChunks, foundSoFar) → Done(count, quizId)
                                                  → Error(message)
```

#### Key Observations

- The `extractionMode` preference controls prompt selection: `"simple"` → `EXT_SIMPLE`/`GEN_SIMPLE`; anything else → `EXT_DEFAULT`/`GEN_DEFAULT`.
- `currentProviderConfig()` builds a snapshot config from DataStore — useful for preview/testing.
- **Quiz description includes metadata:** `"Generated from: $sectionName (Chapter $chapterNum) · N questions"`.

---

## 8. Persistent AI Settings — DataStore

- **File:** [DataStoreManager.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/data/src/main/java/com/ahmedyejam/mks/data/preferences/DataStoreManager.kt) (lines 63–259)

### AI Preference Keys

| DataStore Key | Type | Default | Used By |
|---|---|---|---|
| `ai_provider_id` | `String` | `"ollama"` | All AI features |
| `ai_base_url` | `String` | `"http://10.0.2.2:11434/v1"` | All AI features |
| `ai_api_key` | `String` | `""` | All AI features |
| `ai_chat_model` | `String` | `"llama3.1:latest"` | MCQ pipeline, Prompt Deck, test calls |
| `ai_vision_model` | `String` | `""` (falls back to chat model) | Vision OCR pipeline |
| `ai_mcq_review_enabled` | `Boolean` | `true` | MCQ pipeline (review pass toggle) |
| `ai_mcq_extraction_mode` | `String` | `"full"` | MCQ pipeline (prompt selection) |

### Key Observations

- The **default base URL** (`http://10.0.2.2:11434/v1`) targets the Android emulator's localhost — different from the provider descriptor's default (`http://192.168.1.164:11434/v1`) which targets a LAN machine. This mismatch could confuse users.
- The **default chat model** (`llama3.1:latest`) differs from the provider descriptor's default (`gemma4:12b`). The DataStore default wins at runtime.
- All AI flows read preferences via `Flow<T>.first()` — this is a **suspend call** that blocks until the first emission, which is fine for one-shot reads but inefficient if called repeatedly.
- The vision model falls back to the chat model if blank — but this fallback logic is **not in DataStoreManager itself** but must be implemented by each consumer. Currently, `PdfExtractionViewModel` doesn't implement this fallback.

---

## 9. Frontend UI & ViewModels (`feature/ui`)

### 9.1 AI MCQ Generator

| Component | File | Lines |
|---|---|---|
| Screen | [AiMcqGeneratorScreen.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/AiMcqGeneratorScreen.kt) | 527 |
| ViewModel | [AiMcqGeneratorViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/AiMcqGeneratorViewModel.kt) | 178 |

**3-Step UI Flow:**
1. **Input:** Paste/import educational text, set chapter number, section name, quiz title.
2. **Configure:** Select AI provider via `ProviderConfigDialog`, toggle Review Pass and Simple Mode.
3. **Generate:** Progress indicator with chunk count and MCQ count. Cancel button available.

**ViewModel Features:**
- Mirrors `AiMcqProgress` flow into UI state.
- Exposes `availableProviders` (all 17) for the picker.
- `cancelGeneration()` cancels via `viewModelScope.coroutineContext[Job]?.cancelChildren()`.
- Saves AI settings back to DataStore via `saveAiProvider()`.

### 9.2 PDF / Image Extraction

| Component | File | Lines |
|---|---|---|
| Screen | [PdfExtractionScreen.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/PdfExtractionScreen.kt) | ~430 |
| ViewModel | [PdfExtractionViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/PdfExtractionViewModel.kt) | 320 |

**Dual Extraction Paths:**
- **Non-AI:** `extractViaText()` → `PdfTextExtractor` → raw text from PDF text layer.
- **Vision AI:** `extractViaVision()` → renders pages to 300 DPI bitmaps → base64 JPEG → `OcrService.processPages()`.

**Post-Extraction Actions per Block:**
- Edit inline.
- Save as Note (`NoteBlueprintEntity`).
- Run AI Review (`OcrService.refineRawText()`).
- Copy for MCQs (clipboard + navigation).

**ViewModel Also Includes:**
- `pingProvider()` — tests connection to selected AI provider.
- `fetchModels()` — lists available models from the endpoint.
- `testCall()` — sends a test message to validate the provider.

**Key Observations:**
- Page bitmaps are cached in a `mutableMapOf<Int, Bitmap>` with explicit `recycle()` in `onCleared()` — proper lifecycle management.
- Base64 encoding uses `Base64.NO_WRAP` — correct for API payloads.
- Image compression is JPEG at 90% quality — a good balance of size vs. OCR accuracy.
- Supports both PDF and IMAGE source types (single image = 1 page).

### 9.3 Prompt Deck (Streaming AI Generation)

| Component | File | Lines |
|---|---|---|
| ViewModel | [BookToolsViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookToolsViewModel.kt) | 1125 (AI: L727–L791) |

**`generateWithOllamaStream()`:**
- Reads `baseUrl`, `model`, `apiKey` from DataStore.
- Uses `OllamaRepository.generateCompletionStream()` — streaming flow.
- Accumulates tokens into `accumulatedResponse` and calls `onUpdate(accumulatedResponse)` for real-time typing effect.
- Supports optional `images` parameter for vision prompts.
- Cancellable via `cancelGeneration()` which cancels the `generationJob`.

**Post-Generation Actions:**
- `savePromptOutputAsNote()` — converts output to `NoteBlueprintEntity`.
- `savePromptOutputAsBlueprint()` — similar conversion with different formatting.
- `recordPromptRun()` — persists the run to `PromptRunEntity` for history tracking.

### 9.4 Settings & Provider Configuration

| Component | File | Lines |
|---|---|---|
| Dialog | [ProviderConfigDialog.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/ProviderConfigDialog.kt) | 273 |
| ViewModel | [SettingsViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/SettingsViewModel.kt) | 142 |

**`ProviderConfigDialog` Features:**
- `FilterChip` horizontal row for provider selection.
- Editable fields: Base URL, Model Name (with fetched models dropdown), API Key (password masked).
- Optional AI Prompt editing field.
- Three test buttons: **Ping** (connection test), **Fetch** (list models), **Test** (send completion).
- "Restore Defaults" button resets to provider descriptor defaults.
- Per-provider maps (`apiKeysMap`, `baseUrlsMap`, `modelsMap`) — settings persist per-provider within the dialog session.

**Reused Across:**
- Settings screen (`SettingsViewModel`).
- PDF Extraction screen (`PdfExtractionViewModel`).
- AI MCQ Generator screen (`AiMcqGeneratorViewModel`).

---

## 10. On-Device ML Kit Integration

### Camera Scanner

| Component | File | Lines |
|---|---|---|
| Screen | [ScannerScreen.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerScreen.kt) | ~520 |
| ViewModel | [ScannerViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerViewModel.kt) | 183 |

**Engine:** Google ML Kit `TextRecognition` with `TextRecognizerOptions.DEFAULT_OPTIONS` (Latin script).

**Pipeline:**
1. Camera capture → `Bitmap`.
2. `InputImage.fromBitmap()` → ML Kit processing.
3. `recognizer.process(image).await()` → recognized text.
4. **Regex-based MCQ parsing** (no AI):
   - Question detection: `^(?:Q(?:uestion)?\s*)?(\d+)[\.)\:]\s+(.*)`.
   - Option detection: `^([*])?\s*([A-Ea-e]|[1-5])[\.)\-\s]\s*(.*)`.
   - Bullet option fallback: `^([*])?\s*[\-•]\s+(.*)`.
   - Asterisk-marked options (`*A. ...`) are treated as correct answers.
5. If regex parsing finds no questions, falls back to splitting text on `\n\n` and creating one question per block.

**Key Observations:**
- ML Kit is **Latin-only** — no Arabic OCR support despite MKS having Arabic localization.
- The regex parser is **purely local** — no LLM involvement. This means it can't handle complex formatting or ambiguous question structures.
- Recognized questions are saved via `knowledgeRepository.insertQuestions()`.
- The recognizer is properly closed in `onCleared()`.

---

## 11. Dependency Injection Setup

The entire AI stack is auto-wired by Dagger Hilt — **no manual modules required** for AI components:

```
@Singleton @Inject constructor()
├── AiClient
├── OllamaRepository
├── McqService(aiClient)
├── OcrService(aiClient)
├── PdfRendererService(@ApplicationContext)
├── PdfTextExtractor(@ApplicationContext)
└── AiMcqRepository(mcqService, quizRepository, dataStoreManager)

@HiltViewModel @Inject constructor(...)
├── AiMcqGeneratorViewModel(aiMcqRepository, dataStoreManager)
├── PdfExtractionViewModel(context, assetRepo, knowledgeRepo, pdfRendererService,
│                           pdfTextExtractor, ocrService, dataStoreManager, aiClient, ollamaRepository)
├── BookToolsViewModel(ollamaRepository, dataStoreManager, fileManager, bookRepo,
│                       knowledgeRepo, assetRepo, quizRepo, studyRepo)
├── SettingsViewModel(exportManager, workspaceRepo, quizRepo, assetRepo,
│                      ollamaRepository, dataStoreManager, focusManager, aiClient)
└── ScannerViewModel(knowledgeRepository)  — ML Kit only, no LLM deps
```

**Note:** `PdfExtractionViewModel` injects **both** `AiClient` and `OllamaRepository` — it uses `OllamaRepository.testConnection()` for Ollama providers and `AiClient.listModels()` for OpenAI-compatible providers.

---

## 12. Error Handling & Resilience Audit

### Network-Level

| Component | Strategy | Assessment |
|---|---|---|
| `AiClient.fetchWithRetry()` | 3 retries, exponential backoff, non-retriable fast-fail | ✅ Good — but uses `Thread.sleep()` instead of `delay()` |
| `OllamaRepository` stream | `CancellationException` rethrown, timeout → `OllamaApiException` | ✅ Good |
| `OllamaRepository.testConnection()` | Never-throws, structured result | ✅ Excellent |

### Pipeline-Level

| Component | Strategy | Assessment |
|---|---|---|
| `McqService` per-chunk | `runCatching` + `getOrElse(emptyList())` | ✅ Fault-tolerant per chunk |
| `McqService` review pass | Falls back to unreviewed MCQs on failure | ✅ Good degradation |
| `OcrService` per-page | `runCatching` + error marker in text | ⚠️ Errors embedded in output text |
| `OcrService.refineRawText()` | `runCatching` + prepends `[REFINEMENT FAILED]` | ⚠️ Same issue |

### Repository-Level

| Component | Strategy | Assessment |
|---|---|---|
| `AiMcqRepository` | `try/catch` → `AiMcqProgress.Error` emission | ✅ Good — clean state machine |
| `AiMcqRepository` cancellation | `CancellationException` → reset to Idle | ✅ Correct |

### ViewModel-Level

| Component | Strategy | Assessment |
|---|---|---|
| `BookToolsViewModel` streaming | Separate `CancellationException`, `OllamaApiException`, generic `Exception` | ✅ Good |
| `PdfExtractionViewModel` | Per-block error state with `ExtractionStatus.ERROR` | ✅ Good |
| `ScannerViewModel` | Single `try/catch` → `ScannerUiState.Error` | ✅ Adequate |

---

## 13. Security Considerations

| Area | Current State | Risk Level |
|---|---|---|
| API Key Storage | Jetpack DataStore (unencrypted SharedPreferences under the hood) | ⚠️ Medium — keys readable with root/ADB |
| API Key Transmission | Bearer header over HTTPS (except Ollama default: `http://`) | ⚠️ Medium — Ollama default is plain HTTP |
| API Key Display | `PasswordVisualTransformation()` in UI | ✅ Good |
| PDF/Image Data | Base64-encoded in request body — sent to remote LLM providers | ⚠️ Medium — sensitive documents leave device |
| Prompt Injection | User-provided text is sent directly as the `user` message | ⚠️ Low — system prompts are well-structured |
| ML Kit (Scanner) | Fully on-device — no data leaves the device | ✅ Excellent |

---

## 14. Recommendations & Improvement Ideas

### 🔴 Critical (High Impact, Low Effort)

#### R1: Replace `Thread.sleep()` with `delay()` in AiClient Retry Loop
- **File:** [AiClient.kt L258](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/AiClient.kt#L258)
- **Issue:** `Thread.sleep()` blocks the IO dispatcher thread during backoff, preventing it from serving other coroutines.
- **Fix:** Replace with `kotlinx.coroutines.delay(backoffMs)`.

#### R2: Fix Arabic/Non-Latin Deduplication in McqService
- **File:** [McqService.kt L224](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/McqService.kt#L224)
- **Issue:** `tokenize()` strips all non-ASCII characters (`[^a-z0-9\\s]`), causing Arabic question stems to produce empty token sets — deduplication effectively skips all Arabic content.
- **Fix:** Use `\\p{L}` (Unicode letter) instead of `[a-z]` in the regex. Consider a Unicode-aware word boundary tokenizer.

#### R3: Fix DataStore Default URL/Model Mismatch
- **File:** [DataStoreManager.kt L228, L240](file:///Users/ahmedejam/Projects/MKS%20android/core/data/src/main/java/com/ahmedyejam/mks/data/preferences/DataStoreManager.kt#L228)
- **Issue:** DataStore defaults (`http://10.0.2.2:11434/v1`, `llama3.1:latest`) don't match the `AiProviderDescriptor` defaults (`http://192.168.1.164:11434/v1`, `gemma4:12b`). First-time users on real devices get emulator-only defaults.
- **Fix:** Source DataStore defaults from `AI_PROVIDERS.first()` or detect emulator vs. real device.

#### R4: Encrypt API Keys at Rest
- **File:** [DataStoreManager.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/data/src/main/java/com/ahmedyejam/mks/data/preferences/DataStoreManager.kt)
- **Issue:** API keys stored in plain text in DataStore (backed by SharedPreferences XML).
- **Fix:** Use AndroidX `EncryptedSharedPreferences` or the `security-crypto` library for the API key field specifically.

---

### 🟡 Important (Medium Impact)

#### R5: Implement Vision Model Fallback Consistently
- **Issue:** `DataStoreManager.aiVisionModel` documents "Falls back to aiChatModel if blank" but the fallback is **not implemented** in `PdfExtractionViewModel`. Users selecting a provider without a vision model will send the chat model name to the vision endpoint.
- **Fix:** Add fallback logic in `AiMcqRepository` and `PdfExtractionViewModel` when building `AiProviderConfig` for vision calls. Consider adding a `resolvedVisionModel` property to `AiProviderConfig`.

#### R6: Add Streaming Support to AiClient (Replace OllamaRepository for /v1)
- **Issue:** The app maintains two separate HTTP stacks. `OllamaRepository` is used for streaming Prompt Deck generation, but `AiClient` already connects to the same `/v1` endpoints. This creates duplication and limits Prompt Deck streaming to Ollama-compatible servers only.
- **Fix:** Add a `chatCompleteStream()` method to `AiClient` that returns `Flow<String>` using SSE parsing, enabling streaming from **any** OpenAI-compatible provider (Groq, Gemini, etc.) for the Prompt Deck feature.

#### R7: Fix StringBuilder Concatenation in Prompt Deck Streaming
- **File:** [BookToolsViewModel.kt L746-L758](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookToolsViewModel.kt#L746)
- **Issue:** `accumulatedResponse += chunk` creates a new String object per token — O(n²) memory allocation for long responses.
- **Fix:** Use `StringBuilder` and emit `sb.toString()`.

#### R8: Centralize the Prompt Deck System Prompt
- **File:** [BookToolsViewModel.kt L744](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookToolsViewModel.kt#L744)
- **Issue:** The Prompt Deck system prompt is hardcoded inline in the ViewModel instead of being centralized in `McqPrompts`.
- **Fix:** Add `PROMPT_DECK_SYSTEM` constant to `McqPrompts` for consistency, versioning, and A/B testability.

#### R9: Parallelize OCR Page Processing
- **File:** [OcrService.kt L49](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/OcrService.kt#L49)
- **Issue:** Pages are processed sequentially. For a 20-page document, this means 20 serial API calls — potentially 10+ minutes.
- **Fix:** Use `coroutineScope { }` with limited parallelism (e.g., `Semaphore(3)`) to process 3 pages concurrently.

#### R10: Add ML Kit Arabic Script Support to Scanner
- **File:** [ScannerViewModel.kt L32](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerViewModel.kt#L32)
- **Issue:** `TextRecognizerOptions.DEFAULT_OPTIONS` only supports Latin script. MKS supports Arabic localization.
- **Fix:** Use `TextRecognition.getClient(new TextRecognizerOptions.Builder().build())` with the Devanagari/Arabic script recognizer, or switch to ML Kit's `TextRecognition.getClient(TextRecognizerOptions.Builder().build())` which auto-detects script in newer versions. Alternatively, detect the user's language preference and load the appropriate script model.

#### R11: Add Token Cost Estimation Before Generation
- **Issue:** Users have no visibility into how much a generation run will cost or how many tokens will be consumed before starting.
- **Fix:** Add a pre-flight estimation in `AiMcqRepository` that calculates approximate token count from input text length (÷4 for English, ÷2 for Arabic/CJK) and multiplies by the number of pipeline passes (extraction + optional review). Display in the UI as "Estimated: ~X tokens, ~$Y.YY".

---

### 🟢 Enhancement Ideas (Nice-to-Have)

#### R12: Add Conversation History to Prompt Deck
- **Issue:** `generateWithOllamaStream()` sends a single-turn prompt — no conversation history. Users can't iteratively refine AI output.
- **Fix:** Maintain a `List<Message>` (role + content) per Prompt Deck session. Send the full history with each call using `OllamaRepository`'s chat endpoint or switch to `AiClient.chatComplete()` with multi-turn messages.

#### R13: Add Rate Limiting / Quota Tracking per Provider
- **Issue:** No tracking of API call counts, token usage, or cost per provider. Users on metered plans can accidentally exhaust their quota.
- **Fix:** Add a `UsageTracker` repository that persists `(providerId, date, promptTokens, completionTokens)` tuples from the `usage` response object that `AiClient` already logs.

#### R14: Add Structured Output via JSON Schema (Gemini/OpenAI)
- **Issue:** MCQ generation relies on `response_format: {"type": "json_object"}` which only asks the model to output valid JSON — it doesn't enforce the MCQ schema.
- **Fix:** For providers that support it (Gemini, GPT-4o), use `response_format: {"type": "json_schema", "json_schema": {...}}` to enforce the exact `ParsedMcq` schema, eliminating parsing failures.

#### R15: Add Offline MCQ Generation Queue
- **Issue:** Generation requires an active network connection. If the user's internet drops mid-chunk, the pipeline fails.
- **Fix:** Queue generation requests in WorkManager. Each chunk becomes a work item that retries with exponential backoff. Results are merged and persisted when all chunks complete.

#### R16: Implement Model Capability Verification
- **Issue:** Users can select any model for vision OCR, but not all models support image input. Sending images to a text-only model silently fails or produces garbage.
- **Fix:** After fetching models via `/v1/models`, check for vision capability flags in the model metadata. Warn users or filter the model dropdown for vision-specific tasks.

#### R17: Add AI-Powered Flashcard Generation from Text
- **Issue:** `FlashcardGenerationConfig` only maps existing question fields to flashcard format — it doesn't generate flashcards directly from text.
- **Fix:** Create a `FlashcardService` in `core/network` that takes raw text and generates flashcard front/back pairs using a dedicated prompt, similar to how `McqService` generates MCQs.

#### R18: Add AI-Powered Note Summarization
- **Issue:** Users must manually edit extracted text into notes. There's no AI summarization step.
- **Fix:** Add a `SummarizationService` that takes raw extracted text and produces structured note content (outline, key points, definitions) using a dedicated prompt.

#### R19: Improve Provider Autodiscovery
- **Issue:** Users must manually type base URLs and model names. For Ollama, the app could auto-discover local servers.
- **Fix:** Add mDNS/Bonjour discovery for Ollama servers on the local network. For cloud providers, pre-populate the model dropdown by fetching on dialog open (currently requires clicking "Fetch").

#### R20: Add Generation History & Replay
- **Issue:** MCQ generation results are immediately persisted — there's no way to review what was generated, compare runs, or rollback.
- **Fix:** Add a `GenerationRunEntity` that records timestamp, provider, model, input text hash, output MCQ count, and a link to the created quiz. This enables an "AI History" tab in the book dashboard.

#### R21: Separate AI Module from `core/network`
- **Issue:** AI services (`AiClient`, `McqService`, `OcrService`) live alongside non-AI utilities (`RemoteAssetFetcher`, `RemoteAssetPolicy`) in `core/network`. The module also contains `PdfRendererService` and `PdfTextExtractor` which aren't network-related.
- **Fix:** Create a `core/ai` module for AI-specific code. Move `AiClient`, `McqService`, `OcrService`, `PdfRendererService`, `PdfTextExtractor` into it. Keep `RemoteAssetFetcher` in `core/network`.

#### R22: Add Automated Testing for AI Pipelines
- **Issue:** There are no unit tests for `McqService.chunkText()`, `McqService.deduplicate()`, `ParsedMcq.parseJsonArray()`, or `OcrService` logic. These are pure functions that are highly testable.
- **Fix:** Add unit tests with recorded LLM responses (golden files) to validate parsing logic, deduplication thresholds, and prompt rendering. Use a mock `AiClient` for integration tests.

---

## Appendix: Complete AI File Inventory

| Layer | File | Lines | Size | Key Class |
|---|---|---|---|---|
| **Network** | [AiClient.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/AiClient.kt) | 275 | 11KB | `AiClient` |
| **Network** | [OllamaRepository.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/repository/OllamaRepository.kt) | 260 | 11KB | `OllamaRepository` |
| **Network** | [RemoteAssetFetcher.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/RemoteAssetFetcher.kt) | 67 | 3KB | `RemoteAssetFetcher` |
| **Network** | [RemoteAssetPolicy.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/RemoteAssetPolicy.kt) | 22 | 1KB | `RemoteAssetPolicy` |
| **Pipeline** | [McqService.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/McqService.kt) | 248 | 11KB | `McqService` |
| **Pipeline** | [OcrService.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/OcrService.kt) | 121 | 5KB | `OcrService` |
| **Pre-process** | [PdfRendererService.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/PdfRendererService.kt) | 74 | 3KB | `PdfRendererService` |
| **Pre-process** | [PdfTextExtractor.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/network/src/main/java/com/ahmedyejam/mks/data/network/PdfTextExtractor.kt) | 51 | 2KB | `PdfTextExtractor` |
| **Model** | [AiProviderConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/AiProviderConfig.kt) | 183 | 6KB | `AiProviderConfig`, `AI_PROVIDERS` |
| **Model** | [McqPrompts.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/McqPrompts.kt) | 231 | 11KB | `McqPrompts` |
| **Model** | [ParsedMcq.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/ParsedMcq.kt) | 154 | 7KB | `ParsedMcq` |
| **Model** | [OllamaModels.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/OllamaModels.kt) | 26 | 1KB | `OllamaRequest`, `OllamaResponse` |
| **Model** | [FlashcardGenerationConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/FlashcardGenerationConfig.kt) | 21 | 1KB | `FlashcardGenerationConfig` |
| **Model** | [SlideGenerationConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/SlideGenerationConfig.kt) | 24 | 1KB | `SlideGenerationConfig` |
| **Model** | [ArticleGenerationConfig.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/model/src/main/java/com/ahmedyejam/mks/data/model/ArticleGenerationConfig.kt) | 16 | 0.5KB | `ArticleGenerationConfig` |
| **Data** | [AiMcqRepository.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/data/src/main/java/com/ahmedyejam/mks/data/repository/AiMcqRepository.kt) | 159 | 7KB | `AiMcqRepository` |
| **Data** | [DataStoreManager.kt](file:///Users/ahmedejam/Projects/MKS%20android/core/data/src/main/java/com/ahmedyejam/mks/data/preferences/DataStoreManager.kt) | 449 | 17KB | `DataStoreManager` (AI: L63–L259) |
| **UI** | [AiMcqGeneratorScreen.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/AiMcqGeneratorScreen.kt) | 527 | 25KB | MCQ Generator Screen |
| **UI** | [AiMcqGeneratorViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/AiMcqGeneratorViewModel.kt) | 178 | 7KB | MCQ Generator ViewModel |
| **UI** | [PdfExtractionScreen.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/PdfExtractionScreen.kt) | ~430 | 17KB | PDF Extraction Screen |
| **UI** | [PdfExtractionViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/PdfExtractionViewModel.kt) | 320 | 13KB | PDF Extraction ViewModel |
| **UI** | [BookToolsViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/booktools/BookToolsViewModel.kt) | 1125 | 55KB | Prompt Deck AI (L727–L791) |
| **UI** | [ScannerScreen.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerScreen.kt) | ~520 | 21KB | Camera Scanner Screen |
| **UI** | [ScannerViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/scanner/ScannerViewModel.kt) | 183 | 8KB | Camera Scanner ViewModel |
| **UI** | [ProviderConfigDialog.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/ProviderConfigDialog.kt) | 273 | 13KB | Reusable Provider Config Dialog |
| **UI** | [SettingsViewModel.kt](file:///Users/ahmedejam/Projects/MKS%20android/feature/ui/src/main/java/com/ahmedyejam/mks/ui/settings/SettingsViewModel.kt) | 142 | 5KB | Settings ViewModel |

**Total AI codebase:** ~4,800 lines across 27 files.
