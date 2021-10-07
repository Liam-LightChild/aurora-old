package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Vector2f
import com.liamcoalstudio.aurora.Vector3f
import com.liamcoalstudio.aurora.draw.DrawCollection
import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.input.Key
import com.liamcoalstudio.aurora.input.setupKeyHandlers
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class GameTest {
    abstract class FailableGame : Game() {
        private var fail: Boolean = false

        override fun init(window: WindowHandle) {
            super.init(window)
            window.setupKeyHandlers {
                on(Key.F) { fail = true }
                on(Key.G) { window.shouldClose = true }
            }
        }

        override fun update(window: WindowHandle) {
            super.update(window)
            if(fail) fail("Failed at user request")
        }
    }

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

    class DrawGame : FailableGame() {
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
            draw = shader.get().constructDrawCollection(
                DrawGameVertex(Vector2f(0f, 0f)),
                DrawGameVertex(Vector2f(0f, 1f)),
                DrawGameVertex(Vector2f(1f, 0f))
            )
            super.init(window)
        }

        override fun draw(window: WindowHandle) {
            shader.get().use()
            draw?.draw()
        }

        override fun quit(window: WindowHandle) {
            draw?.delete()
            shader.get().delete()
        }
    }

    @Test fun drawGame_test() = DrawGame().run()

    data class ColorfulGameVertex(
        val vPosition: Vector2f,
        val vColor: Vector3f
    )

    class ColorfulGame : FailableGame() {
        override fun WindowOpenBuilder.window() {
            windowed(800, 600)
            title("Colorful Game")
        }

        val shader by shader<ColorfulGameVertex> {
            vertex("""
                #version 150 core
                
                in vec2 vPosition;
                in vec3 vColor;
                
                out vec3 fColor;
                
                void main() {
                    gl_Position = vec4(vPosition, 0, 1);
                    fColor = vColor;
                }
            """.trimIndent())

            fragment("""
                #version 150 core
                
                in vec3 fColor;
                
                out vec4 oColor;
                
                void main() {
                    oColor = vec4(fColor, 1);
                }
            """.trimIndent())

            input(ColorfulGameVertex::vPosition, ShaderInputType.Vector2f)
            input(ColorfulGameVertex::vColor, ShaderInputType.Vector3f)
        }

        private var draw: DrawCollection<ColorfulGameVertex>? = null

        override fun init(window: WindowHandle) {
            build(shader)
            draw = shader.get().constructDrawCollection(
                ColorfulGameVertex(Vector2f(0f, 0f), Vector3f(1f, 0f, 0f)),
                ColorfulGameVertex(Vector2f(0f, 1f), Vector3f(0f, 1f, 0f)),
                ColorfulGameVertex(Vector2f(1f, 0f), Vector3f(0f, 0f, 1f))
            )
            super.init(window)
        }

        override fun draw(window: WindowHandle) {
            shader.get().use()
            draw?.draw()
        }

        override fun quit(window: WindowHandle) {
            draw?.delete()
            shader.get().delete()
        }
    }

    @Test fun colorfulGame_test() = ColorfulGame().run()
}
