package io.github.skeptick.libres.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import io.github.skeptick.libres.images.Image
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM
import org.jetbrains.skia.Image as SkiaImage

@Composable
internal fun ByteArray?.asPainter(image: Image): Painter {
    return when (val bytes = this) {
        null -> ColorPainter(color = Color.Transparent)
        else -> when (image.substringAfterLast(".")) {
            "svg" -> rememberSvgResource(image, bytes)
            else -> rememberBitmapResource(image, bytes)
        }
    }
}

@Composable
internal fun rememberSvgResource(name: String, bytes: ByteArray): Painter {
    val density = LocalDensity.current
    return remember(name, density) {
        val data = Data.makeFromBytes(bytes)
        JsSvgPainter(SVGDOM(data), density)
    }
}

@Composable
internal fun rememberBitmapResource(name: String, bytes: ByteArray): Painter {
    return remember(name) {
        val skiaImage = SkiaImage.makeFromEncoded(bytes = bytes)
        BitmapPainter(image = skiaImage.toComposeImageBitmap())
    }
}