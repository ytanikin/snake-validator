package com.yeldos.snakevalidator.service

interface MaliciousValidator {
    fun validate(x: Int, y: Int, uid: String, code: String?)
    fun encodeFruit(x: Int, y: Int, uid: String): String?
}