package io.github.skeptick.libres.plugin.common.extensions

import org.gradle.api.file.Directory

internal fun Directory.deleteFiles() {
    asFile.listFiles().forEach {
        if (it.isFile) it.delete()
    }
}