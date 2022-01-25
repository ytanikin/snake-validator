package com.yeldos.snakevalidator.controller.fixtures

import com.yeldos.snakevalidator.infrastructure.controller.request.SnakeRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.TickRequest

object SnakeRequestFixture {
    val snake by lazy { snake() }
    fun snake(x: Int = 0, y: Int = 0, velX: Int = 1, velY: Int = 0): SnakeRequest = SnakeRequest(x, y, velX, velY)
    fun snake(x: Int = 0, y: Int = 0, tick: TickRequest): SnakeRequest = SnakeRequest(x, y, tick.velX, tick.velY)
}
