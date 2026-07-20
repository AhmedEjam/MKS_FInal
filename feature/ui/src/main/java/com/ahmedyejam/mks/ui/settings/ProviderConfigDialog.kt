package com.ahmedyejam.mks.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.model.AiProviderConfig
import com.ahmedyejam.mks.data.model.AiProviderDescriptor
import kotlinx.coroutines.launch

private const val STEP_PROVIDER = 0
private const val STEP_CREDENTIAL = 1
private const val STEP_TEST = 2
private const val STEP_MODEL = 3
private const val STEP_PRIVACY = 4
private const val STEP_SAVE = 5

@Composable
fun ProviderConfigDialog(
    providers: List<AiProviderDescriptor>,
    initialConfig: AiProviderConfig?,
    onDismiss: () -> Unit,
    onConfirm: (AiProviderConfig) -> Unit,
    onPing: (suspend (AiProviderConfig) -> String)? = null,
    onFetchModels: (suspend (AiProviderConfig) -> List<String>)? = null,
    onTestCall: (suspend (AiProviderConfig, String) -> String)? = null,
    initialPrompt: String? = null,
    defaultPrompt: String? = null,
    onPromptChange: ((String) -> Unit)? = null
) {
    var currentStep by remember { mutableIntStateOf(STEP_PROVIDER) }
    var selectedProvider by remember {
        mutableStateOf(providers.find { it.id == initialConfig?.providerId } ?: providers.first())
    }

    val apiKeysMap = remember { mutableStateMapOf<String, String>().apply {
        if (initialConfig != null) this[initialConfig.providerId] = initialConfig.apiKey
    } }
    val baseUrlsMap = remember { mutableStateMapOf<String, String>().apply {
        if (initialConfig != null) this[initialConfig.providerId] = initialConfig.baseUrl
    } }
    val modelsMap = remember { mutableStateMapOf<String, String>().apply {
        if (initialConfig != null) this[initialConfig.providerId] = initialConfig.model
    } }

    var fetchedModels by remember { mutableStateOf<List<String>>(emptyList()) }
    var modelsDropdownExpanded by remember { mutableStateOf(false) }

    var testMessage by remember { mutableStateOf("Hello!") }
    var testResult by remember { mutableStateOf<String?>(null) }
    var isTesting by remember { mutableStateOf(false) }

    var promptState by remember { mutableStateOf(initialPrompt ?: "") }

    val scope = rememberCoroutineScope()
    val isCloud = selectedProvider.requiresKey

    fun buildConfig() = AiProviderConfig(
        providerId = selectedProvider.id,
        baseUrl = baseUrlsMap[selectedProvider.id] ?: selectedProvider.defaultBaseUrl,
        apiKey = apiKeysMap[selectedProvider.id] ?: "",
        model = modelsMap[selectedProvider.id] ?: selectedProvider.defaultChatModel
    )

    val stepTitles = listOf("Choose Provider", "Credentials", "Test Connection", "Select Model", "Privacy", "Save")
    val maxStep = if (isCloud) STEP_SAVE else STEP_MODEL

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("AI Provider Setup — Step ${currentStep + 1}/${maxStep + 1}")
                Text(stepTitles[currentStep], style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // Step indicators
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    for (i in 0..maxStep) {
                        Surface(
                            modifier = Modifier.size(width = if (i == currentStep) 20.dp else 8.dp, height = 8.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(99.dp),
                            color = if (i <= currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            content = {}
                        )
                    }
                }

                when (currentStep) {
                    STEP_PROVIDER -> {
                        Text("Local or Cloud?", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Local providers run on your device. Cloud providers send data to external servers.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(12.dp))
                        LazyRow(modifier = Modifier.padding(bottom = 8.dp)) {
                            items(providers) { provider ->
                                FilterChip(
                                    selected = provider.id == selectedProvider.id,
                                    onClick = {
                                        selectedProvider = provider
                                        fetchedModels = emptyList()
                                        testResult = null
                                    },
                                    label = { Text(provider.name) },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }
                        Text(
                            if (isCloud) "Cloud provider — requires API key" else "Local provider — no key needed",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCloud) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                        )
                    }

                    STEP_CREDENTIAL -> {
                        OutlinedTextField(
                            value = baseUrlsMap[selectedProvider.id] ?: selectedProvider.defaultBaseUrl,
                            onValueChange = { baseUrlsMap[selectedProvider.id] = it },
                            label = { Text("Base URL") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        if (isCloud) {
                            OutlinedTextField(
                                value = apiKeysMap[selectedProvider.id] ?: "",
                                onValueChange = { apiKeysMap[selectedProvider.id] = it },
                                label = { Text("API Key") },
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                visualTransformation = PasswordVisualTransformation()
                            )
                            Text(
                                "Your API key is encrypted with Android Keystore and never shown again after saving.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        } else {
                            Text(
                                "No API key required for local providers.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (initialPrompt != null && onPromptChange != null) {
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = promptState,
                                onValueChange = {
                                    promptState = it
                                    onPromptChange(it)
                                },
                                label = { Text("AI Prompt") },
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            )
                        }
                    }

                    STEP_TEST -> {
                        if (onPing != null || onFetchModels != null || onTestCall != null) {
                            Text("Verify your connection works before proceeding.", style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                            if (onTestCall != null) {
                                OutlinedTextField(
                                    value = testMessage,
                                    onValueChange = { testMessage = it },
                                    label = { Text("Test Message") },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (onPing != null) {
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                isTesting = true
                                                testResult = "Pinging..."
                                                testResult = onPing(buildConfig())
                                                isTesting = false
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) { Text("Ping") }
                                }
                                if (onFetchModels != null) {
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                isTesting = true
                                                testResult = "Fetching models..."
                                                val models = onFetchModels(buildConfig())
                                                if (models.isNotEmpty()) {
                                                    fetchedModels = models
                                                    testResult = "Found ${models.size} models."
                                                } else {
                                                    testResult = "No models found or fetch failed."
                                                }
                                                isTesting = false
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) { Text("Fetch") }
                                }
                                if (onTestCall != null) {
                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                isTesting = true
                                                testResult = "Testing..."
                                                testResult = onTestCall(buildConfig(), testMessage)
                                                isTesting = false
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) { Text("Test") }
                                }
                            }
                            if (isTesting || testResult != null) {
                                Spacer(Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isTesting) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp).padding(end = 8.dp), strokeWidth = 2.dp)
                                        Text("Testing...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                    } else if (testResult != null) {
                                        OutlinedTextField(
                                            value = testResult ?: "",
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Response") },
                                            modifier = Modifier.fillMaxWidth().height(100.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            Text("Test connection not available. Proceed to next step.", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    STEP_MODEL -> {
                        Text("Choose the model to use for chat and generation.", style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = modelsMap[selectedProvider.id] ?: selectedProvider.defaultChatModel,
                                onValueChange = { modelsMap[selectedProvider.id] = it },
                                label = { Text("Model Name") },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    if (fetchedModels.isNotEmpty()) {
                                        IconButton(onClick = { modelsDropdownExpanded = true }) {
                                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Model")
                                        }
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = modelsDropdownExpanded,
                                onDismissRequest = { modelsDropdownExpanded = false }
                            ) {
                                fetchedModels.forEach { fetchedModel ->
                                    DropdownMenuItem(
                                        text = { Text(fetchedModel) },
                                        onClick = {
                                            modelsMap[selectedProvider.id] = fetchedModel
                                            modelsDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        if (selectedProvider.defaultVisionModel != null) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Vision model: ${selectedProvider.defaultVisionModel}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (!isCloud) {
                            Spacer(Modifier.height(12.dp))
                            Text("Setup complete! Tap Save to finish.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    STEP_PRIVACY -> {
                        Text("Privacy Summary", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("• Provider: ${selectedProvider.name}")
                            Text("• Endpoint: ${baseUrlsMap[selectedProvider.id] ?: selectedProvider.defaultBaseUrl}")
                            Text("• Model: ${modelsMap[selectedProvider.id] ?: selectedProvider.defaultChatModel}")
                            Text("• API key: Encrypted with Android Keystore")
                            Text("• Data sent: Question text, prompts, and any attached images")
                            Text("• Data is processed on ${selectedProvider.name}'s servers according to their privacy policy")
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "You can change or delete the API key at any time from Settings.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    STEP_SAVE -> {
                        Text("Ready to Save", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(8.dp))
                        Text("Provider: ${selectedProvider.name}")
                        Text("Model: ${modelsMap[selectedProvider.id] ?: selectedProvider.defaultChatModel}")
                        Text("Base URL: ${baseUrlsMap[selectedProvider.id] ?: selectedProvider.defaultBaseUrl}")
                        Spacer(Modifier.height(8.dp))
                        Text("Tap Save to apply these settings.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        },
        confirmButton = {
            if (currentStep < maxStep) {
                Button(onClick = { currentStep++ }) { Text("Next") }
            } else {
                Button(onClick = { onConfirm(buildConfig()) }) { Text("Save") }
            }
        },
        dismissButton = {
            Row {
                if (currentStep > STEP_PROVIDER) {
                    TextButton(onClick = { currentStep-- }) { Text("Back") }
                }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}
