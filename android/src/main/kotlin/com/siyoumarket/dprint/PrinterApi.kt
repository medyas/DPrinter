package com.siyoumarket.dprint

import android.graphics.Bitmap
import com.lvrenyang.io.Pos


/**
 * 公司： 志众电子有限公司
 * 说明： 志众打印机API
 * 作者： 湖中鸟
 * QQ:    403949692
 */
object PrinterApi {

    // byte1+byte2
    fun byteMerger(byte_1: ByteArray?, byte_2: ByteArray?): ByteArray {
        var size = 0
        if (byte_1 != null) {
            size += byte_1.size
        }
        if (byte_2 != null) {
            size += byte_2.size
        }
        val byte_3 = ByteArray(size)
        var pos = 0
        if (byte_1 != null) {
            System.arraycopy(byte_1, 0, byte_3, pos, byte_1.size)
            pos += byte_1.size
        }
        if (byte_2 != null) {
            System.arraycopy(byte_2, 0, byte_3, pos, byte_2.size)
            //pos += byte_2.length;
        }
        return byte_3
    }

    // 获取打印UTF8字符串
    fun getPrintStringGBKBytes(str: String): ByteArray? {
        try {
            return str.toByteArray(charset("GBK"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 获取打印UTF8字符串
    fun getPrintStringUTF8Bytes(str: String): ByteArray? {
        try {
            return str.toByteArray(charset("UTF8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 获取128条码
    // str 输入只能是英文字符或数字
    fun getPrintBarcodeBytes(str: String): ByteArray? {
        try {
            var data = getPrintStringGBKBytes(str)      // 输入只能是英文字符或数字
            //byte []data = getPrintStringUTF8Bytes(str);      // 输入只能是英文字符或数字
            data = byteMerger("1d6b08".toByteArray(), data)
            data = byteMerger(data, "00".toByteArray())
            return data
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 获取128条码
    // str 输入只能是英文字符或数字
    // textPos: 文本位置
    //      0   不打印文本
    //      1   在条码上面
    //      2   在条码下面
    //      3   条码上下两个位置
    // widht： 条码宽度放大倍数: 1---6
    // height：条码高度:  1--n
    fun getPrintBarcodeBytes(str: String, textPos: Int, width_X: Int, height: Int): ByteArray? {
        try {
            var data_pos = byteMerger("1d48".toByteArray(), byteArrayOf(textPos.toByte()))   // 文本位置
            val data_height = byteMerger("1d68".toByteArray(), byteArrayOf(height.toByte()))   // 条码高度
            val data_widht = byteMerger("1d77".toByteArray(), byteArrayOf(width_X.toByte()))   // 条码宽度放大倍数

            data_pos = byteMerger(data_pos, data_height)
            data_pos = byteMerger(data_pos, data_widht)
            var data = getPrintBarcodeBytes(str)      // 输入只能是英文字符或数字
            data = byteMerger(data_pos, data)
            return data
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 数据长度不能超过128bytes
    fun getQrcodeBytes(size: Int, errLevel: Int, data: ByteArray?): ByteArray? {
        var size = size
        var errLevel = errLevel
        var data = data
        if (size > 16) size = 16
        if (errLevel > 3) errLevel = 3
        var data_size = byteMerger("1d286b03003143".toByteArray(), byteArrayOf(size.toByte()))   // 二维码大小
        val data_err = byteMerger("1d286b03003145".toByteArray(), byteArrayOf((0x30 + errLevel).toByte()))   // 二维码错误等级 30/31/32/33
        data_size = byteMerger(data_size, data_err)
        // 二维码数据
        val data_head = byteMerger("1d286B".toByteArray(),
                byteArrayOf((data!!.size + 3 and 0xFF).toByte(), 0x00, 0x31, 0x50, 0x30))
        data = byteMerger(data_head, data)

        data = byteMerger(data_size, data)
        return data
    }

    // 获取二维码
    // 输入可以为中文
    // size: 0--16   0表示不改变大小
    // errLevel:  0-3
    // 数据长度不能超过128bytes
    fun getPrintQrcodeGBKBytes(str: String, size: Int, errLevel: Int): ByteArray? {
        try {
            val data = getPrintStringGBKBytes(str)
            return getQrcodeBytes(size, errLevel, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 获取二维码
    // 输入可以为中文
    // size: 0--16   0表示不改变大小
    // errLevel:  0-3
    // 数据长度不能超过128bytes
    fun getPrintQrcodeUTF8Bytes(str: String, size: Int, errLevel: Int): ByteArray? {
        try {
            val data = getPrintStringUTF8Bytes(str)
            return getQrcodeBytes(size, errLevel, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    // 以下打印图片指令，适用于所有打印机。
    // bitmap: 位图
    // printWidth: 打印宽度， 不得超过打印头最大宽度（如384，576等）
    //             若位图宽度比打印头大， 则会根据printWidht尺寸裁剪
    // bPicture:   是否按照图片方式解析打印.
    //              图片方式解析，会压缩部分信息。对于有文字的图片，可能会失真。
    fun getPrintBitmap(bitmap: Bitmap, printWidth: Int, bPicture: Boolean): ByteArray {
        return Pos.POS_Bitmap2Data(bitmap, printWidth, if (bPicture) 0 else 1, 0)
    }

    // 以下打印图片指令，适用于专用的标签打印机。数据有压缩, , 压缩率较低
    // 不是所有机器都支持。 具体咨询 志众电子...
    // bitmap: 位图
    // printWidth: 打印宽度， 不得超过打印头最大宽度（如384，576等）
    //             若位图宽度比打印头大， 则会根据printWidht尺寸裁剪
    // bPicture:   是否按照图片方式解析打印.
    //              图片方式解析，会压缩部分信息。对于有文字的图片，可能会失真。
    fun getPrintBitmap_zz(bitmap: Bitmap, printWidth: Int, bPicture: Boolean): ByteArray {
        return ImageUtil.Bitmap2PosCmd(bitmap, printWidth, bPicture)!!
    }


    // 以下打印图片指令，适用于专用的标签打印机。数据有压缩, 数据压缩率较高
    // 不是所有机器都支持。 具体咨询 志众电子...
    // bitmap: 位图
    // printWidth: 打印宽度， 不得超过打印头最大宽度（如384，576等）
    //             若位图宽度比打印头大， 则会根据printWidht尺寸裁剪
    // bPicture:   是否按照图片方式解析打印.
    //              图片方式解析，会压缩部分信息。对于有文字的图片，可能会失真。
    fun getPrintBitmapFast_zz(bitmap: Bitmap, printWidth: Int, bPicture: Boolean): ByteArray {
        return ImageUtil.Bitmap2PosFastPictureCmd(bitmap, printWidth, bPicture)!!
    }

}
