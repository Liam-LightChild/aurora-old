package com.liamcoalstudio.aurora.window

import com.liamcoalstudio.aurora.GLFW_VISIBLE

actual enum class WindowConfig(val native: Int) {
    Visible(GLFW_VISIBLE)
}
