package com.yeldos.snakevalidator.domain

data class Fruit(val coordinate: Coordinate) {
    init {
        if (coordinate.col < 0 || coordinate.row < 0) {
            throw IllegalArgumentException("Fruit coordinate cannot be negative")
        }
    }
    fun isEaten(coordinate: Coordinate): Boolean {
        return this.coordinate == coordinate
    }
}