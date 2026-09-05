package com.lmalecic.lovefinderzz.handler

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult

fun getGifEnabledLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            if ( SDK_INT >= 28 ) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()
}

suspend fun Context.loadCachedImageBitmap(imageUrl: String) = runCatching {
    val request = ImageRequest.Builder(this)
        .data(imageUrl)
        .allowHardware(false)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.DISABLED)
        .build()

    val result = imageLoader.execute(request)

    (result as? SuccessResult)?.drawable
        ?.toBitmap()
}.getOrNull()