package com.yeldos.snakevalidator.service.impl

import com.yeldos.snakevalidator.domain.Move
import com.yeldos.snakevalidator.domain.exception.InvalidMoveException
import com.yeldos.snakevalidator.infrastructure.controller.request.FruitRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.SnakeRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.TickRequest
import com.yeldos.snakevalidator.service.InputValidator
import com.yeldos.snakevalidator.service.exception.InvalidRequestException
import org.springframework.stereotype.Service

@Service
class InputValidatorImpl : InputValidator {
    override fun validate(input: StateRequest) {
        verifyInput(input)
        verifyBackwardMove(input)
    }

    /**
     * * Verifies backward move and throws [InvalidMoveException] if it is detected.
     * * Verifies the tick has a valid move in [Move.valueOf] and throws [InvalidMoveException] if it is not.
     */
    private fun verifyBackwardMove(input: StateRequest) {
        if (input.ticks.isEmpty()) return
        verifyFirstMoveWithSnakeDirection(input)
        val first = input.ticks.first()
        val initial = Move.valueOf(first.velX, first.velY)
        input.ticks.fold(initial) { prev, tick ->
             Move.valueOf(tick.velX, tick.velY).also { move ->
                if (move.isBackward(prev)) {
                    throw InvalidMoveException()
                }
            }
        }
    }

    private fun verifyFirstMoveWithSnakeDirection(input: StateRequest) {
        val snakeMove = Move.valueOf(input.snake.velX, input.snake.velY)
        val firstTick = input.ticks.first()
        val firstTickMove = Move.valueOf(firstTick.velX, firstTick.velY)
        if (snakeMove.isBackward(firstTickMove)) {
            throw InvalidMoveException()
        }
    }

    private fun verifyInput(input: StateRequest) {
        val messages = buildList {
            verifyState(input)
            verifyBord(input.width, input.height)
            verifySnake(input.snake, input.width, input.height)
            verifyFruit(input.fruit, input.width, input.height)
            verifyTicks(input.ticks)
        }
        if (messages.isNotEmpty()) {
            throw InvalidRequestException(messages)
        }
    }

    /**
     * Verifies the state request has a valid **gameId** and score to be greater than 0.
     */
    private fun MutableList<String>.verifyState(input: StateRequest) {
        if (input.gameId.isEmpty()) add("GameId must not be empty but was: ${input.gameId}")
        if (input.score < 0) add("Score must be 0 or greater than 0 but was: ${input.score}")
    }

    /**
     * Verifies ticks  has a valid **velX** and **velY**, such as not both of them are 0 or both of them are not 0.
     * And one of the -1, 0, 1 values
     * Expected to be one 0 and one not 0.
     */
    private fun MutableList<String>.verifyTicks(ticks: List<TickRequest>) {
        if (ticks.any { it.velX == 0 && it.velY == 0 }) add("Tick must not be x: 0 and y: 0 but was: $ticks")
        if (ticks.any { it.velX !in -1..1 || it.velY !in -1..1 }) add("Tick must  be -1 or 0 or 1, but was: $ticks")
    }

    /**
     * Verifies snake has a valid **velX** and **velY**, such as not both of them are 0 or both of them are not 0.
     * Verifies that initial direction was sent to the client and returned are not out of bounds.
     */
    private fun MutableList<String>.verifySnake(snake: SnakeRequest, width: Int, height: Int) {
        if (snake.x < 0 || snake.y < 0) {
            add("Snake coordinates must be greater than 0 but were x: ${snake.x} and y: ${snake.y}")
        }
        if (snake.x >= width || snake.y >= height) {
            add("Snake coordinates must be less than width and height "
                    + "but were snake.x: ${snake.x} and snake.y: ${snake.y} and width: $width and height: $height")
        }
        if (snake.velX == 0 && snake.velY == 0 || snake.velX != 0 && snake.velY != 0) {
            add("Snake must move in one of the directions but was: x: ${snake.x}, and y: ${snake.y}")
        }
    }

    /**
     * Verifies fruit has a valid **x** and **y**, such as not negative and inside the box
     */
    private fun MutableList<String>.verifyFruit(fruit: FruitRequest, width: Int, height: Int) {
        if (fruit.x < 0 || fruit.y < 0) {
            add("Fruit coordinates must be greater than 0 but were x: ${fruit.x} and y: ${fruit.y}")
        }
        if (fruit.x > width || fruit.y > height) {
            add("Fruit coordinates must be less than width: $width and height: $height but were x: ${fruit.x} and y: ${fruit.y}")
        }
    }

    /**
     * Verifies the border has a valid [width] and [height], such as not negative and not 1 both of them.
     */
    private fun MutableList<String>.verifyBord(width: Int, height: Int) {
        if (width < 1 || height < 1) add("Width and height must be greater than 0 but were width: $width and height: $height")
        if (width + height <= 2) add("Total value of width and height must be greater than 2 but were width: $width and height: $height")
    }
}