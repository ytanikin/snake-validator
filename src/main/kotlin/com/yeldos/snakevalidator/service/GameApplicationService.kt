package com.yeldos.snakevalidator.service

import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest

interface GameApplicationService {
    fun play(stateRequest: StateRequest): StateRequest
    fun createNewGame(height: Int, width: Int): StateRequest
}