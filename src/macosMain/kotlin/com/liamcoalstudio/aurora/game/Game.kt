package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.convert
import platform.OpenGL3.GL_COLOR_BUFFER_BIT
import platform.OpenGL3.GL_DEPTH_BUFFER_BIT
import platform.OpenGL3.glClear

internal actual fun startFrame(windowHandle: WindowHandle) {
    glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).convert())
}
