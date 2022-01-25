package com.yeldos.snakevalidator.domain

import com.yeldos.snakevalidator.domain.Move.*
import com.yeldos.snakevalidator.domain.fixture.SnakeFixture
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SnakeTest {

    @Test
    fun hasNextMove() {
        assertTrue(SnakeFixture.snake().hasNextMove())
        assertFalse(SnakeFixture.snake(moves = emptyList()).hasNextMove())
    }

    @Test
    fun testFruitEaten() {
        val snake = SnakeFixture.snake(coordinate = Coordinate(2, 2), moves = listOf(RIGHT, DOWN), move = UP)

        val nextCoordinate: Coordinate = snake.nextMove()

        val expectedCoordinate = Coordinate(3, 2)
        assertEquals(expectedCoordinate, nextCoordinate)
        assertEquals(expectedCoordinate, snake.currentCoordinate)
        assertEquals(listOf(DOWN), snake.restMoves)
        assertEquals(RIGHT, snake.lastDirection)
    }

}