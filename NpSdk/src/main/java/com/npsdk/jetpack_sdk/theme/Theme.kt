package com.npsdk.jetpack_sdk.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.npsdk.R
import com.npsdk.module.NPayLibrary

private val DarkColorScheme = darkColorScheme(
	primary = Color.White,
	secondary = Color.White,
	tertiary = Color.White
)

private val LightColorScheme = lightColorScheme(
	primary = Color.White,
	secondary = Color.White,
	tertiary = Color.White

	/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable()
fun initColor() : Color {
	var colorMerchant : String? = NPayLibrary.getInstance()?.sdkConfig?.brandColor
	try {
		if (!colorMerchant.isNullOrBlank()) {
			val replaceColorWrong = "#"+colorMerchant.replace("#", "").replace(" ", "")
			val parseColor = android.graphics.Color.parseColor(replaceColorWrong)
			return Color(parseColor)
		}
	} catch (e: Exception) {
		return colorResource(R.color.green)
	}
	return colorResource(R.color.green)
}

@Composable
fun PaymentNinepayTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Dynamic color is available on Android 12+
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}

		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			var colorMerchant : String? = NPayLibrary.getInstance()?.sdkConfig?.brandColor
			try {
				if (!colorMerchant.isNullOrBlank()) {
					val replaceColorWrong = "#"+colorMerchant.replace("#", "").replace(" ", "")
					val parseColor = android.graphics.Color.parseColor(replaceColorWrong)
					window.statusBarColor = Color(parseColor).toArgb()
				} else {
					window.statusBarColor = Color(0xFF15AE62).toArgb()
				}
			} catch (e: Exception) {
				window.statusBarColor = Color(0xFF15AE62).toArgb()
			}
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}