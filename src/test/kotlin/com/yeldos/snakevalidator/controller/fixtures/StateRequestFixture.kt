package com.yeldos.snakevalidator.controller.fixtures

import com.yeldos.snakevalidator.controller.fixtures.FruitRequestFixture.fourToFour
import com.yeldos.snakevalidator.controller.fixtures.FruitRequestFixture.fruit
import com.yeldos.snakevalidator.controller.fixtures.FruitRequestFixture.fruitFourToZero
import com.yeldos.snakevalidator.controller.fixtures.SnakeRequestFixture.snake
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.downTick
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.fourDownTicks
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.fourRightTicks
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.leftTick
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.rightTick
import com.yeldos.snakevalidator.controller.fixtures.TickRequestFixture.tick
import com.yeldos.snakevalidator.infrastructure.controller.request.FruitRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.SnakeRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.TickRequest
import org.apache.tomcat.util.codec.binary.Base64


object StateRequestFixture {
    val fruitFar by lazy { state(fruit = fruitFourToZero, tickRequests = listOf(rightTick)) }
    val fruitFarNoTicks by lazy { state(fruit = fruitFourToZero) }
    val fruitClose by lazy { state(fruit = fruitFourToZero, tickRequests = listOf(rightTick, rightTick, rightTick, downTick, rightTick, downTick)) }
    val oneTickToReachFarFruit by lazy { state(fruit = fourToFour, tickRequests = fourRightTicks + downTick + downTick + downTick) }

    val eatFruit by lazy { state(fruit = fruitFourToZero, tickRequests = fourRightTicks) }
    val reachFarFruit by lazy { state(fruit = fourToFour, score = 4, tickRequests = fourRightTicks + fourDownTicks) }
    val eatFruitExtraTicksRemained by lazy { state(fruit = fruitFourToZero, score = 4, tickRequests = fourRightTicks + downTick + downTick) }
    val eatFruitInSmallBoard by lazy { state(width = 1, height = 2, fruit = fruit(0, 1), snakeRequest = snake(velX = 0, velY = 1), tickRequests = listOf(downTick)) }
    val eatFarFruitZigZag by lazy { state(fruit = fourToFour, tickRequests = listOf(rightTick, rightTick, downTick, rightTick, downTick, downTick, rightTick, downTick)) }

    val outOfBound by lazy { state(fruit = fruit(x = 1, y = 0), tickRequests = fourRightTicks + rightTick + rightTick) }
    val leftToRightTicks by lazy { state(fruit = fruit(1, 0), tickRequests = listOf(leftTick, rightTick)) }
    val firstOppositeTick by lazy { state(fruit = fruit(x = 1, y = 1), snakeRequest = snake(1, 1, -1, 0), tickRequests = listOf(rightTick)) }

    val incorrectSnakeDirection by lazy { state(fruit = fruit(x = 1, y = 0), snakeRequest = snake(1, 1, -1, 1), tickRequests = listOf(rightTick)) }
    val incorrectSnakeInitialPosition by lazy { state(fruit = fruit(x = 1, y = 0), snakeRequest = snake(-1, 1, 1, 0), tickRequests = listOf(rightTick)) }
    val invalidTick by lazy { state(fruit = fruit(1, 0), tickRequests = listOf(tick(1, 1))) }
    val invalidBoard by lazy { state(width = 0, height = 4, fruit = fruit(1, 0), tickRequests = listOf(tick(0, 1))) }
    val invalidBoardNegativeHeight by lazy { state(width = 3, height = -1, fruit = fruit(1, 0), tickRequests = listOf(tick(0, 1))) }
    val oneToOneBoard by lazy { state(width = 1, height = 1, fruit = fruit(1, 0), tickRequests = listOf(tick(0, 1))) }
    val fruitInvalidPosition11to11 by lazy { state(fruit = fruit(11, 11), tickRequests = listOf()) }
    val fruitInvalidPosition by lazy { state(fruit = fruit(-1, 0), tickRequests = listOf()) }
    val emptyGameId by lazy { state(gameId = "", fruit = fruit(-1, 0), tickRequests = listOf()) }

    fun state(
        gameId: String = "game id",
        width: Int = 5,
        height: Int = 5,
        score: Int = 0,
        snakeRequest: SnakeRequest = snake,
        fruit: FruitRequest = fruit(),
        tickRequests: List<TickRequest> = listOf(rightTick),
        code: String? = encodeFruit(fruit.x, fruit.y, gameId),
    ) = StateRequest(gameId = gameId, width = width, height = height, score = score, fruit = fruit, snake = snakeRequest, ticks = tickRequests, code = code)

    //incorrect to have copy of encoding function
    private fun encodeFruit(x: Int, y: Int, uid: String): String? {
        return Base64.encodeBase64String("the most secure encoding in the entire galaxy $x $y $uid".toByteArray())
    }
}