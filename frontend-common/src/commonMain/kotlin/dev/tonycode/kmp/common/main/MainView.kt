package dev.tonycode.kmp.common.main

import com.arkivanov.mvikotlin.core.view.MviView
import dev.tonycode.kmp.common.main.store.MainStore


interface MainView : MviView<MainStore.State, MainStore.Intent>
