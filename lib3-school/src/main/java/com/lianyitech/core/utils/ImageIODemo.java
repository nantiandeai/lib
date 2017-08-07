package com.lianyitech.core.utils;


import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.jxls.WriteBookLabelExportExcel;
import com.lianyitech.modules.circulate.entity.CardPrintConfig;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.PrintDTO;
import com.lianyitech.modules.circulate.entity.Reader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 绘制读者证
 */
public class ImageIODemo {

    /**
     * 创建一个BufferedImage图片缓冲区对象并指定它宽高和类型 这样相当于创建一个画板，然后可以在上面画画
     */
    public BufferedImage bi = new BufferedImage(6300, 8910, BufferedImage.TYPE_INT_BGR);

    /**
     * 要生成图片的类型,可以是JPG GIF JPEG PNG等...
     */
    public static final String picType = "png";

    /**
     * 成生成图片的保存路径和图片名称
     */
    public final File file = new File("D:\\imgs\\my." + picType);


    /**
     * 打印设置预览专用
     * @param config
     * @param reader
     * @param filePath
     */
    public static void writeOneImage(CardPrintConfig config,Reader reader, String filePath){
        BufferedImage b = new BufferedImage(2700, 1700, BufferedImage.TYPE_INT_BGR);
        // 拿到画笔
        Graphics g = b.getGraphics();
        // 画一个图形系统默认是白色
        g.fillRect(0, 0, 2700, 1700);
        // 设置画笔颜色
        g.setColor(new Color(0, 0, 0));
        PrintDTO dto = getOne();
        dto.setReader(reader);
        writeOne(g,dto,config);
        // 释放画笔
        g.dispose();
        // 将画好的图片通过流形式写到硬盘上
        boolean val = false;
        try {
            val = ImageIO.write(b, "png", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 通过指定参数写一个图片
//     *
//     * @param bi
//     * @param picType
//     * @param file
//     * @return boolean 如果失败返回一个布尔值
//     */
//    public boolean writeImage(BufferedImage bi, String picType, File file) {
//        // 拿到画笔
//        Graphics g = bi.getGraphics();
//        // 画一个图形系统默认是白色
//        g.fillRect(0, 0, 6300, 8910);
//        // 设置画笔颜色
//        g.setColor(new Color(0, 0, 0));
//
//        java.util.List<PrintDTO> list = getList();
//        int i = 1;
//        for (PrintDTO dto : list) {
//            /*********校徽*********/
//            try {
//                Image src = ImageIO.read(new File("D:\\imgs\\001.jpg"));
//                g.drawImage(src, dto.getSchoolLogoX(), dto.getSchoolLogoY(), 240, 240, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            /*********校名*********/
//            // 设置画笔画出的字体风格
//                g.setFont(new Font("微软雅黑", Font.PLAIN, 80));
//            g.setColor(new Color(53, 78, 162));
//            g.drawString("北雅中学", dto.getSchoolNameX(), dto.getSchoolNameY());
//
//            /*********借阅证*********/
//            g.setFont(new Font("微软雅黑", Font.BOLD, 204));
//            g.setColor(new Color(53, 78, 162));
//            g.drawString("借阅证", dto.getTitleBorrowCardX(), dto.getTitleBorrowCardY());
//
//            /*********读者头像*********/
//            try {
//                Image src = ImageIO.read(new File("D:\\imgs\\002.jpg"));
//                g.drawImage(src, dto.getCardPhotoX(), dto.getCardPhotoY(), 750, 1050, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            /*********姓名*********/
//            g.setFont(new Font("微软雅黑", Font.PLAIN, 120));
//            g.setColor(new Color(0, 0, 0));
//            g.drawString("姓名：", dto.getTitleNameX(), dto.getTitleNameY());
//
//            /*********姓名-内容*********/
//            g.setFont(new Font("微软雅黑", Font.BOLD, 120));
//            g.setColor(new Color(0, 0, 0));
//            g.drawString("李白" + i, dto.getNameX(), dto.getNameY());
//            i++;
//
//            /*********名字下划线********/
//            g.drawLine(dto.getNameUnderlineX1(), dto.getNameUnderlineY1(), dto.getNameUnderlineX2(), dto.getNameUnderlineY2());//画线
//            g.drawLine(dto.getNameUnderlineX1(), dto.getNameUnderlineY1() + 1, dto.getNameUnderlineX2(), dto.getNameUnderlineY2() + 1);//画线
//
//            /*********条码*********/
//            try {
//                Image src = ImageIO.read(new File("D:\\imgs\\barcode.png"));//1128  367
//                g.drawImage(src, dto.getBarCodeX(), dto.getBarCodeY(), dto.getBarCodeWidth(), 367, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            /*********外层边框*********/
//            g.setColor(new Color(0, 0, 0));
//            g.drawRect(dto.getOutBorderX(), dto.getOutBorderY(), 2568, 1620);
//            g.drawRect(dto.getOutBorderX() + 1, dto.getOutBorderY() + 1, 2568, 1620);
//        }
//
//        // 释放画笔
//        g.dispose();
//        // 将画好的图片通过流形式写到硬盘上
//        boolean val = false;
//        try {
//            val = ImageIO.write(bi, picType, file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return val;
//    }


    public static void writeOne(Graphics g,PrintDTO dto,CardPrintConfig config){
        {
            /*********校徽*********/
            try {
                Image src = ImageIO.read(new File(config.getSchoolBadge()));
                g.drawImage(src, dto.getSchoolLogoX(), dto.getSchoolLogoY(), 240, 240, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Reader reader = dto.getReader();
            /*********校名*********/
            // 设置画笔画出的字体风格
            g.setFont(new Font(config.getCompFont(), Font.PLAIN, config.getCompFontSize()));
            g.setColor(new Color(53, 78, 162));
            g.drawString(config.getCompName(), dto.getSchoolNameX(), dto.getSchoolNameY());

            /*********借阅证*********/
            g.setFont(new Font(config.getCardFont(), Font.BOLD, config.getCardFontSize()));
            g.setColor(new Color(53, 78, 162));
            g.drawString(config.getCardName(), dto.getTitleBorrowCardX(), dto.getTitleBorrowCardY());

            String readerImage = WriteBookLabelExportExcel.class.getClassLoader().getResource("").getPath() + "imgs/defaultReader.jpg";
            if(config.getPrintImage().equals("1")) {
                if(StringUtils.isNotBlank(reader.getImg())) {
                    readerImage = Global.getUploadRootPath() + reader.getImg();
                }
            }
            /*********读者头像*********/
            try {
                Image src = ImageIO.read(new File(readerImage));
                g.drawImage(src, dto.getCardPhotoX(), dto.getCardPhotoY(), 750, 1050, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*********姓名*********/
            g.setFont(new Font("微软雅黑", Font.PLAIN, 120));
            g.setColor(new Color(0, 0, 0));
            g.drawString("姓名：", dto.getTitleNameX(), dto.getTitleNameY());

            /*********姓名-内容*********/
            g.setFont(new Font("微软雅黑", Font.BOLD, 120));
            g.setColor(new Color(0, 0, 0));
            g.drawString(reader.getName(), dto.getNameX(), dto.getNameY());

            /*********名字下划线********/
            g.drawLine(dto.getNameUnderlineX1(), dto.getNameUnderlineY1(), dto.getNameUnderlineX2(), dto.getNameUnderlineY2());//画线
            g.drawLine(dto.getNameUnderlineX1(), dto.getNameUnderlineY1() + 1, dto.getNameUnderlineX2(), dto.getNameUnderlineY2() + 1);//画线

            /*********条码*********/
            try {
                Image src = ImageIO.read(new File(reader.getCardImg()));//1128  367
                g.drawImage(src, dto.getBarCodeX(), dto.getBarCodeY(), dto.getBarCodeWidth(), 367, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*********外层边框*********/
            g.setColor(new Color(0, 0, 0));
            g.drawRect(dto.getOutBorderX(), dto.getOutBorderY(), 2568, 1620);
            g.drawRect(dto.getOutBorderX() + 1, dto.getOutBorderY() + 1, 2568, 1620);
        }
    }



    public static void main(String[] args) {
        ImageIODemo image = new ImageIODemo();
//        image.writeImage(image.bi, image.picType, image.file);
    }

    /**
     * 一次处理8个
     * @param readerList
     * @param config
     * @param imageFilePath
     */
    public static void writeImage(java.util.List<Reader> readerList,CardPrintConfig config,String imageFilePath){
        java.util.List<PrintDTO> list = getList(readerList);
        BufferedImage bi = new BufferedImage(6300, 8910, BufferedImage.TYPE_INT_BGR);
        File file = new File(imageFilePath);
        Graphics g = bi.getGraphics();
        // 画一个图形系统默认是白色
        g.fillRect(0, 0, 6300, 8910);
        // 设置画笔颜色
        g.setColor(new Color(0, 0, 0));
        config.setSchoolBadge(Global.getUploadRootPath() + config.getSchoolBadge());
       // int i = 1;
        for (int j = 0 ; j < list.size() ; j ++) {
            PrintDTO dto = list.get(j);
            dto.getReader().setCardImg(Global.getUploadRootPath() + dto.getReader().getCardImg());
            writeOne(g,dto,config);
        }
        // 释放画笔
        g.dispose();
        // 将画好的图片通过流形式写到硬盘上
        boolean val = false;
        try {
            val = ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrintDTO getOne() {
        Integer schoolLogoX = 205;
        Integer schoolLogoY = 205;
        Integer schoolNameX = 465;
        Integer schoolNameY = 345;
        Integer titleBorrowCardX = 1678;
        Integer titleBorrowCardY = 365;
        Integer cardPhotoX = 208;
        Integer cardPhotoY = 497;
        Integer titleNameX = 1198;
        Integer titleNameY = 997;
        Integer nameX = 1698;
        Integer nameY = 1007;
        Integer nameUnderlineX1 = 1553;
        Integer nameUnderlineY1 = 1034;
        Integer nameUnderlineX2 = 2316;
        Integer nameUnderlineY2 = 1034;
        Integer barCodeX = 1548;
        Integer barCodeY = 1095;
        Integer barCodeWidth = 464;
        Integer outBorderX = 65;
        Integer outBorderY = 65;
        Integer spaceX = 0;//间距-X坐标
        Integer spaceY = 0;//间距-Y坐标
        PrintDTO dto = new PrintDTO();
        barCodeX = 1198;
        barCodeWidth = 1028;
        dto.setSchoolLogoX(schoolLogoX + spaceX);
        dto.setSchoolLogoY(schoolLogoY + spaceY);
        dto.setSchoolNameX(schoolNameX + spaceX);
        dto.setSchoolNameY(schoolNameY + spaceY);
        dto.setTitleBorrowCardX(titleBorrowCardX + spaceX);
        dto.setTitleBorrowCardY(titleBorrowCardY + spaceY);
        dto.setCardPhotoX(cardPhotoX + spaceX);
        dto.setCardPhotoY(cardPhotoY + spaceY);
        dto.setTitleNameX(titleNameX + spaceX);
        dto.setTitleNameY(titleNameY + spaceY);
        dto.setNameX(nameX + spaceX);
        dto.setNameY(nameY + spaceY);
        dto.setNameUnderlineX1(nameUnderlineX1 + spaceX);
        dto.setNameUnderlineY1(nameUnderlineY1 + spaceY);
        dto.setNameUnderlineX2(nameUnderlineX2 + spaceX);
        dto.setNameUnderlineY2(nameUnderlineY2 + spaceY);
        dto.setBarCodeX(barCodeX + spaceX);
        dto.setBarCodeY(barCodeY + spaceY);
        dto.setBarCodeWidth(barCodeWidth);
        dto.setOutBorderX(outBorderX + spaceX);
        dto.setOutBorderY(outBorderY + spaceY);
        return dto;

    }

    public static java.util.List<PrintDTO> getList(java.util.List<Reader> readerList) {
        java.util.List<PrintDTO> list = new ArrayList<PrintDTO>();
        Integer schoolLogoX = 682;
        Integer schoolLogoY = 905;
        Integer schoolNameX = 942;
        Integer schoolNameY = 1045;
        Integer titleBorrowCardX = 2155;
        Integer titleBorrowCardY = 1065;
        Integer cardPhotoX = 685;
        Integer cardPhotoY = 1197;
        Integer titleNameX = 1675;
        Integer titleNameY = 1697;
        Integer nameX = 2175;
        Integer nameY = 1707;
        Integer nameUnderlineX1 = 2030;
        Integer nameUnderlineY1 = 1734;
        Integer nameUnderlineX2 = 2793;
        Integer nameUnderlineY2 = 1734;
        Integer barCodeX = 2025;
        Integer barCodeY = 1795;
        Integer barCodeWidth = 564;
        Integer outBorderX = 462;
        Integer outBorderY = 765;

//        //根据条码长度设置条码图X坐标、宽度
//        String barCode = "0001000100010001";
//        if (barCode.length() > 10 && barCode.length() <= 20) {
//            barCodeX = 1675;
//            barCodeWidth = 1128;
//        } else if (barCode.length() > 20) {
//            barCodeX = 1625;
//            barCodeWidth = 1350;
//        }

        Integer spaceX = 0;//间距-X坐标
        Integer spaceY = 0;//间距-Y坐标
        for (int i = 1; i <= 8 && i<=readerList.size(); i++) {
            if (i == 3 || i == 4) {
                spaceY = 1 * (291 + 1620);
            }
            if (i == 5 || i == 6) {
                spaceY = 2 * (291 + 1620);
            }
            if (i == 7 || i == 8) {
                spaceY = 3 * (291 + 1620);
            }
            if (i % 2 == 0) {
                spaceX = 288 + 2568;
            } else {
                spaceX = 0;
            }
            String barCode = readerList.get(i-1).getCard();
            if (barCode.length() > 10 && barCode.length() <= 20) {
                barCodeX = 1675;
                barCodeWidth = 1128;
            } else if (barCode.length() > 20) {
                barCodeX = 1625;
                barCodeWidth = 1350;
            }
            PrintDTO dto = new PrintDTO();
            dto.setSchoolLogoX(schoolLogoX + spaceX);
            dto.setSchoolLogoY(schoolLogoY + spaceY);
            dto.setSchoolNameX(schoolNameX + spaceX);
            dto.setSchoolNameY(schoolNameY + spaceY);
            dto.setTitleBorrowCardX(titleBorrowCardX + spaceX);
            dto.setTitleBorrowCardY(titleBorrowCardY + spaceY);
            dto.setCardPhotoX(cardPhotoX + spaceX);
            dto.setCardPhotoY(cardPhotoY + spaceY);
            dto.setTitleNameX(titleNameX + spaceX);
            dto.setTitleNameY(titleNameY + spaceY);
            dto.setNameX(nameX + spaceX);
            dto.setNameY(nameY + spaceY);
            dto.setNameUnderlineX1(nameUnderlineX1 + spaceX);
            dto.setNameUnderlineY1(nameUnderlineY1 + spaceY);
            dto.setNameUnderlineX2(nameUnderlineX2 + spaceX);
            dto.setNameUnderlineY2(nameUnderlineY2 + spaceY);
            dto.setBarCodeX(barCodeX + spaceX);
            dto.setBarCodeY(barCodeY + spaceY);
            dto.setBarCodeWidth(barCodeWidth);
            dto.setOutBorderX(outBorderX + spaceX);
            dto.setOutBorderY(outBorderY + spaceY);
            dto.setReader(readerList.get(i-1));
            list.add(dto);
        }

        return list;

    }


}