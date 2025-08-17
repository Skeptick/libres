@file:Suppress("FunctionName")

package io.github.skeptick.libres.plurals.plugin.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plurals.plugin.extensions.addStatements

internal fun IntPluralRulesTestsFile(
    packageName: String,
    samples: Map<String, Map<PluralForm, List<IntRange>>>
) =
    FileSpec
        .builder(packageName, "IntPluralRulesTests")
        .addKotlinDefaultImports()
        .indent(" ".repeat(4))
        .addImport("kotlin.test", "Test", "assertEquals")
        .addType(
            TypeSpec
                .classBuilder("IntPluralRulesTests")
                .addFunctions(
                    samples.map { (languageCode, samplesByForm) ->
                        val testFunName = getTestFunNameByLanguageCode(languageCode)
                        FunSpec
                            .builder(testFunName)
                            .addAnnotation(ClassName("kotlin.test", "Test"))
                            .addStatements(samplesByForm.toAssertStatements(languageCode))
                            .build()
                    }
                ).build()
        ).build()

/*
 * assertEquals(PluralForm.One, IntPluralRules.getPluralForm("ru", 1))
 */
private fun Map<PluralForm, List<IntRange>>.toAssertStatements(languageCode: String): List<String> =
    entries.flatMap { (form, ranges) ->
        ranges.flatMap { range ->
            when (range.first) {
                range.last -> listOf(
                    "assertEquals(PluralForm.$form, IntPluralRules.getPluralForm(\"$languageCode\", ${range.first}))"
                )
                else -> range.map { number ->
                    "assertEquals(PluralForm.$form, IntPluralRules.getPluralForm(\"$languageCode\", $number))"
                }
            }
        }
    }

/*
 * testRuPluralForm
 */
private fun getTestFunNameByLanguageCode(languageCode: String): String =
    "test${languageCode.replaceFirstChar { it.uppercase() }}PluralForm"