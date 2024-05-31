kvStore-kmp
===========

Kotlin MultiPlatform demo project


## console `cli-native`

```shell
./gradlew cli-native:runDebugExecutableNative
```


## console `cli-jvm`

```shell
./gradlew cli-jvm:runInteractive --console=plain
```


## `backend-jvm`

```shell
./gradlew backend-jvm:run
```

- `curl http://0.0.0.0:8080`
- `curl http://0.0.0.0:8080/ping`

- `curl -X POST -d "SET foo 123" http://localhost:8080`
- `curl -X POST -d "GET foo" http://localhost:8080`
- `curl -X POST -d "DELETE foo" http://localhost:8080`
- `curl -X POST -d "COUNT 123" http://localhost:8080`
- `curl -X POST -d "BEGIN" http://localhost:8080`
- `curl -X POST -d "COMMIT" http://localhost:8080`
- `curl -X POST -d "ROLLBACK" http://localhost:8080`


## `frontend-android`

```shell
# set proper AndroidSDK location in local.properties, e.g.:
# echo "sdk.dir=/home/user/Android/Sdk" > local.properties

./gradlew frontend-android:installDebug
```


## `frontend-web-js`

```shell
# after each dependencyUpdate
#./gradlew kotlinUpgradeYarnLock

./gradlew frontend-web-js:jsBrowserRun
```

### bundle distributable

- `./gradlew frontend-web:jsBrowserDistribution -Pprod=true` (production - no source-maps)
- see `frontend-web/build/dist/js/productionExecutable/*`
