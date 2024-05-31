package dev.tonycode.kmp.android.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dev.tonycode.kmp.android.ui.theme.MinAppTheme


@Composable
fun ScreenPreview(
    content: @Composable (() -> Unit),
) {

    MinAppTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }

}
