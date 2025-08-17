@file:Suppress(
    "unused",
    "IntroduceWhenSubject",
)

package io.github.skeptick.libres.plurals

public object IntPluralRules {

    public fun getPluralForm(languageCode: String, number: Int): PluralForm = when (languageCode) {
        "af" -> getAfPluralForm(number)
        "ak" -> getAkPluralForm(number)
        "am" -> getAmPluralForm(number)
        "an" -> getAnPluralForm(number)
        "ar" -> getArPluralForm(number)
        "ars" -> getArsPluralForm(number)
        "as" -> getAsPluralForm(number)
        "asa" -> getAsaPluralForm(number)
        "ast" -> getAstPluralForm(number)
        "az" -> getAzPluralForm(number)
        "bal" -> getBalPluralForm(number)
        "be" -> getBePluralForm(number)
        "bem" -> getBemPluralForm(number)
        "bez" -> getBezPluralForm(number)
        "bg" -> getBgPluralForm(number)
        "bho" -> getBhoPluralForm(number)
        "blo" -> getBloPluralForm(number)
        "bm" -> getBmPluralForm(number)
        "bn" -> getBnPluralForm(number)
        "bo" -> getBoPluralForm(number)
        "br" -> getBrPluralForm(number)
        "brx" -> getBrxPluralForm(number)
        "bs" -> getBsPluralForm(number)
        "ca" -> getCaPluralForm(number)
        "ce" -> getCePluralForm(number)
        "ceb" -> getCebPluralForm(number)
        "cgg" -> getCggPluralForm(number)
        "chr" -> getChrPluralForm(number)
        "ckb" -> getCkbPluralForm(number)
        "cs" -> getCsPluralForm(number)
        "csw" -> getCswPluralForm(number)
        "cy" -> getCyPluralForm(number)
        "da" -> getDaPluralForm(number)
        "de" -> getDePluralForm(number)
        "doi" -> getDoiPluralForm(number)
        "dsb" -> getDsbPluralForm(number)
        "dv" -> getDvPluralForm(number)
        "dz" -> getDzPluralForm(number)
        "ee" -> getEePluralForm(number)
        "el" -> getElPluralForm(number)
        "en" -> getEnPluralForm(number)
        "eo" -> getEoPluralForm(number)
        "es" -> getEsPluralForm(number)
        "et" -> getEtPluralForm(number)
        "eu" -> getEuPluralForm(number)
        "fa" -> getFaPluralForm(number)
        "ff" -> getFfPluralForm(number)
        "fi" -> getFiPluralForm(number)
        "fil" -> getFilPluralForm(number)
        "fo" -> getFoPluralForm(number)
        "fr" -> getFrPluralForm(number)
        "fur" -> getFurPluralForm(number)
        "fy" -> getFyPluralForm(number)
        "ga" -> getGaPluralForm(number)
        "gd" -> getGdPluralForm(number)
        "gl" -> getGlPluralForm(number)
        "gsw" -> getGswPluralForm(number)
        "gu" -> getGuPluralForm(number)
        "guw" -> getGuwPluralForm(number)
        "gv" -> getGvPluralForm(number)
        "ha" -> getHaPluralForm(number)
        "haw" -> getHawPluralForm(number)
        "he" -> getHePluralForm(number)
        "hi" -> getHiPluralForm(number)
        "hnj" -> getHnjPluralForm(number)
        "hr" -> getHrPluralForm(number)
        "hsb" -> getHsbPluralForm(number)
        "hu" -> getHuPluralForm(number)
        "hy" -> getHyPluralForm(number)
        "ia" -> getIaPluralForm(number)
        "id" -> getIdPluralForm(number)
        "ig" -> getIgPluralForm(number)
        "ii" -> getIiPluralForm(number)
        "io" -> getIoPluralForm(number)
        "is" -> getIsPluralForm(number)
        "it" -> getItPluralForm(number)
        "iu" -> getIuPluralForm(number)
        "ja" -> getJaPluralForm(number)
        "jbo" -> getJboPluralForm(number)
        "jgo" -> getJgoPluralForm(number)
        "jmc" -> getJmcPluralForm(number)
        "jv" -> getJvPluralForm(number)
        "jw" -> getJwPluralForm(number)
        "ka" -> getKaPluralForm(number)
        "kab" -> getKabPluralForm(number)
        "kaj" -> getKajPluralForm(number)
        "kcg" -> getKcgPluralForm(number)
        "kde" -> getKdePluralForm(number)
        "kea" -> getKeaPluralForm(number)
        "kk" -> getKkPluralForm(number)
        "kkj" -> getKkjPluralForm(number)
        "kl" -> getKlPluralForm(number)
        "km" -> getKmPluralForm(number)
        "kn" -> getKnPluralForm(number)
        "ko" -> getKoPluralForm(number)
        "ks" -> getKsPluralForm(number)
        "ksb" -> getKsbPluralForm(number)
        "ksh" -> getKshPluralForm(number)
        "ku" -> getKuPluralForm(number)
        "kw" -> getKwPluralForm(number)
        "ky" -> getKyPluralForm(number)
        "lag" -> getLagPluralForm(number)
        "lb" -> getLbPluralForm(number)
        "lg" -> getLgPluralForm(number)
        "lij" -> getLijPluralForm(number)
        "lkt" -> getLktPluralForm(number)
        "lld" -> getLldPluralForm(number)
        "ln" -> getLnPluralForm(number)
        "lo" -> getLoPluralForm(number)
        "lt" -> getLtPluralForm(number)
        "lv" -> getLvPluralForm(number)
        "mas" -> getMasPluralForm(number)
        "mg" -> getMgPluralForm(number)
        "mgo" -> getMgoPluralForm(number)
        "mk" -> getMkPluralForm(number)
        "ml" -> getMlPluralForm(number)
        "mn" -> getMnPluralForm(number)
        "mo" -> getMoPluralForm(number)
        "mr" -> getMrPluralForm(number)
        "ms" -> getMsPluralForm(number)
        "mt" -> getMtPluralForm(number)
        "my" -> getMyPluralForm(number)
        "nah" -> getNahPluralForm(number)
        "naq" -> getNaqPluralForm(number)
        "nb" -> getNbPluralForm(number)
        "nd" -> getNdPluralForm(number)
        "ne" -> getNePluralForm(number)
        "nl" -> getNlPluralForm(number)
        "nn" -> getNnPluralForm(number)
        "nnh" -> getNnhPluralForm(number)
        "no" -> getNoPluralForm(number)
        "nqo" -> getNqoPluralForm(number)
        "nr" -> getNrPluralForm(number)
        "nso" -> getNsoPluralForm(number)
        "ny" -> getNyPluralForm(number)
        "nyn" -> getNynPluralForm(number)
        "om" -> getOmPluralForm(number)
        "or" -> getOrPluralForm(number)
        "os" -> getOsPluralForm(number)
        "osa" -> getOsaPluralForm(number)
        "pa" -> getPaPluralForm(number)
        "pap" -> getPapPluralForm(number)
        "pcm" -> getPcmPluralForm(number)
        "pl" -> getPlPluralForm(number)
        "prg" -> getPrgPluralForm(number)
        "ps" -> getPsPluralForm(number)
        "pt" -> getPtPluralForm(number)
        "rm" -> getRmPluralForm(number)
        "ro" -> getRoPluralForm(number)
        "rof" -> getRofPluralForm(number)
        "ru" -> getRuPluralForm(number)
        "rwk" -> getRwkPluralForm(number)
        "sah" -> getSahPluralForm(number)
        "saq" -> getSaqPluralForm(number)
        "sat" -> getSatPluralForm(number)
        "sc" -> getScPluralForm(number)
        "scn" -> getScnPluralForm(number)
        "sd" -> getSdPluralForm(number)
        "sdh" -> getSdhPluralForm(number)
        "se" -> getSePluralForm(number)
        "seh" -> getSehPluralForm(number)
        "ses" -> getSesPluralForm(number)
        "sg" -> getSgPluralForm(number)
        "sh" -> getShPluralForm(number)
        "shi" -> getShiPluralForm(number)
        "si" -> getSiPluralForm(number)
        "sk" -> getSkPluralForm(number)
        "sl" -> getSlPluralForm(number)
        "sma" -> getSmaPluralForm(number)
        "smi" -> getSmiPluralForm(number)
        "smj" -> getSmjPluralForm(number)
        "smn" -> getSmnPluralForm(number)
        "sms" -> getSmsPluralForm(number)
        "sn" -> getSnPluralForm(number)
        "so" -> getSoPluralForm(number)
        "sq" -> getSqPluralForm(number)
        "sr" -> getSrPluralForm(number)
        "ss" -> getSsPluralForm(number)
        "ssy" -> getSsyPluralForm(number)
        "st" -> getStPluralForm(number)
        "su" -> getSuPluralForm(number)
        "sv" -> getSvPluralForm(number)
        "sw" -> getSwPluralForm(number)
        "syr" -> getSyrPluralForm(number)
        "ta" -> getTaPluralForm(number)
        "te" -> getTePluralForm(number)
        "teo" -> getTeoPluralForm(number)
        "th" -> getThPluralForm(number)
        "ti" -> getTiPluralForm(number)
        "tig" -> getTigPluralForm(number)
        "tk" -> getTkPluralForm(number)
        "tl" -> getTlPluralForm(number)
        "tn" -> getTnPluralForm(number)
        "to" -> getToPluralForm(number)
        "tpi" -> getTpiPluralForm(number)
        "tr" -> getTrPluralForm(number)
        "ts" -> getTsPluralForm(number)
        "tzm" -> getTzmPluralForm(number)
        "ug" -> getUgPluralForm(number)
        "uk" -> getUkPluralForm(number)
        "und" -> getUndPluralForm(number)
        "ur" -> getUrPluralForm(number)
        "uz" -> getUzPluralForm(number)
        "ve" -> getVePluralForm(number)
        "vec" -> getVecPluralForm(number)
        "vi" -> getViPluralForm(number)
        "vo" -> getVoPluralForm(number)
        "vun" -> getVunPluralForm(number)
        "wa" -> getWaPluralForm(number)
        "wae" -> getWaePluralForm(number)
        "wo" -> getWoPluralForm(number)
        "xh" -> getXhPluralForm(number)
        "xog" -> getXogPluralForm(number)
        "yi" -> getYiPluralForm(number)
        "yo" -> getYoPluralForm(number)
        "yue" -> getYuePluralForm(number)
        "zh" -> getZhPluralForm(number)
        "zu" -> getZuPluralForm(number)
        else -> throw IllegalArgumentException("Unsupported language: $languageCode")
    }

    private fun getAfPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getAkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getAmPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getAnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getArPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            mod100 in 3..10 -> PluralForm.Few
            mod100 in 11..99 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getArsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            mod100 in 3..10 -> PluralForm.Few
            mod100 in 11..99 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getAsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getAsaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getAstPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getAzPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBalPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            mod10 == 0 || mod10 in 5..9 || mod100 in 11..14 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getBemPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBezPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBgPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBhoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBloPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBmPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getBnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBoPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getBrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        val mod1000000 = abs % 1000000
        return when {
            mod10 == 1 && mod100 != 11 && mod100 != 71 && mod100 != 91 -> PluralForm.One
            mod10 == 2 && mod100 != 12 && mod100 != 72 && mod100 != 92 -> PluralForm.Two
            (mod10 in 3..4 || mod10 == 9) && mod100 !in 10..19 && mod100 !in 70..79 && mod100 !in 90..99 -> PluralForm.Few
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getBrxPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getBsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getCaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs == 1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getCePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getCebPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        return when {
            abs in 1..3 || (mod10 != 4 && mod10 != 6 && mod10 != 9) -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getCggPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getChrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getCkbPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getCsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs in 2..4 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getCswPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getCyPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            abs == 3 -> PluralForm.Few
            abs == 6 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getDaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getDePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getDoiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getDsbPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            mod100 == 1 -> PluralForm.One
            mod100 == 2 -> PluralForm.Two
            mod100 in 3..4 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getDvPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getDzPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getEePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getElPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getEnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getEoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getEsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs == 1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getEtPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getEuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFfPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFilPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        return when {
            abs in 1..3 || (mod10 != 4 && mod10 != 6 && mod10 != 9) -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs in 0..1 -> PluralForm.One
            mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getFurPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getFyPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getGaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            abs in 3..6 -> PluralForm.Few
            abs in 7..10 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getGdPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 || abs == 11 -> PluralForm.One
            abs == 2 || abs == 12 -> PluralForm.Two
            abs in 3..10 || abs in 13..19 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getGlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getGswPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getGuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getGuwPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getGvPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 -> PluralForm.One
            mod10 == 2 -> PluralForm.Two
            mod100 == 0 || mod100 == 20 || mod100 == 40 || mod100 == 60 || mod100 == 80 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getHaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getHawPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getHePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getHiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getHnjPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getHrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getHsbPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            mod100 == 1 -> PluralForm.One
            mod100 == 2 -> PluralForm.Two
            mod100 in 3..4 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getHuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getHyPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getIaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getIdPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getIgPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getIiPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getIoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getIsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getItPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs == 1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getIuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getJaPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getJboPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getJgoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getJmcPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getJvPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getJwPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getKaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKabPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKajPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKcgPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKdePluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getKeaPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getKkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKkjPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKmPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getKnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKoPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getKsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKsbPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKshPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getKwPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        val mod1000 = abs % 1000
        val mod100000 = abs % 100000
        val mod1000000 = abs % 1000000
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            mod100 == 2 || mod100 == 22 || mod100 == 42 || mod100 == 62 || mod100 == 82 || (mod1000 == 0 && (mod100000 in 1000..20000 || mod100000 == 40000 || mod100000 == 60000 || mod100000 == 80000)) || mod1000000 == 100000 -> PluralForm.Two
            mod100 == 3 || mod100 == 23 || mod100 == 43 || mod100 == 63 || mod100 == 83 -> PluralForm.Few
            mod100 == 1 || mod100 == 21 || mod100 == 41 || mod100 == 61 || mod100 == 81 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getKyPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getLagPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 0 -> PluralForm.Zero
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getLbPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getLgPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getLijPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getLktPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getLldPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs == 1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getLnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getLoPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getLtPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 !in 11..19 -> PluralForm.One
            mod10 in 2..9 && mod100 !in 11..19 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getLvPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 0 || mod100 in 11..19 -> PluralForm.Zero
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMasPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMgPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMgoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            abs == 1 -> PluralForm.One
            abs == 0 || mod100 in 1..19 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getMrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getMsPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getMtPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            abs == 0 || mod100 in 3..10 -> PluralForm.Few
            mod100 in 11..19 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getMyPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getNahPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNaqPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getNbPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNdPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNnhPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNqoPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getNrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNsoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNyPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getNynPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getOmPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getOrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getOsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getOsaPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getPaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getPapPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getPcmPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getPlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            abs == 1 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            mod10 in 0..1 || mod10 in 5..9 || mod100 in 12..14 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getPrgPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 0 || mod100 in 11..19 -> PluralForm.Zero
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getPsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getPtPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs in 0..1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getRmPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getRoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            abs == 1 -> PluralForm.One
            abs == 0 || mod100 in 1..19 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getRofPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getRuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            mod10 == 0 || mod10 in 5..9 || mod100 in 11..14 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getRwkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSahPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getSaqPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSatPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getScPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getScnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs == 1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getSdPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSdhPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getSehPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSesPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getSgPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getShPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getShiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            abs in 2..10 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getSiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs in 2..4 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getSlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod100 = abs % 100
        return when {
            mod100 == 1 -> PluralForm.One
            mod100 == 2 -> PluralForm.Two
            mod100 in 3..4 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getSmaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getSmiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getSmjPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getSmnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getSmsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            abs == 2 -> PluralForm.Two
            else -> PluralForm.Other
        }
    }

    private fun getSnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSqPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            else -> PluralForm.Other
        }
    }

    private fun getSsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSsyPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getStPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSuPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getSvPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSwPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getSyrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTeoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getThPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getTiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTigPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTlPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        return when {
            abs in 1..3 || (mod10 != 4 && mod10 != 6 && mod10 != 9) -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTnPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getToPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getTpiPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getTrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTsPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getTzmPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 || abs in 11..99 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getUgPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getUkPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod10 = abs % 10
        val mod100 = abs % 100
        return when {
            mod10 == 1 && mod100 != 11 -> PluralForm.One
            mod10 in 2..4 && mod100 !in 12..14 -> PluralForm.Few
            mod10 == 0 || mod10 in 5..9 || mod100 in 11..14 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getUndPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getUrPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getUzPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getVePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getVecPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        val mod1000000 = abs % 1000000
        return when {
            abs == 1 -> PluralForm.One
            abs != 0 && mod1000000 == 0 -> PluralForm.Many
            else -> PluralForm.Other
        }
    }

    private fun getViPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getVoPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getVunPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getWaPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getWaePluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getWoPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getXhPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getXogPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getYiPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs == 1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

    private fun getYoPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getYuePluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getZhPluralForm(number: Int): PluralForm = PluralForm.Other

    private fun getZuPluralForm(number: Int): PluralForm {
        val abs = kotlin.math.abs(number)
        return when {
            abs in 0..1 -> PluralForm.One
            else -> PluralForm.Other
        }
    }

}
