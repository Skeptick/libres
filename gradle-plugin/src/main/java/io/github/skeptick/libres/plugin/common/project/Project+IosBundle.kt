package io.github.skeptick.libres.plugin.common.project

import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

private const val appleBundlesDirectory = "generated/libres/apple/libres-bundles"

internal fun Project.appendAppleBundles(currentResources: String): String {
    File(buildDirectory, appleBundlesDirectory).mkdirs()
    val path = "'${buildDirectory.relativeTo(projectDir).path}/$appleBundlesDirectory'"
    return if (currentResources.isBlank() || currentResources.trim().matches(Regex("^\\[\\s*]$"))) {
        "[$path]"
    } else {
        val existPaths = currentResources.substringAfter('[').substringBeforeLast(']')
        "[$existPaths, $path]"
    }
}

internal fun createBundlesSymLinks(umbrella: Project, exports: List<Project>) {
    val umbrellaBundleName = umbrella.appleBundleName
    val bundlesDir = Path(umbrella.buildDirectory.absolutePath, appleBundlesDirectory)
    val umbrellaBundle = Path(bundlesDir.pathString, "${umbrellaBundleName}.bundle")
    if (!Files.exists(umbrellaBundle)) Files.createDirectories(umbrellaBundle)
    exports.forEach { addSymlinkToBundle(bundlesDir = bundlesDir, export = it) }
}

internal val Project.appleBundleName: String
    get() = "Libres" + path.split(":", "-", "_").joinToString("") { partOfName ->
        partOfName.replaceFirstChar { it.titlecase() }
    }

private fun addSymlinkToBundle(bundlesDir: Path, export: Project) {
    val bundleName = export.appleBundleName
    val target = Path(export.buildDirectory.absolutePath, appleBundlesDirectory, "${bundleName}.bundle")
    val link = Path(bundlesDir.pathString, "${bundleName}.bundle")
    if (!Files.exists(target)) Files.createDirectories(target)
    if (!Files.exists(link)) Files.createSymbolicLink(link, target)
}

private val Project.buildDirectory: File get() = layout.buildDirectory.asFile.get()