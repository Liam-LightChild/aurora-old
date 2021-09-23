package com.liamcoalstudio.aurora

import com.liamcoalstudio.aurora.enums.ShaderStage
import com.liamcoalstudio.aurora.enums.ShaderStage.*
import kotlinx.cinterop.convert
import platform.OpenGL3.*
import platform.OpenGLCommon.GLenum

val ShaderStage.native
    get() = when(this) {
        VERTEX -> GL_VERTEX_SHADER
        FRAGMENT -> GL_FRAGMENT_SHADER
    }.convert<GLenum>()
