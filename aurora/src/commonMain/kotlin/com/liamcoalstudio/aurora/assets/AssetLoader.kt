package com.liamcoalstudio.aurora.assets

interface AssetLoader<T> {
    infix fun from(asset: Asset): T
}
