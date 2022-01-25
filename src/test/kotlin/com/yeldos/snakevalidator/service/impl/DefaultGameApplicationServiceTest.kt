package com.yeldos.snakevalidator.service.impl

import com.yeldos.snakevalidator.MockitoHelper.anyObject
import com.yeldos.snakevalidator.MockitoHelper.whenever
import com.yeldos.snakevalidator.controller.fixtures.StateRequestFixture
import com.yeldos.snakevalidator.domain.Game
import com.yeldos.snakevalidator.domain.GameResult
import com.yeldos.snakevalidator.domain.exception.FruitNotFoundException
import com.yeldos.snakevalidator.domain.exception.SnakeOutOfBoundsException
import com.yeldos.snakevalidator.domain.fixture.GameFixture
import com.yeldos.snakevalidator.service.InputValidator
import com.yeldos.snakevalidator.service.MaliciousValidator
import com.yeldos.snakevalidator.service.SnakeRequestMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

internal class DefaultGameApplicationServiceTest {

    private var validator: InputValidator = mock(InputValidator::class.java)
    private var mapper: SnakeRequestMapper = mock(SnakeRequestMapper::class.java)
    private var maliciousValidator: MaliciousValidator = mock(MaliciousValidator::class.java)

    private val gameApplicationService = DefaultGameApplicationService(validator, mapper, maliciousValidator)

    @Test
    fun validate() {
        val gameFruitEaten = mock(Game::class.java) { GameFixture.game(result = GameResult.FruitEaten) }
        doReturn(gameFruitEaten).`when`(mapper).map(anyObject())
        whenever(mapper.map(anyObject())).thenReturn(gameFruitEaten)
        val stateRequest = StateRequestFixture.eatFruit
        doReturn(stateRequest).`when`(mapper).mapToStateRequest(anyObject(), anyObject())

        val actualRequest = gameApplicationService.play(stateRequest)

        assertEquals(stateRequest, actualRequest)
    }

    @Test
    fun validateFruitNotFoundException() {
        val gameMock = mock(Game::class.java) { GameFixture.game(result = GameResult.GameOn) }
        doReturn(gameMock).`when`(mapper).map(anyObject())
        whenever(mapper.map(anyObject())).thenReturn(gameMock)
        doReturn(StateRequestFixture.eatFruit).`when`(mapper).mapToStateRequest(anyObject(), anyObject())

        assertThrows<FruitNotFoundException> { gameApplicationService.play(StateRequestFixture.fruitFar) }
        verify(mapper).map(anyObject())
        verify(validator).validate(anyObject())
    }

    @Test
    fun validateOutOfBoundException() {
        val gameMock = mock(Game::class.java) { GameFixture.game(result = GameResult.GameOverSnakeOutOfBounds) }
        doReturn(gameMock).`when`(mapper).map(anyObject())
        whenever(mapper.map(anyObject())).thenReturn(gameMock)
        doReturn(StateRequestFixture.eatFruit).`when`(mapper).mapToStateRequest(anyObject(), anyObject())

        assertThrows<SnakeOutOfBoundsException> { gameApplicationService.play(StateRequestFixture.fruitFar) }
    }

    @Test
    fun createNewGame() {
        doReturn(StateRequestFixture.state()).`when`(mapper).mapToStateRequest(anyObject(), anyObject())

        gameApplicationService.createNewGame(3, 3)

        verify(mapper).mapToStateRequest(anyObject(), anyObject())
    }
}

