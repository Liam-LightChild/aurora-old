package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MathTest {
    @Test
    fun matrix2x2_scale2() {
        assertEquals(Matrix2x2(
            Vector2f(2f, 0f),
            Vector2f(0f, 3f)
        ), Matrix.scale(Vector2f(2f, 3f)))
    }

    @Test
    fun matrix3x3_scale2() {
        assertEquals(Matrix3x3(
            Vector3f(2f, 0f, 0f),
            Vector3f(0f, 3f, 0f),
            Vector3f(0f, 0f, 1f)
        ), Matrix.scale(Vector2f(2f, 3f)).toMatrix3x3())
    }

    @Test
    fun matrix4x4_scale2() {
        assertEquals(Matrix4x4(
            Vector4f(2f, 0f, 0f, 0f),
            Vector4f(0f, 3f, 0f, 0f),
            Vector4f(0f, 0f, 1f, 0f),
            Vector4f(0f, 0f, 0f, 1f)
        ), Matrix.scale(Vector2f(2f, 3f)).toMatrix4x4())
    }
}
