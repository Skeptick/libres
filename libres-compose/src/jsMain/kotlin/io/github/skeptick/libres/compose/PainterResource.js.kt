package io.github.skeptick.libres.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.painter.Painter
import io.github.skeptick.libres.images.Image
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.Int8Array
import org.w3c.fetch.Response

@Composable
actual fun painterResource(image: Image): Painter {
    val state by produceState<ByteArray?>(initialValue = null, image) {
        val response = window.fetch(image).await()
        if (response.ok) value = response.byteArray()
    }

    return state.asPainter(image)
}

private suspend fun Response.byteArray() = arrayBuffer().await().let(::Int8Array).unsafeCast<ByteArray>()
