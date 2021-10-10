package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.window.WindowHandle

internal actual fun startFrame(windowHandle: WindowHandle) {
    glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).convert())
}