package com.yeldos.snakevalidator.infrastructure.controller.request

data class StateRequest(
    val gameId: String,
    val width: Int,
    val height: Int,
    val score: Int,
    val fruit: FruitRequest,
    val snake: SnakeRequest,
    val ticks: List<TickRequest>,
    val code: String? = null
)