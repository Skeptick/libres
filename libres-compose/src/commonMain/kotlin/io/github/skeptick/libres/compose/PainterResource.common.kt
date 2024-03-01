package io.github.skeptick.libres.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import io.github.skeptick.libres.images.Image

@Composable
expect fun painterResource(image: Image): Painter

@Suppress("unused")
@Composable
fun Image.painterResource() = painterResource(this)