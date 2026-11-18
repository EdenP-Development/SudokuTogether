package com.playereden.sudokutogether

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable internal fun AboutGame(onDismiss: () -> Unit, share: (() -> Unit)?) {
    AlertDialog(
        modifier = Modifier.testTag("about_gridmates"),
        onDismissRequest = onDismiss,
        title = { Text("About GridMates") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GridMatesMark(Modifier.size(48.dp))
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium)
                }
                Text("Version ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodySmall)
                HorizontalDivider()
                Text("Good puzzles. Good company.", style = MaterialTheme.typography.titleSmall)
                Text("Enjoy Solo and Daily puzzles offline, or play nearby Co-op and Versus directly with another phone on the same Wi-Fi. No account is needed.")
                Text("Your progress", style = MaterialTheme.typography.titleSmall)
                Text("Puzzles, records, settings, and earned Collection items stay on this device. Compatible app updates normally retain them. For extra protection, choose Settings → Progress → Back up progress before updating or changing phones. Don't uninstall the app to update it.")
                Text("A progress backup does not include active nearby multiplayer rooms. There is no account-based cloud recovery.")
                Text("Nearby privacy", style = MaterialTheme.typography.titleSmall)
                Text("Nearby game information and chat travel over your local Wi-Fi without encryption. Play together on a network you trust.")
                if (share != null) {
                    HorizontalDivider()
                    OutlinedButton(share, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                        GameIcon(GameSymbol.SHARE, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Share game installer")
                    }
                    Text("Shares the installed game APK, not your saved puzzles or progress. Each player's data stays on their own device.",
                        style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = { TextButton(onDismiss) { Text("Done") } }
    )
}
