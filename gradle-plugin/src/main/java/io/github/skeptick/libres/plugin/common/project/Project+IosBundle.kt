package io.github.skeptick.libres.plugin.common.project

import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

internal const val appleBundlesDirectory = "generated/libres/apple/libres-bundles"

internal fun Project.appendAppleBundles(currentResources: String): String {
    File(buildDir, appleBundlesDirectory).mkdirs()
    val path = "'${buildDir.relativeTo(projectDir).path}/$appleBundlesDirectory'"
    if (currentResources.isBlank() || currentResources.trim().matches(Regex("^\\[\\s*]$"))) return "[$path]"
    val existPaths = currentResources.substringAfter('[').substringBeforeLast(']')
    return "[$existPaths, $path]"
}

internal fun createBundlesSymLinks(rootProject: Project, exportedProjects: List<Project>) {
    val rootBundlesDir = Path(rootProject.buildDir.absolutePath, appleBundlesDirectory)
    val rootBundleName = rootProject.appleBundleName
    val rootBundle = Path(rootProject.buildDir.absolutePath, appleBundlesDirectory, "${rootBundleName}.bundle")
    if (!Files.exists(rootBundle)) Files.createDirectories(rootBundle)
    exportedProjects.forEach { rootBundlesDir.addSymlinkToBundle(it) }
}

internal val Project.appleBundleName: String
    get() = "Libres" + path.split(":", "-", "_").joinToString("") { partOfName ->
        partOfName.replaceFirstChar { it.titlecase() }
    }

private fun Path.addSymlinkToBundle(project: Project) {
    val bundleName = project.appleBundleName
    val target = Path(project.buildDir.absolutePath, appleBundlesDirectory, "${bundleName}.bundle")
    val link = Path(pathString, "${bundleName}.bundle")
    if (!Files.exists(target)) Files.createDirectories(target)
    if (!Files.exists(link)) Files.createSymbolicLink(link, target)
}