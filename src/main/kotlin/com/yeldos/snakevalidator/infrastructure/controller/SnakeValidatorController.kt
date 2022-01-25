package com.yeldos.snakevalidator.infrastructure.controller

import com.yeldos.snakevalidator.domain.exception.BusinessException
import com.yeldos.snakevalidator.domain.exception.FruitNotFoundException
import com.yeldos.snakevalidator.domain.exception.InvalidMoveException
import com.yeldos.snakevalidator.domain.exception.SnakeOutOfBoundsException
import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest
import com.yeldos.snakevalidator.infrastructure.controller.response.ErrorResponse
import com.yeldos.snakevalidator.service.GameApplicationService
import com.yeldos.snakevalidator.service.exception.InvalidRequestException
import com.yeldos.snakevalidator.service.exception.MaliciousManipulationException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestController
class SnakeValidatorController(val applicationService: GameApplicationService) {

    @GetMapping("/new")
    fun newGame(@RequestParam("w") width: Int, @RequestParam("h") height: Int): StateRequest {
        return applicationService.createNewGame(width, height)
    }

    @PostMapping("/validate")
    fun validate(@RequestBody request: StateRequest): StateRequest {
        return applicationService.play(request)
    }

    @ExceptionHandler(InvalidMoveException::class, SnakeOutOfBoundsException::class)
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    fun handleInvalidMoveAndSnakeOutOfBoundException(ex: BusinessException) {}

    @ExceptionHandler(MaliciousManipulationException::class)
    @ResponseStatus(value = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
    fun handleMaliciousManipulationException(ex: MaliciousManipulationException) {}

    @ExceptionHandler(FruitNotFoundException::class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    fun handleFruitNotFoundException(ex: FruitNotFoundException) {}

    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleBusinessException(ex: BusinessException): ErrorResponse = ErrorResponse(ex.messages)

    @ExceptionHandler(InvalidRequestException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleInvalidRequest(ex: InvalidRequestException): ErrorResponse = ErrorResponse(ex.messages)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleBeanValidationExceptions(exception: MethodArgumentNotValidException): ErrorResponse {
        return ErrorResponse(exception.bindingResult.allErrors.map(::formatBeanValidationError))
    }

    private fun formatBeanValidationError(o: ObjectError) = "${(o as FieldError).field} = ${o.rejectedValue}, ${o.getDefaultMessage()}"
}
