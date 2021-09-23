package com.liamcoalstudio.aurora

internal actual fun init() {
    if(glfwInit() == 0) throw IllegalStateException("GLFW failed to initialize; cannot continue")
}

internal actual fun terminate() {
    glfwTerminate()
}
