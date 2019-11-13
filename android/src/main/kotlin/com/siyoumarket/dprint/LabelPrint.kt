package com.siyoumarket.dprint

import android.util.Log
import com.wisdom.tian.cpp.NativeUtil
import java.util.*

object LabelPrint {
    private val smailByte = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0x07.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x07.toByte(), 0x00.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x80.toByte(), 0xFF.toByte(), 0x03.toByte(), 0xC0.toByte(), 0x0F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0x03.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x00.toByte(), 0x00.toByte(), 0x78.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x01.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x01.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x78.toByte(), 0x00.toByte(), 0x00.toByte(), 0x78.toByte(), 0x00.toByte(), 0x00.toByte(), 0x78.toByte(), 0x80.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x00.toByte(), 0xF8.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x03.toByte(), 0x00.toByte(), 0xE0.toByte(), 0x07.toByte(), 0x00.toByte(), 0xC0.toByte(), 0x0F.toByte(), 0x04.toByte(), 0x80.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x07.toByte(), 0x00.toByte(), 0xE0.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
    private val bmpData_1 = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x7E.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0xC0.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0xE0.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFD.toByte(), 0xFE.toByte(), 0x7F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x3E.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x3E.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x02.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())
    private val bigByte = byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x1E.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0x0F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0x0F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x07.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0xFF.toByte(), 0x1F.toByte(), 0x00.toByte(), 0x30.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF8.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x83.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x3F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xE0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFC.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xC0.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xFE.toByte(), 0xFF.toByte(), 0x7F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xF0.toByte(), 0xFF.toByte(), 0x0F.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte())

    // label api
    private fun zzLabelBarcode(x: Int, y: Int, type: Int, height: Int, unitWidth: Int, roate: Int, butf8: Boolean, str: String): ByteArray {
        return NativeUtil.zzLabelBarcode(x, y, type, height, unitWidth, roate, butf8, str)
    }

    private fun zzLabelBitmap(x: Int, y: Int, widht: Int, height: Int, dat: ByteArray): ByteArray {
        return NativeUtil.zzLabelBitmap(x, y, widht, height, dat)
    }

    private fun zzLabelBitmap(x: Int, y: Int, widht: Int, height: Int, rvs: Int, rotate: Int, wx: Int, hx: Int, dat: ByteArray): ByteArray {
        return NativeUtil.zzLabelBitmap2(x, y, widht, height, rvs, rotate, wx, hx, dat)
    }

    private fun zzLabelBlackBox(left: Int, top: Int, right: Int, bottom: Int, color: Int): ByteArray {
        return NativeUtil.zzLabelBlackBox(left, top, right, bottom, color)
    }

    private fun zzLabelEnd(): ByteArray {
        return NativeUtil.zzLabelEnd()
    }

    private fun zzLabelFeed(): ByteArray {
        return NativeUtil.zzLabelFeed()
    }

    private fun zzLabelLine(x0: Int, y0: Int, x1: Int, y1: Int): ByteArray {
        return NativeUtil.zzLabelLine(x0, y0, x1, y1)
    }

    private fun zzLabelLine(x0: Int, y0: Int, x1: Int, y1: Int, width: Int, color: Int): ByteArray {
        return NativeUtil.zzLabelLine2(x0, y0, x1, y1, width, color)
    }

    private fun zzLabelPdf417(x: Int, y: Int, colNum: Int, ver: Int, ratio: Int, unitWidht: Int, rotate: Int, bUtr8: Boolean, str: String): ByteArray {
        return NativeUtil.zzLabelPdf417(x, y, colNum, ver, ratio, unitWidht, rotate, bUtr8, str)
    }

    private fun zzLabelPrint(): ByteArray {
        return NativeUtil.zzLabelPrint()
    }

    private fun zzLabelPrint(num: Int): ByteArray {
        return NativeUtil.zzLabelPrint2(num)
    }

    private fun zzLabelQrcode(x: Int, y: Int, ver: Int, ecc: Int, unitWidht: Int, rotate: Int, butf8: Boolean, str: String): ByteArray {
        return NativeUtil.zzLabelQrcode(x, y, ver, ecc, unitWidht, rotate, butf8, str)
    }

    private fun zzLabelRect(left: Int, top: Int, right: Int, bottom: Int): ByteArray {
        return NativeUtil.zzLabelRect(left, top, right, bottom)
    }

    private fun zzLabelRect(left: Int, top: Int, right: Int, bottom: Int, width: Int, color: Int): ByteArray {
        return NativeUtil.zzLabelRect2(left, top, right, bottom, width, color)
    }

    private fun zzLabelStart(): ByteArray {
        return NativeUtil.zzLabelStart()
    }

    private fun zzLabelStart(x: Int, y: Int, width: Int, height: Int, rotate: Int): ByteArray {
        return NativeUtil.zzLabelStart2(x, y, width, height, rotate)
    }

    private fun zzLabelText(x: Int, y: Int, fontHeight: Int, bold: Int, underLine: Int, rvs: Int, delLine: Int, rotate: Int, wx: Int, hx: Int, butf8: Boolean, str: String): ByteArray {
        return NativeUtil.zzLabelText2(x, y, fontHeight, bold, underLine, rvs, delLine, rotate, wx, hx, butf8, str)
    }

    private fun zzLabelText(x: Int, y: Int, butf8: Boolean, str: String): ByteArray {
        return NativeUtil.zzLabelText(x, y, butf8, str)
    }

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

    //zzLabelText中的fontHeight取值范围{16,24,32,48,64,80,96}
    fun printDoubleData(chain_name: String, product_name: String, discount: String, product_vip_price: String, price: String, qcode: String): ByteArray {
        var pName = product_name
        var pVipPrice = product_vip_price
        var pPrice = price
        pPrice = pPrice.replace(".", ",")
        pVipPrice = pVipPrice.replace(".", ",")
        var cmd = zzLabelStart()
        // text demo
        val titleLenght = pName.length
        var titleY = 35
        var isLine = false
        if (titleLenght <= 29) {
            val emptystr = (29 - titleLenght) / 2
            for (i in 0 until emptystr) {
                pName = " $pName"
            }
            val dat = zzLabelText(110, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, pName)
            cmd = byteMerger(cmd, dat)
        } else {
            val subText = splitText(pName, 29)
            isLine = true
            for (text in subText) {
                val emptystr = (29 - text.length) / 2
                var t = ""
                for (i in 0 until emptystr) {
                    t = " $text"
                }
                val dat = zzLabelText(110, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, t)
                cmd = byteMerger(cmd, dat)
                titleY += 30
            }
        }
        val lineStartY = titleY + if (isLine) 0 else 35
        //         line demo
        var dat = zzLabelLine(125, lineStartY, 550, lineStartY, 3, 1)
        cmd = byteMerger(cmd, dat)
        //
        dat = zzLabelBitmap(350, lineStartY + 10, 40, 80, bmpData_1)
        cmd = byteMerger(cmd, dat)
        // text demo
        dat = zzLabelText(390, lineStartY + 20, 32, 1, 1, 0, 1, 0, 2, 2, false, pPrice)
        cmd = byteMerger(cmd, dat)
        val length = chain_name.length
        Log.e("Laber", length.toString() + "")
        if (length <= 10) {
            dat = zzLabelText(110, 100, 16, 1, 1, 0, 1, 0, 2, 2, false, chain_name)
            cmd = byteMerger(cmd, dat)
        } else {
            val subText = splitText(chain_name, 13)
            var startY = 110
            for (text in subText) {
                dat = zzLabelText(110, startY, 16, 1, 1, 0, 1, 0, 2, 2, false, text)
                cmd = byteMerger(cmd, dat)
                startY += 30
            }
        }
        // barcode demo
        dat = zzLabelBarcode(110, 200, 8, 80, 2, 0, false, qcode)
        cmd = byteMerger(cmd, dat)
        dat = zzLabelText(125, 280, 16, 0, 1, 0, 1, 0, 2, 2, false, qcode)
        cmd = byteMerger(cmd, dat)
        dat = zzLabelText(385, lineStartY + 100, 16, 1, 1, 0, 1, 0, 2, 2, false, discount)
        cmd = byteMerger(cmd, dat)
        dat = zzLabelBitmap(380, 215, 40, 80, bmpData_1)
        cmd = byteMerger(cmd, dat)
        // text demo
        dat = zzLabelText(405, 230, 24, 1, 1, 0, 1, 0, 2, 2, false, pVipPrice)
        cmd = byteMerger(cmd, dat)
        dat = zzLabelEnd()
        cmd = byteMerger(cmd, dat)

        dat = zzLabelPrint()
        cmd = byteMerger(cmd, dat)
        return cmd
    }

    /*
    public static byte[] printDoubleData(String chain_name, String product_name, String product_vip_price, String price, String qcode) {
        price = price.replace(".", ",");
        product_vip_price = product_vip_price.replace(".", ",");
        byte[] cmd = zzLabelStart();
        // text demo
        int titleLenght = product_name.length();
        int titleY = 5;
        boolean isLine = false;
        if (titleLenght <= 29) {
            byte[] dat = zzLabelText(65, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, product_name);
            cmd = byteMerger(cmd, dat);
        } else {
            ArrayList<String> subText = splitText(product_name, 29);
            isLine = true;
            for (String text : subText) {
                byte[] dat = zzLabelText(65, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, text);
                cmd = byteMerger(cmd, dat);
                titleY += 30;
            }
        }
        int lineStartY = titleY + (isLine ? 0 : 35);
//         line demo
        byte[] dat = zzLabelLine(50, lineStartY, 520, lineStartY, 3, 1);
        cmd = byteMerger(cmd, dat);
//
        dat = zzLabelBitmap(300, lineStartY + 10, 40, 80, bmpData_1);
        cmd = byteMerger(cmd, dat);
        // text demo
        dat = zzLabelText(340, lineStartY + 20, 32, 1, 1, 0, 1, 0, 2, 2, false, product_vip_price);
        cmd = byteMerger(cmd, dat);
        int length = chain_name.length();
        Log.e("Laber", length + "");
        if (length <= 10) {
            dat = zzLabelText(80, 140, 16, 1, 1, 0, 1, 0, 2, 2, false, chain_name);
            cmd = byteMerger(cmd, dat);
        } else {
            ArrayList<String> subText = splitText(chain_name, 13);
            int startY = 110;
            for (String text : subText) {
                dat = zzLabelText(80, startY, 16, 1, 1, 0, 1, 0, 2, 2, false, text);
                cmd = byteMerger(cmd, dat);
                startY += 30;
            }
        }
//         barcode demo
        dat = zzLabelBarcode(70, 180, 8, 80, 2, 0, false, qcode);
        cmd = byteMerger(cmd, dat);
        dat = zzLabelText(85, 265, 16, 0, 1, 0, 1, 0, 2, 2, false, qcode);
        cmd = byteMerger(cmd, dat);
        dat = zzLabelText(345, lineStartY + 100, 16, 1, 1, 0, 1, 0, 2, 2, false, "(P.SOCIO)");
        cmd = byteMerger(cmd, dat);
        dat = zzLabelBitmap(350, 195, 40, 80, bmpData_1);
        cmd = byteMerger(cmd, dat);
        // text demo
        dat = zzLabelText(380, 210, 24, 1, 1, 0, 1, 0, 2, 2, false, price);
        cmd = byteMerger(cmd, dat);
        dat = zzLabelEnd();
        cmd = byteMerger(cmd, dat);

        dat = zzLabelPrint();
        cmd = byteMerger(cmd, dat);
        return cmd;
    }
*/

    //zzLabelText中的fontHeight取值范围{16,24,32,48,64,80,96}
    /*
    public static byte[] printDefaultData(String chain_name, String product_name, String product_vip_price, String price, String qcode) {
        price = price.replace(".", ",");
        byte[] cmd = zzLabelStart();
        // text demo
        int titleLenght = product_name.length();
        int titleY = 5;
        boolean isLine = false;
        if (titleLenght <= 29) {
            byte[] dat = zzLabelText(65, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, product_name);
            cmd = byteMerger(cmd, dat);
        } else {
            isLine = true;
            ArrayList<String> subText = splitText(product_name, 29);
            for (String text : subText) {
                byte[] dat = zzLabelText(65, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, text);
                cmd = byteMerger(cmd, dat);
                titleY += 30;
            }
        }
        int lineStartY = titleY + (isLine ? 0 : 35);
//         line demo
        byte[] dat = zzLabelLine(50, lineStartY, 520, lineStartY, 3, 1);
        cmd = byteMerger(cmd, dat);
        //bitmap
        dat = zzLabelBitmap(140, lineStartY + 5, 60, 120, bigByte);
        cmd = byteMerger(cmd, dat);
        // text demo
        dat = zzLabelText(180, lineStartY + 15, 48, 1, 1, 0, 1, 0, 2, 2, false, price);
        cmd = byteMerger(cmd, dat);
//         barcode demo
        dat = zzLabelBarcode(70, 180, 8, 80, 2, 0, false, qcode);
        cmd = byteMerger(cmd, dat);
        dat = zzLabelText(85, 265, 16, 0, 1, 0, 1, 0, 2, 2, false, qcode);
        cmd = byteMerger(cmd, dat);
        int length = chain_name.length();
        if (length <= 10) {
            dat = zzLabelText(340, 205, 16, 1, 1, 0, 1, 0, 2, 2, false, chain_name);
            cmd = byteMerger(cmd, dat);
        } else {
            ArrayList<String> subText = splitText(chain_name, 10);
            int startY = 200;
            for (String text : subText) {
                dat = zzLabelText(340, startY, 16, 1, 1, 0, 1, 0, 2, 2, false, text);
                cmd = byteMerger(cmd, dat);
                startY += 30;
            }
        }
        cmd = byteMerger(cmd, dat);
        dat = zzLabelEnd();
        cmd = byteMerger(cmd, dat);

        dat = zzLabelPrint();
        cmd = byteMerger(cmd, dat);
        return cmd;
    }
*/
    ///////
    ///小字体
    //////
    fun printDefaultData(chain_name: String, product_name: String, price: String, qcode: String): ByteArray {
        var pName = product_name
        var pPrice = price
        pPrice = pPrice.replace(".", ",")
        var cmd = zzLabelStart()
        // text demo
        val titleLenght = pName.length
        var titleY = 35
        var isLine = false
        if (titleLenght <= 29) {
            val emptystr = (29 - titleLenght) / 2
            for (i in 0 until emptystr) {
                pName = " $pName"
            }
            val dat = zzLabelText(110, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, pName)
            cmd = byteMerger(cmd, dat)
        } else {
            isLine = true
            val subText = splitText(pName, 29)
            for (text in subText) {
                val emptystr = (29 - text.length) / 2
                var t: String = " "
                for (i in 0 until emptystr) {
                    t = " $text"
                }
                val dat = zzLabelText(110, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, t)
                cmd = byteMerger(cmd, dat)
                titleY += 30
            }
        }
        val lineStartY = titleY + if (isLine) 0 else 35
        //         line demo
        var dat = zzLabelLine(125, lineStartY, 550, lineStartY, 3, 1)
        cmd = byteMerger(cmd, dat)
        //bitmap
        dat = zzLabelBitmap(210, lineStartY + 5, 40, 80, bmpData_1)
        cmd = byteMerger(cmd, dat)
        // text demo
        dat = zzLabelText(250, lineStartY + 15, 32, 1, 1, 0, 1, 0, 2, 2, false, pPrice)
        cmd = byteMerger(cmd, dat)
        //         barcode demo
        //dat = zzLabelBarcode(110, 210, 8, 80, 2, 0, false, qcode);
        dat = zzLabelBarcode(120, 190, 8, 80, 2, 0, false, qcode)
        cmd = byteMerger(cmd, dat)
        dat = zzLabelText(125, 273, 16, 0, 1, 0, 1, 0, 2, 2, false, qcode)
        cmd = byteMerger(cmd, dat)
        val length = chain_name.length
        if (length <= 10) {
            dat = zzLabelText(390, 190, 16, 1, 1, 0, 1, 0, 2, 2, false, chain_name)
            cmd = byteMerger(cmd, dat)
        } else {
            val subText = splitText(chain_name, 10)
            var startY = 200
            for (text in subText) {
                dat = zzLabelText(380, startY, 16, 1, 1, 0, 1, 0, 2, 2, false, text)
                cmd = byteMerger(cmd, dat)
                startY += 30
            }
        }
        cmd = byteMerger(cmd, dat)
        dat = zzLabelEnd()
        cmd = byteMerger(cmd, dat)

        dat = zzLabelPrint()
        cmd = byteMerger(cmd, dat)
        return cmd
    }

    ///////
///大字体
//////
/*
    public static byte[] printDefaultData(String chain_name, String product_name, String product_vip_price, String price, String qcode) {
        price = price.replace(".", ",");
        byte[] cmd = zzLabelStart();
        // text demo
        int titleLenght = product_name.length();
        int titleY = 35;
        boolean isLine = false;
        if (titleLenght <= 29) {
            int emptystr = (29 - titleLenght)/2;
            for(int i = 0;i< emptystr;i++){
                product_name = " " + product_name;
            }
            byte[] dat = zzLabelText(110, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, product_name);
            cmd = byteMerger(cmd, dat);
        } else {
            isLine = true;
            ArrayList<String> subText = splitText(product_name, 29);
            for (String text : subText) {
                int emptystr = (29 - text.length())/2;
                for(int i = 0;i< emptystr;i++){
                    text = " " + text;
                }
                byte[] dat = zzLabelText(110, titleY, 16, 0, 1, 0, 1, 0, 2, 2, false, text);
                cmd = byteMerger(cmd, dat);
                titleY += 30;
            }
        }
        int lineStartY = titleY + (isLine ? 0 : 35);
//         line demo
        byte[] dat = zzLabelLine(125, lineStartY, 550, lineStartY, 3, 1);
        cmd = byteMerger(cmd, dat);
        //bitmap
        dat = zzLabelBitmap(210, lineStartY + 5, 60, 120, bigByte);
        cmd = byteMerger(cmd, dat);
        // text demo
        dat = zzLabelText(250, lineStartY + 15, 48, 1, 1, 0, 1, 0, 2, 2, false, price);
        cmd = byteMerger(cmd, dat);
//         barcode demo
        //dat = zzLabelBarcode(110, 210, 8, 80, 2, 0, false, qcode);
        dat = zzLabelBarcode(120, 190, 8, 80, 2, 0, false, qcode);
        cmd = byteMerger(cmd, dat);
        dat = zzLabelText(125, 273, 16, 0, 1, 0, 1, 0, 2, 2, false, qcode);
        cmd = byteMerger(cmd, dat);
        int length = chain_name.length();
        if (length <= 10) {
            dat = zzLabelText(390, 190, 16, 1, 1, 0, 1, 0, 2, 2, false, chain_name);
            cmd = byteMerger(cmd, dat);
        } else {
            ArrayList<String> subText = splitText(chain_name, 10);
            int startY = 200;
            for (String text : subText) {
                dat = zzLabelText(380, startY, 16, 1, 1, 0, 1, 0, 2, 2, false, text);
                cmd = byteMerger(cmd, dat);
                startY += 30;
            }
        }
        cmd = byteMerger(cmd, dat);
        dat = zzLabelEnd();
        cmd = byteMerger(cmd, dat);

        dat = zzLabelPrint();
        cmd = byteMerger(cmd, dat);
        return cmd;
    }

    /**
     * 根据splitNum截取一次字符串
     *
     * @param text
     * @return
     */
*/
    private fun splitText(text: String, splitNum: Int): ArrayList<String> {
        val chars = text.toCharArray()
        val length = chars.size
        val subText = ArrayList<String>()
        var sb = StringBuffer()
        for (i in 0 until length) {
            sb.append(chars[i])
            if ((i + 1) % splitNum == 0) {
                subText.add(sb.toString())
                sb = StringBuffer()
            }
        }
        if (length % splitNum != 0) {
            subText.add(sb.toString())
        }
        return subText
    }
}
