package com.liamcoalstudio.aurora

data class Vector2<T>(val x: T, val y: T)
data class Vector3<T>(val x: T, val y: T, val z: T)
data class Vector4<T>(val x: T, val y: T, val z: T, val w: T)

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
