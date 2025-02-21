package io.github.skeptick.libres.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import io.github.skeptick.libres.images.Image
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.jetbrains.skia.Data
import org.jetbrains.skia.svg.SVGDOM
import org.jetbrains.skia.Image as SkiaImage
import org.khronos.webgl.Int8Array
import org.w3c.fetch.Response

@Composable
public actual fun painterResource(image: Image): Painter {
    val state by produceState<ByteArray?>(initialValue = null, image) {
        val response = window.fetch(image).await()
        if (response.ok) value = response.byteArray()
    }

    return when (val bytes = state) {
        null -> ColorPainter(color = Color.Transparent)
        else -> when (image.substringAfterLast(".")) {
            "svg" -> rememberSvgResource(image, bytes)
            else -> rememberBitmapResource(image, bytes)
        }
    }
}

@Composable
private fun rememberSvgResource(name: String, bytes: ByteArray): Painter {
    val density = LocalDensity.current
    return remember(name, density) {
        val data = Data.makeFromBytes(bytes)
        WebSvgPainter(SVGDOM(data), density)
    }
}

@Composable
private fun rememberBitmapResource(name: String, bytes: ByteArray): Painter {
    return remember(name) {
        val skiaImage = SkiaImage.makeFromEncoded(bytes = bytes)
        BitmapPainter(image = skiaImage.toComposeImageBitmap())
    }
}

private suspend fun Response.byteArray() = arrayBuffer().await().let(::Int8Array).unsafeCast<ByteArray>()