package dev.tonycode.kmp.android.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tonycode.kmp.common.KvStoreUiState
import dev.tonycode.kvstore.TransactionalKeyValueStore
import dev.tonycode.kvstore.TransactionalKeyValueStoreImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(KvStoreUiState())
    val uiState: StateFlow<KvStoreUiState> = _uiState

    private val trkvs: TransactionalKeyValueStore = TransactionalKeyValueStoreImpl()


    fun onCommand(commandString: String) {
        viewModelScope.launch {
            _uiState.value = uiState.value.mutate(commandString, trkvs)
        }
    }

}
