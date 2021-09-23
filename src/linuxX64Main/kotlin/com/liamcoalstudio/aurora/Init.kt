package com.liamcoalstudio.aurora

actual fun init() {
    if(glfwInit() == 0) {
        throw IllegalStateException("GLFW failed to initialize; cannot continue")
    }
}

actual fun terminate() {
    glfwTerminate()
}
