package com.ahmedyejam.mks.data.model

/**
 * Configuration for a single AI API call, resolved from user preferences + provider defaults.
 * Passed through the entire AI pipeline (AiClient → McqService → AiMcqRepository).
 */
data class AiProviderConfig(
    /** Provider ID from [AI_PROVIDERS], e.g. "ollama", "groq", "google". */
    val providerId: String,
    /** Base URL of the API, without trailing slash. e.g. "https://api.groq.com/openai/v1" */
    val baseUrl: String,
    /** Bearer API key. Null/blank for local Ollama. */
    val apiKey: String = "",
    /** The model name to use for this call. */
    val model: String,
) {
    val resolvedBaseUrl: String
        get() = baseUrl.trimEnd('/')
}

/**
 * Descriptor for a known provider shown in Settings → AI Integrations.
 */
data class AiProviderDescriptor(
    val id: String,
    val name: String,
    val defaultBaseUrl: String,
    val requiresKey: Boolean,
    /** Sensible default model for chat/text tasks. */
    val defaultChatModel: String,
    /** Sensible default vision model for OCR tasks (null = use chat model). */
    val defaultVisionModel: String? = null,
)

/**
 * All known AI providers. Adding a new provider costs zero code —
 * just add an entry here. The base URL and model are always editable in Settings.
 */
val AI_PROVIDERS: List<AiProviderDescriptor> = listOf(
    AiProviderDescriptor(
        id = "ollama",
        name = "Ollama (Local)",
        defaultBaseUrl = "http://192.168.1.164:11434/v1",
        requiresKey = false,
        defaultChatModel = "gemma4:12b",
        defaultVisionModel = "glm-ocr-strict:latest",
    ),
    AiProviderDescriptor(
        id = "google",
        name = "Google Gemini",
        defaultBaseUrl = "https://generativelanguage.googleapis.com/v1beta/openai",
        requiresKey = true,
        defaultChatModel = "gemini-2.5-flash",
        defaultVisionModel = "gemini-2.5-flash",
    ),
    AiProviderDescriptor(
        id = "groq",
        name = "Groq (LPU)",
        defaultBaseUrl = "https://api.groq.com/openai/v1",
        requiresKey = true,
        defaultChatModel = "llama-3.3-70b-versatile",
        defaultVisionModel = "llama-4-scout-17b-16e-instruct",
    ),
    AiProviderDescriptor(
        id = "openrouter",
        name = "OpenRouter",
        defaultBaseUrl = "https://openrouter.ai/api/v1",
        requiresKey = true,
        defaultChatModel = "google/gemini-flash-1.5",
        defaultVisionModel = "google/gemini-flash-1.5",
    ),
    AiProviderDescriptor(
        id = "alibaba",
        name = "Alibaba Qwen (DashScope)",
        defaultBaseUrl = "https://dashscope-intl.aliyuncs.com/compatible-mode/v1",
        requiresKey = true,
        defaultChatModel = "qwen-plus",
        defaultVisionModel = "qwen-vl-max",
    ),
    AiProviderDescriptor(
        id = "deepseek",
        name = "DeepSeek",
        defaultBaseUrl = "https://api.deepseek.com/v1",
        requiresKey = true,
        defaultChatModel = "deepseek-chat",
    ),
    AiProviderDescriptor(
        id = "mistral",
        name = "Mistral AI",
        defaultBaseUrl = "https://api.mistral.ai/v1",
        requiresKey = true,
        defaultChatModel = "mistral-large-latest",
        defaultVisionModel = "pixtral-large-latest",
    ),
    AiProviderDescriptor(
        id = "together",
        name = "Together AI",
        defaultBaseUrl = "https://api.together.xyz/v1",
        requiresKey = true,
        defaultChatModel = "meta-llama/Llama-3-70b-chat-hf",
        defaultVisionModel = "meta-llama/Llama-4-Scout-17B-16E-Instruct-Turbo",
    ),
    AiProviderDescriptor(
        id = "cohere",
        name = "Cohere",
        defaultBaseUrl = "https://api.cohere.ai/v1",
        requiresKey = true,
        defaultChatModel = "command-r-plus",
    ),
    AiProviderDescriptor(
        id = "cerebras",
        name = "Cerebras",
        defaultBaseUrl = "https://api.cerebras.ai/v1",
        requiresKey = true,
        defaultChatModel = "llama3.1-70b",
    ),
    AiProviderDescriptor(
        id = "huggingface",
        name = "Hugging Face Inference",
        defaultBaseUrl = "https://api-inference.huggingface.co/v1",
        requiresKey = true,
        defaultChatModel = "meta-llama/Meta-Llama-3-8B-Instruct",
    ),
    AiProviderDescriptor(
        id = "anthropic",
        name = "Anthropic (Claude)",
        defaultBaseUrl = "https://api.anthropic.com/v1",
        requiresKey = true,
        defaultChatModel = "claude-3-5-haiku-20241022",
        defaultVisionModel = "claude-3-5-sonnet-20241022",
    ),
    AiProviderDescriptor(
        id = "perplexity",
        name = "Perplexity",
        defaultBaseUrl = "https://api.perplexity.ai",
        requiresKey = true,
        defaultChatModel = "llama-3.1-sonar-large-128k-online",
    ),
    AiProviderDescriptor(
        id = "fireworks",
        name = "Fireworks AI",
        defaultBaseUrl = "https://api.fireworks.ai/inference/v1",
        requiresKey = true,
        defaultChatModel = "accounts/fireworks/models/llama-v3p1-70b-instruct",
    ),
    AiProviderDescriptor(
        id = "nvidia",
        name = "NVIDIA NIM",
        defaultBaseUrl = "https://integrate.api.nvidia.com/v1",
        requiresKey = true,
        defaultChatModel = "meta/llama-3.1-70b-instruct",
        defaultVisionModel = "nvidia/llama-3.2-90b-vision-instruct",
    ),
    AiProviderDescriptor(
        id = "zenmux",
        name = "Zenmux",
        defaultBaseUrl = "https://zenmux.ai/api/v1",
        requiresKey = true,
        defaultChatModel = "deepseek/deepseek-chat",
    ),
    AiProviderDescriptor(
        id = "custom",
        name = "Custom OpenAI-Compatible Endpoint",
        defaultBaseUrl = "https://api.openai.com/v1",
        requiresKey = true,
        defaultChatModel = "gpt-4o",
        defaultVisionModel = "gpt-4o",
    ),
)

/** Quick lookup for a provider descriptor by ID. Falls back to "custom". */
fun findProvider(id: String): AiProviderDescriptor =
    AI_PROVIDERS.find { it.id == id } ?: AI_PROVIDERS.last()

/** Build a default [AiProviderConfig] from a descriptor. */
fun AiProviderDescriptor.toDefaultConfig(apiKey: String = ""): AiProviderConfig =
    AiProviderConfig(
        providerId = id,
        baseUrl = defaultBaseUrl,
        apiKey = apiKey,
        model = defaultChatModel,
    )
