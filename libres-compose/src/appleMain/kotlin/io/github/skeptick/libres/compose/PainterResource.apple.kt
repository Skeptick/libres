package io.github.skeptick.libres.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.github.skeptick.libres.images.Image

@Composable
actual fun painterResource(image: Image): Painter {
    return when (image.extension) {
        "svg" -> rememberSvgResource(image)
        else -> rememberBitmapResource(image)
    }
}

@Composable
internal fun rememberSvgResource(image: Image): Painter {
    return remember(image) {
        AppleSvgPainter(image)
    }
}

@Composable
internal fun rememberBitmapResource(image: Image): Painter {
    return remember(image) {
        val skiaImage = image.toSkiaImage()
        BitmapPainter(skiaImage.toComposeImageBitmap())
    }
}