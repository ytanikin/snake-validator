package com.yeldos.snakevalidator.service.impl

import com.yeldos.snakevalidator.domain.*
import com.yeldos.snakevalidator.infrastructure.controller.request.FruitRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.SnakeRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest
import com.yeldos.snakevalidator.infrastructure.controller.request.TickRequest
import com.yeldos.snakevalidator.service.SnakeRequestMapper
import org.springframework.stereotype.Component

@Component
class SnakeRequestMapperImpl : SnakeRequestMapper {

    override fun map(stateRequest: StateRequest) : Game {
        val move = Move.valueOf(stateRequest.snake.velX, stateRequest.snake.velY)
        val moves = stateRequest.ticks.map { Move.valueOf(it.velX, it.velY) }
        val snake = Snake(Coordinate(stateRequest.snake.x, stateRequest.snake.y), move, moves)
        val fruit = Fruit(Coordinate(stateRequest.fruit.x, stateRequest.fruit.y))
        val board = Board(stateRequest.width, stateRequest.height, snake, fruit)
        return Game(board, stateRequest.score, stateRequest.gameId, GameResult.GameOn)
    }

    override fun mapToStateRequest(game: Game, encodedFruit: String?): StateRequest {
        val board = game.board
        val snake = board.snake
        return StateRequest(
            gameId = game.uid,
            width = board.width,
            height = board.height,
            score = game.score,
            fruit = FruitRequest(board.fruit.coordinate.row, board.fruit.coordinate.col),
            snake = SnakeRequest(snake.coordinate.col, snake.coordinate.row, snake.direction.velX, snake.direction.velY),
            ticks = snake.moves.map { TickRequest(it.velX, it.velY) },
            code = encodedFruit
        )
    }
}
