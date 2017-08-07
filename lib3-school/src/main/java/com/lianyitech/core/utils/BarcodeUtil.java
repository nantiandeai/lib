package com.lianyitech.core.utils;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 条形码工具类
 */
public class BarcodeUtil {

    /**
     * 生成文件
     *
     * @param msg
     * @param path
     * @return
     */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成字节
     *
     * @param msg
     * @return
     */
    public static byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous);
        return ous.toByteArray();
    }

    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream ous) {
        if (null == msg || msg.equals("") || ous == null) {
            return;
        }

        Code128Bean bean = new Code128Bean();

        // 精细度
        final int dpi = 300;//影响高度
        // module宽度
        final double moduleWidth = UnitConv.in2mm(2.5f / dpi);//影响宽度

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.setBarHeight(7);
        bean.doQuietZone(false);

        String format = "image/png";
        try {
            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // 生成条形码
            bean.generateBarcode(canvas, msg);
            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        //A0001B0001C0001D00001E00001  00000019`
        String msg = "A0001B0001C0001D00001E00001";
        String path = "D:\\imgs\\barcode.png";
        generateFile(msg, path);
    }
}