package com.yeldos.snakevalidator.controller.fixtures

import com.yeldos.snakevalidator.infrastructure.controller.request.FruitRequest

object FruitRequestFixture {
    val fruitFourToZero by lazy { fruit(4, 0) }
    val fourToFour by lazy { fruit(4, 4) }
    fun fruit(x: Int = 2, y: Int = 2): FruitRequest = FruitRequest(x, y)
}
