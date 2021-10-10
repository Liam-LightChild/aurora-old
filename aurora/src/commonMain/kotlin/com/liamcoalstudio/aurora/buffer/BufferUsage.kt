package com.liamcoalstudio.aurora.buffer

enum class BufferUsage {
    Draw,
    DrawStream,
    DrawStatic,

    Data,
    DataStream,
    DataStatic,

    Copy,
    CopyStream,
    CopyStatic
}