package com.liamcoalstudio.aurora.assets

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class Asset(
    val type: UInt,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Asset) return false

        if (type != other.type) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    infix fun <V, T : AssetLoader<V>> into(t: T) = t from this
}

@OptIn(ExperimentalSerializationApi::class)
expect fun asset(path: String): Asset
