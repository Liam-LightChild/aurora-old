package com.liamcoalstudio.aurora

internal expect object Internal {
    fun openWindow(
        name: String,
        fullscreen: Boolean,
        width: Int?,
        height: Int?,
        share: WindowHandle?,
        config: Map<WindowConfig, Any>
    ): WindowHandle
}
