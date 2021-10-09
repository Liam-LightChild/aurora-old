package com.liamcoalstudio.aurora.shader

import com.liamcoalstudio.aurora.toByteArray
import kotlinx.cinterop.*

actual sealed class ShaderInputType<T> actual constructor(actual val size: kotlin.Int) {
    actual abstract fun encode(t: T): ByteArray

    actual object Byte : ShaderInputType<kotlin.Byte>(sizeOf<ByteVar>().convert()) {
        actual override fun encode(t: kotlin.Byte): ByteArray {
            return byteArrayOf(t)
        }
    }

    actual object Int : ShaderInputType<kotlin.Int>(sizeOf<IntVar>().convert()) {
        actual override fun encode(t: kotlin.Int): ByteArray {
            return t.toByteArray()
        }
    }

    actual object UInt : ShaderInputType<kotlin.UInt>(sizeOf<UIntVar>().convert()) {
        actual override fun encode(t: kotlin.UInt): ByteArray {
            return t.toInt().toByteArray()
        }
    }

    actual object Float : ShaderInputType<kotlin.Float>(sizeOf<FloatVar>().convert()) {
        actual override fun encode(t: kotlin.Float): ByteArray {
            return t.toByteArray()
        }
    }

    actual object Double : ShaderInputType<kotlin.Double>(0) {
        actual override fun encode(t: kotlin.Double): ByteArray {
            TODO("not implemented")
        }
    }

    actual sealed class Vector2<R, T : ShaderInputType<R>>(val value: T) : ShaderInputType<com.liamcoalstudio.aurora.Vector2<R>>(value.size * 2) {
        actual override fun encode(t: com.liamcoalstudio.aurora.Vector2<R>): ByteArray {
            return value.encode(t.x) + value.encode(t.y)
        }
    }

    actual sealed class Vector3<R, T : ShaderInputType<R>>(val value: T) : ShaderInputType<com.liamcoalstudio.aurora.Vector3<R>>(value.size * 3) {
        actual override fun encode(t: com.liamcoalstudio.aurora.Vector3<R>): ByteArray {
            return value.encode(t.x) + value.encode(t.y) + value.encode(t.z)
        }
    }

    actual sealed class Vector4<R, T : ShaderInputType<R>>(val value: T) : ShaderInputType<com.liamcoalstudio.aurora.Vector4<R>>(value.size * 4) {
        actual override fun encode(t: com.liamcoalstudio.aurora.Vector4<R>): ByteArray {
            return value.encode(t.x) + value.encode(t.y) + value.encode(t.z) + value.encode(t.w)
        }
    }

    actual object Vector2f : Vector2<kotlin.Float, Float>(Float)
    actual object Vector3f : Vector3<kotlin.Float, Float>(Float)
    actual object Vector4f : Vector4<kotlin.Float, Float>(Float)
    actual object Vector2i : Vector2<kotlin.Int, Int>(Int)
    actual object Vector3i : Vector3<kotlin.Int, Int>(Int)
    actual object Vector4i : Vector4<kotlin.Int, Int>(Int)
    actual object Vector2ui : Vector2<kotlin.UInt, UInt>(UInt)
    actual object Vector3ui : Vector3<kotlin.UInt, UInt>(UInt)
    actual object Vector4ui : Vector4<kotlin.UInt, UInt>(UInt)
    actual object Vector2d : Vector2<kotlin.Double, Double>(Double)
    actual object Vector3d : Vector3<kotlin.Double, Double>(Double)
    actual object Vector4d : Vector4<kotlin.Double, Double>(Double)
}
