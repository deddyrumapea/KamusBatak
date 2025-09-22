# Kamus Batak

<a href="https://play.google.com/store/apps/details?id=com.romnan.kamusbatak" target="_blank">
  <img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height="80" />
</a>

![Kamus Batak app icon](app/src/main/ic_launcher-playstore.png)

Horas! Kamus Batak is the most comprehensive and the only open-source Batak–Indonesian dictionary application. Built entirely with Jetpack Compose, it puts thousands of vocabulary entries, cultural notes, and learning tools in your pocket so you can explore and preserve the Batak language wherever you are.

## Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack Highlights](#tech-stack-highlights)
- [Architecture](#architecture)
- [Project Resources](#project-resources)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone the repository](#clone-the-repository)
  - [Configure local secrets](#configure-local-secrets)
  - [Open in Android Studio](#open-in-android-studio)
  - [Command-line build](#command-line-build)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Overview
Kamus Batak delivers a modern learning experience for both Batak speakers and new learners. The app ships with an offline database and can fetch updates from the network, giving you dependable access to translations between Batak Toba and Indonesian, thematic cultural references, and gamified quizzes.

## Key Features
- **Bidirectional dictionary** – Search Batak ➜ Indonesian or Indonesian ➜ Batak translations with smart keyword matching and bookmarking for quick recall.
- **Offline-first experience** – A pre-packaged Room database keeps the core dictionary accessible without connectivity, while optional synchronization refreshes entries when you are online.
- **Vocabulary quizzes** – Multiple quiz modes challenge you to match translations, celebrate streaks with confetti animations, and share results with friends.
- **Cultural insights** – Explore _Partuturan_ (traditional forms of address) curated inside the app for context beyond direct translations.
- **Personalized learning** – Save favorite entries, enable Word-of-the-Day notifications, choose light/dark/system themes, and submit suggestions directly to the maintainer.

## Tech Stack Highlights
- **Language & UI**: Kotlin with Jetpack Compose for a fully declarative interface and responsive layout logic.
- **State management**: MVVM with Kotlin Coroutines and Flow to drive UI state from repositories and DataStore preferences.
- **Data layer**: Room for the local dictionary cache, Retrofit + OkHttp for network access, and Kotlin Serialization/Gson for parsing remote responses.
- **Dependency injection**: Dagger Hilt wires application, repository, and utility components for testability and modularity.
- **User analytics & quality**: Firebase Analytics, Crashlytics, and Remote Config monitor app health (requires a project-specific `google-services.json`).
- **Native bridge**: A small JNI module stores API credentials in `native-lib` to keep secrets outside the Kotlin bytecode.

## Architecture
The codebase follows a layered, package-by-feature structure:
- `application/` – Application class and native-bound secret helpers that bootstrap logging and secure configuration.
- `data/` – Implementations of repositories, Retrofit APIs, Room DAOs/entities, and DataStore managers handling offline/online sources.
- `domain/` – Business models, repository interfaces, and utility classes consumed by the presentation layer.
- `presentation/` – Jetpack Compose screens, navigation graph (Compose Destinations), theming, and ViewModels that orchestrate user flows.

This structure keeps UI concerns, domain logic, and data sources loosely coupled, making the app easier to maintain and extend.

## Project Resources
- **Dictionary dataset**: `app/src/main/assets/kamus_batak.db` contains the initial bilingual entries bundled with the APK.
- **Localization**: String resources are provided in both English and Indonesian (`values/` & `values-in-rID/`).
- **Native secrets**: JNI bindings expect implementations for `baseUrl`, `keyParam`, and `keyValue` in `app/src/main/cpp/native-lib.cpp`. Provide your own values to enable remote dictionary updates.

## Getting Started
### Prerequisites
- Android Studio Koala (2024.1.1) or newer with the Android Gradle Plugin 8.1+ toolchain.
- Java 17 (required by the Android Gradle Plugin used in this project).
- Android SDK platforms and build tools for API level 21 through 35.
- A Firebase project if you plan to use Analytics, Crashlytics, or Remote Config.

### Clone the repository
```bash
git clone https://github.com/deddyrumapea/KamusBatak.git
cd KamusBatak
```

### Configure local secrets
1. **Firebase services** – Download your `google-services.json` from the Firebase Console and place it under `app/google-services.json`. Without it the Gradle sync will fail.
2. **Native API credentials** – Create `app/src/main/cpp/native-lib.cpp` with your endpoint and header values:
   ```cpp
   #include <jni.h>

   extern "C" JNIEXPORT jstring JNICALL
   Java_com_romnan_kamusbatak_application_SecretValues_baseUrl(JNIEnv* env, jobject /* this */) {
       return env->NewStringUTF("https://your-api.example.com/");
   }

   extern "C" JNIEXPORT jstring JNICALL
   Java_com_romnan_kamusbatak_application_SecretValues_keyParam(JNIEnv* env, jobject /* this */) {
       return env->NewStringUTF("X-Api-Key");
   }

   extern "C" JNIEXPORT jstring JNICALL
   Java_com_romnan_kamusbatak_application_SecretValues_keyValue(JNIEnv* env, jobject /* this */) {
       return env->NewStringUTF("replace-with-your-secret" );
   }
   ```
   Returning dummy values is fine for local UI development, but remote updates will be disabled.

### Open in Android Studio
1. `File` → `Open...` and select the project root (`KamusBatak`).
2. Wait for Gradle sync to finish and ensure the Kotlin KSP tasks complete successfully.
3. Choose the `app` run configuration and click **Run** ▶ to install the debug build on your emulator or device.

### Command-line build
Run the usual Gradle tasks from the project root:
```bash
./gradlew assembleDebug    # Builds a debug APK
./gradlew assembleRelease  # Builds a release bundle (requires a signing config)
```
Release builds have code shrinking enabled (`minifyEnabled true`), so remember to supply a `proguard-rules.pro` update or signing configuration as needed.

## Testing
Execute unit tests with:
```bash
./gradlew test
```
You can also run `./gradlew connectedAndroidTest` to execute instrumented tests on an attached device or emulator.

## Contributing
Contributions are welcome! Feel free to open an issue for bugs, feature ideas, or localization help. For larger changes, please fork the repository, create a topic branch, and submit a pull request describing your improvements. Keeping discussions in English or Indonesian helps the maintainers respond quickly.

## License
```
Copyright 2022-present Deddy Romnan Rumapea

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

See the [privacy policy](PRIVACY_POLICY.md) for details on data handling in the published app.
