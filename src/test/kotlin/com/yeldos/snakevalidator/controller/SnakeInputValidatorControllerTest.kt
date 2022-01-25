package com.yeldos.snakevalidator.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yeldos.snakevalidator.controller.fixtures.StateRequestFixture
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.downTick
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.rightTick
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.TickRequest
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.stream.Stream


@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration
internal class SnakeInputValidatorControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val validateEndpoint = "/validate"

    @ParameterizedTest
    @ArgumentsSource(FruitEatenRequestArgumentsProvider::class)
    fun testFruitEaten(request: StateRequest, expectedDirection: TickRequest, remainedTicks: List<TickRequest>) {
        mockMvc.post(validateEndpoint) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { match(MockMvcResultMatchers.jsonPath("$.score").value(request.score + 1)) }
            content { match(MockMvcResultMatchers.jsonPath("$.fruit").value(not(equalTo(request.fruit)))) }
            content { match(MockMvcResultMatchers.jsonPath("$.snake").value(not(equalTo(request.snake)))) }
            content { match(MockMvcResultMatchers.jsonPath("$.snake.x").value(request.fruit.x)) }
            content { match(MockMvcResultMatchers.jsonPath("$.snake.y").value(request.fruit.y)) }
            content { match(MockMvcResultMatchers.jsonPath("$.snake.velX").value(expectedDirection.velX)) }
            content { match(MockMvcResultMatchers.jsonPath("$.snake.velY").value(expectedDirection.velY)) }
            content { match(MockMvcResultMatchers.jsonPath("$.width").value(request.width)) }
            content { match(MockMvcResultMatchers.jsonPath("$.height").value(request.height)) }
            content { match(MockMvcResultMatchers.jsonPath("$.gameId").value(request.gameId)) }
            content { match(MockMvcResultMatchers.jsonPath("$.ticks").value(matchByList(remainedTicks))) }
        }
    }

    class FruitEatenRequestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments?>? {
            val emptyTicks = emptyList<TickRequest>()
            return Stream.of(
                of(StateRequestFixture.eatFruit, rightTick, emptyTicks),
                of(StateRequestFixture.reachFarFruit, downTick, emptyTicks),
                of(StateRequestFixture.eatFarFruitZigZag, downTick, emptyTicks),
                of(StateRequestFixture.eatFruitInSmallBoard, downTick, emptyTicks),
                of(StateRequestFixture.eatFruitExtraTicksRemained, rightTick, listOf(downTick, downTick)),
            )
        }
    }

    fun matchByList(ticks: List<TickRequest>): BaseMatcher<String> {
        return object : BaseMatcher<String>() {
            override fun describeTo(description: Description?) {
                description?.appendText(objectMapper.writeValueAsString(ticks))
            }

            override fun matches(item: Any?): Boolean {
                return objectMapper.writeValueAsString(item) == objectMapper.writeValueAsString(ticks)
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(NotFoundRequestArgumentsProvider::class)
    fun testNotFoundExceptionOrOutOfBounds(request: StateRequest) {
        mockMvc.post(validateEndpoint) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }

    class NotFoundRequestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments?>? {
            return Stream.of(
                of(StateRequestFixture.oneTickToReachFarFruit),
                of(StateRequestFixture.fruitClose),
                of(StateRequestFixture.fruitFar),
                of(StateRequestFixture.fruitFarNoTicks),
            )
        }
    }

    @ParameterizedTest(name = "{index} => {1}")
    @ArgumentsSource(InvalidMoveRequestArgumentsProvider::class)
    fun testInvalidMoveExceptionOrOutOfBounds(request: StateRequest, description: String) {
        mockMvc.post(validateEndpoint) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isIAmATeapot() }
        }
    }

    class InvalidMoveRequestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments?>? {
            return Stream.of(
                of(StateRequestFixture.outOfBound, "snake out of bound"),
                of(StateRequestFixture.leftToRightTicks, "snake moves backward"),
                of(StateRequestFixture.firstOppositeTick, "the first tick is opposite"),
                of(StateRequestFixture.invalidTick, "invalid tick, 1 and 1"),
            )
        }
    }

    @ParameterizedTest(name = "{index} => {1}")
    @ArgumentsSource(InvalidRequestArgumentsProvider::class)
    fun testInvalidRequest(request: StateRequest, description: String) {
        mockMvc.post(validateEndpoint) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    class InvalidRequestArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments?>? {
            return Stream.of(
                of(StateRequestFixture.incorrectSnakeDirection, "direction is 1 and -1"),
                of(StateRequestFixture.invalidBoard, "board size is 0 to 4"),
                of(StateRequestFixture.oneToOneBoard, "board size is 1 to 1"),
                of(StateRequestFixture.fruitInvalidPosition11to11, "fruit out of board"),
                of(StateRequestFixture.fruitInvalidPosition, "fruit position is negative"),
                of(StateRequestFixture.incorrectSnakeInitialPosition, "snake out of board"),
                of(StateRequestFixture.invalidBoardNegativeHeight, "board size is negative"),
                of(StateRequestFixture.emptyGameId, "gameId is empty"),
            )
        }
    }

    @Test
    fun newGame() {
        val width = 3
        val height = 2
        mockMvc.get("/new?w=$width&h=$height") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { match(MockMvcResultMatchers.jsonPath("$.score").value(0)) }
            content { match(MockMvcResultMatchers.jsonPath("$.width").value(width)) }
            content { match(MockMvcResultMatchers.jsonPath("$.height").value(height)) }
            content { match(MockMvcResultMatchers.jsonPath("$.gameId").isNotEmpty) }
            content { match(MockMvcResultMatchers.jsonPath("$.ticks").isEmpty) }
        }
    }
}
