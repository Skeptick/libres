package io.github.skeptick.libres.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertTrue

class LibresStringGenerationTaskTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `ensure ResStrings classes are built`() {
        val project = ProjectBuilder.builder().withProjectDir(temporaryFolder.newFolder()).build()
        val task = project.tasks.register("generateStrings", LibresStringGenerationTask::class.java).get()

        val stringsFile = javaClass.classLoader.getResource("strings_en.xml")
        val stringArraysFile = javaClass.classLoader.getResource("string-arrays_en.xml")
        val pluralsFile = javaClass.classLoader.getResource("plurals_en.xml")

        val outputDir = temporaryFolder.newFolder("generated")
        val packagePath = "com/example"

        task.apply {
            outputPackageName.set("com.example")
            outputClassName.set("Res")
            baseLocaleLanguageCode.set("en")
            generateNamedArguments.set(false)
            camelCaseNamesForAppleFramework.set(false)
            inputFiles.from(stringsFile, stringArraysFile, pluralsFile)
            outputDirectory.set(outputDir)
        }

        task.apply()

        val resStrings = File(outputDir, "$packagePath/ResStrings.kt")
        assertTrue(resStrings.exists(), "Generated file should exist at ${resStrings.absolutePath}")
        checkContent(resStrings.readText(), "object ResStrings")

        val strings = File(outputDir, "$packagePath/Strings.kt")
        assertTrue(strings.exists(), "Generated file should exist at ${strings.absolutePath}")
        checkContent(strings.readText(), "interface Strings")

        val stringsEn = File(outputDir, "$packagePath/StringsEn.kt")
        assertTrue(stringsEn.exists(), "Generated file should exist at ${stringsEn.absolutePath}")
        val content = stringsEn.readText()
        checkContent(content, "object StringsEn")
        assertTrue(content.contains($$"listOf(io.github.skeptick.libres.strings.VoidFormattedString(\"Reports of %1${'$'}s in the %2${'$'}s System.\"), io.github.skeptick.libres.strings.VoidFormattedString(\"News of %1${'$'}s in the %2${'$'}s System.\"), io.github.skeptick.libres.strings.VoidFormattedString(\"New Rumors of %1${'$'}s in the %2${'$'}s System.\"), io.github.skeptick.libres.strings.VoidFormattedString(\"Sources say %1${'$'}s in the %2${'$'}s System.\"), io.github.skeptick.libres.strings.VoidFormattedString(\"Notice: %1${'$'}s in the %2${'$'}s System.\"), io.github.skeptick.libres.strings.VoidFormattedString(\"Evidence Suggests %1${'$'}s in the %2${'$'}s System.\"), )"))
    }

    fun checkContent(content: String, typeDeclaration: String) {
        assertTrue(content.contains(typeDeclaration), "Generated file should contain '$typeDeclaration'; Actual content:\n $content")
        assertTrue(content.contains("val generic_yes: String"), "Generated file should contain 'generic_yes'; Actual content:\n $content")
        assertTrue(content.contains("val generic_no: String"), "Generated file should contain 'generic_no'; Actual content:\n $content")
        assertTrue(content.contains("val hello: VoidFormattedString"), "Generated file should contain 'hello'; Actual content:\n $content")
        assertTrue(content.contains("val headline_capitalist: List<String>"), "Generated file should contain 'headline_capitalist'; Actual content:\n $content")
        assertTrue(content.contains("val headline_communist: List<VoidFormattedString>"), "Generated file should contain 'headline_communist'; Actual content:\n $content")
        assertTrue(content.contains("val headline_remote: List<VoidFormattedString>"), "Generated file should contain 'headline_communist'; Actual content:\n $content")
        assertTrue(content.contains("val format_days: VoidFormattedPluralString"), "Generated file should contain 'format_days'; Actual content:\n $content")
        assertTrue(content.contains("val screen_status_cargo_tribbles: VoidFormattedPluralString"), "Generated file should contain 'screen_status_cargo_tribbles'; Actual content:\n $content")
    }
}
