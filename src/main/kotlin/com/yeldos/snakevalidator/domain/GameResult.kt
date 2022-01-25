package com.yeldos.snakevalidator.domain

sealed class GameResult {
    object FruitEaten : GameResult()
    object GameOn : GameResult()
    object GameOverSnakeOutOfBounds : GameResult()
}