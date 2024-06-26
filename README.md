kvStore-kmp - key-value store with transactions support
=======================================================

Kotlin Multiplatform (KMP) library with demo apps for several platforms (`cli-native`, `cli-jvm`, `backend-jvm`, `frontend-android`, `frontend-web-js`)

NB: The main project's goal is a demonstration of KMP library development, artifacts aren't published yet


- simple non-thread safe
    - interface: [TransactionalKeyValueStore](/kvstore-core/src/commonMain/kotlin/dev/tonycode/kvstore/TransactionalKeyValueStore.kt)
    - factory: [TransactionalKeyValueStoreFactory](/kvstore-core/src/commonMain/kotlin/dev/tonycode/kvstore/TransactionalKeyValueStoreFactory.kt)
    - [TransactionalKeyValueStoreImpl](/kvstore-core/src/commonMain/kotlin/dev/tonycode/kvstore/TransactionalKeyValueStoreImpl.kt)
        - in-memory impl based on Kotlin stdlib's HashMap
    - [TransactionalKeyValueStoreRbtImpl](/kvstore-core/src/commonMain/kotlin/dev/tonycode/kvstore/TransactionalKeyValueStoreRbtImpl.kt)
        - in-memory impl based on RedBlackTree (adapted version of [aostrouhhov/trees/redblacktree](https://github.com/aostrouhhov/trees/tree/master/src/redblacktree))
- thread-safe
    - interface: [ConcurrentTransactionalKeyValueStore](/kvstore-coroutines/src/commonMain/kotlin/dev/tonycode/kvstore/coroutines/ConcurrentTransactionalKeyValueStore.kt)
    - factory: [ConcurrentTransactionalKeyValueStoreFactory](/kvstore-coroutines/src/commonMain/kotlin/dev/tonycode/kvstore/coroutines/ConcurrentTransactionalKeyValueStoreFactory.kt)
    - [ConcurrentTransactionalKeyValueStoreImpl](/kvstore-coroutines/src/commonMain/kotlin/dev/tonycode/kvstore/coroutines/ConcurrentTransactionalKeyValueStoreImpl.kt)
        - in-memory impl, backed by HashMap or RedBlackTree impl

commands:

- `SET key value`
- `GET key`
- `DELETE key`
- `COUNT value`
- transaction
    - `BEGIN`
    - `COMMIT`
    - `ROLLBACK`


## console `cli-native`

```shell
./gradlew cli-native:runDebugExecutableNative --console=plain
```

<a href="docs/screenshot-cli-native.png"><img src="docs/screenshot-cli-native.png" /></a>


## console `cli-jvm`

```shell
./gradlew cli-jvm:runInteractive --console=plain
```

<a href="docs/screenshot-cli-jvm.png"><img src="docs/screenshot-cli-jvm.png" /></a>


## `backend-jvm`

```shell
./gradlew backend-jvm:run
```

```shell
curl http://0.0.0.0:8080
curl http://0.0.0.0:8080/ping

curl -X POST -d "SET foo 123" http://localhost:8080
curl -X POST -d "GET foo" http://localhost:8080
curl -X POST -d "DELETE foo" http://localhost:8080
curl -X POST -d "COUNT 123" http://localhost:8080
curl -X POST -d "BEGIN" http://localhost:8080
curl -X POST -d "COMMIT" http://localhost:8080
curl -X POST -d "ROLLBACK" http://localhost:8080
```

<a href="docs/screenshot-backend-jvm.png"><img src="docs/screenshot-backend-jvm.png" /></a>


## `frontend-android`

```shell
# set proper AndroidSDK location in local.properties, e.g.:
# echo "sdk.dir=/home/user/Android/Sdk" > local.properties

./gradlew frontend-android:installDebug
```

<a href="docs/screenshot-frontend-android.png"><img src="docs/screenshot-frontend-android.png" /></a>


## `frontend-web-js`

```shell
# after each dependencyUpdate
#./gradlew kotlinUpgradeYarnLock

./gradlew frontend-web-js:jsBrowserRun
```

<a href="docs/screenshot-frontend-web-js.png"><img src="docs/screenshot-frontend-web-js.png" /></a>

You can play with already deployed demo here:

- [https://tonycode.dev/demos/kvStore-kmp-web-js-demo/](https://tonycode.dev/demos/kvStore-kmp-web-js-demo/)

### bundle distributable

- `./gradlew frontend-web-js:jsBrowserDistribution -Pprod=true` (production - no source-maps and logs)
- see `frontend-web-js/build/dist/js/productionExecutable/*`
