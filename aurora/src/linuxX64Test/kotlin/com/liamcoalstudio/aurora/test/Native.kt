package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.GL_NO_ERROR
import com.liamcoalstudio.aurora.GLenum
import com.liamcoalstudio.aurora.glGetError
import com.liamcoalstudio.aurora.glewGetErrorString
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlin.test.fail

actual fun assertNoError() {
    val err = glGetError()
    if(err != GL_NO_ERROR.convert<GLenum>()) {
        fail("GL ERROR: ${glewGetErrorString(err)?.reinterpret<ByteVar>()?.toKString()}")
    }
}
