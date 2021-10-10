@file:OptIn(ExperimentalSerializationApi::class)

package com.liamcoalstudio.aurora.assets

import com.liamcoalstudio.aurora.game.TextureRef
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty1

@Suppress("UNCHECKED_CAST")
fun <T> texture(asset: Asset, vararg properties: KProperty1<T, Any>): TextureRef = TextureRef(
    if(asset.data[0].toInt().toChar() == '{') Json.decodeFromString(asset.data.decodeToString())
    else Cbor.decodeFromByteArray(asset.data)
)
