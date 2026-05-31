# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Run all unit tests
./gradlew test                          # macOS/Linux
gradlew test                            # Windows

# Run a single test class
./gradlew test --tests "com.aagamshah.worldt2.OutcomeTest"

# Run tests and see output even when passing
./gradlew test --info

# Lint
./gradlew lint
```

## Architecture

WorldT2 is a two-screen cricket T20 match simulator using MVVM + Clean Architecture. No DI framework — manual `ViewModelFactory` pattern throughout.

**Screen flow**: `MainActivity` (pick 2 teams from API) → `MatchActivity` (ball-by-ball match simulation)

```
data/
  remote/         — ApiService (Retrofit interface), RetrofitClient (singleton OkHttp+Gson)
  model/          — Raw API DTO: TeamsModel(name, flag)
  repositoryimpl/ — TeamsRepositoryImpl: wraps API call in Resource<T>

domain/
  model/          — Domain TeamsModel(name, flag, isSelected); toDomainModel() maps data→domain
                    TeamStatsModel(status, score, overs, wickets)
  repository/     — TeamsRepository interface (single method: suspend getTeams())

presentation/
  mainactivity/   — MainViewModel (fetches teams, enforces max-2 selection via _selectedTeams list)
  matchactivity/  — MatchViewModel (entire match engine: simulateBall, updateScore, innings switch)
  adapter/        — BaseAdapter<T>, TeamsAdapter, LogsAdapter
  bottomsheet/    — LogsBottomsheet

utils/
  Resource<T>     — sealed class Success/Error wrapping API results
  Outcome         — enum: ZERO/ONE/TWO/THREE/FOUR/SIX/WIDE/NO_BALL/OUT, each with probability weight
  Status          — enum: BATTING / BOWLING
  MatchConstants  — MAX_OVERS=2, BALLS_PER_OVER=6, TOTAL_BALLS=12, MAX_WICKETS=3
```

**Key facts for writing tests:**
- `MatchViewModel` receives team names as constructor args — no Android framework deps, only LiveData
- `simulateBall()` builds a weighted list from `Outcome.probability`, calls `.random()` — randomness source
- `MainViewModel` enforces max 2 teams via size check on `_selectedTeams: MutableList<String>`
- `TeamsRepositoryImpl` is the only class calling the network; all else is pure Kotlin
- ViewBinding enabled; no Compose

---

## Tutor Mode — How to Run Sessions

The developer is learning Android testing hands-on. Follow this protocol every session:

1. **Check progress** — ask "where did we leave off?" or read the Roadmap below to see which task is next
2. **Give ONE worked example** for the new concept being introduced (explain every line)
3. **Assign a task** — a specific test to write for a real class in this repo
4. **Wait** — do not write the solution; let the developer attempt it
5. **Review thoroughly** — when they paste their attempt, check: correct assertions, AAA structure, edge cases missed, naming, what they did well
6. **Then move on** — suggest the next task only after the current one passes review

Never write all the tests for a class upfront. One concept → one example → one task → review → repeat.

---

## Learning Roadmap

Track progress here. Mark tasks `[x]` when the developer has written and had their test reviewed.

Reference files are in `testing/`. Open the relevant one for the full concept explanation, worked example, and task details.

### Module 1 — Pure Functions → `testing/module-1-pure-functions.md`
- [x] **Task 1a** — Test `toDomainModel()` → `app/src/test/.../domain/model/TeamsModelMappingTest.kt`
- [x] **Task 1b** — Test `Outcome` enum → `app/src/test/.../utils/OutcomeTest.kt`
- [x] **Task 1c** — Test `Resource<T>` sealed class → `app/src/test/.../utils/ResourceTest.kt`

### Module 2 — MatchViewModel (LiveData) → `testing/module-2-match-viewmodel.md`
- [x] **Task 2a** — Test all 6 initial LiveData fields → `app/src/test/.../presentation/matchactivity/MatchViewModelTest.kt`
- [x] **Task 2b** — Test `addLog()` appends correctly
- [x] **Task 2c** — Test `playNextBall()` guard fires when match is over

### Module 3 — MainViewModel (MockK) → `testing/module-3-main-viewmodel.md`
- [ ] **Task 3a** — Test API error path via mock → `app/src/test/.../presentation/mainactivity/MainViewModelTest.kt`
- [ ] **Task 3b** — Test `toggleTeamSelection` adds/removes + updates LiveData
- [ ] **Task 3c** — Test max-2 cap: selecting a 3rd team does nothing

### Module 4 — TeamsRepositoryImpl (network mocking) → `testing/module-4-repository.md`
- [ ] **Task 4a** — Success path: data is correctly mapped → `app/src/test/.../data/repositoryimpl/TeamsRepositoryImplTest.kt`
- [ ] **Task 4b** — HTTP error response → `Resource.Error`
- [ ] **Task 4c** — Network exception → `Resource.Error` with correct message + null fallback
