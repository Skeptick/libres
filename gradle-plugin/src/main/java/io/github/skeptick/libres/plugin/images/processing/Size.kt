package io.github.skeptick.libres.plugin.images.processing

import org.bytedeco.opencv.opencv_core.Size

internal fun calculateSize(oldSize: Size, newSideSize: Int): Size {
    val height = oldSize.height()
    val width = oldSize.width()

    return when {
        height == width -> Size(newSideSize, newSideSize)
        height > width -> Size((width.toDouble() / height * newSideSize).toInt(), newSideSize)
        else -> Size(newSideSize, (height.toDouble() / width * newSideSize).toInt())
    }
}

internal fun Size.isLessThan(sideSize: Int): Boolean {
    return width() < sideSize && height() < sideSize
}

internal fun Size.isEqual(sideSize: Int): Boolean {
    return (width() == sideSize && height() <= width()) || (height() == sideSize && width() <= height())
}