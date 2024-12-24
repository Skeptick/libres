package io.github.skeptick.libres.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import io.github.skeptick.libres.images.Image

@Composable
public actual fun painterResource(image: Image): Painter {
    return androidx.compose.ui.res.painterResource(image)
}