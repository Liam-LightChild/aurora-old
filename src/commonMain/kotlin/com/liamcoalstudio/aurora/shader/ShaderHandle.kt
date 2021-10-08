package com.liamcoalstudio.aurora.shader

import com.liamcoalstudio.aurora.Resource
import com.liamcoalstudio.aurora.draw.DrawCollection
import kotlin.reflect.KFunction1
import com.liamcoalstudio.aurora.Vector2 as AVector2
import com.liamcoalstudio.aurora.Vector3 as AVector3
import com.liamcoalstudio.aurora.Vector4 as AVector4
import kotlin.Double as KDouble
import kotlin.Float as KFloat
import kotlin.Int as KInt
import kotlin.UInt as KUInt

expect sealed class ShaderInputType<T>(size: KInt) {
    abstract fun encode(t: T): ByteArray

    object Byte : ShaderInputType<kotlin.Byte> {
        override fun encode(t: kotlin.Byte): ByteArray
    }

    object Int : ShaderInputType<KInt> {
        override fun encode(t: KInt): ByteArray
    }

    object UInt : ShaderInputType<KUInt> {
        override fun encode(t: KUInt): ByteArray
    }

    object Float : ShaderInputType<KFloat> {
        override fun encode(t: KFloat): ByteArray
    }

    object Double : ShaderInputType<KDouble> {
        override fun encode(t: KDouble): ByteArray
    }

    sealed class Vector2<R, T : ShaderInputType<R>> : ShaderInputType<AVector2<R>> {
        override fun encode(t: AVector2<R>): ByteArray
    }

    sealed class Vector3<R, T : ShaderInputType<R>> : ShaderInputType<AVector3<R>> {
        override fun encode(t: AVector3<R>): ByteArray
    }

    sealed class Vector4<R, T : ShaderInputType<R>> : ShaderInputType<AVector4<R>> {
        override fun encode(t: AVector4<R>): ByteArray
    }

    object Vector2f : Vector2<KFloat, Float>
    object Vector3f : Vector3<KFloat, Float>
    object Vector4f : Vector4<KFloat, Float>

    object Vector2i : Vector2<KInt, Int>
    object Vector3i : Vector3<KInt, Int>
    object Vector4i : Vector4<KInt, Int>

    object Vector2ui : Vector2<KUInt, UInt>
    object Vector3ui : Vector3<KUInt, UInt>
    object Vector4ui : Vector4<KUInt, UInt>

    object Vector2d : Vector2<KDouble, Double>
    object Vector3d : Vector3<KDouble, Double>
    object Vector4d : Vector4<KDouble, Double>

    val size: KInt
}

@Suppress("UNCHECKED_CAST")
fun <T> ShaderInputType<T>.encode(any: Any): ByteArray {
    return encode(any as T)
}

data class ShaderInput<R, T>(val name: String, val inputType: ShaderInputType<R>, val p: KFunction1<T, R>)

expect class ShaderHandle<T> internal constructor(handle: KUInt) : Resource {
    companion object {
        fun <T> open(): ShaderHandle<T>
    }

    fun addShader(type: ShaderType, source: String)
    fun addInput(input: ShaderInput<*, T>)
    fun finish()
    override fun resourceUse()
    override fun resourceDelete()
    fun constructDrawCollection(vararg values: T): DrawCollection<T>
}
