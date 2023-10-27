package io.github.skeptick.libres.compose

import androidx.compose.ui.unit.IntSize
import io.github.skeptick.libres.images.Image as LibresImage
import kotlinx.cinterop.*
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Image as SkiaImage
import platform.CoreGraphics.*

internal expect val LibresImage.extension: String?

internal expect val LibresImage.intSize: IntSize

internal expect fun MemScope.libresImageToCGImage(image: LibresImage, size: IntSize): CGImageRef

internal fun LibresImage.toSkiaImage(size: IntSize = intSize): SkiaImage = memScoped {
    val imageRef = libresImageToCGImage(image = this@toSkiaImage, size)
    val width = size.width.toULong()
    val height = size.height.toULong()
    val bytesPerComponent = 8UL
    val bytesPerRow = width * 4UL
    val dataLength = (bytesPerRow * height).toInt()
    val rawData = allocArray<ByteVar>(dataLength)

    val context = CGBitmapContextCreate(
        data = rawData,
        width = width,
        height = height,
        bitsPerComponent = bytesPerComponent,
        bytesPerRow = bytesPerRow,
        space = CGColorSpaceCreateDeviceRGB(),
        bitmapInfo = CGImageGetBitmapInfo(imageRef)
    )
    CGContextDrawImage(context, size.toCGRect(), imageRef)
    CGContextRelease(context)

    return SkiaImage.makeRaster(
        imageInfo = ImageInfo(
            width = size.width,
            height = size.height,
            colorType = ColorType.RGBA_8888,
            alphaType = imageRef.skiaAlphaType
        ),
        bytes = rawData.readBytes(dataLength),
        rowBytes = bytesPerRow.toInt()
    )
}

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

internal fun IntSize.toCGRect() = CGRectMake(0.0, 0.0, width.toDouble(), height.toDouble())