package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Window
import kotlin.test.Test

class WindowTest {
    @Test
    fun test_windowed() = withGlfw {
        val window = Window.open {
            windowed(800, 600)
            title("Hello, World")
        }

        window.close()
    }

    @Test
    fun test_fullscreen() = withGlfw {
        val window = Window.open {
            fullscreen()
            title("Hello, World")
        }

        window.close()
    }

    @Test
    fun test_invisible() = withGlfw {
        val window = Window.open {
            invisible()
            title("Hello, World")
        }

        window.close()
    }
}