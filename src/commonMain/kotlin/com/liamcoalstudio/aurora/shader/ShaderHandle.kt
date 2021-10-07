package com.liamcoalstudio.aurora.shader

import com.liamcoalstudio.aurora.Resource
import com.liamcoalstudio.aurora.Vector2
import com.liamcoalstudio.aurora.buffer.BufferHandle
import com.liamcoalstudio.aurora.draw.DrawCollection
import kotlin.reflect.KFunction1
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

expect sealed class ShaderInputType<T>(size: kotlin.Int) {
    abstract fun encode(t: T): ByteArray

    object Byte : ShaderInputType<kotlin.Byte> {
        override fun encode(t: kotlin.Byte): ByteArray
    }

    object Int : ShaderInputType<kotlin.Int> {
        override fun encode(t: kotlin.Int): ByteArray
    }

    object UInt : ShaderInputType<kotlin.UInt> {
        override fun encode(t: kotlin.UInt): ByteArray
    }

    object Float : ShaderInputType<kotlin.Float> {
        override fun encode(t: kotlin.Float): ByteArray
    }

    object Double : ShaderInputType<kotlin.Double> {
        override fun encode(t: kotlin.Double): ByteArray
    }

    sealed class Vector2<R, T : ShaderInputType<R>> : ShaderInputType<com.liamcoalstudio.aurora.Vector2<R>> {
        override fun encode(t: com.liamcoalstudio.aurora.Vector2<R>): ByteArray
    }

    sealed class Vector3<R, T : ShaderInputType<R>> : ShaderInputType<com.liamcoalstudio.aurora.Vector3<R>> {
        override fun encode(t: com.liamcoalstudio.aurora.Vector3<R>): ByteArray
    }

    sealed class Vector4<R, T : ShaderInputType<R>> : ShaderInputType<com.liamcoalstudio.aurora.Vector4<R>> {
        override fun encode(t: com.liamcoalstudio.aurora.Vector4<R>): ByteArray
    }

    object Vector2f : Vector2<kotlin.Float, Float>
    object Vector3f : Vector3<kotlin.Float, Float>
    object Vector4f : Vector4<kotlin.Float, Float>

    object Vector2i : Vector2<kotlin.Int, Int>
    object Vector3i : Vector3<kotlin.Int, Int>
    object Vector4i : Vector4<kotlin.Int, Int>

    object Vector2ui : Vector2<kotlin.UInt, UInt>
    object Vector3ui : Vector3<kotlin.UInt, UInt>
    object Vector4ui : Vector4<kotlin.UInt, UInt>

    object Vector2d : Vector2<kotlin.Double, Double>
    object Vector3d : Vector3<kotlin.Double, Double>
    object Vector4d : Vector4<kotlin.Double, Double>

    val size: kotlin.Int
}

@Suppress("UNCHECKED_CAST")
fun <T> ShaderInputType<T>.encode(any: Any): ByteArray {
    return encode(any as T)
}

data class ShaderInput<R, T>(val name: String, val inputType: ShaderInputType<R>, val p: KFunction1<T, R>)

expect class ShaderHandle<T> internal constructor(handle: UInt) : Resource {
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