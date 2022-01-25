package com.yeldos.snakevalidator.domain

import com.yeldos.snakevalidator.domain.Move.RIGHT
import com.yeldos.snakevalidator.domain.fixture.BoardFixture
import com.yeldos.snakevalidator.domain.fixture.GameFixture
import com.yeldos.snakevalidator.domain.fixture.GameFixture.game
import com.yeldos.snakevalidator.domain.fixture.SnakeFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GameTest {

    @Test
    fun testStartFruitNotEaten() {
        val game = GameFixture.game.start()

        assertEquals(0, game.score)
        assertEquals(GameResult.GameOn, game.result)
        assertEquals(Coordinate(3, 1), game.board.snake.coordinate)
        assertEquals(Coordinate(4, 4), game.board.fruit.coordinate)
        assertEquals(RIGHT, game.board.snake.direction)
    }

    @Test
    fun testStartSnakeOutOfBound() {
        val game = game(BoardFixture.board(snake = SnakeFixture.snake(moves = listOf(RIGHT, RIGHT, RIGHT, RIGHT, RIGHT))))

        val result = game.start()

        assertEquals(0, result.score)
        assertEquals(GameResult.GameOverSnakeOutOfBounds, result.result)
        assertEquals(Coordinate(1, 1), result.board.snake.coordinate)
        assertEquals(Coordinate(4, 4), result.board.fruit.coordinate)
        assertEquals(RIGHT, result.board.snake.direction)
    }

    @Test
    fun testFruitNotEatenScoreTheSame() {
        val game = game(BoardFixture.board(snake = SnakeFixture.initial))

        val result = game.start()

        assertEquals(0, result.score)
        assertEquals(GameResult.GameOn, result.result)
        assertEquals(Coordinate.initial, result.board.snake.coordinate)
        assertEquals(Coordinate(4, 4), result.board.fruit.coordinate)
        assertEquals(RIGHT, result.board.snake.direction)
    }

}