# Module 2 — MatchViewModel (LiveData + InstantTaskExecutorRule)

**Prerequisite:** Module 1 complete.

---

## The problem with LiveData in tests

`LiveData` is designed to post updates on the **main thread**. In a unit test there is no Android main thread. If you try to observe LiveData without fixing this, your assertions will silently fail or crash.

**The fix:** `InstantTaskExecutorRule`

This JUnit rule replaces Android's background executor with a synchronous one, so LiveData updates happen immediately on the test thread.

```kotlin
@get:Rule
val instantTaskExecutorRule = InstantTaskExecutorRule()
```

Add this field to any test class that reads `.value` from a `LiveData`.

---

## Reading LiveData in a test

You don't need to `observe()` in unit tests. Just read `.value` directly after calling the method under test:

```kotlin
val result = viewModel.someLiveData.value
assertEquals("expected", result)
```

This works because `InstantTaskExecutorRule` makes all posts synchronous.

---

## Worked example

Here's a complete test for `MatchViewModel`'s initial `outcome` LiveData value.

```kotlin
package com.aagamshah.worldt2.presentation.matchactivity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchViewModelWorkedExampleTest {

    // This rule makes LiveData work synchronously in tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var sut: MatchViewModel   // sut = System Under Test

    @Before
    fun setUp() {
        // MatchViewModel takes team names directly — no mocking needed
        sut = MatchViewModel("India", "Australia")
    }

    @Test
    fun `initial outcome is Match Start`() {
        // No Arrange needed — init{} already ran in setUp()

        // Act — read the LiveData value
        val result = sut.outcome.value

        // Assert
        assertEquals("Match Start", result)
    }
}
```

Notice:
- `@get:Rule` (not `@Rule`) is the Kotlin syntax for JUnit rules
- `MatchViewModel` takes strings, not Android objects — so no mocking needed here
- `setUp()` runs `init {}` which sets the initial state

---

## Task 2a — Test initial state

**File to look at:** `app/src/main/java/com/aagamshah/worldt2/presentation/matchactivity/MatchViewModel.kt` (the `init {}` block)

**Create:** `app/src/test/java/com/aagamshah/worldt2/presentation/matchactivity/MatchViewModelTest.kt`

The `init {}` block sets up 6 LiveData fields. Test that ALL of them start with the correct value:

| LiveData field | Expected initial value |
|----------------|------------------------|
| `winner` | `""` (empty string) |
| `outcome` | `"Match Start"` |
| `isFirstInnings` | `true` |
| `teamOneStats.status` | `Status.BATTING` |
| `teamTwoStats.status` | `Status.BOWLING` |
| `logs` | non-null, not empty (init adds 3 log entries) |

Write one test per LiveData field. Reuse the same `setUp()`.

**How to run:**
```bash
./gradlew test --tests "com.aagamshah.worldt2.presentation.matchactivity.MatchViewModelTest"
```

---

## Task 2b — Test `addLog()`

Still in the same `MatchViewModelTest.kt` file.

`addLog()` appends a string to the `logs` LiveData list. At init, 3 logs are already added.

Write tests for:

| # | Scenario | What to assert |
|---|----------|----------------|
| 1 | After calling `addLog("Test event")`, logs size increases by 1 | `logs.value?.size == 4` |
| 2 | The added string appears at the end of the list | `logs.value?.last() == "Test event"` |
| 3 | Calling `addLog()` twice adds both entries | `logs.value?.size == 5` |

---

## Task 2c — Test the `playNextBall()` guard

Look at `playNextBall()` in `MatchViewModel`:

```kotlin
fun playNextBall() {
    if (_winner.value?.isNotBlank() == true) return  // <-- guard clause
    ...
}
```

When the match is over (`winner` is not blank), `playNextBall()` should do nothing.

The problem: you can't set `_winner` from outside — it's private. But you CAN drive the match to a finished state by calling `playNextBall()` until a winner appears.

Write a test that:
1. Calls `playNextBall()` in a loop until `winner.value` is not blank
2. Records the `logs` size at that point
3. Calls `playNextBall()` one more time
4. Asserts that `logs` size did NOT change (the guard fired)

**Hint:** The match runs for max 12 balls per innings × 2 innings = 24 balls max. A `repeat(50)` loop that breaks when winner is set is safe.

---

## What "passing" looks like

Paste your test file once tests are green. Review will check:
- Is `@get:Rule` applied correctly?
- Are all 6 initial state fields tested in 2a?
- Does 2c actually verify the guard clause fires (not just that it doesn't crash)?
