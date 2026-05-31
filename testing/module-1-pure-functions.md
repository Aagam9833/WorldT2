# Module 1 — Pure Functions

**No mocking. No Android deps. Just JUnit.**

---

## The big idea

A function is "pure" if:
- same input always gives the same output
- it doesn't read from or write to anything outside itself (no network, no database, no Android)

Pure functions are the easiest things to test. You call the function, check the result, done.

---

## The AAA Pattern

Every test you write should have three sections:

```
Arrange  — set up the input
Act      — call the function
Assert   — check the result
```

Never skip a section, and never mix them. One test = one thing being verified.

---

## Test naming

Use backtick names that read like a sentence:

```kotlin
fun `given empty list, when mapped, then returns empty list`()
```

Format: `given [state], when [action], then [result]`

---

## Worked example

Here's a complete test for the `Outcome.ZERO` enum entry — a property that never changes, so we can assert its exact value.

```kotlin
package com.aagamshah.worldt2.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class OutcomeWorkedExampleTest {

    @Test
    fun `ZERO outcome has correct display text`() {
        // Arrange — the enum entry itself is the input
        val outcome = Outcome.ZERO

        // Act
        val result = outcome.displayText

        // Assert
        assertEquals("0 run", result)
    }

    @Test
    fun `ZERO outcome has probability of 5`() {
        assertEquals(5, Outcome.ZERO.probability)
    }
}
```

Notice:
- No `@Before` needed — enums are always available
- Each test asserts exactly ONE thing
- Short tests are fine; don't pad them

---

## Task 1a — Test `toDomainModel()`

**File to look at:** `app/src/main/java/com/aagamshah/worldt2/domain/model/TeamsModel.kt`

```kotlin
fun List<TeamsModel>.toDomainModel(): List<com.aagamshah.worldt2.domain.model.TeamsModel> {
    return this.map {
        TeamsModel(name = it.name, flag = it.flag, isSelected = false)
    }
}
```

This maps `List<data.model.TeamsModel>` → `List<domain.model.TeamsModel>`.
The key invariant: `isSelected` is **always** `false` after mapping, no matter what.

**Create:** `app/src/test/java/com/aagamshah/worldt2/domain/model/TeamsModelMappingTest.kt`

Write tests for these four cases:

| # | Scenario | What to assert |
|---|----------|----------------|
| 1 | Empty list input | Result is empty |
| 2 | Single item | `name` and `flag` are preserved |
| 3 | Multiple items | Result size matches input size |
| 4 | Item passed in with any `isSelected` value | `isSelected` is always `false` in the result |

**Hint for case 4:** `data.model.TeamsModel` doesn't have `isSelected`, so you're proving the domain model always starts as `false` regardless of what the mapping function does.

**How to run:**
```bash
./gradlew test --tests "com.aagamshah.worldt2.domain.model.TeamsModelMappingTest"
```

---

## Task 1b — Test `Outcome` enum

**File to look at:** `app/src/main/java/com/aagamshah/worldt2/utils/Outcome.kt`

```kotlin
enum class Outcome(val displayText: String, val probability: Int) {
    ZERO("0 run", 5),
    ONE("1 run", 4),
    TWO("2 runs", 3),
    THREE("3 runs", 1),
    FOUR("4 runs", 2),
    SIX("6 runs", 1),
    WIDE("Wide", 1),
    NO_BALL("No Ball", 2),
    OUT("Out", 2)
}
```

**Create:** `app/src/test/java/com/aagamshah/worldt2/utils/OutcomeTest.kt`

Write tests for these cases:

| # | Scenario | What to assert |
|---|----------|----------------|
| 1 | No outcome has a blank `displayText` | All entries have non-empty display text |
| 2 | No outcome has a zero or negative `probability` | All entries have `probability > 0` |
| 3 | Total probability across all entries equals 21 | Sum of all `probability` values |

**Hint for cases 1 and 2:** Use `Outcome.entries` to iterate all enum values in a loop inside a single test.

**How to run:**
```bash
./gradlew test --tests "com.aagamshah.worldt2.utils.OutcomeTest"
```

---

## What "passing" looks like

When you run the tests and see:

```
BUILD SUCCESSFUL in Xs
X tests completed, 0 failed
```

Paste your test file in the chat. The review will check:
- Do you cover all 4 cases for 1a and all 3 for 1b?
- Is each test asserting exactly one thing?
- Are the test names readable?
- Are there any edge cases you missed?
