@file:Suppress("FunctionName")

package io.github.skeptick.libres.plurals.plugin.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plurals.plugin.extensions.addStatements

private val EmptyRules = mapOf(PluralForm.Other to emptyList<Conjunction>())

private const val True = true.toString()

private const val False = false.toString()

internal fun IntPluralRulesFile(
    packageName: String,
    rules: Map<String, Map<PluralForm, Dnf>>
) =
    FileSpec
        .builder(packageName, "IntPluralRules")
        .addKotlinDefaultImports()
        .indent(" ".repeat(4))
        .addAnnotation(
            AnnotationSpec.builder(Suppress::class)
                .addMember("\"unused\"")
                .addMember("\"IntroduceWhenSubject\"")
                .build()
        )
        .addType(
            TypeSpec
                .objectBuilder("IntPluralRules")
                .addModifiers(KModifier.PUBLIC)
                .addFunction(
                    FunSpec
                        .builder("getPluralForm")
                        .addModifiers(KModifier.PUBLIC)
                        .returns(PluralForm::class)
                        .addParameter("languageCode", String::class)
                        .addParameter("number", Int::class)
                        .beginControlFlow("return when (languageCode)")
                        .addStatements(rules.keys.map(String::toLanguageCallStatement))
                        .addStatement($$"else -> throw IllegalArgumentException(\"Unsupported language: $languageCode\")")
                        .endControlFlow()
                        .build()
                )
                .addFunctions(
                    rules.map { (languageCode, rulesByForm) ->
                        val funName = getRuleFunNameByLanguageCode(languageCode)
                        if (rulesByForm == EmptyRules) {
                            FunSpec
                                .builder(funName)
                                .addModifiers(KModifier.PRIVATE)
                                .returns(PluralForm::class)
                                .addParameter("number", Int::class)
                                .addStatement("return PluralForm.Other")
                                .build()
                        } else {
                            FunSpec
                                .builder(funName)
                                .addModifiers(KModifier.PRIVATE)
                                .returns(PluralForm::class)
                                .addParameter("number", Int::class)
                                .addStatement("val abs = kotlin.math.abs(number)")
                                .addStatements(rulesByForm.toModStatements())
                                .beginControlFlow("return when")
                                .addStatements(rulesByForm.toWhenBranches())
                                .endControlFlow()
                                .build()
                        }
                    }
                ).build()
        ).build()

/**
 * Simplifies DNF for integers by dropping out always positive and always negative conditions
 */
internal fun Dnf.simplifyForIntegers(): Dnf =
    mapNotNull conjunctionLoop@{ conjunction ->
        conjunction.mapNotNull relationLoop@{ relation ->
            val containsZero = relation.ranges.any { 0 in it }
            when (relation.varName) {
                'i' -> relation // 'i' - integer part of number
                'n' -> relation.copy(varName = 'i') // 'n' - number as is, always integer in our case
                'v', 'f', 't', 'w', 'e' -> when { // only for floating point numbers
                    relation.negated && containsZero -> return@conjunctionLoop null // "v != 0" always false for int, drop conjunction
                    !relation.negated && !containsZero -> return@conjunctionLoop null // "v = 1" always false for int, drop conjunction
                    else -> return@relationLoop null // "v = 0" or "v != 1" always true for int, drop relation
                }
                else -> error("Unknown variable: ${relation.varName}")
            }
        }
    }

/*
 * "ru" -> getRuPluralForm(number)
 */
private fun String.toLanguageCallStatement(): String =
    "\"$this\" -> ${getRuleFunNameByLanguageCode(this)}(number)"

/*
 * val mod10 = abs % 10
 * val mod100 = abs % 100
 */
private fun Map<PluralForm, Dnf>.toModStatements(): List<String> =
    values.flatten().flatten().mapNotNull(Relation::mod).toSortedSet().map { "val mod$it = abs %% $it" }

/*
 * mod10 == 1
 */
private fun Relation.toKotlinExpression(): String {
    val prefix = if (mod == null) "abs" else "mod$mod"
    return if (negated) {
        ranges.joinToString(separator = " && ") { range ->
            if (range.first == range.last) "$prefix != ${range.first}" else "$prefix !in ${range.first}..${range.last}"
        }
    } else {
        ranges.joinToString(separator = " || ") { range ->
            if (range.first == range.last) "$prefix == ${range.first}" else "$prefix in ${range.first}..${range.last}"
        }
    }
}

/*
 * mod10 == 1 && mod100 != 11
 */
@JvmName("conjunctionToKotlinExpression")
private fun Conjunction.toKotlinExpression(): String {
    return when {
        isEmpty() -> error("Conjunction cannot be empty")
        all { it.ranges.size == 1 } -> joinToString(separator = " && ") { it.toKotlinExpression() }
        else -> joinToString(separator = " && ") { "(${it.toKotlinExpression()})" }
    }
}

/*
 * mod10 == 0 || mod10 in 5..9 || mod100 in 11..14
 */
@JvmName("dnfToKotlinExpression")
private fun Dnf.toKotlinExpression(): String {
    return when {
        isEmpty() -> return False
        any { it.isEmpty() } -> return True
        size == 1 -> this[0].toKotlinExpression()
        all { it.size == 1 } -> joinToString(separator = " || ") { it.toKotlinExpression() }
        else -> joinToString(separator = " || ") { "(${it.toKotlinExpression()})" }
    }
}

/**
 * mod10 == 1 && mod100 != 11 -> PluralForm.One
 * mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
 * mod10 == 0 || mod10 in 5..9 || mod100 in 11..14 -> PluralForm.Many
 * else -> PluralForm.Other
 */
private fun Map<PluralForm, Dnf>.toWhenBranches(): List<String> =
    buildList {
        for (form in PluralForm.entries) {
            val cond = this@toWhenBranches[form]?.toKotlinExpression() ?: continue
            this += when (cond) {
                False -> continue
                True -> "else -> PluralForm.$form"
                else -> "$cond -> PluralForm.$form"
            }
            if (cond == True) return@buildList
        }
        this += "else -> PluralForm.Other"
    }

/*
 * getRuPluralForm
 */
private fun getRuleFunNameByLanguageCode(languageCode: String): String =
    "get${languageCode.replaceFirstChar { it.uppercase() }}PluralForm"