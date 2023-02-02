package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.strings.hasJavaArguments
import org.junit.Test

class HasJavaArgumentsTest {

    /* region 'true' cases */

    @Test
    fun `true with percent symbol for formatting`() {
        val string = "Скидки до %d от поставщиков на бренды для профи"
        assert(string.hasJavaArguments())
    }
    @Test
    fun `true with percent symbol for formatting and double-percent`() {
        val string = "Скидки до 50%% от поставщиков на бренды для %s"
        assert(string.hasJavaArguments())
    }

    @Test
    fun `true with percent symbol for formatting (numbered) - case 1`() {
        val string = "Hello, %1\$s! You have %2\$d new messages."
        assert(string.hasJavaArguments())
    }

    @Test
    fun `true with percent symbol for formatting (numbered) - case 2 `() {
        val string = "Hello, %10\$s!"
        assert(string.hasJavaArguments())
    }

    /* endregion */

    /* region 'false' cases */

    @Test
    fun `false with single percent symbol`() {
        val string = "Скидки до 50% от поставщиков на бренды для профи"
        assert(!string.hasJavaArguments())
    }

    @Test
    fun `false with double-percent`() {
        val string = "Скидки до 50%% от поставщиков на бренды для профи"
        assert(!string.hasJavaArguments())
    }

    @Test
    fun `false without both double-percent and single percent symbol`() {
        val string = "Скидки до 50 процентов от поставщиков на бренды для профи"
        assert(!string.hasJavaArguments())
    }

    @Test
    fun `false with percent symbol and next number symbol`() {
        val string = "Hello, %1"
        assert(!string.hasJavaArguments())
    }

    /* endregion */
}