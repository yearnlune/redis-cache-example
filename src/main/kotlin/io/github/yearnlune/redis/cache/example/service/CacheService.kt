package io.github.yearnlune.redis.cache.example.service

import io.github.yearnlune.redis.cache.example.config.CacheConfig.Companion.CACHE_TTL
import io.github.yearnlune.redis.cache.example.domain.dto.CacheBaseDTO
import io.github.yearnlune.redis.cache.example.domain.entity.CacheBase
import io.github.yearnlune.redis.cache.example.event.AddCacheEvent
import io.github.yearnlune.redis.cache.example.event.EvictCacheEvent
import io.github.yearnlune.redis.cache.example.logger
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class CacheService(
    private val eventPublisher: ApplicationEventPublisher,
    val redisTemplate: RedisTemplate<String, Any>?
) {

    @Async
    @EventListener
    fun handleAddCache(addCacheEvent: AddCacheEvent) {
        runCatching {
            pushWithPipeline(addCacheEvent)
        }
    }

    @Async
    @EventListener
    fun handleEvictCache(evictCacheEvent: EvictCacheEvent) {
        runCatching {
            redisTemplate?.delete(evictCacheEvent.toFullPathKeys())
        }
    }

    fun findCollectionByIdsWithCache(
        ids: List<String>,
        cachePrefix: String,
        findFunction: (List<String>) -> List<CacheBase>
    ): List<CacheBaseDTO> {
        val cachedMap = runCatching {
            redisTemplate?.opsForValue()?.multiGet(ids.map { cachePrefix + it })
                ?.filterIsInstance<CacheBaseDTO>()
                ?.associateBy { it.id } ?: emptyMap()
        }.getOrElse {
            logger().error(it.localizedMessage)
            emptyMap()
        }

        val missedIds = ids
            .filter { cachedMap[it] == null }
            .also { logger().info("[$cachePrefix] Missed ID: [${it.size}]") }
        val missedCollection = if (missedIds.isNotEmpty()) findFunction(missedIds) else emptyList()

        return (cachedMap.values.toList() + missedCollection.map(CacheBase::toCacheBaseDTO))
            .also {
                if (missedIds.isNotEmpty()) {
                    eventPublisher.publishEvent(
                        AddCacheEvent.create(
                            cachePrefix,
                            missedCollection.map(CacheBase::toCacheBaseDTO).associateBy { it.id }
                        )
                    )
                }
            }
    }

    private fun pushWithPipeline(addCacheEvent: AddCacheEvent) {
        val key = redisTemplate?.keySerializer as RedisSerializer<String>
        val value = redisTemplate?.valueSerializer as RedisSerializer<Any>
        redisTemplate?.executePipelined { connection ->
            addCacheEvent.values.forEach {
                connection.stringCommands().mSet(
                    mapOf(key.serialize(addCacheEvent.prefix + it.key)!! to value.serialize(it.value)!!)
                )
                connection.expire(key.serialize(addCacheEvent.prefix + it.key)!!, CACHE_TTL)
            }
        }
    }
}