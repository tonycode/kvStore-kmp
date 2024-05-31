package dev.tonycode.kmp.android.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


private const val GROUP_FONT_SCALES = "Font scales"
private const val GROUP_UI_MODE = "UI mode"
private const val GROUP_LOCALES = "Locales"


@Preview(
    name = "font-scale-1 Large font",
    group = GROUP_FONT_SCALES,
    fontScale = 1.5f,
)
@Preview(
    name = "font-scale-2 Small font",
    group = GROUP_FONT_SCALES,
    fontScale = 0.5f,
)
annotation class FontScalePreviews


@Preview(
    name = "ui-mode-1 Light mode",
    group = GROUP_UI_MODE,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
)
@Preview(
    name = "ui-mode-2 Dark mode",
    group = GROUP_UI_MODE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
)
annotation class LightDarkPreviews


@Preview(
    name = "Arabic (RTL)",
    group = GROUP_LOCALES,
    locale = "ar-rSA",
)
annotation class RtlLocalePreview


@Preview(  // RTL
    name = "a) Arabic (RTL)",
    group = GROUP_LOCALES,
    locale = "ar-rSA",
)
@Preview(  // Long-texts
    name = "b) Vietnamese",
    group = GROUP_LOCALES,
    locale = "vi",
)
@Preview(  // Glyphs
    name = "c) Japanese",
    group = GROUP_LOCALES,
    locale = "ja",
)
@Preview(
    name = "d) Hindi",
    group = GROUP_LOCALES,
    locale = "hi",
)
annotation class LocalePreviews


@Preview(
    name = "English",
    group = GROUP_LOCALES,
    locale = "en",
)
@Preview(
    name = "Arabic (RTL)",
    group = GROUP_LOCALES,
    locale = "ar-rSA",
)
@Preview(
    name = "Bengali",
    group = GROUP_LOCALES,
    locale = "bn",
)
@Preview(
    name = "Spanish",
    group = GROUP_LOCALES,
    locale = "es-rAR",
)
@Preview(
    name = "French",
    group = GROUP_LOCALES,
    locale = "fr",
)
@Preview(
    name = "Hindi",
    group = GROUP_LOCALES,
    locale = "hi",
)
@Preview(
    name = "Indonesian",
    group = GROUP_LOCALES,
    locale = "in",
)
@Preview(
    name = "Japanese",
    group = GROUP_LOCALES,
    locale = "ja",
)
@Preview(
    name = "Korean",
    group = GROUP_LOCALES,
    locale = "ko",
)
@Preview(
    name = "Portuguese (Brazil)",
    group = GROUP_LOCALES,
    locale = "pt-rBR",
)
@Preview(
    name = "Thai",
    group = GROUP_LOCALES,
    locale = "th",
)
@Preview(
    name = "Urdu",
    group = GROUP_LOCALES,
    locale = "ur-rPK",
)
@Preview(
    name = "Vietnamese",
    group = GROUP_LOCALES,
    locale = "vi",
)
@Preview(
    name = "Chinese",
    group = GROUP_LOCALES,
    locale = "zh-rCN",
)
annotation class AllLocalesPreviews
