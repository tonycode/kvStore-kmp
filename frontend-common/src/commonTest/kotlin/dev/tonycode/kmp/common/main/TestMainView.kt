package dev.tonycode.kmp.common.main

import dev.tonycode.kmp.common.TestMviView
import dev.tonycode.kmp.common.main.store.MainStore


class TestMainView : TestMviView<MainStore.State, MainStore.Intent>(), MainView
