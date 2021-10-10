@file:OptIn(ExperimentalSerializationApi::class)

package com.liamcoalstudio.aurora.assets

import com.liamcoalstudio.aurora.Vector3f
import com.liamcoalstudio.aurora.Vector4f
import com.liamcoalstudio.aurora.game.ShaderRef
import com.liamcoalstudio.aurora.shader.ShaderInput
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.shader.ShaderType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.reflect.KProperty1

@Serializable
data class GLSLShaderPart(
    val stage: ShaderType,
    val glsl: String
)

@Serializable
data class GLSLShaderInput(
    val name: String,
    val type: GLSLShaderInputType
)

@Serializable
enum class GLSLShaderInputType(val type: ShaderInputType<*>) {
    BYTE(ShaderInputType.Byte),
    FLOAT(ShaderInputType.Float),
    DOUBLE(ShaderInputType.Double),
    INT(ShaderInputType.Int),
    UINT(ShaderInputType.UInt),

    FLOAT2(ShaderInputType.Vector2f),
    FLOAT3(ShaderInputType.Vector3f),
    FLOAT4(ShaderInputType.Vector4f),

    INT2(ShaderInputType.Vector2i),
    INT3(ShaderInputType.Vector3i),
    INT4(ShaderInputType.Vector4i),

    UINT2(ShaderInputType.Vector2ui),
    UINT3(ShaderInputType.Vector3ui),
    UINT4(ShaderInputType.Vector4ui),

    DOUBLE2(ShaderInputType.Vector2d),
    DOUBLE3(ShaderInputType.Vector3d),
    DOUBLE4(ShaderInputType.Vector4d),
}

@Serializable
data class GLSLShaderConstructionInfo(
    val parts: List<GLSLShaderPart>,
    val inputs: List<GLSLShaderInput>
)

@Suppress("UNCHECKED_CAST")
fun <T> shader(asset: Asset, vararg properties: KProperty1<T, Any>): ShaderRef<T> {
    val info: GLSLShaderConstructionInfo =
        if(asset.data[0].toInt().toChar() == '{')
            Json.decodeFromString(asset.data.decodeToString())
        else Cbor.decodeFromByteArray(asset.data)
    val p = properties.associateBy { it.name }

    val v = info.parts.single { it.stage == ShaderType.VERTEX }
    val f = info.parts.single { it.stage == ShaderType.FRAGMENT }

    return ShaderRef(v.glsl, f.glsl, info.inputs.map { ShaderInput(
        it.name,
        it.type.type as ShaderInputType<Any>,
        p[it.name]!!::get) })
}

fun test() {
    data class Vertex(val vPosition: Vector3f, val vColor: Vector4f)
}
