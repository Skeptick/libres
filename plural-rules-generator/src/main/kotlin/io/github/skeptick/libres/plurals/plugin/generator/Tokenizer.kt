package io.github.skeptick.libres.plurals.plugin.generator

internal sealed interface IntToken

internal sealed interface ConditionToken

internal sealed interface Token {

    data object And : Token

    data object Or : Token

    data object Mod : Token

    data object Eq : Token, ConditionToken

    data object Neq : Token, ConditionToken

    @JvmInline
    value class Identifier(val char: Char) : Token

    @JvmInline
    value class IntLiteral(val value: Int) : Token, IntToken

    @JvmInline
    value class IntRange(val value: kotlin.ranges.IntRange) : Token, IntToken

    @JvmInline
    value class IntTokenList(val tokens: List<IntToken>) : Token, IntToken

}

internal class LexError(message: String) : RuntimeException(message)

private val IdentifierRegex = Regex("^[efintv]$")

private val IntLiteralRegex = Regex("^\\d+$")

private val IntRangeRegex = Regex("^\\d+\\.\\.\\d+$")

private val IntTokenListRegex = Regex("^(?:\\d+|\\d+\\.\\.\\d+)(?:,(?:\\d+|\\d+\\.\\.\\d+))+$")

internal fun tokenize(plurals: Plurals): Map<String, Map<PluralForm, List<Token>>> {
    return plurals.supplemental.cardinal.mapValues { (_, rules) ->
        buildMap {
            if (rules.zero != null) put(PluralForm.Zero, tokenize(rules.zero))
            if (rules.one != null) put(PluralForm.One, tokenize(rules.one))
            if (rules.two != null) put(PluralForm.Two, tokenize(rules.two))
            if (rules.few != null) put(PluralForm.Few, tokenize(rules.few))
            if (rules.many != null) put(PluralForm.Many, tokenize(rules.many))
            if (rules.other != null) put(PluralForm.Other, tokenize(rules.other))
        }
    }
}

private fun tokenize(input: String): List<Token> {
    return buildList {
        for (sentence in input.stringTokens) when {
            sentence == "and" -> this += Token.And
            sentence == "or" -> this += Token.Or
            sentence == "!=" -> this += Token.Neq
            sentence == "=" -> this += Token.Eq
            sentence == "%" -> this += Token.Mod
            IdentifierRegex.matches(sentence) -> this += Token.Identifier(sentence[0])
            IntLiteralRegex.matches(sentence) -> this += Token.IntLiteral(sentence.toInt())
            IntRangeRegex.matches(sentence) -> this += Token.IntRange(sentence.toIntRange())
            IntTokenListRegex.matches(sentence) -> this += Token.IntTokenList(sentence.toIntTokenList())
            else -> throw LexError("Unknown token: $sentence")
        }
    }
}

private val String.stringTokens: List<String>
    get() = substringBefore('@').trim().split(' ').filterNot(String::isBlank)

private fun String.toIntRange(): IntRange =
    substringBefore("..").toInt()..substringAfter("..").toInt()

private fun String.toIntTokenList(): List<IntToken> =
    split(',').map {
        when {
            it.matches(IntLiteralRegex) -> Token.IntLiteral(it.toInt())
            it.matches(IntRangeRegex) -> Token.IntRange(it.toIntRange())
            else -> throw LexError("Unknown token: $it")
        }
    }