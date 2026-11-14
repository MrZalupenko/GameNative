package app.gamenative.ui.screen.library.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import app.gamenative.R
import app.gamenative.ui.theme.PluviaTheme
import app.gamenative.ui.theme.ThemedAlertDialog

@Composable
fun RecommendationDisclosureDialog(
    onContinue: () -> Unit,
    onDismiss: () -> Unit,
) {
    ThemedAlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.rec_disclosure_title)) },
        text = { Text(text = stringResource(R.string.rec_disclosure_body)) },
        confirmButton = {
            TextButton(onClick = onContinue) {
                Text(text = stringResource(R.string.rec_disclosure_allow))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.rec_disclosure_not_now))
            }
        },
        containerColor = PluviaTheme.dialogContainerColor,
        titleContentColor = PluviaTheme.dialogTitleColor,
        textContentColor = PluviaTheme.dialogTextColor,
    )
}
