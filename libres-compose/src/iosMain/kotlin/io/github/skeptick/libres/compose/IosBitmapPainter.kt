package io.github.skeptick.libres.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.*
import platform.UIKit.UIGraphicsImageRenderer
import platform.UIKit.UIGraphicsImageRendererFormat
import platform.UIKit.UIImage
import kotlin.math.roundToInt

internal class IosBitmapPainter(private val sourceImage: UIImage) : Painter() {

    private var srcSize: IntSize = with(sourceImage.CGImage) {
        IntSize(
            width = CGImageGetWidth(this).toInt(),
            height = CGImageGetHeight(this).toInt()
        )
    }

    override val intrinsicSize: Size get() = srcSize.toSize()

    private var alpha: Float = 1.0f

    private var colorFilter: ColorFilter? = null

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun DrawScope.onDraw() {
        srcSize = size.toIntSize()
        val resizedImage = sourceImage.resize(this.size)
        val skiaImage = resizedImage.toSkiaImage() ?: error("Error when converting UIImage to SkiaImage")
        val composeBitmap = skiaImage.toComposeImageBitmap()
        drawImage(composeBitmap, srcSize = srcSize, alpha = alpha, colorFilter = colorFilter)
    }

}

private fun UIImage.resize(size: Size): UIImage {
    val width = size.width.toDouble()
    val height = size.height.toDouble()
    val cgSize = CGSizeMake(width, height)
    val cgRect = CGRectMake(0.0, 0.0, width, height)
    val format = UIGraphicsImageRendererFormat().apply { scale = 1.0 }
    val newImage = UIGraphicsImageRenderer(cgSize, format).imageWithActions { drawInRect(cgRect) }
    return newImage.imageWithRenderingMode(renderingMode)
}

private fun UIImage.toSkiaImage(): org.jetbrains.skia.Image? {
    val imageRef = CGImageCreateCopyWithColorSpace(CGImage, CGColorSpaceCreateDeviceRGB()) ?: return null
    val imageInfo = imageRef.skiaImageInfo
    val byteArray = imageRef.toByteArray() ?: return null
    val rowBytes = CGImageGetBytesPerRow(imageRef).toInt()
    CFRelease(imageRef)
    return org.jetbrains.skia.Image.makeRaster(imageInfo, byteArray, rowBytes)
}

private fun Size.toIntSize() = IntSize(width.roundToInt(), height.roundToInt())