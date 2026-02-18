package com.bonustrack02.lotterygenerator.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.drawToBitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.suspendCancellableCoroutine
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
}