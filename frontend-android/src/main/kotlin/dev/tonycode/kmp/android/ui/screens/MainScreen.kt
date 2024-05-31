package dev.tonycode.kmp.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.tonycode.kmp.android.ui.preview.ScreenPreview
import dev.tonycode.kmp.android.ui.util.FontScalePreviews
import dev.tonycode.kmp.android.ui.util.LightDarkPreviews
import dev.tonycode.kvstore.Operations


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
) {

    var cmdIdx by remember { mutableIntStateOf(0) }
    var cmdArgs by remember { mutableStateOf(Operations[0].second) }

    val uiState by mainViewModel.uiState.collectAsState()


    Column(
        modifier = modifier.padding(vertical = 32.dp),
    ) {
        // Command picker
        Text(
            text = "Pick a command:",
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(4.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            itemsIndexed(Operations) { index, item ->
                Text(
                    text = item.first,
                    modifier = Modifier
                        .background(
                            color = if (index != cmdIdx) Color.LightGray else Color.Yellow,
                            shape = RoundedCornerShape(15)
                        ).clickable {
                            cmdIdx = index
                            cmdArgs = item.second
                        }.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        // Optional command arguments
        if (Operations[cmdIdx].second != null) {
            Text(
                text = "Enter arguments:",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(12.dp))

            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                value = cmdArgs ?: "",
                onValueChange = { cmdArgs = it },
            )
            Spacer(Modifier.height(12.dp))
        }


        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
                mainViewModel.onCommand(
                    Operations[cmdIdx].first + (if (cmdArgs != null) " $cmdArgs" else "")
                )
            }
        ) { Text("Execute") }

        Spacer(Modifier.height(24.dp))

        // Execution result
        uiState.executionResult?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(25))
                    .padding(16.dp)
            )
        }
    }

}

@LightDarkPreviews
@FontScalePreviews
@Composable
fun MainScreenPreview() = ScreenPreview {
    MainScreen()
}
