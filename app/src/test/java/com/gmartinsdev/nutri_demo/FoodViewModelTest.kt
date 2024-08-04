package com.gmartinsdev.nutri_demo

import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.data.remote.ApiError
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.domain.GetCommonFoodsByName
import com.gmartinsdev.nutri_demo.domain.GetFoodByName
import com.gmartinsdev.nutri_demo.domain.GetFoods
import com.gmartinsdev.nutri_demo.ui.home.FoodViewModel
import com.gmartinsdev.nutri_demo.ui.home.UiState
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * unit test class which covers test cases in food repository
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FoodViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private lateinit var viewModel: FoodViewModel

    @Mock
    private lateinit var getFoods: GetFoods

    @Mock
    private lateinit var getFoodByName: GetFoodByName

    @Mock
    private lateinit var getCommonFoodsByName: GetCommonFoodsByName

    @Before
    fun init() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FoodViewModel(getFoods, getFoodByName, getCommonFoodsByName)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScheduler.advanceUntilIdle() // ensure all coroutines finish
    }

    @Test
    fun `ui state  - get common foods by name - success`() = runTest {
        val fakeGetCommonFoodsByName = FakeUseCase<CommonFood>()
        val fakeUseCase = FakeUseCase<Food>()

        `when`(getCommonFoodsByName("aaa")).thenReturn(fakeGetCommonFoodsByName.flow)
        `when`(getFoodByName("aaa")).thenReturn(fakeUseCase.flow)

        viewModel.fetchData("aaa")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult.success(genCommonFoods())
        fakeGetCommonFoodsByName.emit(response)
        val foodByNameResponse = ApiResult.success(genFoods())
        fakeUseCase.emit(foodByNameResponse)

        advanceUntilIdle()

        assertTrue(state.size == 2)
        assertThat(state, instanceOf(ArrayList::class.java))
        assertTrue((state[1] as UiState.Loaded).data.size == 3)
        assertTrue((state[1] as UiState.Loaded).data[0].name == "aaa")
    }

    @Test
    fun `ui state - get common foods by name - error`() = runTest {
        val fakeUseCase = FakeUseCase<CommonFood>()

        `when`(getCommonFoodsByName("zaaaaa")).thenReturn(fakeUseCase.flow)

        viewModel.fetchData("zaaaaa")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult(
            status = Status.ERROR,
            data = emptyList<CommonFood>(),
            error = ApiError(404, "movie not found")
        )
        fakeUseCase.emit(response)

        assertTrue(state.size == 2)
        assertThat(state[1], instanceOf(UiState.Error::class.java))
        assertTrue((state[1] as UiState.Error).errorMessage == "movie not found")
    }

    @Test
    fun `ui state  - get all foods - success`() = runTest {
        val fakeGetAllFoods = FakeUseCase<Food>()

        `when`(getFoods()).thenReturn(fakeGetAllFoods.flow)

        viewModel.fetchData("")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult.success(genAllFoods())
        fakeGetAllFoods.emit(response)


        assertTrue(state.size == 2)
        assertThat(state, instanceOf(ArrayList::class.java))
        assertTrue((state[1] as UiState.Loaded).data.size == 3)
        assertTrue((state[1] as UiState.Loaded).data[1].name == "bbb")
    }

    @Test
    fun `ui state - get all foods - error`() = runTest {
        val fakeGetFoodByName = FakeUseCase<Food>()

        `when`(getFoods()).thenReturn(fakeGetFoodByName.flow)

        viewModel.fetchData("")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult(
            status = Status.ERROR,
            data = emptyList<Food>(),
            error = ApiError(500, "api error")
        )
        fakeGetFoodByName.emit(response)

        assertTrue(state.size == 2)
        assertThat(state[1], instanceOf(UiState.Error::class.java))
        assertTrue((state[1] as UiState.Error).errorMessage == "api error")
    }

    /**
     * generate mock data of list of common foods
     */
    private fun genCommonFoods() =
        listOf(
            CommonFood(
                1,
                "aaa",
                "111"
            ),
            CommonFood(
                2,
                "aaa",
                "222"
            ),
            CommonFood(
                3,
                "aaa",
                "333"
            )
        )

    /**
     * generate mock data of list of foods
     */
    private fun genFoods() =
        listOf(
            Food(
                1,
                "aaa",
                1.0,
                "g",
                1.0,
                100.0,
                10.0,
                10.0,
                0.5,
                1.0,
                100.0,
                5.0,
                2.0,
                15.0,
                20.0,
                200.0,
                Photo(
                    thumb = "url.com/photoUrl"
                )
            )
        )

    /**
     * generate mock data of list of all foods
     */
    private fun genAllFoods() =
        listOf(
            Food(
                1,
                "aaa",
                1.0,
                "g",
                1.0,
                100.0,
                10.0,
                10.0,
                0.5,
                1.0,
                100.0,
                5.0,
                2.0,
                15.0,
                20.0,
                200.0,
                Photo(
                    thumb = "url.com/photoUrl"
                )
            ),
            Food(
                2,
                "bbb",
                1.0,
                "g",
                1.0,
                100.0,
                10.0,
                10.0,
                0.5,
                1.0,
                100.0,
                5.0,
                2.0,
                15.0,
                20.0,
                200.0,
                Photo(
                    thumb = "url.com/photoUrl"
                )
            ),
            Food(
                3,
                "ccc",
                1.0,
                "g",
                1.0,
                100.0,
                10.0,
                10.0,
                0.5,
                1.0,
                100.0,
                5.0,
                2.0,
                15.0,
                20.0,
                200.0,
                Photo(
                    thumb = "url.com/photoUrl"
                )
            ),
        )
}

/**
 * test class required in order to test StateFlow variable in viewmodel
 */
class FakeUseCase<T> {
    val flow = MutableSharedFlow<ApiResult<List<T>>>()

    suspend fun emit(value: ApiResult<List<T>>) {
        flow.emit(value)
    }
}