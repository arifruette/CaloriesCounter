package ru.ari.caloriescounter.feature.diary.presentation.common

import java.util.Locale

fun Double.formatRuDecimal(): String = String.format(Locale.forLanguageTag("ru-RU"), "%.1f", this)
