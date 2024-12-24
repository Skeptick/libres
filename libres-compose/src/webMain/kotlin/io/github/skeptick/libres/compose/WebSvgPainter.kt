@file:Suppress("NOTHING_TO_INLINE")

package io.github.skeptick.libres.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import org.jetbrains.skia.svg.*
import kotlin.math.ceil

internal class WebSvgPainter(private val dom: SVGDOM, density: Density) : Painter() {

    private val srcSize: Size = dom.pxSize ?: Size.Unspecified

    private lateinit var cachedImage: ImageBitmap
    private lateinit var cachedCanvas: Canvas
    private var cachedSize: Size = Size.Unspecified
    private val cacheScope: CanvasDrawScope = CanvasDrawScope()

    override val intrinsicSize = if (srcSize.isSpecified) srcSize * density.density else Size.Unspecified

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
        cachedImage = ImageBitmap(size.toIntSize())
        cachedCanvas = Canvas(cachedImage)
        cachedSize = size
        cacheScope.clearAndDrawSvg(density = this, layoutDirection, size)
        cachedImage.prepareToDraw()
    }

    private fun CanvasDrawScope.clearAndDrawSvg(density: Density, layoutDirection: LayoutDirection, size: Size) {
        draw(density, layoutDirection, cachedCanvas, size) {
            drawRect(color = Color.Black, blendMode = BlendMode.Clear)
            drawIntoCanvas { canvas ->
                dom.root?.width = SVGLength(size.width, SVGLengthUnit.PX)
                dom.root?.height = SVGLength(size.height, SVGLengthUnit.PX)
                dom.root?.preserveAspectRatio = SVGPreserveAspectRatio(SVGPreserveAspectRatioAlign.NONE)
                dom.render(canvas.nativeCanvas)
            }
        }
    }

}

private inline fun ImageBitmap(intSize: IntSize) = ImageBitmap(intSize.width, intSize.height)

private inline fun Size.toIntSize() = IntSize(ceil(width).toInt(), ceil(height).toInt())

private inline val SVGDOM.pxSize: Size?
    get() = root?.run {
        val width = width.withUnit(SVGLengthUnit.PX).value
        val height = height.withUnit(SVGLengthUnit.PX).value
        if (width != 0f && height != 0f) Size(width, height) else Size.Unspecified
    }