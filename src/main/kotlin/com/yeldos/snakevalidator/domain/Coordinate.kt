package com.yeldos.snakevalidator.domain

data class Coordinate(val col: Int, val row: Int) {

    fun move(move: Move): Coordinate = copy(col = col + move.velX, row = row + move.velY)

    companion object {
        val initial = Coordinate(0, 0)
    }
}