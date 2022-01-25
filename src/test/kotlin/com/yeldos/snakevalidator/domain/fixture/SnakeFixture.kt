package com.yeldos.snakevalidator.domain.fixture

import com.yeldos.snakevalidator.domain.Coordinate
import com.yeldos.snakevalidator.domain.Move
import com.yeldos.snakevalidator.domain.Snake

object SnakeFixture {

    val snake by lazy { snake() }
    val initial = Snake.initial

    fun snake(
        coordinate: Coordinate = Coordinate(1, 1),
        move: Move = Move.RIGHT,
        moves: List<Move> = listOf(Move.RIGHT, Move.RIGHT)
    ) = Snake(coordinate, move, moves)
}