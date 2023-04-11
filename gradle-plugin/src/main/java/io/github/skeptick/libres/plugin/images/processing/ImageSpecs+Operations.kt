package io.github.skeptick.libres.plugin.images.processing

import com.android.ide.common.vectordrawable.Svg2Vector
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.bytedeco.opencv.global.opencv_imgcodecs
import org.bytedeco.opencv.global.opencv_imgproc
import org.bytedeco.opencv.opencv_core.Mat
import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.images.models.*
import io.github.skeptick.libres.plugin.images.models.ImageScale
import io.github.skeptick.libres.plugin.images.models.ImageProps
import io.github.skeptick.libres.plugin.images.models.androidName
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

private val jsonWriter = jacksonObjectMapper().let {
    it.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    it.writer(DefaultPrettyPrinter().apply {
        indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
    })
}

internal fun ImageProps.saveImage(directories: Map<KotlinPlatform, File>) {
    directories[KotlinPlatform.Apple]?.let {
        saveImageSetContents(it)
    }

    if (targetSize == null) {
        saveOriginal(directories)
    } else {
        val src = opencv_imgcodecs.imread(file.absolutePath, opencv_imgcodecs.IMREAD_UNCHANGED)
        for (scale in ImageScale.values()) {
            if (!directories.any { it.key in scale.supportedPlatforms }) continue
            val newSideSize = targetSize * scale
            when {
                src.size().isLessThan(newSideSize) -> error("Image '$name' has too low resolution")
                src.size().isEqual(newSideSize) -> saveOriginal(scale, directories)
                else -> resizeAndSave(src, scale, targetSize, directories)
            }
        }
    }
}

internal fun ImageProps.removeImage(directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        when {
            platform == KotlinPlatform.Common -> continue
            platform == KotlinPlatform.Apple -> File(directory, "$name.imageset").deleteRecursively()
            platform == KotlinPlatform.Android && targetSize != null -> ImageScale.values().forEach {
                File(directory, targetFilePath(platform, it)).delete()
            }
            else ->  File(directory, targetFilePath(platform)).delete()
        }
    }
}

private fun ImageProps.saveOriginal(directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        when {
            platform == KotlinPlatform.Common -> continue
            platform == KotlinPlatform.Android && isVector -> {
                val targetFile = File(directory, targetFilePath(platform))
                targetFile.parentFile.mkdirs()
                val output = FileOutputStream(targetFile)
                parseSvgToXml(file, output)
            }
            else -> {
                val targetFile = File(directory, targetFilePath(platform))
                targetFile.parentFile.mkdirs()
                file.copyTo(targetFile, overwrite = true)
            }
        }
    }
}

private fun ImageProps.saveOriginal(scale: ImageScale, directories: Map<KotlinPlatform, File>) {
    for ((platform, directory) in directories) {
        if (platform in scale.supportedPlatforms) {
            val targetFile = File(directory, targetFilePath(platform, scale))
            file.parentFile.mkdirs()
            file.copyTo(targetFile, overwrite = true)
        }
    }
}

private fun ImageProps.resizeAndSave(src: Mat, scale: ImageScale, size: Int, directories: Map<KotlinPlatform, File>) {
    val destinationImage = Mat()
    val newSize = calculateSize(src.size(), size * scale)
    opencv_imgproc.resize(src, destinationImage, newSize)

    for ((platform, directory) in directories) {
        if (platform in scale.supportedPlatforms) {
            val targetPath = directory.absolutePath + targetFilePath(platform, scale)
            File(targetPath).parentFile.mkdirs()
            opencv_imgcodecs.imwrite(targetPath, destinationImage)
        }
    }
}

private fun ImageProps.saveImageSetContents(directory: File) {
    val text = jsonWriter.writeValueAsString(toImageSetContents())
    val file = File(directory, "$name.imageset/Contents.json")
    file.parentFile.mkdirs()
    file.writeText(text)
}

private fun ImageProps.targetFilePath(platform: KotlinPlatform, scale: ImageScale? = null): String =
    when (platform) {
        KotlinPlatform.Android -> "/drawable-${scale?.androidName ?: "nodpi"}/$name.${if (isVector) "xml" else extension}"
        KotlinPlatform.Apple -> "/$name.imageset/$name${if (scale != null) "_${scale.name}" else ""}.$extension"
        KotlinPlatform.Jvm, KotlinPlatform.Js -> "/$name.$extension"
        KotlinPlatform.Common -> throw IllegalArgumentException()
    }

/**
 * Workaround for Svg2Vector::parseSvgToXml
 */
private fun parseSvgToXml(inputSvg: File, output: OutputStream) {
    val method = Svg2Vector::class.java.declaredMethods.first { it.name == "parseSvgToXml" }
    when (method.parameterTypes[0]) {
        File::class.java -> method(null, inputSvg, output)
        else -> method(null, inputSvg.toPath(), output)
    }
}