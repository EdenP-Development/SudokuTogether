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
                Text("Good puzzles. Good company.")
                Text("Play Solo and Daily offline. Nearby Co-op and Versus connect phones directly on the same Wi-Fi; no account is needed.")
                Text("Your puzzles, progress, and earned Collection items are stored on this phone. Before uninstalling or changing phones, use Settings → Progress → Back up progress. Active multiplayer rooms are not included in that backup.")
                Text("Nearby game messages and chat travel on the local Wi-Fi network without encryption. Play together on networks you trust.")
                if (share != null) {
                    OutlinedButton(share, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                        GameIcon(GameSymbol.SHARE, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Share game installer")
                    }
                }
            }
        },
        confirmButton = { TextButton(onDismiss) { Text("Done") } }
    )
}
