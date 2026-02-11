package com.bonustrack02.lotterygenerator.presentation.history

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonustrack02.lotterygenerator.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val PaperColor = Color(0xFFFFFDF5)
val InkColor = Color(0xFF333333)
val AccentColor = Color(0xFFE91E63) // 마킹 색상 (분홍/빨강 계열)

@Composable
fun GeneratedTicketImage(
    selectedNumbers: List<Int>,
    timestamp: Long,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
        Date(
            timestamp * 1000
        )
    )

    val density = LocalDensity.current
    val dateTimeString = stringResource(R.string.share_image_datetime, dateStr)

    val textPaint = remember {
        Paint().apply {
            color = InkColor.toArgb()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }

    val smallTextPaint = remember {
        Paint().apply {
            color = InkColor.toArgb()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textSize = with(density) { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(Color.Gray)
            .padding(20.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawRoundRect(
                color = PaperColor,
                size = size,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )
            drawRoundRect(
                color = Color.LightGray,
                size = size,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )

            val headerHeight = canvasHeight * 0.15f
            val titleSectionHeight = headerHeight * 0.6f
            val dateSectionHeight = headerHeight - titleSectionHeight

            drawRect(
                color = AccentColor.copy(alpha = 0.1f),
                topLeft = Offset(0f, 0f),
                size = Size(canvasWidth, headerHeight * 0.6f)
            )

            drawIntoCanvas { canvas ->
                textPaint.textSize = with(density) { 24.sp.toPx() }

                val titleOffset = (textPaint.descent() + textPaint.ascent()) / 2
                val titleCenterY = (titleSectionHeight / 2) - titleOffset

                textPaint.color = AccentColor.toArgb()
                canvas.nativeCanvas.drawText(
                    "LOTTO",
                    canvasWidth / 2,
                    titleCenterY,
                    textPaint
                )

                val dateSectionCenterY = titleSectionHeight + (dateSectionHeight / 2)
                val dateOffset = (smallTextPaint.descent() + smallTextPaint.ascent()) / 2
                val dateDrawY = dateSectionCenterY - dateOffset

                canvas.nativeCanvas.drawText(
                    dateTimeString,
                    canvasWidth / 2,
                    dateDrawY,
                    smallTextPaint
                )
            }

            drawLine(
                color = Color.LightGray,
                start = Offset(0f, headerHeight),
                end = Offset(canvasWidth, headerHeight),
                strokeWidth = 2.dp.toPx()
            )

            val gridTopPadding = 12.dp.toPx()
            val gridStartY = headerHeight + gridTopPadding

            val gridBottomPadding = 12.dp.toPx()

            val columns = 7
            val rows = 7

            val cellWidth = canvasWidth / columns
            val cellHeight = (canvasHeight - gridStartY - gridBottomPadding) / rows

            val numberPaint = Paint().apply {
                color = InkColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = with(density) { 14.sp.toPx() }
                typeface = Typeface.DEFAULT_BOLD
            }

            for (i in 1..45) {
                val index = i - 1
                val col = index % columns
                val row = index / columns

                val centerX = col * cellWidth + cellWidth / 2
                val centerY = gridStartY + row * cellHeight + cellHeight / 2

                val isSelected = selectedNumbers.contains(i)

                if (isSelected) {
                    drawCircle(
                        color = AccentColor,
                        radius = minOf(cellWidth, cellHeight) * 0.35f,
                        center = Offset(centerX, centerY)
                    )
                    numberPaint.color = Color.White.toArgb()
                } else {
                    numberPaint.color = InkColor.toArgb()
                }

                drawIntoCanvas { canvas ->
                    val fontMetrics = numberPaint.fontMetrics
                    val baselineY = centerY - (fontMetrics.ascent + fontMetrics.descent) / 2

                    canvas.nativeCanvas.drawText(
                        i.toString(),
                        centerX,
                        baselineY,
                        numberPaint
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneratedTicketImagePreview() {
    GeneratedTicketImage(
        selectedNumbers = listOf(3, 12, 25, 33, 41, 7),
        timestamp = System.currentTimeMillis() / 1000
    )
}