package com.liamcoalstudio.aurora.test

import com.liamcoalstudio.aurora.Vector2f
import com.liamcoalstudio.aurora.draw.DrawCollection
import com.liamcoalstudio.aurora.game.Game
import com.liamcoalstudio.aurora.game.run
import com.liamcoalstudio.aurora.game.shader
import com.liamcoalstudio.aurora.shader.ShaderInputType
import com.liamcoalstudio.aurora.texture.Image
import com.liamcoalstudio.aurora.texture.Texture
import com.liamcoalstudio.aurora.window.WindowHandle
import com.liamcoalstudio.aurora.window.WindowOpenBuilder
import kotlin.test.Test

class TextureGameTest : Game() {
    data class Vertex(
        val vPosition: Vector2f,
        val vTexCoords: Vector2f
    )

    override fun WindowOpenBuilder.window() {
        windowed(800, 600)
        title("Texture Game")
    }

    private val shader by shader<Vertex> {
        vertex("""
            #version 150 core
            
            in vec2 vPosition;
            in vec2 vTexCoords;
            
            out vec2 fTexCoords;
            
            uniform mat4 transform;
            
            void main() {
                gl_Position = transform * vec4(vPosition, 0, 1);
                fTexCoords = vTexCoords;
            }
        """.trimIndent())

        fragment("""
            #version 150 core
            
            in vec2 fTexCoords;
            
            out vec4 oColor;
            
            uniform sampler2D uTexture;
            
            void main() {
                oColor = texture(uTexture, fTexCoords);
            }
        """.trimIndent())

        input(Vertex::vPosition, ShaderInputType.Vector2f)
        input(Vertex::vTexCoords, ShaderInputType.Vector2f)
    }

    private var draw: DrawCollection<Vertex>? = null
    private var texture: Texture? = null
    private var frameCount = 0

    override fun init(window: WindowHandle) {
        build(shader)
        shader.get().use()
        shader.get().uniform("transform").push(aspectRatioCompensation)
        draw = shader.get().constructDrawCollection(
            Vertex(Vector2f(-1f, -1f), Vector2f(0f, 0f)),
            Vertex(Vector2f(-1f, 1f), Vector2f(0f, 1f)),
            Vertex(Vector2f(1f, -1f), Vector2f(1f, 0f)),
            Vertex(Vector2f(1f, 1f), Vector2f(1f, 1f)),
            Vertex(Vector2f(-1f, 1f), Vector2f(0f, 1f)),
            Vertex(Vector2f(1f, -1f), Vector2f(1f, 0f)),
        )
    }

    override fun draw(window: WindowHandle) {
        shader.get().use()
        texture = Image.checkerboard.asTexture()
        texture!!.use(0)
        shader.get().uniform("uTexture").push(0)
        draw?.draw()
        texture!!.delete()
    }

    override fun update(window: WindowHandle) {
        if(frameCount++ >= 15) window.shouldClose = true
    }

    override fun quit(window: WindowHandle) {
        draw?.delete()
        shader.get().delete()
    }

    @Test fun test() = run()
}
