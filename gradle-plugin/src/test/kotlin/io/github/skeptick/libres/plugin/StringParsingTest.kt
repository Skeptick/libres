package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.strings.parseStringResources
import org.junit.Test
import java.io.File

class StringParsingTest {
    @Test
    fun `ensure string can be parsed`() {
        val resources = parseStringResources(
            inputFiles = setOf(File(javaClass.classLoader.getResource("strings_en.xml")!!.toURI())),
            baseLocaleLanguageCode = "en"
        )
        assert(resources.values.first().size == 3)
    }

    @Test
    fun `ensure string-array can be parsed`() {
        val resources = parseStringResources(
            inputFiles = setOf(File(javaClass.classLoader.getResource("string-arrays_en.xml")!!.toURI())),
            baseLocaleLanguageCode = "en"
        )
        assert(resources.values.first().size == 3)
    }

    @Test
    fun `ensure plurals can be parsed`() {
        val resources = parseStringResources(
            inputFiles = setOf(File(javaClass.classLoader.getResource("plurals_en.xml")!!.toURI())),
            baseLocaleLanguageCode = "en"
        )
        assert(resources.values.first().size == 2)
    }
}