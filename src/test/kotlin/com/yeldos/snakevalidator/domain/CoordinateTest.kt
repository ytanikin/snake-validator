package com.yeldos.snakevalidator.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CoordinateTest {

    @Test
    fun move() {
        val coordinateZeroToZero = Coordinate.initial
        coordinateZeroToZero.move(Move.RIGHT).move(Move.RIGHT).let {
            assertEquals(0, it.row)
            assertEquals(2, it.col)
        }
        coordinateZeroToZero.move(Move.RIGHT).let {
            assertEquals(0, it.row)
            assertEquals(1, it.col)
        }
        coordinateZeroToZero.move(Move.LEFT).let {
            assertEquals(0, it.row)
            assertEquals(-1, it.col)
        }
        coordinateZeroToZero.move(Move.UP).let {
            assertEquals(-1, it.row)
            assertEquals(0, it.col)
        }
        coordinateZeroToZero.move(Move.DOWN).let {
            assertEquals(1, it.row)
            assertEquals(0, it.col)
        }
        coordinateZeroToZero.move(Move.RIGHT).move(Move.DOWN).let {
            assertEquals(1, it.row)
            assertEquals(1, it.col)
        }
    }

}