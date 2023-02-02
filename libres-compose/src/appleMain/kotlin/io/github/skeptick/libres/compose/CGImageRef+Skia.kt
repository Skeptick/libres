package io.github.skeptick.libres.compose

import kotlinx.cinterop.get
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.*

internal fun CGImageRef.toByteArray(): ByteArray? {
    val data = CGDataProviderCopyData(CGImageGetDataProvider(this))
    val bytePointer = CFDataGetBytePtr(data) ?: return null
    val length = CFDataGetLength(data)
    val byteArray = ByteArray(length.toInt()) { index -> bytePointer[index].toByte() }
    CFRelease(data)
    return byteArray
}

internal val CGImageRef.skiaImageInfo: ImageInfo
    get() = ImageInfo(
        width = CGImageGetWidth(this).toInt(),
        height = CGImageGetHeight(this).toInt(),
        colorType = ColorType.RGBA_8888,
        alphaType = skiaAlphaType
    )

internal val CGImageRef.skiaAlphaType: ColorAlphaType
    get() = when (CGImageGetAlphaInfo(this)) {
        CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst,
        CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL
        CGImageAlphaInfo.kCGImageAlphaFirst,
        CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL
        CGImageAlphaInfo.kCGImageAlphaNone,
        CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst,
        CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE
        else -> ColorAlphaType.UNKNOWN
    }