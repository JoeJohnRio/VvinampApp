@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package vvinampapp.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainFont0 {
  public val circular_std_black: FontResource by 
      lazy { init_circular_std_black() }

  public val circular_std_bold: FontResource by 
      lazy { init_circular_std_bold() }

  public val circular_std_light: FontResource by 
      lazy { init_circular_std_light() }

  public val circular_std_medium: FontResource by 
      lazy { init_circular_std_medium() }

  public val gotham_bold: FontResource by 
      lazy { init_gotham_bold() }

  public val gotham_light: FontResource by 
      lazy { init_gotham_light() }

  public val gotham_medium: FontResource by 
      lazy { init_gotham_medium() }

  public val gotham_regular: FontResource by 
      lazy { init_gotham_regular() }
}

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("circular_std_black", CommonMainFont0.circular_std_black)
  map.put("circular_std_bold", CommonMainFont0.circular_std_bold)
  map.put("circular_std_light", CommonMainFont0.circular_std_light)
  map.put("circular_std_medium", CommonMainFont0.circular_std_medium)
  map.put("gotham_bold", CommonMainFont0.gotham_bold)
  map.put("gotham_light", CommonMainFont0.gotham_light)
  map.put("gotham_medium", CommonMainFont0.gotham_medium)
  map.put("gotham_regular", CommonMainFont0.gotham_regular)
}

internal val Res.font.circular_std_black: FontResource
  get() = CommonMainFont0.circular_std_black

private fun init_circular_std_black(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:circular_std_black",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/circular_std_black.otf", -1, -1),
    )
)

internal val Res.font.circular_std_bold: FontResource
  get() = CommonMainFont0.circular_std_bold

private fun init_circular_std_bold(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:circular_std_bold",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/circular_std_bold.otf", -1, -1),
    )
)

internal val Res.font.circular_std_light: FontResource
  get() = CommonMainFont0.circular_std_light

private fun init_circular_std_light(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:circular_std_light",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/circular_std_light.otf", -1, -1),
    )
)

internal val Res.font.circular_std_medium: FontResource
  get() = CommonMainFont0.circular_std_medium

private fun init_circular_std_medium(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:circular_std_medium",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/circular_std_medium.otf", -1, -1),
    )
)

internal val Res.font.gotham_bold: FontResource
  get() = CommonMainFont0.gotham_bold

private fun init_gotham_bold(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:gotham_bold",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/gotham_bold.ttf", -1, -1),
    )
)

internal val Res.font.gotham_light: FontResource
  get() = CommonMainFont0.gotham_light

private fun init_gotham_light(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:gotham_light",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/gotham_light.ttf", -1, -1),
    )
)

internal val Res.font.gotham_medium: FontResource
  get() = CommonMainFont0.gotham_medium

private fun init_gotham_medium(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:gotham_medium",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/gotham_medium.ttf", -1, -1),
    )
)

internal val Res.font.gotham_regular: FontResource
  get() = CommonMainFont0.gotham_regular

private fun init_gotham_regular(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:gotham_regular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/vvinampapp.composeapp.generated.resources/font/gotham_regular.otf", -1, -1),
    )
)
