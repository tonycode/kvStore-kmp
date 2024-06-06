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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import dev.tonycode.kmp.android.ui.theme.MinAppPalette
import dev.tonycode.kmp.common.IDispatchers
import dev.tonycode.kmp.common.main.MainController
import dev.tonycode.kmp.common.main.MainView
import dev.tonycode.kmp.common.main.store.MainStore
import dev.tonycode.kmp.common.main.store.MainStore.Intent
import dev.tonycode.kmp.common.utils.ViewProxy
import dev.tonycode.kvstore.TransactionalKeyValueStore


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    storeFactory: StoreFactory,
    trkvs: TransactionalKeyValueStore,
    lifecycle: Lifecycle,
    instranceKeeper: InstanceKeeper,
    dispatches: IDispatchers,
) {

    val controller: MainController by remember { mutableStateOf(
        MainController(
            storeFactory,
            trkvs,
            instranceKeeper,
            dispatches,
        )
    ) }

    var model : MainStore.State by remember { mutableStateOf(MainStore.State()) }

    val view by remember { mutableStateOf(
        object : ViewProxy<MainStore.State, Intent>(
            render = { model = it }
        ), MainView {}
    ) }

    LaunchedEffect(true) {
        controller.onViewCreated(view, lifecycle)
    }


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
            items(model.commands) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15))
                        .background(
                            color = if (item != model.selectedCommand) MinAppPalette.Teal200 else MinAppPalette.Purple200,
                        )
                        .clickable {
                            view.dispatch(Intent.SelectCommand(command = item))
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        // Optional command arguments
        if (model.hasKeyInput) {
            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = { Text("key") },
                value = model.key ?: "",
                onValueChange = {
                    view.dispatch(Intent.SetKeyArgument(key = it))
                },
            )
            Spacer(Modifier.height(12.dp))
        }

        if (model.hasValueInput) {
            TextField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = { Text("value") },
                value = model.value ?: "",
                onValueChange = {
                    view.dispatch(Intent.SetValueArgument(value = it))
                },
            )
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(8.dp))

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            enabled = model.isExecuteButtonEnabled,
            onClick = {
                view.dispatch(Intent.ExecuteCommand)
            }
        ) { Text("Execute") }

        Spacer(Modifier.height(24.dp))

        // Execution result
        model.executionResult?.let {
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

//@LightDarkPreviews
//@FontScalePreviews
//@Composable
//fun MainScreenPreview() = ScreenPreview {
//    MainScreen()
//}
