package com.siyoumarket.dprint;

public class NativeUtil {
    static {

        System.loadLibrary("native-lib");
    }

//    public native static void keyInit(int key, int key2);
//
//    public native static void encodeBuffer(byte[] buf);
//
//    public native static void KeyInit(String key);
//
//    public native static void EncodeBuffer(byte[] buf);
//
//    public native static int getPrintPictureCommand(byte[] out, byte[] in, int w_bytes);
//
//    public native static int getPrintPictureLineCommand(byte[] out, byte[] in, int width, int height);
//
//    public native static int getPrintPictureLineBufferCommand(byte[] out, byte[] in, int width, int height);

}
