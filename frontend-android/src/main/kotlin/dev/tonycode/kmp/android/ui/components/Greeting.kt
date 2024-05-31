package dev.tonycode.kmp.android.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.tonycode.kmp.android.ui.preview.ElementPreview
import dev.tonycode.kmp.android.ui.util.FontScalePreviews
import dev.tonycode.kmp.android.ui.util.LightDarkPreviews


@Composable
fun Greeting(name: String) {

    Text(text = "Hello $name!")

}


@LightDarkPreviews
@FontScalePreviews
@Composable
private fun GreetingPreview(
    @PreviewParameter(NamePreviewParameterProvider::class) name: String,
) = ElementPreview {
    Greeting(name = name)
}

private class NamePreviewParameterProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "John",
        "Averylonglongsomebodyname",
    )
}
