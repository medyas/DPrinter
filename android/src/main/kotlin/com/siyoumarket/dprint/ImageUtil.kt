package com.siyoumarket.dprint

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.view.View
import com.lvrenyang.io.Pos
import kotlin.experimental.or


/**
 * Created by jc on 2017/11/10.
 */

object ImageUtil {

    fun resizeImage(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        // load the origial Bitmap

        val width = bitmap.width
        val height = bitmap.height

        // calculate the scale
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height

        // create a matrix for the manipulation
        val matrix = Matrix()
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight)
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true)
    }

    /**将view转化为bitmap并且保存本地 */
    fun getViewBitmap(view: View): Bitmap? {
        view.clearFocus()
        view.isPressed = false

        val willNotCache = view.willNotCacheDrawing()
        view.setWillNotCacheDrawing(false)

        val color = view.drawingCacheBackgroundColor
        view.drawingCacheBackgroundColor = 0

        if (color != 0) {
            view.destroyDrawingCache()
        }
        view.buildDrawingCache()
        val cacheBitmap = view.drawingCache ?: return null

        val bitmap = Bitmap.createBitmap(cacheBitmap)

        view.destroyDrawingCache()
        view.setWillNotCacheDrawing(willNotCache)
        view.drawingCacheBackgroundColor = color

        return bitmap
    }

    /**
     * 按照一定的宽高比例裁剪图片
     * @param bitmap 要裁剪的图片
     * @param num1 长边的比例
     * @param num2 短边的比例
     * @param isRecycled 是否回收原图片
     * @return 裁剪后的图片
     */
    fun imageCropAll(bitmap: Bitmap?, num1: Int, num2: Int, isRecycled: Boolean): Bitmap? {
        var bitmap: Bitmap? = bitmap ?: return null
        val w = bitmap!!.width // 得到图片的宽，高
        val h = bitmap.height
        val retX: Int
        val retY: Int
        val nw: Int
        val nh: Int
        if (w > h) {
            if (h > w * num2 / num1) {
                nw = w
                nh = w * num2 / num1
                retX = 0
                retY = (h - nh) / 2
            } else {
                nw = h * num1 / num2
                nh = h
                retX = (w - nw) / 2
                retY = 0
            }
        } else {
            if (w > h * num2 / num1) {
                nh = h
                nw = h * num2 / num1
                retY = 0
                retX = (w - nw) / 2
            } else {
                nh = w * num1 / num2
                nw = w
                retY = (h - nh) / 2
                retX = 0
            }
        }
        Log.e("imageCrop", "-----------imageCrop--retX->$retX;retY=$retY;nw=$nw;nh=$nh")
        val bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null, true)
        if (isRecycled && bitmap != null && bitmap != bmp && !bitmap.isRecycled) {
            bitmap.recycle()//回收原图片
            bitmap = null
        }
        return bmp
    }

    // print
    // 使用命令
    // HS 'L' n w_bytes repeat start dat[n]
    // n数据字节数。
    // repeat是否重复
    // start为偏移字节
    private fun dotPixel2PosCmd(dotsmap: ByteArray, dstbuf: ByteArray?, nWidth: Int, nHeight: Int): Int {
//        return getPrintPictureLineCommand(dstbuf!!, dotsmap, nWidth, nHeight)
        return 0
    }

    // print
    // 使用命令
    // HS 'l' n w_bytes repeat start dat[n]
    // n数据字节数。
    // repeat是否重复
    // start为偏移字节
    private fun dotPixel2PosMdjbtCmd(dotsmap: ByteArray, dstbuf: ByteArray?, nWidth: Int, nHeight: Int): Int {
//        return getPrintPictureLineBufferCommand(dstbuf!!, dotsmap, nWidth, nHeight)
        return 0
    }

    private fun bitmap2PrintDotsmap(mBitmap: Bitmap, nWidth: Int, bPicture: Boolean): ByteArray {
        val dstw = (nWidth + 7) / 8 * 8
        val dsth = mBitmap.height * dstw / mBitmap.width
        val dithered = Pos.getBitmapDots(mBitmap, nWidth, bPicture)
        val w_bytes = (dstw + 7) / 8
        val dotsmap = ByteArray(w_bytes * dsth)
        for (i in 0 until dsth) {
            for (j in 0 until dstw) {
                if (dithered[i * dstw + j]) {
                    dotsmap[i * w_bytes + j / 8] = dotsmap[i * w_bytes + j / 8] or (0x80 shr (j and 7)).toByte()
                }
            }
        }
        return dotsmap
    }

    fun Bitmap2PosCmd(mBitmap: Bitmap, nWidth: Int, bPicture: Boolean): ByteArray? {
        val dstw = (nWidth + 7) / 8 * 8
        val dsth = mBitmap.height * dstw / mBitmap.width

        val dotsmap = bitmap2PrintDotsmap(mBitmap, nWidth, bPicture)
        var size = dotPixel2PosCmd(dotsmap, null, dstw, dsth)
        if (size > 0) {
            val buffer = ByteArray(size)
            size = dotPixel2PosCmd(dotsmap, buffer, dstw, dsth)
            return buffer
        }
        return null
    }

    fun Bitmap2PosCmd(mBitmap: Bitmap, pic_bitmap: Bitmap, nWidth: Int, bPic: Boolean): ByteArray? {
        val dstw = (nWidth + 7) / 8 * 8
        val dsth = mBitmap.height * dstw / mBitmap.width

        val dotsmap = bitmap2PrintDotsmap(mBitmap, nWidth, bPic)

        var size = dotPixel2PosCmd(dotsmap, null, dstw, dsth)
        if (size > 0) {
            val buffer = ByteArray(size)
            size = dotPixel2PosCmd(dotsmap, buffer, dstw, dsth)
            return buffer
        }
        return null
    }

    fun Bitmap2PosMdjbtCmd(mBitmap: Bitmap, pic_bitmap: Bitmap, nWidth: Int, bPic: Boolean): ByteArray? {
        val dstw = (nWidth + 7) / 8 * 8
        val dsth = mBitmap.height * dstw / mBitmap.width

        val dotsmap = bitmap2PrintDotsmap(mBitmap, nWidth, bPic)

        var size = dotPixel2PosMdjbtCmd(dotsmap, null, dstw, dsth)
        if (size > 0) {
            val buffer = ByteArray(size)
            size = dotPixel2PosMdjbtCmd(dotsmap, buffer, dstw, dsth)
            return buffer
        }
        return null
    }

    // 高级指令
    fun Bitmap2PosFastPictureCmd(mBitmap: Bitmap, nWidth: Int, bPic: Boolean): ByteArray? {
        val dstw = (nWidth + 7) / 8 * 8
        val dsth = mBitmap.height * dstw / mBitmap.width

        val dotsmap = bitmap2PrintDotsmap(mBitmap, nWidth, bPic)

//        var size = getPrintPictureCommand(ByteArray(0), dotsmap, dstw / 8)
//        if (size > 0) {
//            val buffer = ByteArray(size)
//            size = getPrintPictureCommand(buffer, dotsmap, dstw / 8)
//            return buffer
//        }
        return null
    }


}
