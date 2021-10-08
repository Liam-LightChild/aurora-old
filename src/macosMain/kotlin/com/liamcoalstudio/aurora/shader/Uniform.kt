package com.liamcoalstudio.aurora.shader

import com.liamcoalstudio.aurora.*
import kotlinx.cinterop.*
import platform.OpenGL3.*

actual class Uniform actual constructor(actual val shader: ShaderHandle<*>, actual val name: Int) {
    actual inline fun push(v: Float) { glUniform1f(name, v) }
    actual inline fun push(v: Double) { glUniform1d(name, v) }
    actual inline fun push(v: Int) { glUniform1i(name, v) }
    actual inline fun push(v: UInt) { glUniform1ui(name, v) }

    actual inline fun push(v: Vector2<Float>) { glUniform2f(name, v.x, v.y) }
    actual inline fun push(v: Vector3<Float>) { glUniform3f(name, v.x, v.y, v.z) }
    actual inline fun push(v: Vector4<Float>) { glUniform4f(name, v.x, v.y, v.z, v.w) }
    actual inline fun push(v: Vector2<Double>) { glUniform2d(name, v.x, v.y) }
    actual inline fun push(v: Vector3<Double>) { glUniform3d(name, v.x, v.y, v.z) }
    actual inline fun push(v: Vector4<Double>) { glUniform4d(name, v.x, v.y, v.z, v.w) }
    actual inline fun push(v: Vector2<Int>) { glUniform2i(name, v.x, v.y) }
    actual inline fun push(v: Vector3<Int>) { glUniform3i(name, v.x, v.y, v.z) }
    actual inline fun push(v: Vector4<Int>) { glUniform4i(name, v.x, v.y, v.z, v.w) }
    actual inline fun push(v: Vector2<UInt>) { glUniform2ui(name, v.x, v.y) }
    actual inline fun push(v: Vector3<UInt>) { glUniform3ui(name, v.x, v.y, v.z) }
    actual inline fun push(v: Vector4<UInt>) { glUniform4ui(name, v.x, v.y, v.z, v.w) }

    actual inline fun push(v: Matrix2x2<Float>) {
        val a = nativeHeap.allocArrayOf(
            v.x.x, v.y.x,
            v.x.y, v.y.y
        )
        glUniformMatrix2fv(name, 1, GL_FALSE.convert(), a)
        nativeHeap.free(a)
    }

    actual inline fun push(v: Matrix3x3<Float>) {
        val a = nativeHeap.allocArrayOf(
            v.x.x, v.y.x, v.z.x,
            v.x.y, v.y.y, v.z.y,
            v.x.z, v.y.z, v.z.z
        )
        glUniformMatrix3fv(name, 1, GL_FALSE.convert(), a)
        nativeHeap.free(a)
    }

    actual inline fun push(v: Matrix4x4<Float>) {
        val a = nativeHeap.allocArrayOf(
            v.x.x, v.y.x, v.z.x, v.w.x,
            v.x.y, v.y.y, v.z.y, v.w.y,
            v.x.z, v.y.z, v.z.z, v.w.z,
            v.x.w, v.y.w, v.z.w, v.w.w
        )
        glUniformMatrix4fv(name, 1, GL_FALSE.convert(), a)
        nativeHeap.free(a)
    }

    actual inline fun push(v: Matrix2x2<Double>) {
        val a = nativeHeap.allocArray<DoubleVar>(2*2)
        arrayOf(
            v.x.x, v.y.x,
            v.x.y, v.y.y
        ).forEachIndexed(a::set)
        glUniformMatrix2dv(name, 1, GL_FALSE.convert(), a)
        nativeHeap.free(a)
    }

    actual inline fun push(v: Matrix3x3<Double>) {
        val a = nativeHeap.allocArray<DoubleVar>(3*3)
        arrayOf(
            v.x.x, v.y.x, v.z.x,
            v.x.y, v.y.y, v.z.y,
            v.x.z, v.y.z, v.z.z
        ).forEachIndexed(a::set)
        glUniformMatrix3dv(name, 1, GL_FALSE.convert(), a)
        nativeHeap.free(a)
    }

    actual inline fun push(v: Matrix4x4<Double>) {
        val a = nativeHeap.allocArray<DoubleVar>(4*4)
        arrayOf(
            v.x.x, v.y.x, v.z.x, v.w.x,
            v.x.y, v.y.y, v.z.y, v.w.y,
            v.x.z, v.y.z, v.z.z, v.w.z,
            v.x.w, v.y.w, v.z.w, v.w.w
        ).forEachIndexed(a::set)
        glUniformMatrix4dv(name, 1, GL_FALSE.convert(), a)
        nativeHeap.free(a)
    }
}
