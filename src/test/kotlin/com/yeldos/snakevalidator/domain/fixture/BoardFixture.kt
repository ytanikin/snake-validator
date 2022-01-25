package com.yeldos.snakevalidator.domain.fixture

import com.yeldos.snakevalidator.domain.Board
import com.yeldos.snakevalidator.domain.Fruit
import com.yeldos.snakevalidator.domain.Snake

object BoardFixture {
    val board5to5 by lazy(LazyThreadSafetyMode.NONE) { board() }

    fun board(
        width: Int = 5,
        height: Int = 5,
        snake: Snake = SnakeFixture.snake,
        fruit: Fruit = FruitFixture.fruit4to4
    ) = Board(width, height, snake, fruit)
}