package io.github.skeptick.libres.images

import platform.Foundation.NSBundle
import platform.UIKit.UIImage
import platform.UIKit.accessibilityValue

public fun NSBundle.Companion.bundleWithName(name: String): NSBundle {
    val url = mainBundle.URLForResource(name = name, withExtension = "bundle", subdirectory = "libres-bundles") ?: error("$name.bundle not found")
    return bundleWithURL(url = url) ?: error("Cannot access $name.bundle")
}

public fun NSBundle.image(name: String, extension: String): UIImage {
    val image = UIImage.imageNamed(name = name, inBundle = this, withConfiguration = null) ?: error("Image $name not found")
    image.accessibilityValue = "$name.$extension"
    return image
}