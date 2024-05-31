kvStore-kmp
===========

Kotlin MultiPlatform demo project


## console `cli-native`

```shell
./gradlew cli-native:runDebugExecutableNative
```


## console `cli-jvm`

```shell
./gradlew cli-jvm:run
```


## `backend-jvm`

```shell
./gradlew backend-jvm:run
```

- `curl http://0.0.0.0:8080`
- `curl http://0.0.0.0:8080/ping`
- `curl -X POST -d "2 3" http://localhost:8080/add`


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
