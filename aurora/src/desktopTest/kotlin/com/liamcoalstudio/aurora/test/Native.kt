package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.glfwInit
import com.liamcoalstudio.aurora.glfwTerminate
import kotlin.test.fail

actual fun withGlfw(f: () -> Unit) {
    if(glfwInit() > 0) {
        f()
        glfwTerminate()
    } else fail("GLFW did not initialize")
}

