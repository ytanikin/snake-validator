package com.yeldos.snakevalidator.domain

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class BoardTest {

    @Test
    fun hasReachedBorder() {
        var board = Board(5, 5, Snake.initial, Fruit(Coordinate(2, 2)))
        assertFalse(board.hasReachedBorder())

        board = Board(5, 5, Snake(Coordinate(0, 0), Move.RIGHT, emptyList()), Fruit(Coordinate(2, 2)))
        assertFalse(board.hasReachedBorder())

        board = Board(5, 5, Snake(Coordinate(0, 0), Move.RIGHT, listOf(Move.UP)), Fruit(Coordinate(2, 2)))
        assertTrue(board.hasReachedBorder())
    }

    @Test
    fun reachFruit() {
        var board = Board(5, 5, Snake.initial, Fruit(Coordinate(2, 2)))
        assertFalse(board.reachFruit())

        val moves = listOf(Move.RIGHT, Move.DOWN, Move.RIGHT, Move.DOWN)
        board = Board(5, 5, Snake(Coordinate(0, 0), Move.RIGHT, moves), Fruit(Coordinate(2, 2)))
        assertTrue(board.reachFruit())
    }

    @Test
    fun initException() {
        assertThrows(IllegalArgumentException::class.java) {
            Board(0, 0, Snake.initial, Fruit(Coordinate(2, 2)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            Board(1, 1, Snake.initial, Fruit(Coordinate(2, 2)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            Board(100, 0, Snake.initial, Fruit(Coordinate(2, 2)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            Board(1, 0, Snake.initial, Fruit(Coordinate(2, 2)))
        }
        assertThrows(IllegalArgumentException::class.java) {
            Board(5, 5, Snake(Coordinate(20, 10), Move.RIGHT, emptyList()), Fruit(Coordinate(2, 2)))
        }
    }
}