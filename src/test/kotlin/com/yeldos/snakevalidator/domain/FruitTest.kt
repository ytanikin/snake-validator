package com.yeldos.snakevalidator.domain

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class FruitTest {

    @Test
    fun isEaten() {
        assertTrue(Fruit(Coordinate(1, 1)).isEaten(Coordinate(1, 1)))
        assertTrue(Fruit(Coordinate(1, 3)).isEaten(Coordinate(1, 3)))
        assertFalse(Fruit(Coordinate(1, 1)).isEaten(Coordinate(1, 2)))
        assertFalse(Fruit(Coordinate(1, 1)).isEaten(Coordinate(2, 1)))
    }
}