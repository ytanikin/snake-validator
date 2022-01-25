package com.yeldos.snakevalidator.domain

data class Snake(val coordinate: Coordinate, val direction: Move, val moves: List<Move>) {
    var currentCoordinate: Coordinate = coordinate
        private set

    var lastDirection: Move = direction
        private set

    private val nextMoves: MutableList<Move> = moves.toMutableList()
    val restMoves get() = nextMoves.toList()

    fun hasNextMove(): Boolean = nextMoves.isNotEmpty()

    fun nextMove(): Coordinate {
        lastDirection = nextMoves.removeFirst()
        return currentCoordinate.move(lastDirection).also { currentCoordinate = it }
    }

    fun copyWithLastPosition(): Snake {
        return copy(coordinate = currentCoordinate, direction = lastDirection, moves = restMoves)
    }

    companion object {
        val initial = Snake(Coordinate.initial, Move.RIGHT, emptyList())
    }
}
