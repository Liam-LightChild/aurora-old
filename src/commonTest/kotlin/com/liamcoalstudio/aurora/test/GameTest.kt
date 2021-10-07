package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Vector2f
import com.liamcoalstudio.aurora.draw.DrawCollection
import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.test.Test
import kotlin.test.assertTrue

class GameTest {
    class BasicGame : Game() {
        override fun WindowOpenBuilder.window() {
            windowed(800, 600)
            title("Window")
        }

        override fun update(window: WindowHandle) {
            window.shouldClose = true
            assertTrue(window.shouldClose, "window would not close")
        }
    }

    @Test fun basicGame_test() = BasicGame().run()

    class ShaderGame : Game() {
        override fun WindowOpenBuilder.window() {
            invisible()
            title("Shader Game")
        }

        val shader by shader<Nothing> {
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

        override fun init(window: WindowHandle) {
            build(shader)
            window.shouldClose = true
        }

        override fun quit(window: WindowHandle) {
            shader.get().delete()
        }
    }

    @Test fun shaderGame_test() = ShaderGame().run()

    data class DrawGameVertex(
        val vPosition: Vector2f
    )

    class DrawGame : Game() {
        override fun WindowOpenBuilder.window() {
            windowed(800, 600)
            title("Draw Game")
        }

        val shader by shader<DrawGameVertex> {
            vertex("""
                #version 150 core
                
                in vec2 vPosition;
                
                void main() {
                    gl_Position = vec4(vPosition, 0, 1);
                }
            """.trimIndent())

            fragment("""
                #version 150 core
                
                out vec4 oColor;
                
                void main() {
                    oColor = vec4(1, 1, 1, 1);
                }
            """.trimIndent())

            input(DrawGameVertex::vPosition, ShaderInputType.Vector2f)
        }

        private var draw: DrawCollection<DrawGameVertex>? = null

        override fun init(window: WindowHandle) {
            build(shader)
//            draw = shader.get().constructDrawCollection(
//                DrawGameVertex(Vector2f(0f, 0f)),
//                DrawGameVertex(Vector2f(0f, 1f)),
//                DrawGameVertex(Vector2f(1f, 0f))
//            )
        }

        override fun draw(window: WindowHandle) {
            shader.get().use()
            shader.get().constructDrawCollection(
                DrawGameVertex(Vector2f(0f, 0f)),
                DrawGameVertex(Vector2f(0f, 1f)),
                DrawGameVertex(Vector2f(1f, 0f))
            ).run {
                draw()
                delete()
            }
        }

        override fun quit(window: WindowHandle) {
            draw?.delete()
            shader.get().delete()
        }
    }

    @Test fun drawGame_test() = DrawGame().run()
}