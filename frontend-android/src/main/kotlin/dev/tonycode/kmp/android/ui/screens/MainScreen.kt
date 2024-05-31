package dev.tonycode.kmp.android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tonycode.kmp.android.ui.components.Greeting
import dev.tonycode.kmp.android.ui.preview.ScreenPreview
import dev.tonycode.kmp.android.ui.util.FontScalePreviews
import dev.tonycode.kmp.android.ui.util.LightDarkPreviews
import dev.tonycode.kmp.lib.Adder


@Composable
fun MainScreen() {

    var a by remember { mutableStateOf<Int?>(null) }
    var b by remember { mutableStateOf<Int?>(null) }

    val adder = Adder()


    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Greeting("Android")

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = a?.toString() ?: "",
            onValueChange = { a = it.toIntOrNull() },
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = b?.toString() ?: "",
            onValueChange = { b = it.toIntOrNull() },
        )

        Spacer(Modifier.height(12.dp))

        a?.let { a ->
            b?.let { b ->
                Text(
                    text = "$a + $b = ${ adder.add(a, b) }"
                )
            }
        }
    }

}

@LightDarkPreviews
@FontScalePreviews
@Composable
fun MainScreenPreview() = ScreenPreview {
    MainScreen()
}
