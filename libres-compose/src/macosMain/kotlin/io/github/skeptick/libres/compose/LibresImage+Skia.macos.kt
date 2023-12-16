@file:OptIn(ExperimentalForeignApi::class)

package io.github.skeptick.libres.compose

import androidx.compose.ui.unit.IntSize
import io.github.skeptick.libres.images.Image
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import platform.AppKit.NSImageRep
import platform.CoreGraphics.CGImageRef
import platform.Foundation.NSRectFromCGRect

internal actual val Image.extension: String?
    get() = name()?.substringAfterLast('.')

internal actual val Image.intSize: IntSize
    get() {
        val rep = representations.firstOrNull() as? NSImageRep ?: return IntSize.Zero
        return IntSize(rep.pixelsWide.toInt(), rep.pixelsHigh.toInt())
    }

internal actual fun MemScope.libresImageToCGImage(image: Image, size: IntSize): CGImageRef {
    val nsRect = NSRectFromCGRect(size.toCGRect())
    return image.CGImageForProposedRect(nsRect.ptr, null, null) ?: error("Cannot get CGImage from NSImage")
}