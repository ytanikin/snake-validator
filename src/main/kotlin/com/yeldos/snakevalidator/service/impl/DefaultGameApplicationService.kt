package com.yeldos.snakevalidator.service.impl

import com.yeldos.snakevalidator.domain.Game
import com.yeldos.snakevalidator.domain.GameResult
import com.yeldos.snakevalidator.domain.exception.FruitNotFoundException
import com.yeldos.snakevalidator.domain.exception.SnakeOutOfBoundsException
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest
import com.yeldos.snakevalidator.service.GameApplicationService
import com.yeldos.snakevalidator.service.InputValidator
import com.yeldos.snakevalidator.service.MaliciousValidator
import com.yeldos.snakevalidator.service.SnakeRequestMapper
import org.springframework.stereotype.Service

@Service
class DefaultGameApplicationService(
    val inputValidator: InputValidator,
    val mapper: SnakeRequestMapper,
    val maliciousValidator: MaliciousValidator,
) : GameApplicationService {

    override fun play(stateRequest: StateRequest): StateRequest {
        maliciousValidator.validate(stateRequest.fruit.x, stateRequest.fruit.y, stateRequest.gameId, stateRequest.code)
        inputValidator.validate(stateRequest)
        val game = mapper.map(stateRequest)
        val finishedGame = game.start()
        return checkResultAndMap(finishedGame)
    }

    private fun checkResultAndMap(finishedGame: Game): StateRequest {
        return when (finishedGame.result) {
            GameResult.GameOn -> throw FruitNotFoundException()
            GameResult.GameOverSnakeOutOfBounds -> throw SnakeOutOfBoundsException()
            GameResult.FruitEaten -> mapResult(finishedGame)
        }
    }

    override fun createNewGame(height: Int, width: Int): StateRequest {
        val game = Game.from(height, width)
        return mapResult(game)
    }

    private fun mapResult(finishedGame: Game): StateRequest {
        val encodedFruit = maliciousValidator.encodeFruit(finishedGame.fruitPosition.row, finishedGame.fruitPosition.col, finishedGame.uid)
        return mapper.mapToStateRequest(finishedGame, encodedFruit)
    }
}