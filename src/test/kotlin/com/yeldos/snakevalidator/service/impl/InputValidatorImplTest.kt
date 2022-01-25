package com.yeldos.snakevalidator.service.impl

import com.yeldos.snakevalidator.controller.fixtures.StateRequestFixture
import com.yeldos.snakevalidator.domain.exception.InvalidMoveException
import com.yeldos.snakevalidator.service.exception.InvalidRequestException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class InputValidatorImplTest {

    private val inputValidator = InputValidatorImpl()

    @Test
    fun successValidation() {
        inputValidator.validate(StateRequestFixture.eatFruit)
        inputValidator.validate(StateRequestFixture.reachFarFruit)
        inputValidator.validate(StateRequestFixture.oneTickToReachFarFruit)
        inputValidator.validate(StateRequestFixture.eatFarFruitZigZag)
        inputValidator.validate(StateRequestFixture.eatFruitInSmallBoard)
        inputValidator.validate(StateRequestFixture.outOfBound)
    }

    @Test
    internal fun throwException() {
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.incorrectSnakeDirection)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.invalidBoardNegativeHeight)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.fruitInvalidPosition11to11)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.emptyGameId)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.oneToOneBoard)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.fruitInvalidPosition11to11)
        }
        Assertions.assertThrows(InvalidRequestException::class.java) {
            inputValidator.validate(StateRequestFixture.fruitInvalidPosition)
        }

        Assertions.assertThrows(InvalidMoveException::class.java) {
            inputValidator.validate(StateRequestFixture.firstOppositeTick)
        }
    }

    @Test
    internal fun invalidMoveException() {
         Assertions.assertThrows(InvalidMoveException::class.java) {
            inputValidator.validate(StateRequestFixture.leftToRightTicks)
        }
    }
}