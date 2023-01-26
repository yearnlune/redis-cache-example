package io.github.yearnlune.redis.cache.example.event

data class AddCacheEvent(
    val prefix: String,
    val values: Map<String, Any>
) {

    companion object {
        fun create(prefix: String, values: Map<String, Any>) = AddCacheEvent(prefix, values)
    }
}