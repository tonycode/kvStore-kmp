package dev.tonycode.kmp.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dev.tonycode.kmp.android.BuildConfig
import dev.tonycode.kmp.android.ui.screens.MainScreen
import dev.tonycode.kmp.android.ui.theme.MinAppTheme
import dev.tonycode.kmp.common.DefaultDispatchers
import dev.tonycode.kvstore.TransactionalKeyValueStoreFactory


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MinAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(
                        storeFactory = if (BuildConfig.DEBUG) LoggingStoreFactory(DefaultStoreFactory()) else DefaultStoreFactory(),
                        trkvs = TransactionalKeyValueStoreFactory.create(),
                        lifecycle = essentyLifecycle(),
                        instranceKeeper = instanceKeeper(),
                        dispatches = DefaultDispatchers,
                    )
                }
            }
        }
    }

}
