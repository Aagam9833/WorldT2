# Module 3 — MainViewModel (MockK + Coroutines)

**Prerequisite:** Module 2 complete.

---

## The problem this module solves

`MainViewModel` depends on `TeamsRepository`. In a test you cannot use the real repository — it would make a real network call. Instead, you create a **fake version** of the repository that you control.

That fake is called a **mock**.

---

## MockK basics

```kotlin
// Create a mock — a fake object that implements the interface
val mockRepo = mockk<TeamsRepository>()

// Tell the mock what to return when a method is called
// Use `coEvery` (not `every`) for suspend functions
coEvery { mockRepo.getTeams() } returns Resource.Success(emptyList())

// After the test, verify a method was actually called
coVerify { mockRepo.getTeams() }
```

**`every` vs `coEvery`:**
- `every` — for regular functions
- `coEvery` — for `suspend` functions (the `co` stands for coroutine)

**relaxed mocks:** If you don't care about a method's return value but don't want to crash, use `mockk(relaxed = true)`. It returns sensible defaults (0, false, empty, null) automatically.

---

## The coroutines problem

`MainViewModel.callTeamsApi()` launches a coroutine on `Dispatchers.IO`. In a test, `Dispatchers.IO` is a real thread pool — your test could finish before the coroutine does.

The fix: add `InstantTaskExecutorRule` (same as Module 2) AND use `runTest` + `TestCoroutineScheduler`. But since `MainViewModel` calls the API in `init {}`, the simplest approach for these tasks is to advance the coroutine manually after creating the ViewModel.

You'll see `advanceUntilIdle()` used in the examples below — it runs all pending coroutine work before you assert.

---

## Dependency you need to add

Add this import at the top of your test file:

```kotlin
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
```

And in your test class setUp/tearDown:

```kotlin
private val testDispatcher = StandardTestDispatcher()

@Before
fun setUp() {
    Dispatchers.setMain(testDispatcher)
    // create the ViewModel here
}

@After
fun tearDown() {
    Dispatchers.resetMain()
}
```

---

## Worked example

```kotlin
package com.aagamshah.worldt2.presentation.mainactivity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aagamshah.worldt2.domain.model.TeamsModel
import com.aagamshah.worldt2.domain.repository.TeamsRepository
import com.aagamshah.worldt2.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelWorkedExampleTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val mockRepo = mockk<TeamsRepository>()
    private lateinit var sut: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given api returns success, when viewmodel initialises, then teams LiveData has Success`() = runTest {
        // Arrange
        val fakeTeams = listOf(TeamsModel(name = "India", flag = "🇮🇳"))
        coEvery { mockRepo.getTeams() } returns Resource.Success(fakeTeams)

        // Act — creating the ViewModel triggers init{} which calls the API
        sut = MainViewModel(mockRepo)
        advanceUntilIdle()  // wait for the coroutine to finish

        // Assert
        assertTrue(sut.teams.value is Resource.Success)
    }
}
```

---

## Task 3a — Test API error path

Still using `mockRepo`, make `getTeams()` return `Resource.Error("Network failed")`.
Assert that `sut.teams.value` is `Resource.Error` and that the message matches.

**Create:** `app/src/test/java/com/aagamshah/worldt2/presentation/mainactivity/MainViewModelTest.kt`

---

## Task 3b — Test `toggleTeamSelection()`

`toggleTeamSelection()` is pure state logic — it does not call the repository.
Set up the ViewModel, post a fake success state to `teams` LiveData first (by going through the API mock), then call `toggleTeamSelection()`.

Write tests for:

| # | Scenario | What to assert |
|---|----------|----------------|
| 1 | Selecting a team adds it to `selectedTeams` | `selectedTeams.contains("India") == true` |
| 2 | Selecting the same team again removes it | `selectedTeams.isEmpty()` |
| 3 | Toggling a team updates `isSelected` on the LiveData item | The item in `teams.value!!.data` has `isSelected == true` |

---

## Task 3c — Test the max-2 cap

Write a test that:
1. Selects team A → verify size is 1
2. Selects team B → verify size is 2
3. Tries to select team C → verify size is still 2 (C was ignored)

The enforcement is in `MainViewModel.toggleTeamSelection()` — read it carefully before writing this test.

---

## What "passing" looks like

Paste your file once all tests are green. Review will check:
- Is `Dispatchers.setMain` / `resetMain` set up in `@Before` / `@After`?
- Are you using `coEvery` (not `every`) for suspend functions?
- Is `advanceUntilIdle()` called before asserting LiveData state?
- Are the sad path (error) tests as thorough as the happy path?
