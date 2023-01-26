package io.github.yearnlune.redis.cache.example.domain.entity

import io.github.yearnlune.redis.cache.example.domain.dto.CacheBaseDTO


interface CacheBase {

    fun toCacheBaseDTO(): CacheBaseDTO { throw RuntimeException("Not supported dto: ${this.javaClass.simpleName}") }
}