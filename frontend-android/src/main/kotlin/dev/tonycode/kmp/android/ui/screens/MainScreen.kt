package dev.tonycode.kmp.android.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.tonycode.kmp.android.ui.preview.ScreenPreview
import dev.tonycode.kmp.android.ui.util.FontScalePreviews
import dev.tonycode.kmp.android.ui.util.LightDarkPreviews


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
) {

    var cmd by remember { mutableStateOf("") }

    val uiState by mainViewModel.uiState.collectAsState()


    Column(
        modifier = modifier.padding(32.dp)
    ) {
        Text("Enter a command:")

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = cmd,
            onValueChange = { cmd = it },
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                mainViewModel.onCommand(cmd)
            }
        ) { Text("Execute") }

        Spacer(Modifier.height(12.dp))

        uiState.executionResult?.let {
            Text(text = it)
        }
    }

}

@LightDarkPreviews
@FontScalePreviews
@Composable
fun MainScreenPreview() = ScreenPreview {
    MainScreen()
}
