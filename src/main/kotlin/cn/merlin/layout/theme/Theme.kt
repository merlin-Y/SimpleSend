package cn.merlin.layout.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import cn.merlin.database.Data
import java.util.prefs.Preferences

private val LightColors = lightColors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryVariant = md_theme_light_primaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryVariant = md_theme_light_secondaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)


private val DarkColors = darkColors(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryVariant = md_theme_dark_primaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryVariant = md_theme_dark_secondaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
)

@Composable
fun MainTheme(
    data: Preferences = Data,
    content: @Composable ()->Unit
){
    val useDarkTheme: Boolean = data.getBoolean("useDarkTheme",false)


    MaterialTheme(
        colors = if(!useDarkTheme)  LightColors else DarkColors,
        content = content,
        shapes = Shapes
    )
}