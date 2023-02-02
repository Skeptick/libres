package io.github.skeptick.libres.plugin.common.extensions

import java.io.File

internal fun File.deleteFilesInDirectory() {
    listFiles()?.forEach {
        if (it.isFile) it.delete()
    }
}