package com.yeldos.snakevalidator.domain.fixture

import com.yeldos.snakevalidator.domain.Board
import com.yeldos.snakevalidator.domain.Game
import com.yeldos.snakevalidator.domain.GameResult

object GameFixture {
    val game by lazy { game() }

    fun game(
        board: Board = BoardFixture.board5to5,
        result: GameResult = GameResult.GameOn,
        score: Int = 0,
    ) = Game(board, score, "uid", result)
}