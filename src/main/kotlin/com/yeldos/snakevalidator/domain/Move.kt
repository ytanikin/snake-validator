package com.yeldos.snakevalidator.domain

import com.yeldos.snakevalidator.domain.exception.InvalidMoveException

enum class Move(val velX: Int, val velY: Int) {
    UP(0, -1) { override fun isBackward(move: Move) = move == DOWN },
    DOWN(0, 1) { override fun isBackward(move: Move) = move == UP },
    LEFT(-1, 0) { override fun isBackward(move: Move) = move == RIGHT },
    RIGHT(1, 0) { override fun isBackward(move: Move) = move == LEFT };

    abstract fun isBackward(move: Move): Boolean

    companion object {
        fun valueOf(velX: Int, velY: Int): Move {
            values().forEach {
                if (it.velX == velX && it.velY == velY) {
                    return it
                }
            }
            throw InvalidMoveException()
        }
    }

}
