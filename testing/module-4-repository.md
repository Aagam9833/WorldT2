# Module 4 — TeamsRepositoryImpl (Mocking the Network)

**Prerequisite:** Module 3 complete.

---

## What this module covers

`TeamsRepositoryImpl` is the only class in the app that touches the network. It wraps the `ApiService` call and converts the result into `Resource.Success` or `Resource.Error`. This is the layer where most real-world bugs hide — wrong error handling, missing null checks, swallowed exceptions.

Testing it well means covering three paths:
1. API returns 200 with a body → `Resource.Success` with mapped domain model
2. API returns a non-2xx response → `Resource.Error`
3. Network throws an exception → `Resource.Error` with message

---

## How Retrofit responses work in tests

Retrofit uses `Response<T>` to wrap HTTP responses. You create fake ones like this:

```kotlin
import retrofit2.Response

// Simulate a successful 200 response with a body
Response.success(listOf(TeamsModel("India", "🇮🇳")))

// Simulate a 404 error response
Response.error(404, okhttp3.ResponseBody.create(null, "Not Found"))
```

You don't need a real server. You mock `ApiService` to return these `Response` objects directly.

---

## Worked example

```kotlin
package com.aagamshah.worldt2.data.repositoryimpl

import com.aagamshah.worldt2.data.model.TeamsModel
import com.aagamshah.worldt2.data.remote.ApiService
import com.aagamshah.worldt2.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TeamsRepositoryImplWorkedExampleTest {

    private val mockApiService = mockk<ApiService>()
    private lateinit var sut: TeamsRepositoryImpl

    @Before
    fun setUp() {
        sut = TeamsRepositoryImpl(mockApiService)
    }

    @Test
    fun `given api returns 200 with body, when getTeams called, then returns Resource Success`() = runTest {
        // Arrange
        val fakeBody = listOf(TeamsModel(name = "India", flag = "🇮🇳"))
        coEvery { mockApiService.getTeams() } returns Response.success(fakeBody)

        // Act
        val result = sut.getTeams()

        // Assert
        assertTrue(result is Resource.Success)
    }
}
```

Notice:
- `TeamsRepositoryImpl` takes `ApiService` as a constructor param — that's what makes it testable (no static calls)
- `runTest` wraps the whole test because `getTeams()` is a `suspend` function
- You need `coEvery` because `mockApiService.getTeams()` is `suspend`

---

## Task 4a — Test the success path with data integrity

Extend the worked example. After asserting it's a `Resource.Success`, also assert:
- The result contains the correct number of items
- The `name` of the first item matches what was in the mock response
- `isSelected` is `false` on the mapped result

**Create:** `app/src/test/java/com/aagamshah/worldt2/data/repositoryimpl/TeamsRepositoryImplTest.kt`

---

## Task 4b — Test the error response path

Look at `TeamsRepositoryImpl`:

```kotlin
} else {
    Resource.Error(response.errorBody().toString())
}
```

Make `mockApiService.getTeams()` return a non-2xx response and assert:
- Result is `Resource.Error`
- The message is not null or blank

Use `Response.error(500, ResponseBody.create(null, "Server Error"))` to create the fake error response.

---

## Task 4c — Test the exception path

Look at the `catch` block:

```kotlin
} catch (e: Exception) {
    Resource.Error(e.localizedMessage ?: "Something went wrong")
}
```

Make `mockApiService.getTeams()` throw an exception using MockK:

```kotlin
coEvery { mockApiService.getTeams() } throws RuntimeException("No connection")
```

Assert:
- Result is `Resource.Error`
- The message is `"No connection"` (not the fallback `"Something went wrong"`)

Then write a second test where the exception message IS null — verify the fallback message `"Something went wrong"` is used instead.

---

## Putting it all together

At the end of Module 4, you will have tested:

| Class | What you tested |
|-------|-----------------|
| `toDomainModel()` | Pure mapping — 4 cases |
| `Outcome` | Enum properties — 3 cases |
| `MatchViewModel` | Initial state, log appending, guard clause |
| `MainViewModel` | API integration via mock, selection logic, 2-team cap |
| `TeamsRepositoryImpl` | Success, HTTP error, network exception |

This is solid unit test coverage for every non-UI class in the app.

---

## What comes after this?

**Module 5 (bonus):** Instrumented tests — run on a real device/emulator
- Espresso for UI interactions
- Testing that selecting 2 teams enables the Start button
- Testing that `MatchActivity` receives the correct team names via Intent

These are optional for now. Complete Modules 1–4 first.

---

## What "passing" looks like

Paste your file once all tests are green. Review will check:
- Are all three paths covered (success, HTTP error, exception)?
- Do you check the data contents in 4a, not just the `Resource` type?
- Does 4c have BOTH the "message present" and "message null → fallback" cases?
