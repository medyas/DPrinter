package com.siyoumarket.dprint

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import com.lvrenyang.io.BTPrinting
import com.lvrenyang.io.Pos
import java.util.*

class PrinterPlugin {

    private var mPrinter: BTPrinting = BTPrinting()
    private val mPos = Pos()
    private val mBitmap: Bitmap? = null

    fun connectToPrinter(device: Map<*, *>, activity: Activity): Boolean {
        return try {
            mPos.Set(mPrinter)
            var res = mPrinter.Open(device["address"].toString(), activity.applicationContext)
            if (res) {
                val mRandStr = randomHexString(16)
                val buffer = getPrintCheckBytes(mRandStr?: "")
                res = getPrinterStatus(buffer, mRandStr?: "") // 检查打印机
            }
            res
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
//      PrintDemo2()        // 打印演示2
        PrintDemo1()
    }
    
    // BT发送数据
    private fun sendBuffer(data: ByteArray?): Boolean {
        return mPos.POS_SendBuffer(data)
    }


    // 推荐使用
    private fun PrintDemo2() {
        // 打印文本
        sendBuffer("31323334353637380d0a".toByteArray())       // 12345678
        sendBuffer("41424344454647480d0a".toByteArray())       // ABCDEFG..
        // 条码
        sendBuffer("1d4802".toByteArray())
        sendBuffer("1d6832".toByteArray())
        sendBuffer("1d7702".toByteArray())
        sendBuffer("1d6b083031323334353637383900".toByteArray())   // 条码

        // 二维码
        sendBuffer("1d286b03003143081d286B0700315030616263640a".toByteArray())

        // 打印图片
        if (mBitmap != null) {
            // 以下打印图片指令，适用于所有打印机。
            var algo = 1   // 0=纯图片, 1=文字&图片
            val data1 = Pos.POS_Bitmap2Data(mBitmap, printWidth, algo, 0)
            sendBuffer(data1)    // 直接打印数据

            algo = 0   // 0=纯图片, 1=文字&图片
            val data2 = Pos.POS_Bitmap2Data(mBitmap, printWidth, algo, 0)
            sendBuffer(data2)    // 直接打印数据

            // 以下打印图片指令，适用于专用的标签打印机。数据有压缩。
            // 不是所有机器都支持。 具体咨询 志众电子...
            val data3 = ImageUtil.Bitmap2PosCmd(mBitmap, printWidth, true)
            sendBuffer(data3)    // 直接打印数据
        }
        // 走纸,方便撕纸
        sendBuffer(hexStringToBytes("1B6408"))      // 走纸


        // 只有标签打印机，才有对纸命令
        // 普通小票打印机，没有对纸命令
        sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEXT_GAP))     // 对纸
    }

    // 建议参考使用
    // 使用zz打印接口打印
    // 演示如何打印中文、英文、条码、二维码、图片等。
    private fun PrintDemo1() {
        // 打印文本
        var data = PrinterApi.getPrintStringGBKBytes("厦门志众电子\n1234567890abc!\n")
        sendBuffer(data)

        // 条码
        data = PrinterApi.getPrintBarcodeBytes("0123456789", 2, 2, 0x32)        // 条码
        sendBuffer(data)

        // 换行
        data = PrinterApi.getPrintStringGBKBytes("\n\n")
        sendBuffer(data)

        // 二维码1
        data = PrinterApi.getPrintQrcodeGBKBytes("厦门志众电子SDK-二维码GBK", 8, 0)
        sendBuffer(data)

        // 换行
        data = PrinterApi.getPrintStringGBKBytes("\n\n\n")
        sendBuffer(data)

        // 二维码2
        data = PrinterApi.getPrintQrcodeUTF8Bytes("厦门志众电子SDK-二维码UTF8", 8, 0)
        sendBuffer(data)

        // 换行
        data = PrinterApi.getPrintStringGBKBytes("\n\n\n")
        sendBuffer(data)

        // 打印图片
        if (mBitmap != null) {
            // 以下打印图片指令，适用于所有打印机。
            var algo = true   // true=纯图片, false=文字&图片
            val data1 = PrinterApi.getPrintBitmap(mBitmap, printWidth, algo)
            sendBuffer(data1)

            algo = false           // true=纯图片, false=文字&图片
            val data2 = PrinterApi.getPrintBitmap(mBitmap, printWidth, algo)
            sendBuffer(data2)

            // 以下打印图片指令，适用于专用的标签打印机。数据有压缩, 压缩率较低
            // 不是所有机器都支持。 具体咨询 志众电子...
            val data3 = PrinterApi.getPrintBitmap_zz(mBitmap, printWidth, true)
            sendBuffer(data3)

            // 以下打印图片指令，适用于专用的标签打印机。数据有压缩, 压缩率较高
            // 不是所有机器都支持。 具体咨询 志众电子...
            val data4 = PrinterApi.getPrintBitmapFast_zz(mBitmap, printWidth, true)
            sendBuffer(data4)

        }


        // 走纸,方便撕纸
        sendBuffer(hexStringToBytes("1B6408"))      // 走纸

        // 只有标签打印机，才有对纸命令
        // 普通小票打印机，没有对纸命令
        sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEXT_GAP))     // 对纸
    }

/*
* --------------------------------------------------------------------------------------
* */

    private fun getPrinterStatus(dat: ByteArray, mRandStr: String): Boolean {
        var retry = 0
        var isOk = false
        val readBuffer = ByteArray(128)

        // 是否过滤指定的打印机
        if (false) {
            do {
                isOk = false
                sendRead(readBuffer, readBuffer.size, 100 + retry * 100)        // read dummy data
                val result = sendWrite(dat, dat.size)            //发送随机数
                if (result) {
                    isOk = waitPrinterAck(readBuffer, readBuffer.size, mRandStr, 2000)//读取返回数据
                }
                retry++
            } while (!isOk && retry < 3)    // 重试2次
        } else {        // 不过滤指定的打印机
            isOk = true
        }
        return isOk
    }

    // 获取监测数据
    private fun getPrintCheckBytes(mRandStr: String): ByteArray {
        var str: String
        val dat = hexStringToBytes(mRandStr)        // 1. 产生随机数
        // 强加密. hzh add
//        KeyInit(mEncryptPsw)        // 设置加密密码
//        EncodeBuffer(dat!!)                        // 2. 加密数据
        str = AppConst.PRNCMD_TEST_ENCRYPT + byte2hex(dat!!)    // 添加 1e5618 + d1...dn
        return convert2HexArray(str)
    }


    // 写数据
    private fun sendWrite(buffer: ByteArray, count: Int): Boolean {
        return mPos.POS_SendBuffer(buffer, 0, count) == count
    }

    // 读数据
    private fun sendRead(buffer: ByteArray, count: Int, timeout: Int): Int {
        val read = mPos.POS_ReadBuffer(buffer, 0, count, timeout)
        return if (read != -1 && read != 0 && read != count) {
            read
        } else {
            -2
        }
    }

    // 等待打印机应答
    private fun waitPrinterAck(buffer: ByteArray, max_size: Int, valueNum: String, timeout: Int): Boolean {
        var time = 0
        var pos = 0
        val count = 8
        do {
            val read = mPos.POS_ReadBuffer(buffer, pos, 1, 100)
            if (read > 0) {
                time = 0   // 重新计时
                pos += read
                if (pos >= count) {
                    var equals = false
                    val orig = hexStringToBytes(valueNum)
                    if (orig!!.size >= count) {
                        equals = true
                        // 第一个字节的数据为版本号
                        for (i in 1 until count) {   // first type is command type
                            if (orig[i] != buffer[i]) {
                                equals = false
                                break
                            }
                        }
                    }
                    return equals
                }
            }
            time += 10
        } while (time < timeout && pos < count)
        return false
    }

    companion object {
        const val mEncryptKey = 0x12345678       // 弱加密密码, 不同机器密码不同，具体咨询志众电子.
        const val mEncryptKey2 = 1
        const val mEncryptPsw = "keyword"

        const val printWidth = 312//标签宽度 264 384(2寸) 576(3寸)
        const val printHeight = 234//标签高度 216 600// 强加密密码, 不同机器密码不同，具体咨询志众电子.

        @SuppressLint("DefaultLocale")
        @JvmStatic
        fun randomHexString(len: Int): String? {
            try {
                val result = StringBuffer()
                for (i in 0 until len) {
                    result.append(Integer.toHexString(Random().nextInt(16)))
                }
                return result.toString().toUpperCase()

            } catch (e: Exception) {
                e.printStackTrace()

            }

            return null
        }

        /**
         * Convert hex string to byte[]
         * @param hexString the hex string
         * @return byte[]
         */
        @SuppressLint("DefaultLocale")
        @JvmStatic
        fun hexStringToBytes(str: String?): ByteArray? {
            return str?.toByteArray()
        }


        /**
         * Convert char to byte
         * @param c char
         * @return byte
         */
        @JvmStatic
        private fun charToByte(c: Char): Byte {
            return c.toByte()
//            return "0123456789ABCDEF".indexOf(c.toInt()).toByte()
        }

        // 字符串转化为十六进制字节数组
        @JvmStatic
        fun convert2HexArray(apdu: String): ByteArray {
            return apdu.toByteArray()
        }

        /**
         * java字节码转字符串
         *
         * @param b
         * @return
         */
        @SuppressLint("DefaultLocale")
        @JvmStatic
        fun byte2hex(b: ByteArray): String { //一个字节的数，
            return b.toHexString()
        }
    }
}






fun ByteArray.toHexString() : String {
    return this.joinToString("") {
        java.lang.String.format("%02x", it)
    }
}