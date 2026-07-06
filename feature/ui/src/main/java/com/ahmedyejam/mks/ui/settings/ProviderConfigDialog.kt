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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ahmedyejam.mks.data.model.AiProviderConfig
import com.ahmedyejam.mks.data.model.AiProviderDescriptor
import kotlinx.coroutines.launch

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

    fun buildConfig() = AiProviderConfig(
        providerId = selectedProvider.id,
        baseUrl = baseUrlsMap[selectedProvider.id] ?: selectedProvider.defaultBaseUrl,
        apiKey = apiKeysMap[selectedProvider.id] ?: "",
        model = modelsMap[selectedProvider.id] ?: selectedProvider.defaultChatModel
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("AI Provider Settings") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                LazyRow(modifier = Modifier.padding(bottom = 16.dp)) {
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
                
                OutlinedTextField(
                    value = baseUrlsMap[selectedProvider.id] ?: selectedProvider.defaultBaseUrl,
                    onValueChange = { baseUrlsMap[selectedProvider.id] = it },
                    label = { Text("Base URL") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
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
                
                OutlinedTextField(
                    value = apiKeysMap[selectedProvider.id] ?: "",
                    onValueChange = { apiKeysMap[selectedProvider.id] = it },
                    label = { Text("API Key (if required)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                if (initialPrompt != null && onPromptChange != null) {
                    OutlinedTextField(
                        value = promptState,
                        onValueChange = { 
                            promptState = it
                            onPromptChange(it)
                        },
                        label = { Text("AI Prompt") },
                        modifier = Modifier.fillMaxWidth().height(120.dp).padding(bottom = 16.dp)
                    )
                }

                if (onPing != null || onFetchModels != null || onTestCall != null) {
                    Text("Test Provider", style = MaterialTheme.typography.titleSmall)
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
                                            testResult = "Found ${models.size} models. Check the dropdown!"
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
                                        testResult = "Testing connection..."
                                        testResult = onTestCall(buildConfig(), testMessage)
                                        isTesting = false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("Test") }
                        }
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
                                modifier = Modifier.fillMaxWidth().height(120.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(buildConfig()) }) { Text("Confirm") }
        },
        dismissButton = {
            Row {
                TextButton(
                    onClick = {
                        baseUrlsMap[selectedProvider.id] = selectedProvider.defaultBaseUrl
                        modelsMap[selectedProvider.id] = selectedProvider.defaultChatModel
                        apiKeysMap[selectedProvider.id] = ""
                        if (defaultPrompt != null && onPromptChange != null) {
                            promptState = defaultPrompt
                            onPromptChange(defaultPrompt)
                        }
                    }
                ) { Text("Restore Defaults") }
                TextButton(onClick = onDismiss) { Text("Cancel") }
            }
        }
    )
}
