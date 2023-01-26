package io.github.yearnlune.redis.cache.example.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import java.time.LocalDateTime

data class ExceptionResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val name: String,
    val message: String,
    val path: String
) {

    companion object {
        fun create(httpStatus: HttpStatus, bindingResult: BindingResult, path: String): ExceptionResponse {
            return ExceptionResponse(
                status = httpStatus.ordinal,
                name = httpStatus.name,
                message = createErrorMessage(bindingResult),
                path = path
            )
        }

        fun create(httpStatus: HttpStatus, reason: String, path: String): ExceptionResponse {
            return ExceptionResponse(
                status = httpStatus.value(),
                name = httpStatus.name,
                message = reason,
                path = path
            )
        }

        private fun createErrorMessage(bindingResult: BindingResult): String {
            val stringBuilder = StringBuilder()
            var isFirst = true
            for (fieldError in bindingResult.fieldErrors) {
                if (!isFirst) {
                    stringBuilder.append(", ")
                } else {
                    isFirst = false
                }
                stringBuilder.append("[${fieldError.field}] ${fieldError.defaultMessage}")
            }
            return stringBuilder.toString()
        }
    }

    fun toResponseEntity() = ResponseEntity(this, HttpStatus.valueOf(this.status))
}