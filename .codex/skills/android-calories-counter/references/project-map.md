# Project Map

## Current State

- `app/`: the only Android module in the repository
- `core/`: shared infrastructure
- `feature/diary/`: most developed feature; includes remote search and diary models
- `feature/stats/`: placeholder feature with presentation/domain/data split
- `feature/recipes/`: placeholder feature with presentation/domain/data split

## Current Core Packages

- `core/common/mvi/`
  - `BaseMviViewModel.kt`: shared state/effect base using `StateFlow` and `Channel`
  - `MviContracts.kt`: marker interfaces for state, intent, and effect
- `core/di/`
  - `CoreModules.kt`: Retrofit, OkHttp, Json, Room providers
  - `FeatureBindingsModule.kt`: repository and interactor bindings
- `core/navigation/`
  - `AppRoute.kt`, `NavCommand.kt`, `RouteMapper.kt`

## Current App Shell

- `MainActivity.kt`
  - currently uses `ActivityMainBinding`
  - currently hosts `NavHostFragment`
  - currently manages bottom navigation manually through `DefaultRouteMapper`
- `app/CaloriesCounterApp.kt`
  - annotated with `@HiltAndroidApp`

## Current Feature Notes

### Diary

- Main entry screen: `feature/diary/presentation/DiaryFragment.kt`
- View model: `feature/diary/presentation/DiaryViewModel.kt`
- Placeholder destination for meal details: `MealProductsPlaceholderFragment.kt`
- Domain models live in `feature/diary/domain/model/`
- Remote search uses Open Food Facts via `feature/diary/data/remote/OpenFoodFactsApi.kt`

### Stats

- `StatsFragment.kt` and `StatsViewModel.kt` exist but currently behave like an MVP placeholder

### Recipes

- `RecipesFragment.kt` and `RecipesViewModel.kt` exist but currently behave like an MVP placeholder

## Current Resources

- Legacy navigation graph: `app/src/main/res/navigation/main_nav_graph.xml`
- Bottom nav menu: `app/src/main/res/menu/bottom_nav_menu.xml`
- Strings: `app/src/main/res/values/strings.xml`
- Legacy layouts live under `app/src/main/res/layout/`

## Target State

- New screens are implemented with Jetpack Compose.
- Navigation uses Navigation 2 for Compose.
- Existing Fragment/XML/ViewBinding screens are migration targets, not long-term patterns.
- The feature split remains `presentation/domain/data`.
- `ViewModel`, Hilt, Room, Retrofit, and shared state primitives remain part of the core architecture unless changed intentionally.

## Migration Targets

- `MainActivity.kt` should eventually host a Compose root instead of a `NavHostFragment`.
- `main_nav_graph.xml` is a legacy navigation artifact to replace with Compose navigation wiring.
- Fragment screens in `feature/diary`, `feature/stats`, and `feature/recipes` are candidates for Compose migration.
- XML layout files under `res/layout/` are candidates for removal after their screens are migrated.

## Current Constraints

- The checked-in app is still Fragment/XML-based today, so some migration work will need temporary bridging code.
- Java and Kotlin targets are 11.
- Several Russian strings appear garbled in checked-in files; avoid copying that pattern into new code.
- Tests are intentionally out of scope for this project unless the user explicitly requests them.
