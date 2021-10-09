package com.liamcoalstudio.aurora

import kotlinx.serialization.Serializable

@Serializable data class Vector2<T>(val x: T, val y: T)
@Serializable data class Vector3<T>(val x: T, val y: T, val z: T)
@Serializable data class Vector4<T>(val x: T, val y: T, val z: T, val w: T)

typealias Vector2f = Vector2<Float>
typealias Vector3f = Vector3<Float>
typealias Vector4f = Vector4<Float>
typealias Vector2i = Vector2<Int>
typealias Vector3i = Vector3<Int>
typealias Vector4i = Vector4<Int>
typealias Vector2ui = Vector2<UInt>
typealias Vector3ui = Vector3<UInt>
typealias Vector4ui = Vector4<UInt>
typealias Vector2d = Vector2<Double>
typealias Vector3d = Vector3<Double>
typealias Vector4d = Vector4<Double>

@Serializable data class Matrix2x2<T>(val x: Vector2<T>, val y: Vector2<T>)
@Serializable data class Matrix3x3<T>(val x: Vector3<T>, val y: Vector3<T>, val z: Vector3<T>)
@Serializable data class Matrix4x4<T>(val x: Vector4<T>, val y: Vector4<T>, val z: Vector4<T>, val w: Vector4<T>)

object Matrix {
    inline fun scale(by: Vector2<Float>): Matrix2x2<Float> {
        return Matrix2x2(
            x = Vector2f(by.x, 0f),
            y = Vector2f(0f, by.y)
        )
    }

    inline fun scale(by: Vector3<Float>): Matrix3x3<Float> {
        return Matrix3x3(
            x = Vector3f(by.x, 0f, 0f),
            y = Vector3f(0f, by.y, 0f),
            z = Vector3f(0f, 0f, by.z)
        )
    }

    inline fun translate(by: Vector2<Float>): Matrix3x3<Float> {
        return Matrix3x3(
            x = Vector3f(0f, 0f, by.x),
            y = Vector3f(0f, 0f, by.y),
            z = Vector3f(0f, 0f, 1f),
        )
    }

    inline fun translate(by: Vector3<Float>): Matrix4x4<Float> {
        return Matrix4x4(
            x = Vector4f(0f, 0f, 0f, by.x),
            y = Vector4f(0f, 0f, 0f, by.y),
            z = Vector4f(0f, 0f, 0f, by.z),
            w = Vector4f(0f, 0f, 0f, 1f),
        )
    }
}

inline fun Matrix2x2<Float>.toMatrix3x3(): Matrix3x3<Float> {
    return Matrix3x3(
        x = Vector3(x = x.x, y = x.y, z = 0f),
        y = Vector3(x = y.x, y = y.y, z = 0f),
        z = Vector3(x = 0f,  y = 0f,  z = 1f)
    )
}

inline fun Matrix2x2<Float>.toMatrix4x4(): Matrix4x4<Float> {
    return Matrix4x4(
        x = Vector4(x = x.x, y = x.y, z = 0f, w = 0f),
        y = Vector4(x = y.x, y = y.y, z = 0f, w = 0f),
        z = Vector4(x = 0f,  y = 0f,  z = 1f, w = 0f),
        w = Vector4(x = 0f,  y = 0f,  z = 0f, w = 1f),
    )
}

inline fun Matrix3x3<Float>.toMatrix4x4(): Matrix4x4<Float> {
    return Matrix4x4(
        x = Vector4(x = x.x, y = x.y, z = x.z, w = 0f),
        y = Vector4(x = y.x, y = y.y, z = y.z, w = 0f),
        z = Vector4(x = z.x, y = z.y, z = z.z, w = 0f),
        w = Vector4(x = 0f,  y = 0f,  z = 0f,  w = 1f),
    )
}

operator fun Matrix3x3<Float>.times(v: Vector3f): Vector3f {
    return Vector3f(
        x = x.x * v.x + x.y * v.y + x.z * v.z,
        y = y.x * v.x + y.y * v.y + y.z * v.z,
        z = z.x * v.x + z.y * v.y + z.z * v.z,
    )
}

operator fun Matrix4x4<Float>.times(v: Vector4f): Vector4f {
    return Vector4f(
        x = x.x * v.x + x.y * v.y + x.z * v.z + x.w * v.w,
        y = y.x * v.x + y.y * v.y + y.z * v.z + y.w * v.w,
        z = z.x * v.x + z.y * v.y + z.z * v.z + z.w * v.w,
        w = w.x * v.x + w.y * v.y + w.z * v.z + w.w * v.w,
    )
}

inline operator fun Vector2f. plus(o: Vector2f) =   Vector2f(x + o.x, y + o.y)
inline operator fun Vector2i. plus(o: Vector2i) =   Vector2i(x + o.x, y + o.y)
inline operator fun Vector2ui.plus(o: Vector2ui) = Vector2ui(x + o.x, y + o.y)
inline operator fun Vector2d. plus(o: Vector2d) =   Vector2d(x + o.x, y + o.y)
inline operator fun Vector3f. plus(o: Vector3f) =   Vector3f(x + o.x, y + o.y, z + o.z)
inline operator fun Vector3i. plus(o: Vector3i) =   Vector3i(x + o.x, y + o.y, z + o.z)
inline operator fun Vector3ui.plus(o: Vector3ui) = Vector3ui(x + o.x, y + o.y, z + o.z)
inline operator fun Vector3d. plus(o: Vector3d) =   Vector3d(x + o.x, y + o.y, z + o.z)
inline operator fun Vector4f. plus(o: Vector4f) =   Vector4f(x + o.x, y + o.y, z + o.z, w + o.w)
inline operator fun Vector4i. plus(o: Vector4i) =   Vector4i(x + o.x, y + o.y, z + o.z, w + o.w)
inline operator fun Vector4ui.plus(o: Vector4ui) = Vector4ui(x + o.x, y + o.y, z + o.z, w + o.w)
inline operator fun Vector4d. plus(o: Vector4d) =   Vector4d(x + o.x, y + o.y, z + o.z, w + o.w)

inline operator fun Vector2f. minus(o: Vector2f) =   Vector2f(x - o.x, y - o.y)
inline operator fun Vector2i. minus(o: Vector2i) =   Vector2i(x - o.x, y - o.y)
inline operator fun Vector2ui.minus(o: Vector2ui) = Vector2ui(x - o.x, y - o.y)
inline operator fun Vector2d. minus(o: Vector2d) =   Vector2d(x - o.x, y - o.y)
inline operator fun Vector3f. minus(o: Vector3f) =   Vector3f(x - o.x, y - o.y, z - o.z)
inline operator fun Vector3i. minus(o: Vector3i) =   Vector3i(x - o.x, y - o.y, z - o.z)
inline operator fun Vector3ui.minus(o: Vector3ui) = Vector3ui(x - o.x, y - o.y, z - o.z)
inline operator fun Vector3d. minus(o: Vector3d) =   Vector3d(x - o.x, y - o.y, z - o.z)
inline operator fun Vector4f. minus(o: Vector4f) =   Vector4f(x - o.x, y - o.y, z - o.z, w - o.w)
inline operator fun Vector4i. minus(o: Vector4i) =   Vector4i(x - o.x, y - o.y, z - o.z, w - o.w)
inline operator fun Vector4ui.minus(o: Vector4ui) = Vector4ui(x - o.x, y - o.y, z - o.z, w - o.w)
inline operator fun Vector4d. minus(o: Vector4d) =   Vector4d(x - o.x, y - o.y, z - o.z, w - o.w)

inline operator fun Vector2f. div(o: Vector2f) =   Vector2f(x / o.x, y / o.y)
inline operator fun Vector2i. div(o: Vector2i) =   Vector2i(x / o.x, y / o.y)
inline operator fun Vector2ui.div(o: Vector2ui) = Vector2ui(x / o.x, y / o.y)
inline operator fun Vector2d. div(o: Vector2d) =   Vector2d(x / o.x, y / o.y)
inline operator fun Vector3f. div(o: Vector3f) =   Vector3f(x / o.x, y / o.y, z / o.z)
inline operator fun Vector3i. div(o: Vector3i) =   Vector3i(x / o.x, y / o.y, z / o.z)
inline operator fun Vector3ui.div(o: Vector3ui) = Vector3ui(x / o.x, y / o.y, z / o.z)
inline operator fun Vector3d. div(o: Vector3d) =   Vector3d(x / o.x, y / o.y, z / o.z)
inline operator fun Vector4f. div(o: Vector4f) =   Vector4f(x / o.x, y / o.y, z / o.z, w / o.w)
inline operator fun Vector4i. div(o: Vector4i) =   Vector4i(x / o.x, y / o.y, z / o.z, w / o.w)
inline operator fun Vector4ui.div(o: Vector4ui) = Vector4ui(x / o.x, y / o.y, z / o.z, w / o.w)
inline operator fun Vector4d. div(o: Vector4d) =   Vector4d(x / o.x, y / o.y, z / o.z, w / o.w)

inline operator fun Vector2f. times(o: Vector2f) =   Vector2f(x * o.x, y * o.y)
inline operator fun Vector2i. times(o: Vector2i) =   Vector2i(x * o.x, y * o.y)
inline operator fun Vector2ui.times(o: Vector2ui) = Vector2ui(x * o.x, y * o.y)
inline operator fun Vector2d. times(o: Vector2d) =   Vector2d(x * o.x, y * o.y)
inline operator fun Vector3f. times(o: Vector3f) =   Vector3f(x * o.x, y * o.y, z * o.z)
inline operator fun Vector3i. times(o: Vector3i) =   Vector3i(x * o.x, y * o.y, z * o.z)
inline operator fun Vector3ui.times(o: Vector3ui) = Vector3ui(x * o.x, y * o.y, z * o.z)
inline operator fun Vector3d. times(o: Vector3d) =   Vector3d(x * o.x, y * o.y, z * o.z)
inline operator fun Vector4f. times(o: Vector4f) =   Vector4f(x * o.x, y * o.y, z * o.z, w * o.w)
inline operator fun Vector4i. times(o: Vector4i) =   Vector4i(x * o.x, y * o.y, z * o.z, w * o.w)
inline operator fun Vector4ui.times(o: Vector4ui) = Vector4ui(x * o.x, y * o.y, z * o.z, w * o.w)
inline operator fun Vector4d. times(o: Vector4d) =   Vector4d(x * o.x, y * o.y, z * o.z, w * o.w)

inline fun Vector2f.toScaleMatrix2x2() = Matrix.scale(this)
inline fun Vector2f.toScaleMatrix3x3() = Matrix.scale(this).toMatrix3x3()
inline fun Vector2f.toScaleMatrix4x4() = Matrix.scale(this).toMatrix4x4()
inline fun Vector3f.toScaleMatrix3x3() = Matrix.scale(this)
inline fun Vector3f.toScaleMatrix4x4() = Matrix.scale(this).toMatrix4x4()

inline fun Vector2f.toTranslateMatrix2x2() = Matrix.translate(this)
inline fun Vector2f.toTranslateMatrix4x4() = Matrix.translate(this).toMatrix4x4()
inline fun Vector3f.toTranslateMatrix3x3() = Matrix.translate(this)

val Matrix2x2.Companion.identity: Matrix2x2<Float> get() = Matrix2x2(
    Vector2f(1f, 0f),
    Vector2f(0f, 1f)
)

val Matrix3x3.Companion.identity: Matrix3x3<Float> get() = Matrix3x3(
    Vector3f(1f, 0f, 0f),
    Vector3f(0f, 1f, 0f),
    Vector3f(0f, 0f, 1f)
)

val Matrix4x4.Companion.identity: Matrix4x4<Float> get() = Matrix4x4(
    Vector4f(1f, 0f, 0f, 0f),
    Vector4f(0f, 1f, 0f, 0f),
    Vector4f(0f, 0f, 1f, 0f),
    Vector4f(0f, 0f, 0f, 1f)
)
