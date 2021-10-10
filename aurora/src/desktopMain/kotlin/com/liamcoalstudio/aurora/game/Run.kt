package com.liamcoalstudio.aurora.game

import com.liamcoalstudio.aurora.window.WindowHandle
import kotlinx.cinterop.reinterpret

internal actual fun startGame() {
    if(glfwInit() != 1) throw IllegalStateException("glfw failed to initialize")
}

internal actual fun endGame() {
    glfwTerminate()
}

internal actual fun endFrame(windowHandle: WindowHandle) {
    glfwSwapBuffers(windowHandle.handle.reinterpret())
    glfwPollEvents()
}