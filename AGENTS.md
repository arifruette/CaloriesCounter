## Project Scope

This repository contains a single-module Android application in `app/` for calorie tracking.
Use these instructions when implementing, reviewing, debugging, refactoring, or migrating UI in this repo.

## Stack

- Use Kotlin with Jetpack Compose as the default UI framework for all new screens and substantial UI rewrites.
- Use Navigation 2 for Compose as the default navigation mechanism.
- Treat Fragments, XML layouts, ViewBinding, and Navigation XML as legacy code to maintain only while migrating.
- Preserve the current layered structure: `presentation -> domain -> data`.
- Keep dependency injection in Hilt modules under `core/di`.
- Keep persistence in Room and remote access in Retrofit with kotlinx.serialization.
- Reuse the shared MVI/ViewModel state primitives in `core/common/mvi` instead of inventing a second app-wide UI pattern.

## Feature Structure

- Add new product functionality inside `feature/<feature-name>/presentation`, `domain`, and `data` when those layers are needed.
- Keep screen state, intents, and effects close to the owning `ViewModel`.
- Put navigation-specific types under `presentation/navigation` or `core/navigation`, depending on whether the route is feature-local or app-wide.
- Keep app-wide infrastructure under `core/`.

## UI Rules

- Build new screens in Compose.
- Prefer migrating touched legacy screens to Compose instead of extending XML or Fragment code further.
- If a full migration is too large for the current task, make the minimal safe legacy change and leave the screen as an explicit migration target.
- Use screen-level `ViewModel` state with lifecycle-aware collection in Compose.
- Prefer string resources over hardcoded UI copy.
- Treat the current mojibake-looking Russian text as an encoding issue to fix carefully, not as a pattern to copy into new code.
- Keep Compose UI state hoisted and avoid duplicating screen state across composables.

## Legacy Rules

- Follow the existing Fragment binding lifecycle only when touching legacy screens: initialize binding in `onViewCreated`, clear it in `onDestroyView`.
- Keep Fragment or XML edits narrow and migration-friendly.
- Do not add new feature screens in XML unless a migration constraint makes Compose impossible for the current task.

## Data and Domain Rules

- Keep `domain` free from Android framework dependencies.
- Put mapping and data-source orchestration in `data`.
- Inject repositories and interactors through Hilt rather than constructing them in screens.
- Prefer extending existing abstractions before adding new singleton-style helpers.

## Change Workflow

- Before larger edits, inspect the affected feature end-to-end: screen, view model, interactor, repository, and related navigation.
- When adding a screen, update routes, navigation wiring, DI bindings, and strings together.
- When migrating a screen from Fragment/XML to Compose, preserve behavior first and then remove obsolete legacy pieces only when they are no longer referenced.
- When changing persistence or API models, verify all consumers and update mappings explicitly.
- Keep changes focused; do not rewrite unrelated architecture while solving a local task.

## Validation

- For Gradle verification, prefer `./gradlew.bat assembleDebug` on Windows.
- Do not add or maintain unit tests or instrumentation tests for this project.
- Do not propose test-only changes as part of normal implementation work in this repository.
- Skip `./gradlew.bat test` even when business logic changes unless the user explicitly asks for tests.
- If a task touches Compose navigation, state handling, or Android framework behavior, note whether no device or instrumentation validation was performed.
