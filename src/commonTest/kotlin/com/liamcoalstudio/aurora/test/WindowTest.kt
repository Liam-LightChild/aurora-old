package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.openWindow
import kotlin.test.Test

class WindowTest {
    @Test
    fun test_windowed() = withGlfw {
        val window = openWindow {
            windowed(800, 600)
            title("Hello, World")
        }

        window.close()
    }

    @Test
    fun test_fullscreen() = withGlfw {
        val window = openWindow {
            fullscreen()
            title("Hello, World")
        }

        window.close()
    }

    @Test
    fun test_invisible() = withGlfw {
        val window = openWindow {
            invisible()
            title("Hello, World")
        }

        window.close()
    }
}