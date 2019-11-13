package com.siyoumarket.dprint

import android.app.Activity
import android.graphics.BitmapFactory
import com.lvrenyang.io.BTPrinting
import com.lvrenyang.io.Pos

class PrinterPlugin {

    private var mPrinter: BTPrinting = BTPrinting()
    private val mPos = Pos()

    fun connectToPrinter(device: Map<*, *>, activity: Activity): Boolean {
        return try {
            mPos.Set(mPrinter)
            mPrinter.Open(device["address"].toString(), activity.applicationContext)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isConnected() = mPrinter.IsOpened()

    fun destroy() {
        mPrinter.Close()
    }


    fun printDemo() {
        val d1 = LabelPrint.printDefaultData("Siyou Market ONE", "Test Product 1", "2.99", "2010030002880")
        sendBuffer(d1)


        val d2 = LabelPrint.printDoubleData("Siyou Market ONE", "Test Product 1", "20% Discount", "2.99", "3.49", "2010030002880")
        sendBuffer(d2)
    }

    fun printImage(image: ByteArray): Boolean {
        val bmp = BitmapFactory.decodeByteArray(image, 0, image.size)

        //TODO test
       // mPos.POS_SetRightSpacing(0) byteArrayOf(27, 32, 0) change 0 with distance
//        mPos.POS_Reset() byteArrayOf(27, 64) reset printer

//        mPos.POS_FeedLine()
//        mPos.POS_S_Align(2)

        // specifies the x and y of print - needs t
        // ESC $ nL nH
        //Name Specify absolute position
        //Code ASCII ESC $ nL nH
        //     Hex. 1B 24 nL nH
        //     Decimal 27 36 nL nH
        //  Defined Region 0 ≤ nL ≤ 255
        //  0 ≤ nH ≤ 255
        // try merging with text
        var ESC_dollors_nL_nH = byteArrayOf(27, 36, 0, 0)

        val imageData = Pos.POS_Bitmap2Data(bmp, 400, 0, 0)

        var cmd = LabelPrint.byteMerger(byteArrayOf(13, 10), byteArrayOf(27, 97, 2)) // merge feedLine with align
        cmd = LabelPrint.byteMerger(cmd, imageData) // merge image with cmd
        cmd = LabelPrint.byteMerger(cmd, PRNCMD_TEXT_GAP.toByteArray()) // merge cmd with end of print

        return sendBuffer(cmd)

    }


    // BT发送数据
    private fun sendBuffer(data: ByteArray?): Boolean {
        return mPos.POS_SendBuffer(data)
    }



    // 建议参考使用
    // 使用zz打印接口打印
    // 演示如何打印中文、英文、条码、二维码、图片等。
    private fun PrintDemo1() {

        mPos.POS_FeedLine()
//        Set its way
//        (0 left aligned)
//        (1 center alignment)
//        (2 right alignment)
        mPos.POS_S_Align(1)
//        nOrgx
//        The 2-inch printer has 384 points and the 3-inch printer has 576 points.
//        nFontStyle
//        (0x00 normal)
//        (0x08 bold)
//        (0x80 1 point thick underline)
//        (0x100 2 points thick underline)
//        (0x200 is inverted, only valid at the beginning of the line)
//        (0x400 reverse display, white on black)
//        (0x1000 each character rotates 90 degrees clockwise)
        mPos.POS_S_TextOut("Test Product", 10, 0, 0, 0x00, 0x08)

        mPos.POS_S_Align(0)
        mPos.POS_S_TextOut("............................................", 10, 0, 0, 0x00, 0x00)
        mPos.POS_FeedLine()

        mPos.POS_S_Align(1)
        mPos.POS_S_TextOut("$1,99", 0, 1, 1, 0x00, 0x08)
        mPos.POS_FeedLine()

        mPos.POS_S_Align(0)

//        nHriFontPosition
//        0x00 does not print
//        0x01 only prints above the barcode
//        0x02 only prints under the barcode
//        0x03 barcode is printed on and under the barcode
        mPos.POS_S_SetBarcode("2010030002880", 30, 0x43, 5, 80, 0x00, 0x00)

        mPos.POS_S_Align(2)
        mPos.POS_S_TextOut("Siyou Market ONE", 0, 0, 0, 0x00, 0x08)
        mPos.POS_FeedLine()

        mPos.POS_S_Align(0)
        mPos.POS_S_TextOut("2010030002880", 10, 0, 0, 0x00, 0x08)

        mPos.POS_FeedLine()
        sendBuffer(PRNCMD_TEXT_GAP.toByteArray())     // 对纸
    }

    companion object {

        const val printWidth = 312//标签宽度 264 384(2寸) 576(3寸)
        const val printHeight = 234//标签高度 216 600// 强加密密码, 不同机器密码不同，具体咨询志众电子.
        const val PRNCMD_TEXT_GAP = "1241"


        val ESC_a_n = byteArrayOf(27, 97, 2) // change 2 to set the align
    }
}

