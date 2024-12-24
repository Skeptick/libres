package io.github.skeptick.libres.images

import platform.Foundation.NSBundle
import platform.AppKit.NSImage
import platform.AppKit.imageForResource

public fun NSBundle.Companion.bundleWithName(name: String): NSBundle {
    val url = mainBundle.URLForResource(name = name, withExtension = "bundle") ?: error("$name.bundle not found")
    return bundleWithURL(url = url) ?: error("Cannot access $name.bundle")
}

public fun NSBundle.image(name: String, extension: String): NSImage {
    val image = imageForResource(name) ?: error("Image $name not found")
    image.setName("$name.$extension")
    return image
}