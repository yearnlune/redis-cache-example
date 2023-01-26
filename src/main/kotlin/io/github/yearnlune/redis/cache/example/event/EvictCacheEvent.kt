package io.github.yearnlune.redis.cache.example.event

data class EvictCacheEvent(
    val prefix: String,
    val keys: List<String>
) {

    companion object {
        fun create(prefix: String, keys: List<String>) = EvictCacheEvent(prefix, keys)
    }

    fun toFullPathKeys() = keys.map { prefix + it }
}