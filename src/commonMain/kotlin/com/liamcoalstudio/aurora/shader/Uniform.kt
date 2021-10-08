package com.liamcoalstudio.aurora.shader

import com.liamcoalstudio.aurora.*

expect class Uniform(shader: ShaderHandle<*>, name: Int) {
    val shader: ShaderHandle<*>
    val name: Int

    inline fun push(v: Float)
    inline fun push(v: Double)
    inline fun push(v: Int)
    inline fun push(v: UInt)

    inline fun push(v: Vector2<Float>)
    inline fun push(v: Vector3<Float>)
    inline fun push(v: Vector4<Float>)
    inline fun push(v: Vector2<Double>)
    inline fun push(v: Vector3<Double>)
    inline fun push(v: Vector4<Double>)
    inline fun push(v: Vector2<Int>)
    inline fun push(v: Vector3<Int>)
    inline fun push(v: Vector4<Int>)
    inline fun push(v: Vector2<UInt>)
    inline fun push(v: Vector3<UInt>)
    inline fun push(v: Vector4<UInt>)

    inline fun push(v: Matrix2x2<Float>)
    inline fun push(v: Matrix3x3<Float>)
    inline fun push(v: Matrix4x4<Float>)
    inline fun push(v: Matrix2x2<Double>)
    inline fun push(v: Matrix3x3<Double>)
    inline fun push(v: Matrix4x4<Double>)
}
