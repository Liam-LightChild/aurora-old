package com.liamcoalstudio.aurora.window

import com.liamcoalstudio.aurora.Vector2i
import com.liamcoalstudio.aurora.glfwGetPrimaryMonitor
import com.liamcoalstudio.aurora.glfwGetVideoMode
import kotlinx.cinterop.pointed

actual fun getFullscreenSize(): Vector2i = glfwGetVideoMode(glfwGetPrimaryMonitor())
    .let { it!!.pointed }
    .let { Vector2i(it.width, it.height) }
