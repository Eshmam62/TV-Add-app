package com.example.smarttvadplayer.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smarttvadplayer.media.player.CompanyAdProvider
import com.example.smarttvadplayer.media.player.ZoneVideoPlayer
import com.example.smarttvadplayer.ui.components.TvButton
import com.example.smarttvadplayer.ui.theme.TvBackground

/**
 * Company advertisement screen shown after orientation selection.
 * Plays the bundled company_intro.mp4 from resources.
 * User can skip with "Next" button.
 */
@Composable
fun CompanyAdScreen(
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val context = LocalContext.current
    val adUri = remember { CompanyAdProvider.getCompanyAdUri(context) }
    val adAvailable = remember { CompanyAdProvider.isCompanyAdAvailable(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TvBackground)
    ) {
        if (adAvailable) {
            ZoneVideoPlayer(
                uri = adUri,
                modifier = Modifier.fillMaxSize(),
                loop = false,
                onVideoEnd = { onNext() },
                onError = { onSkip() }
            )
        } else {
            // Placeholder when no video is bundled yet
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Company Advertisement",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Video placeholder — replace res/raw/company_intro.mp4",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Next / Skip button — always visible
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            TvButton(
                text = "Next ▶",
                onClick = { onSkip() },
                modifier = Modifier.width(160.dp)
            )
        }
    }
}
