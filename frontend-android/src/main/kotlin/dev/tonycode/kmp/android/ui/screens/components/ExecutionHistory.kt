package dev.tonycode.kmp.android.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.tonycode.kmp.android.ui.preview.ElementPreview
import dev.tonycode.kmp.android.ui.util.FontScalePreviews
import dev.tonycode.kmp.android.ui.util.LightDarkPreviews
import dev.tonycode.kmp.common.main.store.MainStore


@Composable
fun ExecutionHistory(
    items: List<MainStore.State.HistoryItem>,
) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) { item ->
            Text(
                text = StringBuilder("> ").apply {
                    append(item.commandString)
                    item.output?.let { output -> append("\n").append(output) }
                }.toString(),
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(color = if (item.succeed) Color.LightGray else Color.Red.copy(alpha = 0.33f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }

}


@LightDarkPreviews
@FontScalePreviews
@Composable
private fun ExecutionHistoryPreview(
    @PreviewParameter(ExecutionHistoryPreviewStateProvider::class) previewState: ExecutionHistoryPreviewState
) = ElementPreview {
    ExecutionHistory(
        items = previewState.items
    )
}

private data class ExecutionHistoryPreviewState(
    val items: List<MainStore.State.HistoryItem>,
)

private class ExecutionHistoryPreviewStateProvider : PreviewParameterProvider<ExecutionHistoryPreviewState> {
    override val values = sequenceOf(
        ExecutionHistoryPreviewState(items = emptyList()),
        ExecutionHistoryPreviewState(items = listOf(
            MainStore.State.HistoryItem("SET foo 123", null),
            MainStore.State.HistoryItem("GET foo", "123"),
            MainStore.State.HistoryItem("ROLLBACK", "no transaction", succeed = false),
            MainStore.State.HistoryItem("DELETE", "bar", succeed = false),
        )),
    )
}
