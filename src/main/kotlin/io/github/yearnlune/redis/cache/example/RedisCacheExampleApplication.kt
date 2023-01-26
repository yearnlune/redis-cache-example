package io.github.yearnlune.redis.cache.example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [RedisAutoConfiguration::class])
class RedisCacheExampleApplication

fun main(args: Array<String>) {
    runApplication<RedisCacheExampleApplication>(*args)
}

inline fun <reified T> T.logger(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}