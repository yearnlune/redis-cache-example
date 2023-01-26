package io.github.yearnlune.redis.cache.example.controller

import io.github.yearnlune.redis.cache.example.exception.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ControllerAdviser {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        exception: NoSuchElementException,
        request: HttpServletRequest
    ) = ExceptionResponse.create(
        HttpStatus.BAD_REQUEST,
        exception.message ?: "No such element",
        request.requestURI
    ).toResponseEntity()
}