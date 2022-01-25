package com.yeldos.snakevalidator.service.exception

class InvalidRequestException : RuntimeException {
    override val message: String
    val messages: List<String>

    constructor(message: String, messages: List<String> = listOf(message)) : super(message) {
        this.message = message
        this.messages = messages
    }

    constructor(messages: List<String>, message: String = messages.joinToString("\n")) : super(message) {
        this.message = message
        this.messages = messages
    }
}