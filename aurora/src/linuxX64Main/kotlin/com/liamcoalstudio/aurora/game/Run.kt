package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.GL_COLOR_BUFFER_BIT
import com.liamcoalstudio.aurora.GL_DEPTH_BUFFER_BIT
import com.liamcoalstudio.aurora.glClear
import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.convert

internal actual fun startFrame(windowHandle: WindowHandle) {
    glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).convert())
}