package com.example.bookmarked_android.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.bookmarked_android.R

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val fontName = GoogleFont("JetBrains Mono")

val jetbrainsMonofontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = fontProvider),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.Thin),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.ExtraLight),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.Light),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.Normal),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.Medium),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.SemiBold),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.Bold),
    Font(googleFont = fontName, fontProvider = fontProvider, FontWeight.ExtraBold),
)