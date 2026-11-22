package com.example.vvinamp.util

// Not a data class: ByteArray equality is by reference, which is what the "changed" check needs anyway.
class PickedImage(
    val bytes: ByteArray,
    val contentType: String,
    val extension: String
)

const val MAX_COVER_BYTES = 10 * 1024 * 1024

// Reads the type from the file's first bytes, since file names and picker types can't be trusted.
fun checkCoverImage(bytes: ByteArray): Result<PickedImage> {
    if (bytes.isEmpty()) return Result.failure(IllegalArgumentException("Couldn't read that image"))
    if (bytes.size > MAX_COVER_BYTES) return Result.failure(IllegalArgumentException("Image is too large. Max 10 MB"))

    val image = when {
        bytes.startsWith(0xFF, 0xD8, 0xFF) -> PickedImage(bytes, "image/jpeg", "jpg")
        bytes.startsWith(0x89, 0x50, 0x4E, 0x47) -> PickedImage(bytes, "image/png", "png")
        bytes.startsWith(0x52, 0x49, 0x46, 0x46) && bytes.size > 11 &&
            bytes.decodeToString(8, 12) == "WEBP" -> PickedImage(bytes, "image/webp", "webp")
        else -> null
    }
    return image?.let { Result.success(it) }
        ?: Result.failure(IllegalArgumentException("Use a JPG, PNG or WebP image"))
}

private fun ByteArray.startsWith(vararg prefix: Int): Boolean =
    size >= prefix.size && prefix.indices.all { this[it] == prefix[it].toByte() }
