package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Window
import com.liamcoalstudio.aurora.init
import com.liamcoalstudio.aurora.terminate

abstract class TestContainer {
    fun run(f: (Window) -> Unit) {
        init()
        val w = Window.windowed("Test Window", 32, 32)
        w.with(f)
        w.close()
        terminate()
    }
}
