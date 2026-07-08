@file:OptIn(InternalResourceApi::class)

package vvinampapp.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceContentHash
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/vvinampapp.composeapp.generated.resources/"

@delegate:ResourceContentHash(1_466_424_049)
internal val Res.font.circular_std_black: FontResource by lazy {
      FontResource("font:circular_std_black", setOf(
        ResourceItem(setOf(), "${MD}font/circular_std_black.otf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_699_408_366)
internal val Res.font.circular_std_bold: FontResource by lazy {
      FontResource("font:circular_std_bold", setOf(
        ResourceItem(setOf(), "${MD}font/circular_std_bold.otf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-584_623_588)
internal val Res.font.circular_std_light: FontResource by lazy {
      FontResource("font:circular_std_light", setOf(
        ResourceItem(setOf(), "${MD}font/circular_std_light.otf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(839_902_046)
internal val Res.font.circular_std_medium: FontResource by lazy {
      FontResource("font:circular_std_medium", setOf(
        ResourceItem(setOf(), "${MD}font/circular_std_medium.otf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_065_325_489)
internal val Res.font.gotham_bold: FontResource by lazy {
      FontResource("font:gotham_bold", setOf(
        ResourceItem(setOf(), "${MD}font/gotham_bold.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(241_279_335)
internal val Res.font.gotham_light: FontResource by lazy {
      FontResource("font:gotham_light", setOf(
        ResourceItem(setOf(), "${MD}font/gotham_light.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_795_196_138)
internal val Res.font.gotham_medium: FontResource by lazy {
      FontResource("font:gotham_medium", setOf(
        ResourceItem(setOf(), "${MD}font/gotham_medium.ttf", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_731_408_076)
internal val Res.font.gotham_regular: FontResource by lazy {
      FontResource("font:gotham_regular", setOf(
        ResourceItem(setOf(), "${MD}font/gotham_regular.otf", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("circular_std_black", Res.font.circular_std_black)
  map.put("circular_std_bold", Res.font.circular_std_bold)
  map.put("circular_std_light", Res.font.circular_std_light)
  map.put("circular_std_medium", Res.font.circular_std_medium)
  map.put("gotham_bold", Res.font.gotham_bold)
  map.put("gotham_light", Res.font.gotham_light)
  map.put("gotham_medium", Res.font.gotham_medium)
  map.put("gotham_regular", Res.font.gotham_regular)
}
