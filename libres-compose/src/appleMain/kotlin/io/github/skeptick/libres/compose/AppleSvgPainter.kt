package io.github.skeptick.libres.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import io.github.skeptick.libres.images.Image
import kotlin.math.ceil

internal class AppleSvgPainter(private val sourceImage: Image) : Painter() {

    private val srcSize: Size = sourceImage.intSize.takeIfNotNull()?.toSize() ?: Size.Unspecified

    private lateinit var cachedImage: ImageBitmap
    private var cachedSize: Size = Size.Unspecified

    override val intrinsicSize = if (srcSize.isSpecified) srcSize else Size.Unspecified

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
        if (cachedSize != size) prepareImage()
        drawImage(cachedImage, srcSize = cachedSize.toIntSize(), alpha = alpha, colorFilter = colorFilter)
    }

    private fun DrawScope.prepareImage() {
        val skiaImage = sourceImage.toSkiaImage(size.toIntSize())
        cachedImage = skiaImage.toComposeImageBitmap()
        cachedSize = size
        cachedImage.prepareToDraw()
    }

}

private inline fun Size.toIntSize() = IntSize(ceil(width).toInt(), ceil(height).toInt())

private fun IntSize.takeIfNotNull() = takeIf { it.width != 0 && it.height != 0 }