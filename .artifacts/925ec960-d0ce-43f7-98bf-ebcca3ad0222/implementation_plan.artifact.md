# Dependency Setup for Navigation and Serialization

This plan covers adding the necessary dependencies for Navigation and Type-Safe Routing in your Compose Multiplatform project.

## User Review Required

> [!IMPORTANT]
> The Navigation library used is the JetBrains multiplatform port of `androidx.navigation`, which is the standard for Compose Multiplatform.
>
> We are also adding `kotlinx-serialization`, as the modern Navigation API uses it for type-safe routing (no more string-based routes!).

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///Users/narayan/AndroidStudioProjects/RateVault/gradle/libs.versions.toml)
* Add versions for `androidx-navigation` and `kotlinx-serialization`.
* Add library definitions for `navigation-compose` and `kotlinx-serialization-json`.
* Add the Kotlin Serialization plugin definition.

#### [MODIFY] [build.gradle.kts (shared)](file:///Users/narayan/AndroidStudioProjects/RateVault/shared/build.gradle.kts)
* Apply the `kotlinx.serialization` plugin.
* Add the new dependencies to the `commonMain` source set.

## Verification Plan

### Automated Tests
* Run `./gradlew :shared:assemble` to verify the dependencies are resolved and the project builds.

### Manual Verification
* Verify that `NavHost`, `rememberNavController`, and `@Serializable` are available in the IDE's auto-complete within `commonMain`.
