package com.example.footballleague

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.footballleague.data.api.RetrofitService
import com.example.footballleague.data.model.competitions.Area
import com.example.footballleague.data.model.competitions.Competitions
import com.example.footballleague.data.model.competitions.CompetitionsListResponse
import com.example.footballleague.data.model.competitions.CurrentSeason
import com.example.footballleague.data.repos.competitions.CompetitionsListRepository
import com.example.footballleague.ui.viewmodels.CompetitionsListViewModel
import com.example.footballleague.utils.resource.Resource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CompetitionModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    lateinit var competitionsListViewModel: CompetitionsListViewModel
    lateinit var competitionsListRepository: CompetitionsListRepository

    @Mock
    lateinit var apiService: RetrofitService

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        competitionsListRepository = CompetitionsListRepository(apiService)
        competitionsListViewModel =
            CompetitionsListViewModel(
                competitionsListRepository
            )
    }

    @Test
    fun getAllCompetitions() {
        val area = Area(2072, "England", "ENG", "https://crests.football-data.org/770.svg")
        val currentSeason = CurrentSeason(1564, "2023-08-11", "2024-05-19", "15", null)
        val competition = Competitions(
            2021,
            "Premier League",
            "TIER_ONE",
            "PL",
            4,
            "5/12/2023",
            area,
            currentSeason,
            "https://crests.football-data.org/PL.png", "LEAGUE "
        )
        runBlocking {
            Mockito.`when`(competitionsListRepository.getCompetitions())
                .thenReturn(
                    Response.success(
                        CompetitionsListResponse(
                            1, listOf(competition)
                        )
                    )
                )
            competitionsListViewModel.getCompetitions()
            val event = competitionsListViewModel.competitionsResponse.getOrAwaitValue()
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success ->
                        assertEquals(
                            competition, response.data!!.competitions
                        )
                    is Resource.EmptyData, is Resource.Error,
                    is Resource.Loading -> assertNull(
                        response.data
                    )
                    is Resource.CachedData -> TODO()
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)
        try {
            afterObserve.invoke()
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }
        } finally {
            this.removeObserver(observer)
        }
        @Suppress("UNCHECKED_CAST")
        return data as T

    }
}


