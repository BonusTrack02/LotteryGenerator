package com.bonustrack02.lotterygenerator.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.bonustrack02.lotterygenerator.R
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object ShareUtils {
    suspend fun captureComposableAsBitmap(
        context: Context,
        compositionContext: CompositionContext,
        widthPx: Int = 1080,
        content: @Composable () -> Unit
    ): Bitmap = suspendCancellableCoroutine { continuation ->
        val heightPx = (widthPx / 0.7f).toInt()

        val view = ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setParentCompositionContext(compositionContext)
            setContent(content)
        }

        val lifecycleOwner = context as? LifecycleOwner
        val savedStateRegistryOwner = context as? SavedStateRegistryOwner
        if (lifecycleOwner != null && savedStateRegistryOwner != null) {
            view.setViewTreeLifecycleOwner(lifecycleOwner)
            view.setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
        }

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val layoutParams = WindowManager.LayoutParams().apply {
            width = widthPx
            height = heightPx
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            format = PixelFormat.TRANSLUCENT
            alpha = 0f
        }

        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                view.viewTreeObserver.addOnPreDrawListener(object :
                    ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        if (view.width > 0 && view.height > 0) {
                            view.viewTreeObserver.removeOnPreDrawListener(this)

                            view.post {
                                try {
                                    val bitmap = view.drawToBitmap()
                                    continuation.resume(bitmap)
                                } catch (e: Exception) {
                                    continuation.resumeWithException(e)
                                } finally {
                                    try {
                                        windowManager.removeViewImmediate(view)
                                    } catch (e: Exception) {
                                        Log.d(javaClass.simpleName, e.message.toString())
                                    }
                                }
                            }
                            return true
                        }
                        return true
                    }
                })
            }

            override fun onViewDetachedFromWindow(v: View) {}
        })

        try {
            windowManager.addView(view, layoutParams)
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }

        continuation.invokeOnCancellation {
            try {
                windowManager.removeViewImmediate(view)
            } catch (e: Exception) {
            }
        }
    }

    fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()

            val timestamp = Instant.now().epochSecond
            val pathName = "$cachePath/${timestamp}_ticket.png"
            val stream = FileOutputStream(pathName)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                File(pathName)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun shareImage(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.share_number_set_title)
            )
        )
    }
}