package com.yeldos.snakevalidator.service.impl

import com.yeldos.snakevalidator.service.MaliciousValidator
import com.yeldos.snakevalidator.service.exception.MaliciousManipulationException
import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.stereotype.Component


@Component
class MaliciousValidatorImpl : MaliciousValidator {
    val maliciousValidationEnabled = false

    override fun validate(x: Int, y: Int, uid: String, code: String?) {
        if (!maliciousValidationEnabled) {
            return
        }
        if (encodeFruit(x, y, uid) != code) {
            throw MaliciousManipulationException()
        }
    }

    override fun encodeFruit(x: Int, y: Int, uid: String): String? {
        if (!maliciousValidationEnabled) {
            return null
        }
        return Base64.encodeBase64String(getSecretWord(x, y, uid).toByteArray())
    }

    private fun getSecretWord(x: Int, y: Int, uid: String) = "the most secure encoding in the entire galaxy $x $y $uid"
}