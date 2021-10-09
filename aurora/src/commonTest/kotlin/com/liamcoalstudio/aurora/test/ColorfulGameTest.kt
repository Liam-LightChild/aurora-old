package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Vector2f
import com.liamcoalstudio.aurora.Vector3f
import com.liamcoalstudio.aurora.draw.DrawCollection
import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.game.shader
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.test.Test

class ColorfulGameTest : Game() {
    data class ColorfulGameVertex(
        val vPosition: Vector2f,
        val vColor: Vector3f
    )

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
    private var frameCount = 0

    override fun init(window: WindowHandle) {
        build(shader)
        draw = shader.get().constructDrawCollection(
            ColorfulGameVertex(Vector2f(0f, 0f), Vector3f(1f, 0f, 0f)),
            ColorfulGameVertex(Vector2f(0f, 1f), Vector3f(0f, 1f, 0f)),
            ColorfulGameVertex(Vector2f(1f, 0f), Vector3f(0f, 0f, 1f))
        )
    }

    override fun draw(window: WindowHandle) {
        shader.get().use()
        draw?.draw()
    }

    override fun update(window: WindowHandle) {
        if(frameCount++ >= 5) window.shouldClose = true
    }

    override fun quit(window: WindowHandle) {
        draw?.delete()
        shader.get().delete()
    }

    @Test
    fun test() {
        run()
        assertNoError()
    }
}