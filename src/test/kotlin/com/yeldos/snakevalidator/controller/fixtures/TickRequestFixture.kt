package com.yeldos.snakevalidator.controller.fixtures

import com.yeldos.snakevalidator.infrastructure.controller.request.TickRequest

object TickRequestFixture {
    fun tick(velX: Int = 1, velY: Int = 0): TickRequest = TickRequest(velX, velY)
    val rightTick by lazy { tick(1, 0) }
    val leftTick by lazy { tick(-1, 0) }
    val downTick by lazy { tick(0, 1) }
    val fourRightTicks by lazy { listOf(rightTick, rightTick, rightTick, rightTick) }
    val fourDownTicks by lazy { listOf(downTick, downTick, downTick, downTick) }
}
