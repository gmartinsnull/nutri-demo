package com.gmartinsdev.nutri_demo

import com.gmartinsdev.nutri_demo.data.remote.nutri.FoodRepository
import com.gmartinsdev.nutri_demo.data.local.FoodDao
import com.gmartinsdev.nutri_demo.data.model.CommonFood
import com.gmartinsdev.nutri_demo.data.model.Food
import com.gmartinsdev.nutri_demo.data.model.FoodNutrientsResponse
import com.gmartinsdev.nutri_demo.data.model.FoodWithIngredients
import com.gmartinsdev.nutri_demo.data.model.IngredientFood
import com.gmartinsdev.nutri_demo.data.model.Photo
import com.gmartinsdev.nutri_demo.data.model.SubRecipe
import com.gmartinsdev.nutri_demo.data.remote.ApiError
import com.gmartinsdev.nutri_demo.data.remote.ApiResult
import com.gmartinsdev.nutri_demo.data.remote.Status
import com.gmartinsdev.nutri_demo.data.remote.nutri.CommonFoodsApiResponse
import com.gmartinsdev.nutri_demo.data.remote.nutri.FoodNutrientsApiResponse
import com.gmartinsdev.nutri_demo.data.remote.nutri.RemoteDataSource
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * unit test class which covers test cases in food repository
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FoodRepositoryTest {
    private val testScheduler = TestCoroutineScheduler()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private lateinit var repository: FoodRepository

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var dao: FoodDao

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        repository = FoodRepository(remoteDataSource, dao, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch common foods by title network - success`() = runTest {
        val response = genCommonFoods()
        val foodTitle = "sushi"
        `when`(remoteDataSource.fetchCommonFoodByTitle(foodTitle)).thenReturn(
            flowOf(ApiResult.success(CommonFoodsApiResponse(response)))
        )
        `when`(dao.getCommonFoodByName(foodTitle)).thenReturn(flowOf(emptyList()), flowOf(response))

        val result = repository.getCommonFoods(foodTitle).toList()

        verify(remoteDataSource).fetchCommonFoodByTitle(foodTitle)
        verify(dao, Mockito.times(2)).getCommonFoodByName(foodTitle)

        assertTrue(result[0].data?.size == 3)
    }

    @Test
    fun `fetch common foods by title network - error`() = runTest {
        val foodTitle = "zaaaaa"
        `when`(remoteDataSource.fetchCommonFoodByTitle(foodTitle)).thenReturn(
            flowOf(
                ApiResult.error(
                    ApiError(
                        500,
                        "internal api error"
                    )
                )
            )
        )
        `when`(dao.getCommonFoodByName(foodTitle)).thenReturn(flowOf(emptyList()))

        val result = repository.getCommonFoods(foodTitle).toList()

        verify(dao).getCommonFoodByName(foodTitle)
        verify(remoteDataSource).fetchCommonFoodByTitle(foodTitle)
        verify(dao).getCommonFoodByName(foodTitle)

        assertTrue(result[0].status == Status.ERROR)
        assertTrue(result[0].data == null)
    }

    @Test
    fun `fetch food nutrients by title network - success`() = runTest {
        val foodTitle = "sushi"
        val apiResponse = FoodNutrientsApiResponse(genFoodNutrients(), null)
        val dbResponse = genFoods()[0]

        `when`(remoteDataSource.fetchFoodNutrientsByTitle(foodTitle)).thenReturn(
            flowOf(ApiResult.success(apiResponse))
        )
        `when`(dao.getFoodByName(foodTitle)).thenReturn(
            flowOf(emptyList()),
            flowOf(listOf(dbResponse))
        )

        val result = repository.getFoodByName(foodTitle).toList()

        verify(remoteDataSource).fetchFoodNutrientsByTitle(foodTitle)
        verify(dao, Mockito.times(2)).getFoodByName(foodTitle)

        assertTrue(result[0].data?.first() is Food)
    }

    @Test
    fun `fetch food nutrients by title network - error`() = runTest {
        val foodTitle = "sushi"

        `when`(remoteDataSource.fetchFoodNutrientsByTitle(foodTitle)).thenReturn(
            flowOf(
                ApiResult.error(
                    ApiError(
                        500,
                        "internal api error"
                    )
                )
            )
        )
        `when`(dao.getFoodByName(foodTitle)).thenReturn(flowOf(emptyList()))

        val result = repository.getFoodByName(foodTitle).toList()

        verify(dao).getFoodByName(foodTitle)
        verify(remoteDataSource).fetchFoodNutrientsByTitle(foodTitle)
        verify(dao).getFoodByName(foodTitle)

        assertTrue(result[0].status == Status.ERROR)
        assertTrue(result[0].data == null)
    }

    @Test
    fun `fetch ingredient as food by name network - success`() = runTest {
        val foodTitle = "sushi"
        val apiResponse = FoodNutrientsApiResponse(genFoodNutrients(), null)
        val dbResponse = IngredientFood(
            1,
            "aaa",
            1.0,
            50.0,
            1.0,
            100.0,
            10.0,
            10.0,
            0.5,
            1.0,
            100.0,
            5.0,
            2.0,
            15.0
        )

        `when`(remoteDataSource.fetchFoodNutrientsByTitle(foodTitle)).thenReturn(
            flowOf(ApiResult.success(apiResponse))
        )
        `when`(dao.getIngredientAsFoodByName(foodTitle)).thenReturn(
            flowOf(emptyList()),
            flowOf(listOf(dbResponse))
        )

        val result = repository.fetchIngredientAsFoodByName(foodTitle).toList()

        verify(remoteDataSource).fetchFoodNutrientsByTitle(foodTitle)
        verify(dao, Mockito.times(2)).getIngredientAsFoodByName(foodTitle)

        assertTrue(result[0].data?.first() is IngredientFood)
    }

    @Test
    fun `fetch ingredient as food by name network - error`() = runTest {
        val foodTitle = "sushi"

        `when`(remoteDataSource.fetchFoodNutrientsByTitle(foodTitle)).thenReturn(
            flowOf(
                ApiResult.error(
                    ApiError(
                        500,
                        "internal api error"
                    )
                )
            )
        )
        `when`(dao.getIngredientAsFoodByName(foodTitle)).thenReturn(flowOf(emptyList()))

        val result = repository.fetchIngredientAsFoodByName(foodTitle).toList()

        verify(dao).getIngredientAsFoodByName(foodTitle)
        verify(remoteDataSource).fetchFoodNutrientsByTitle(foodTitle)
        verify(dao).getIngredientAsFoodByName(foodTitle)

        assertTrue(result[0].status == Status.ERROR)
        assertTrue(result[0].data?.first() == null)
    }

    @Test
    fun `fetch all foods database - success`() = runTest {
        val response = genFoods()
        `when`(dao.getAll()).thenReturn(flowOf(response))

        val result = repository.getFoodsFromDb().toList()

        verify(dao).getAll()

        assertTrue(result[0].status == Status.SUCCESS)
        assertTrue(result[0].data?.size == 3)
    }

    @Test
    fun `fetch all foods database - success - empty`() = runTest {
        `when`(dao.getAll()).thenReturn(flowOf(emptyList()))

        val result = repository.getFoodsFromDb().toList()

        verify(dao).getAll()

        assertTrue(result[0].status == Status.SUCCESS)
        assertTrue(result[0].data?.size == 0)
    }

    @Test
    fun `fetch food with ingredients by id database - success`() = runTest {
        val response = genFoodWithIngredients()
        val foodId = 123
        `when`(dao.getFoodWithIngredients(foodId)).thenReturn(flowOf(response))

        val result = repository.getFoodIngredientsFromDb(foodId).toList()

        verify(dao).getFoodWithIngredients(foodId)

        assertTrue(result[0].status == Status.SUCCESS)
        assertTrue(result[0].data?.first() is FoodWithIngredients)
        assertTrue(result[0].data?.first()?.food?.name == "aaa")
    }

    @Test
    fun `fetch food with ingredients by id database - success - empty`() = runTest {
        val foodId = 123
        `when`(dao.getFoodWithIngredients(foodId)).thenReturn(flowOf(emptyList()))

        val result = repository.getFoodIngredientsFromDb(foodId).toList()

        verify(dao).getFoodWithIngredients(foodId)

        assertTrue(result[0].status == Status.SUCCESS)
        assertTrue(result[0].data?.size == 0)
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
                "bbb",
                "222"
            ),
            CommonFood(
                3,
                "ccc",
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
            )
        )

    /**
     * generate mock data of list of food with nutrients and ingredients/sub-recipes
     */
    private fun genFoodNutrients() =
        listOf(
            FoodNutrientsResponse(
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
                ),
                listOf(
                    SubRecipe(
                        1,
                        111,
                        1.0,
                        "aaa1-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        2,
                        111,
                        1.0,
                        "aaa2-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        3,
                        111,
                        1.0,
                        "aaa3-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                )
            ),
            FoodNutrientsResponse(
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
                ),
                listOf(
                    SubRecipe(
                        1,
                        111,
                        1.0,
                        "bbb1-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        2,
                        111,
                        1.0,
                        "bbb2-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        3,
                        111,
                        1.0,
                        "bbb3-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                )
            ),
            FoodNutrientsResponse(
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
                ),
                listOf(
                    SubRecipe(
                        1,
                        111,
                        1.0,
                        "ccc1-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        2,
                        111,
                        1.0,
                        "ccc2-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        3,
                        111,
                        1.0,
                        "ccc3-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                )
            )
        )

    /**
     * generate mock data of list of ingredients as food
     */
    private fun genFoodWithIngredients() =
        listOf(
            FoodWithIngredients(
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
                listOf(
                    SubRecipe(
                        1,
                        111,
                        1.0,
                        "aaa1-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        2,
                        111,
                        1.0,
                        "aaa2-recipe",
                        10.0,
                        1.0,
                        "g"
                    ),
                    SubRecipe(
                        3,
                        111,
                        1.0,
                        "aaa3-recipe",
                        10.0,
                        1.0,
                        "g"
                    )
                )
            )
        )
}