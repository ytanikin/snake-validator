package com.yeldos.snakevalidator.service

import com.yeldos.snakevalidator.infrastructure.controller.request.StateRequest

interface InputValidator {
    fun validate(input: StateRequest)
}