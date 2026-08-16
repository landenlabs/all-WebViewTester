# Modernize Gradle Groovy Syntax

Update the project's Gradle configuration files to follow modern Groovy DSL practices and current Android Gradle Plugin (AGP) recommendations.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/dennislang/opt/projects/projects-android/_all/all-WebViewTester/gradle/libs.versions.toml)
- Add a `[plugins]` section to define the Android application plugin using the existing AGP version.

#### [MODIFY] [settings.gradle](file:///Users/dennislang/opt/projects/projects-android/_all/all-WebViewTester/settings.gradle)
- Add `pluginManagement` and `dependencyResolutionManagement` blocks to centralize repository definitions.
- Set `rootProject.name` explicitly.

#### [MODIFY] [build.gradle](file:///Users/dennislang/opt/projects/projects-android/_all/all-WebViewTester/build.gradle) (Root)
- Replace `buildscript` and `allprojects` blocks with a modern `plugins` block.
- Remove redundant repository declarations now handled in `settings.gradle`.

#### [MODIFY] [app/build.gradle](file:///Users/dennislang/opt/projects/projects-android/_all/all-WebViewTester/app/build.gradle)
- Replace `apply plugin:` with the `plugins {}` block using version catalog aliases.
- Modernize property assignments using the `=` operator (e.g., `compileSdk = 37`, `namespace = "..."`).
- Use modern DSL names: `minSdk`, `targetSdk`, and `testInstrumentationRunner`.
- Modernize Java compilation options using `tasks.withType(JavaCompile).configureEach`.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure the project still builds successfully.
- Run `./gradlew help` to verify Gradle configuration is valid.

### Manual Verification
- Verify that the output APK filename renaming still works as expected (check build output folder).
