package com.example.camscannerallinone.data.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.example.camscannerallinone.domain.model.FilterType
import com.example.camscannerallinone.domain.model.Point
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageProcessor @Inject constructor() {

    fun applyFilter(bitmap: Bitmap, filterType: FilterType): Bitmap {
        return when (filterType) {
            FilterType.ORIGINAL -> bitmap
            FilterType.GRAYSCALE -> applyGrayscale(bitmap)
            FilterType.BLACK_AND_WHITE -> applyBlackAndWhite(bitmap)
            FilterType.MAGIC_COLOR -> applyMagicColor(bitmap)
            FilterType.SHARPEN -> applySharpen(bitmap)
            FilterType.HIGH_CONTRAST -> applyHighContrast(bitmap)
        }
    }

    private fun applyGrayscale(bitmap: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }

    private fun applyBlackAndWhite(bitmap: Bitmap): Bitmap {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.threshold(mat, mat, 127.0, 255.0, Imgproc.THRESH_BINARY)
        val result = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, result)
        return result
    }

    private fun applyMagicColor(bitmap: Bitmap): Bitmap {
        // Simple implementation of "Magic Color" (boosted contrast and saturation)
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(1.5f)
            // Adjust contrast
            val scale = 1.2f
            val translate = (-.5f * scale + .5f) * 255f
            val array = floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
            postConcat(ColorMatrix(array))
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }

    private fun applySharpen(bitmap: Bitmap): Bitmap {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        val kernel = Mat(3, 3, CvType.CV_32F).apply {
            put(0, 0, 0.0, -1.0, 0.0, -1.0, 5.0, -1.0, 0.0, -1.0, 0.0)
        }
        Imgproc.filter2D(mat, mat, -1, kernel)
        val result = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, result)
        return result
    }

    private fun applyHighContrast(bitmap: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        val scale = 1.5f
        val translate = (-.5f * scale + .5f) * 255f
        val colorMatrix = ColorMatrix(floatArrayOf(
            scale, 0f, 0f, 0f, translate,
            0f, scale, 0f, 0f, translate,
            0f, 0f, scale, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        ))
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }

    fun perspectiveTransform(bitmap: Bitmap, corners: List<Point>): Bitmap {
        if (corners.size != 4) return bitmap

        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)

        val srcPoints = MatOfPoint2f(
            org.opencv.core.Point(corners[0].x.toDouble(), corners[0].y.toDouble()),
            org.opencv.core.Point(corners[1].x.toDouble(), corners[1].y.toDouble()),
            org.opencv.core.Point(corners[2].x.toDouble(), corners[2].y.toDouble()),
            org.opencv.core.Point(corners[3].x.toDouble(), corners[3].y.toDouble())
        )

        // Determine destination dimensions
        val widthA = Math.sqrt(Math.pow(corners[2].x - corners[3].x.toDouble(), 2.0) + Math.pow(corners[2].y - corners[3].y.toDouble(), 2.0))
        val widthB = Math.sqrt(Math.pow(corners[1].x - corners[0].x.toDouble(), 2.0) + Math.pow(corners[1].y - corners[0].y.toDouble(), 2.0))
        val maxWidth = Math.max(widthA.toInt(), widthB.toInt()).toDouble()

        val heightA = Math.sqrt(Math.pow(corners[1].x - corners[2].x.toDouble(), 2.0) + Math.pow(corners[1].y - corners[2].y.toDouble(), 2.0))
        val heightB = Math.sqrt(Math.pow(corners[0].x - corners[3].x.toDouble(), 2.0) + Math.pow(corners[0].y - corners[3].y.toDouble(), 2.0))
        val maxHeight = Math.max(heightA.toInt(), heightB.toInt()).toDouble()

        val dstPoints = MatOfPoint2f(
            org.opencv.core.Point(0.0, 0.0),
            org.opencv.core.Point(maxWidth - 1, 0.0),
            org.opencv.core.Point(maxWidth - 1, maxHeight - 1),
            org.opencv.core.Point(0.0, maxHeight - 1)
        )

        val transform = Imgproc.getPerspectiveTransform(srcPoints, dstPoints)
        val dst = Mat()
        Imgproc.warpPerspective(mat, dst, transform, Size(maxWidth, maxHeight))

        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }

    fun detectDocumentCorners(bitmap: Bitmap): List<Point> {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        
        val gray = Mat()
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_RGB2GRAY)
        Imgproc.GaussianBlur(gray, gray, Size(5.0, 5.0), 0.0)
        
        val edges = Mat()
        Imgproc.Canny(gray, edges, 75.0, 200.0)
        
        val contours = mutableListOf<org.opencv.core.MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)
        
        contours.sortByDescending { Imgproc.contourArea(it) }
        
        for (contour in contours) {
            val peri = Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
            val approx = MatOfPoint2f()
            Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, 0.02 * peri, true)
            
            if (approx.total() == 4L) {
                val points = approx.toArray().map { Point(it.x.toFloat(), it.y.toFloat()) }
                return sortPoints(points)
            }
        }
        
        // Default to image corners if no document found
        return listOf(
            Point(0f, 0f),
            Point(bitmap.width.toFloat(), 0f),
            Point(bitmap.width.toFloat(), bitmap.height.toFloat()),
            Point(0f, bitmap.height.toFloat())
        )
    }

    private fun sortPoints(points: List<Point>): List<Point> {
        val sorted = points.sortedBy { it.x + it.y }
        val tl = sorted[0]
        val br = sorted[3]
        
        val diff = points.sortedBy { it.y - it.x }
        val tr = diff[0]
        val bl = diff[3]
        
        return listOf(tl, tr, br, bl)
    }
}
