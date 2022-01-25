package com.yeldos.snakevalidator.service

import com.yeldos.snakevalidator.domain.Game
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest

interface SnakeRequestMapper {
    fun map(stateRequest: StateRequest): Game
    fun mapToStateRequest(game: Game, encodedFruit: String?): StateRequest
}