package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import kotlin.test.Test
import kotlin.test.assertTrue

class GameTest {
    class BasicGame : Game() {
        override val window by window {
            windowed(800, 600)
            title("Window")
        }

        override fun update() {
            window.shouldClose = true
            assertTrue(window.shouldClose, "window would not close")
        }
    }

    @Test fun basicGame_test() = BasicGame().run()

    class ShaderGame : Game() {
        override val window by window {
            invisible()
            title("Shader Game")
        }

        val shader by shader {
            vertex("""
                #version 150 core
                
                void main() {
                    gl_Position = vec4(0, 0, 0, 1);
                }
            """.trimIndent())

            fragment("""
                #version 150 core
                
                void main() { }
            """.trimIndent())
        }

        override fun init() {
            build(shader)
            window.shouldClose = true
        }

        override fun quit() {
            shader.get().delete()
        }
    }

    @Test fun shaderGame_test() = ShaderGame().run()
}