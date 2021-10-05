package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.openWindow

expect fun withGlfw(f: () -> Unit)

inline fun inWindow(f: (WindowHandle) -> Unit) {
    val window = openWindow {
        invisible()
        title("Window")
    }

    window.use()
    f(window)

    window.close()
}
