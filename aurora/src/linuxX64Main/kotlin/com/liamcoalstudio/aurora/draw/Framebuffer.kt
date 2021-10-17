package com.liamcoalstudio.aurora.draw

import com.liamcoalstudio.aurora.*
import kotlinx.cinterop.*

actual class Framebuffer(
    internal val handle: Int,
    internal val textureColorBuffer: UInt,
    internal val renderbuffer: UInt
) : DrawTarget() {
    @OptIn(ExperimentalUnsignedTypes::class)
    actual constructor(width: Int, height: Int) : this(kotlin.run {
        val v = UIntArray(1)
        v.usePinned { glGenFramebuffers!!(1, it.addressOf(0)) }
        v[0].also { glBindFramebuffer!!(GL_FRAMEBUFFER.convert(), it) }.toInt()
    }, kotlin.run {
        val t = nativeHeap.alloc<GLuintVar>()
        glGenTextures(1, t.ptr)
        glBindTexture(GL_TEXTURE_2D, t.value)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, null)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        glFramebufferTexture2D!!(GL_FRAMEBUFFER.convert(), GL_COLOR_ATTACHMENT0.convert(), GL_TEXTURE_2D.convert(), t.value, 0)
        t.value
    }, kotlin.run {
        val r = nativeHeap.alloc<GLuintVar>()
        glGenRenderbuffers!!(1, r.ptr)
        glBindRenderbuffer!!(GL_RENDERBUFFER.convert(), r.value)
        glRenderbufferStorage!!(GL_RENDERBUFFER.convert(), GL_DEPTH24_STENCIL8.convert(), width, height)
        glFramebufferRenderbuffer!!(GL_FRAMEBUFFER.convert(), GL_DEPTH_STENCIL_ATTACHMENT.convert(), GL_RENDERBUFFER.convert(), r.value)
        r.value
    })

    actual override fun unUse() {
        val v = IntArray(1)
        v.usePinned { glGetIntegerv(GL_DRAW_FRAMEBUFFER_BINDING, it.addressOf(0)) }
        if(v[0] == 0) return
        glBindFramebuffer!!(GL_FRAMEBUFFER.convert(), 0u)
    }

    actual override fun resourceUse() {
        val v = IntArray(1)
        v.usePinned { glGetIntegerv(GL_DRAW_FRAMEBUFFER_BINDING, it.addressOf(0)) }
        if(v[0] == handle) return
        glBindFramebuffer!!(GL_FRAMEBUFFER.convert(), handle.convert())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    actual override fun resourceDelete() {
        val v = uintArrayOf(handle.toUInt(), textureColorBuffer, renderbuffer)
        v.usePinned {
            glDeleteFramebuffers!!(1, it.addressOf(0))
            glDeleteTextures(1, it.addressOf(1))
            glDeleteRenderbuffers!!(1, it.addressOf(2))
        }
    }
}