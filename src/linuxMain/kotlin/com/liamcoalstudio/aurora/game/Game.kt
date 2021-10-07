package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.*
import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret

internal actual fun startFrame(windowHandle: WindowHandle) {
    glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).convert())
}

internal actual fun endFrame(windowHandle: WindowHandle) {
    glfwSwapBuffers(windowHandle.handle.reinterpret())
    glfwPollEvents()
}

internal actual fun startGame() {
    if(glfwInit() != 1) throw IllegalStateException("glfw failed to initialize")
}

internal actual fun endGame() {
    glfwTerminate()
}
