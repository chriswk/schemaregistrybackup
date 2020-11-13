package com.chriswk.kafka.schemaregistry

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandlers {

    @ExceptionHandler(SchemaNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleSchemaNotFoundException(e: SchemaNotFoundException): ConfluentError {
        return ConfluentError(40403, e.message)
    }

}

data class ConfluentError(val errorCode: Int, val message: String)