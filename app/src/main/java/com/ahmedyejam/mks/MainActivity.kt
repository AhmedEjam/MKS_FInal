package com.ahmedyejam.mks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ahmedyejam.mks.ui.MksNavHost
import com.ahmedyejam.mks.ui.theme.MKSTheme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : AppCompatActivity() {
    private val _sharedUris = MutableStateFlow<List<Uri>?>(null)
    val sharedUris = _sharedUris.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        handleIntent(intent)

        val appModule = (application as MksApplication).appModule
        val initialLanguage = "en"
        val initialShowWelcome = true

        enableEdgeToEdge()

        setContent {
            val themeMode by appModule.dataStoreManager.themeMode.collectAsState(initial = "DAWN")
            val fontScale by appModule.dataStoreManager.fontScale.collectAsState(initial = 1.0f)
            val uiDensity by appModule.dataStoreManager.uiDensity.collectAsState(initial = 1.0f)
            val language by appModule.dataStoreManager.language.collectAsState(initial = initialLanguage)

            LaunchedEffect(language) {
                if (AppCompatDelegate.getApplicationLocales().toLanguageTags() != language) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
                }
            }

            MKSTheme(
                themeMode = themeMode,
                fontScale = fontScale,
                uiDensity = uiDensity
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val showWelcomeOnStartup by appModule.dataStoreManager.showWelcomeOnStartup.collectAsState(initial = initialShowWelcome)
                    val sharedUrisByState by sharedUris.collectAsState()

                    MksNavHost(
                        navController = navController,
                        appModule = appModule,
                        showWelcomeOnStartup = showWelcomeOnStartup,
                        sharedUris = sharedUrisByState,
                        onConsumedSharedUris = { _sharedUris.value = null }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return

        when (intent.action) {
            Intent.ACTION_SEND -> {
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                if (uri != null) {
                    _sharedUris.value = listOf(uri)
                }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                val uris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                if (uris != null) {
                    _sharedUris.value = uris
                }
            }
        }
    }
}
