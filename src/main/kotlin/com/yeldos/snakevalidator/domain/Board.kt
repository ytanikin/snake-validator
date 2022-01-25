package com.yeldos.snakevalidator.domain

data class Board(val width: Int, val height: Int, val snake: Snake, val fruit: Fruit) {
    init {
        require(width > 0) { "width must be greater than 0, but was $width" }
        require(height > 0) { "height must be greater than 0, but was $height" }
        require(width + height !in 0..2) { "width + height must not be in range 0..2, but was ${width + height}" }
        require(snake.coordinate.col <= width) { "snake position col must be less than width or equal, but was ${snake.coordinate}" }
        require(snake.coordinate.row <= height) { "snake position row must be less than height or equal, but was ${snake.coordinate}" }
    }

    fun hasReachedBorder(): Boolean {
        var position = snake.coordinate
        snake.moves.forEach {
            position = position.move(it)
            if (!(isOutOfBorder(position))) {
                return true
            }
        }
        return false
    }

    private fun isOutOfBorder(coordinate: Coordinate) = coordinate.col in 0 until width && coordinate.row in 0 until height

    fun reachFruit(): Boolean {
        while (snake.hasNextMove()) {
            if (fruit.isEaten(snake.nextMove())) {
                return true
            }
        }
        return false
    }

    companion object {
        fun from(width: Int, height: Int, fruit: Fruit, snake: Snake = Snake.initial): Board = Board(width, height, snake, fruit)
    }
}
