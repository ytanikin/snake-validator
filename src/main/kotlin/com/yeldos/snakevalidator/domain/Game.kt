package com.yeldos.snakevalidator.domain

import com.yeldos.snakevalidator.domain.GameResult.*
import java.security.SecureRandom
import java.util.*

data class Game(val board: Board, val score: Int, val uid: String, val result: GameResult) {
    init {
        require(uid.isNotBlank()) { "uid must not be blank" }
        require(score >= 0) { "score must be greater than or equal to 0" }
    }

    val fruitPosition = board.fruit.coordinate

    fun start(): Game {
        if (board.hasReachedBorder()) {
            return copyWithLastSnakePosition(GameOverSnakeOutOfBounds)
        }
        if (board.reachFruit()) {
            return copyWithFreshFruit(FruitEaten)
        }
        return copyWithLastSnakePosition(GameOn)
    }

    private fun copyWithFreshFruit(lastResult: GameResult): Game {
        return copyWithLastSnakePosition(lastResult, generateFreshFruit(board.width, board.height),score + 1)
    }

    private fun copyWithLastSnakePosition(newResult: GameResult, fruit: Fruit = board.fruit, newScore: Int = score): Game {
        return copy(result = newResult, score = newScore, board = board.copy(snake = board.snake.copyWithLastPosition(), fruit = fruit))
    }

    private fun generateFreshFruit(width: Int, height: Int): Fruit {
        var freshFruit: Fruit
        do {
            freshFruit = generateFruit(width, height)
        } while (freshFruit.coordinate == board.snake.currentCoordinate)
        return freshFruit
    }

    companion object {
        fun from(height: Int, width: Int): Game {
            return Game(Board.from(height, width, generateFruit(width, height)), 0, UUID.randomUUID().toString(), GameOn)
        }

        private fun generateFruit(width: Int, height: Int): Fruit {
            val secureRandom = SecureRandom.getInstanceStrong()
            val col = secureRandom.nextInt(height) + 1
            val row =  secureRandom.nextInt(width) + 1
            return Fruit(Coordinate(col, row))
        }
    }
}