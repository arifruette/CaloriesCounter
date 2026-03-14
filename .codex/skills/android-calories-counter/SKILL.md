---
name: android-calories-counter
description: Android Compose-first project workflow and architecture guide for the CaloriesCounter app. Use when working in this repository on Kotlin, Jetpack Compose, Navigation 2 for Compose, Hilt, Room, Retrofit, project-specific MVI/ViewModel state flow, feature additions, screen migrations from Fragments/XML, bug fixes, refactors, or code review.
---

# Android Calories Counter

Use this skill to keep changes aligned with the target architecture of the CaloriesCounter app: Compose-first UI, Navigation 2 for Compose, and gradual migration away from Fragments and XML.

## Follow the Target Architecture

- Treat the app as a single-module Android application rooted in `app/`.
- Preserve the existing layered split inside each feature: `presentation`, `domain`, `data`.
- Use Jetpack Compose for new screens and for substantial rewrites of existing screens.
- Use Navigation 2 for Compose as the default navigation mechanism.
- Treat Fragments, ViewBinding, layout XML, and Navigation XML as legacy code that may need temporary maintenance during migration.
- Use Hilt for dependency wiring, with providers in `core/di/CoreModules.kt` and bindings in `core/di/FeatureBindingsModule.kt`.
- Reuse the shared MVI/ViewModel base in `core/common/mvi/BaseMviViewModel.kt` with `UiIntent`, `UiState`, and `UiEffect`.
- Keep app-wide navigation primitives in `core/navigation` until they are replaced by Compose-native routing.

## Build New UI in Compose

- Implement new screens with composable screen functions and UI components.
- Keep screen-level state in the owning `ViewModel`.
- Hoist Compose UI state and avoid duplicating the same state across composables.
- Use lifecycle-aware state collection from the `ViewModel`.
- Prefer string resources to hardcoded copy in Kotlin.
- Keep navigation decisions and route modeling explicit when wiring Compose destinations.

## Work with Legacy Screens Carefully

- When a task touches an existing Fragment/XML screen, evaluate whether the screen can be migrated to Compose within the same change.
- Prefer migrating the whole touched screen to Compose over extending XML or ViewBinding further.
- If migration is too large for the task, apply the minimal safe legacy fix and keep the result migration-friendly.
- Follow the current Fragment binding lifecycle only when touching legacy code: bind in `onViewCreated`, clear `_binding` in `onDestroyView`.

## Respect Current Feature Boundaries

- Keep diary code under `feature/diary/`.
- Keep stats code under `feature/stats/`.
- Keep recipes code under `feature/recipes/`.
- Place domain contracts in `domain`, implementations in `data`, and screen state logic in `presentation`.
- Put feature-local navigation helpers under a local `presentation/navigation` package when needed.

## Use the Existing Data Stack

- Keep Room entities and DAO access aligned with `core/database/AppDatabase.kt` unless there is a strong reason to create a feature-local database package later.
- Use Retrofit interfaces with kotlinx.serialization DTOs for remote calls, following `feature/diary/data/remote/OpenFoodFactsApi.kt`.
- Keep domain models separate from DTOs and database entities.
- Put transformation logic in repository implementations or dedicated mappers close to the data layer.

## Handle Text and Localization Carefully

- Assume Russian user-facing text is intended.
- Notice that several existing Kotlin and XML literals currently appear as mojibake. Treat this as an encoding defect, not as the desired text format.
- When adding or fixing UI strings, prefer `strings.xml` and keep file encoding consistent.
- Do not spread duplicate hardcoded meal titles across multiple files; centralize or reuse where practical.

## Common Workflows

### Add a new screen

- Create the screen in Compose.
- Add or update the `ViewModel`, screen state contracts, and dependencies together.
- Wire the screen into Navigation 2 for Compose.
- Update strings, route definitions, and DI bindings in the same change.

### Migrate a legacy screen to Compose

- Trace current behavior through Fragment, `ViewModel`, navigation, and related resources.
- Recreate the screen behavior in Compose first.
- Move navigation to Navigation 2 for Compose as part of the migration whenever practical.
- Remove obsolete XML, binding, and Fragment wiring only after verifying they are no longer referenced.

### Fix a screen bug

- Trace the path from screen -> `ViewModel` -> interactor -> repository -> data source.
- Verify whether the bug is state, navigation, mapping, persistence, or remote-data related.
- If the bug is in legacy UI, prefer a fix that also shortens the path to Compose migration.

### Review code

- Prioritize architecture drift, broken lifecycle handling, missing DI bindings, navigation regressions, and unnecessary expansion of legacy UI code.
- Check whether new UI bypasses the shared state model or duplicates state containers.
- Check whether new domain code depends on Android classes.

## Verify Changes

- Use `./gradlew.bat assembleDebug` for build validation on Windows.
- Do not add or maintain unit tests or instrumentation tests for this project.
- Do not plan test-only follow-up work unless the user explicitly requests it.
- Skip `./gradlew.bat test` even when changing domain logic or pure Kotlin behavior unless the user explicitly asks for tests.
- If a task touches Compose navigation, state handling, or Android framework behavior, mention whether no device or instrumentation validation was performed.

## Project References

- Read [references/project-map.md](references/project-map.md) for the current-state versus target-state architecture map.
- Treat `agents/openai.yaml` as UI metadata for this skill. The skill triggers from this `SKILL.md`; the YAML improves how the skill is shown and suggested in Codex.
