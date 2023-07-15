package io.github.skeptick.libres.plugin.images.models

internal class ImageSet(
    val name: String,
    val images: Iterable<ImageProps>,
) {
    val isVector: Boolean
        get() = images.all { it.isVector }

    val isTintable: Boolean
        get() = images.all { it.isTintable }
}
