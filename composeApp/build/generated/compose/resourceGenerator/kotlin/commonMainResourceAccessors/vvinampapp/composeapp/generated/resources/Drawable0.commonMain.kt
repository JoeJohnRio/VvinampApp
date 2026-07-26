@file:OptIn(InternalResourceApi::class)

package vvinampapp.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceContentHash
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/vvinampapp.composeapp.generated.resources/"

@delegate:ResourceContentHash(1_870_468_419)
public val Res.drawable.bg_image: DrawableResource by lazy {
      DrawableResource("drawable:bg_image", setOf(
        ResourceItem(setOf(), "${MD}drawable/bg_image.png", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_866_113_464)
public val Res.drawable.ic_facebook: DrawableResource by lazy {
      DrawableResource("drawable:ic_facebook", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_facebook.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_848_245_674)
public val Res.drawable.ic_google: DrawableResource by lazy {
      DrawableResource("drawable:ic_google", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_google.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(1_027_232_246)
public val Res.drawable.ic_new_v: DrawableResource by lazy {
      DrawableResource("drawable:ic_new_v", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_new_v.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-2_056_915_641)
public val Res.drawable.ic_venn: DrawableResource by lazy {
      DrawableResource("drawable:ic_venn", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_venn.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-859_902_916)
public val Res.drawable.ic_vvinamp: DrawableResource by lazy {
      DrawableResource("drawable:ic_vvinamp", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_vvinamp.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-1_523_157_055)
public val Res.drawable.ic_vvinamp_white: DrawableResource by lazy {
      DrawableResource("drawable:ic_vvinamp_white", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_vvinamp_white.xml", -1, -1),
      ))
    }

@delegate:ResourceContentHash(-102_701_439)
public val Res.drawable.ic_youtube: DrawableResource by lazy {
      DrawableResource("drawable:ic_youtube", setOf(
        ResourceItem(setOf(), "${MD}drawable/ic_youtube.xml", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("bg_image", Res.drawable.bg_image)
  map.put("ic_facebook", Res.drawable.ic_facebook)
  map.put("ic_google", Res.drawable.ic_google)
  map.put("ic_new_v", Res.drawable.ic_new_v)
  map.put("ic_venn", Res.drawable.ic_venn)
  map.put("ic_vvinamp", Res.drawable.ic_vvinamp)
  map.put("ic_vvinamp_white", Res.drawable.ic_vvinamp_white)
  map.put("ic_youtube", Res.drawable.ic_youtube)
}
