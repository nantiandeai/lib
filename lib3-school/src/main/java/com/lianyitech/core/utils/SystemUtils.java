/**
 *
 */
package com.lianyitech.core.utils;

import com.lianyitech.common.utils.SpringContextHolder;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumBillStatus;
import com.lianyitech.core.enmu.EnumCirculateLogType;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.entity.CirculateStatistics;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lianyitech.common.utils.DoubleUtils.formatDouble;

/**
 * 业务系统工具类
 *
 * @version 2016-09-12
 */
public class SystemUtils {
//
    private static ReaderCardDao readerCardDao = SpringContextHolder.getBean(ReaderCardDao.class);
//    private static BarcodeRecordordDao barcodeRecordDao = SpringContextHolder.getBean(BarcodeRecordDao.class);

    private static Logger logger = LoggerFactory.getLogger(SystemUtils.class);

    /**
     * 生成索书号
     *
     * @param librarsortCode 中图分类号
     * @param tanejiNo       种次号
     * @param assNo          辅助区分号
     * @return indexNum
     */
    public static String getIndexNum(String librarsortCode, String tanejiNo, String assNo) {
        StringBuilder indexNum = new StringBuilder("");
        if (StringUtils.isEmpty(librarsortCode)) {
            return "";
        } else {
            indexNum.append(librarsortCode);
        }
        if (StringUtils.isNotEmpty(tanejiNo)) {
            indexNum.append("/").append(tanejiNo);
        }
        if (StringUtils.isNotEmpty(assNo)) {
            indexNum.append(":").append(assNo);
        }
        return indexNum.toString();
    }

    /**
     * 生成索刊号
     *
     * @param librarsortCode 分类号
     * @param bookTimeNo     书次号
     * @param assNo          辅助区分号
     * @return somNo
     */
    public static String getSomNo(String librarsortCode, String bookTimeNo, String assNo) {
        StringBuilder somNo = new StringBuilder("");
        if (StringUtils.isEmpty(librarsortCode)) {
            return "";
        } else {
            somNo.append(librarsortCode);
        }
        if (StringUtils.isNotEmpty(bookTimeNo)) {
            somNo.append("/").append(bookTimeNo);
        }
        if (StringUtils.isNotEmpty(assNo)) {
            somNo.append(":").append(assNo);
        }
        return somNo.toString();
    }

    /**
     * 索书号生成加著者号
     *
     * @param librarsortCode 分类号
     * @param tanejiNo       种次号
     * @param assNo          辅助区分号
     * @param bookNo         著者号
     * @return indexNum
     */
    public static String getIndexNum1(String librarsortCode, String tanejiNo, String assNo, String bookNo) {
        StringBuilder indexNum = new StringBuilder("");
        if (StringUtils.isEmpty(librarsortCode)) {
            return "";
        } else {
            indexNum.append(librarsortCode);
        }
        if (StringUtils.isNotEmpty(tanejiNo)) {
            indexNum.append("/").append(tanejiNo);
        }
        if (StringUtils.isNotEmpty(bookNo)) {
            indexNum.append("/").append(bookNo);
        }
        if (StringUtils.isNotEmpty(assNo)) {
            indexNum.append(":").append(assNo);
        }
        return indexNum.toString();
    }

    /**
     * 根据输入的ISBN号，检验ISBN的有效性。依据 GB/T 5795-2006 和 ISO 2108:2005 ISBN
     * 10位标准和13位标准实现（13位标准自2007年1月1日开始实行，在此之前采用10位标准）。
     *
     * @param isbn：需要进行校验的ISBN字符串
     * @return true：所输入的ISBN校验正确；<br/> false：所输入的ISBN校验错误
     */
    public static boolean checkISBN(String isbn) {
        isbn = isbn.replace("-", "");//去掉-
        int count = 0;
        int checkBitInt = 0;
        // 将ISBN数据全取大写字母
        //isbn = isbn.toUpperCase();

        char[] cs = isbn.toCharArray();
        switch (isbn.length()) {
            case 10:
                // ISBN为10位数据时，前1位目前只能是“7”（已实行）
                if (!isbn.startsWith("7")) {
                    throw new ISBNFormatException("ISBN-10 格式不符合标准");
                }
                // ****************************************************************
                // 当ISBN为10位时，进行的校验，用于2007年1月1日前的出版物
                // 数据格式：从左至右前9位为ISBN数据，第10位为校验位
                // 校验方法：
                // (1) 从左至右将前9位数据从10开始至2进行编号，作为位权
                // (2) 将9位数据与各位位权进行加权，并求其9位和（称为加权和，记作M）
                // (3) 第10位校验位计算方法，校验位为C：
                //         M + C ≡ 0 (mod 11)
                //  C为10时，记作“X”
                // ****************************************************************
                // 取出前9位数字进行加权和计算
                for (int i = 0; i < 9; i++) {
                    // 若前9位数据中有非数字字符，则抛出异常
                    if (cs[i] < '0' || cs[i] > '9') {
                        throw new ISBNFormatException("ISBN " + isbn + " 第 " + (i + 1) + " 位中出现非法字符 " + cs[i]);
                    }


                    /***产品要求屏蔽除**/
//                    int c = cs[i] - '0';
//                    // 求加权和
//                    count += c * (10 - i);

                    /***产品要求屏蔽除**/
                }
                /***产品要求屏蔽除**/
                // 取出校验位数据0～9和X符合校验字符要求
//                if (cs[9] >= '0' && cs[9] <= '9') {
//                    checkBitInt = cs[9] - '0';
//                } else if (cs[9] == 'X' || cs[9] == 'x') {
//                    // 校验位中的“X”表示数据“10”
//                    checkBitInt = 10;
//                } else {
//                    // 非0～9或X时抛出异常
//                    throw new ISBNFormatException("ISBN " + isbn + " 第 10 位中出现非法字符 " + cs[9]);
//                }
//                // 进行校验
//                if ((count + checkBitInt) % 11 == 0) {
//                    return true; // 校验成功
//                } else {
//                    return false; // 校验失败
//                }
                return true;
            /***产品要求屏蔽除**/
            case 13:
                // ****************************************************************
                // 当ISBN为13位时，进行的校验，用于2007年1月1日后的出版物
                // 数据格式：从左至右前12位为ISBN数据，第13位为校验位
                // 校验方法：
                // (1) 从左至右将前12位数的取其奇位数和和偶位数和
                // (2) 将偶位数和乘3，并其与奇位数和的和，得加权和
                // (3) 第13位校验位计算方法，校验位为C：
                //         M + C ≡ 0 (mod 10)
                // ****************************************************************
                // ISBN为13位数据时，前4位目前只能是“978”（已实行）或“979”（暂未实行）
                if (!isbn.startsWith("978") && !isbn.startsWith("979")) {
                    throw new ISBNFormatException("ISBN-13 格式不符合标准");
                }
                // 取出前12位数字进行加权和计算
                int countEven = 0;
                int countOdd = 0;
                for (int i = 0; i < 12; i++) {
                    int c = cs[i] - '0';
                    // 若前12位数据中有非数字字符，则抛出异常
                    if (c < 0 || c > 9) {
                        throw new ISBNFormatException("ISBN " + isbn + " 第 " + (i + 1) + " 位中出现非法字符 " + cs[i]);
                    }
                    /***产品要求屏蔽除**/
                    // 分别计算奇位数和偶位数的和
//                    if ((i & 0x1) == 0) {
//                        countOdd += c;
//                    } else {
//                        countEven += c;
//                    }
                    /***产品要求屏蔽除**/
                }

                /***产品要求屏蔽除**/
                // 求加权和
//                count = countOdd + (countEven * 3);
//                // 取出校验位数据
//                if (cs[12] < '0' || cs[12] > '9') {
//                    // 校验位为非0~9字符时，抛出异常
//                    throw new ISBNFormatException("ISBN " + isbn + " 第 13 位中出现非法字符 " + cs[12]);
//                }
//                checkBitInt = cs[12] - '0';
//                // 进行校验
//                if ((count + checkBitInt) % 10 == 0) {
//                    return true; // 校验成功
//                } else {
//                    return false; // 校验失败
//                }
                /***产品要求屏蔽除**/
                return true;
            default:
                // ISBN为非10位或13位时抛出异常
                throw new ISBNFormatException("ISBN 格式不符合标准");
        }
    }

    /**
     * ISSN校验规则  xxxx-xxxx 或者 xxxx—xxxx
     * 最后一位可以为X
     * 必须数字，必须中间加- —
     * 只有这两种
     *
     * @param issn issn
     * @return boolean
     */
    public static boolean checkISSN(String issn) {
        //必须含-而且一定要在第五个字符
        if (issn.length() != 9) {
            return false;
        }
        if (!issn.substring(4, 5).equals("-") && !issn.substring(4, 5).equals("—")) {
            return false;
        }


        Pattern pattern = Pattern.compile("^[0-9]\\d*$");
        if (issn.contains("-")) {
            issn = issn.trim().replaceFirst("-", "");
        }
        if (issn.contains("—")) {
            issn = issn.replaceFirst("—", "");
        }
        issn = issn.replace(" ", "");
        if (issn.length() != 8) {
            return false;
        }
        String issn17 = issn.substring(0, 7);
        String issn8 = issn.substring(7, 8);
        Matcher matcher = pattern.matcher(issn17);
        if (!matcher.matches()) {
            return false;
        }
        Matcher matcher1 = pattern.matcher(issn8);
        Boolean b = issn8.equalsIgnoreCase("X");
        if (!matcher1.matches() && !b) {
            return false;
        }
        return true;

    }

    /**
     * s
     * 种次号是必填项，且数值为1-99999的区间的纯数字。必须是纯数字。当数值为非纯数字时，导入失败，错误提示为：种次号格式错误。
     *
     * @param tanejiNo
     * @return boolean
     */
    public static boolean checkTanejiNo(String tanejiNo) {
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        if (StringUtils.isNotEmpty(tanejiNo)) {
            Matcher matcher = pattern.matcher(tanejiNo);
            if (!matcher.matches()) {
                return false;
            }
            if (tanejiNo.length() > 5) {
                return false;
            }
        }
        return true;

    }

    /**
     * 著者号校验修改为字母和数字，不校验首字母英文
     *
     * @param bookNo
     * @return
     */
    public static boolean checkBookNo(String bookNo) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        if (StringUtils.isNotEmpty(bookNo)) {
            Matcher matcher = pattern.matcher(bookNo);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }


    /**
     * 分类号验证
     * 需求：
     * ①首字母校验，输入的分类号中必须以首字母开头，仅支持22个字母： A","B","C","D","E","F","G","H","I","J","K","N","O","P","Q","R","S","T","U","V","X","Z；
     * ②其中T类要求支持双字母TB","TD","TE","TF","TG","TH","TJ","TK","TL","TM","TN","TP","TQ","TS","TU","TV，其他类禁止双字母或多字母；
     * ③禁止单独的符号","数字","中文；
     * ④禁用中文汉字，包括简/繁体；
     *
     * @param sortCode 分类号
     * @return boolen 验证是否通过
     */
    public static boolean checkSortCode(String sortCode) {

        boolean isOk;

        sortCode = StringUtils.ToDBC(sortCode.trim());

        //验证特殊字符，只允许如下10个特殊字符、数字和字母
        Pattern pSpecialCharacter = Pattern.compile("[0-9aA-Z:=\\-\\/\\.\\[\\]\\(\\)\\\"<>\\+]*$");
        Matcher mSpecialCharacter = pSpecialCharacter.matcher(sortCode);
        isOk = mSpecialCharacter.matches();
        if (!isOk) {
            return false;
        }

        //单独处理a，首两个字母为a,要转成大写，后面的a不要转，且只能存在一个a
        if (sortCode.length() <= 2) {
            sortCode = sortCode.toUpperCase();
        } else {
            sortCode = sortCode.substring(0, 2) + sortCode.substring(2, sortCode.length()).replace("a", "@");
            sortCode = sortCode.toUpperCase();

            //除了前两位可以为字母，后面都不能出现字母（除了小写a）
            Pattern pNotLetterAfter = Pattern.compile("[A-Z]+");
            Matcher mNotLetterAfter = pNotLetterAfter.matcher(sortCode.substring(2, sortCode.length()));
            isOk = mNotLetterAfter.find();
            if (isOk) {
                return false;
            }

            sortCode = sortCode.replace("@", "a");
            int counter = 0;
            for (int i = 0; i < sortCode.length(); i++) {
                if (sortCode.substring(i, i + 1).equals("a")) {
                    counter++;
                }
            }
            if (counter >= 2) {
                return false;
            }
        }

        //验证是否首字母是指定的22个
        Pattern pFristChar = Pattern.compile("^[ABCDEFGHIJKNOPQRSTUVXZ].*");
        Matcher mFristChar = pFristChar.matcher(sortCode);
        isOk = mFristChar.matches();
        if (!isOk) {
            return false;
        }

        //验证是否包含中文
        Pattern pChinese = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher mChinese = pChinese.matcher(sortCode);
        isOk = !mChinese.find();
        if (!isOk) {
            return false;
        }

        //验证首单字母和数字
        //如下的：分类号不存在。
        /*String[] fristCharAndNumberStr = {"A0", "A6", "A9", "F9", "G9", "O0", "O8", "O9",
                "P0", "P8", "Q0", "R0", "S0", "U0", "U3", "U5", "U7", "U9", "V0", "V6", "V8", "V9", "X0", "X6", "Z0", "Z7", "Z9"};

        Pattern pFristCharAndNumber = Pattern.compile("^[A-Z]{1}[0-9]{1}.*");
        Matcher mFristCharAndNumber = pFristCharAndNumber.matcher(sortCode);
        if (mFristCharAndNumber.matches()) {
            isOk = true;
            for (String fristCharAndNumber : fristCharAndNumberStr) {
                if (sortCode.substring(0, 2).contains(fristCharAndNumber)) {
                    isOk = false;
                    break;
                }
            }
        }
        if (!isOk) {
            return false;
        }*/

        //双字母只存在如下：开头只能是T类和D类
        String[] startTwoCharStr = {"TA","TB","TC","TD","TE","TF","TG","TH","TI","TJ","TK","TL","TM","TN","TO","TP","TQ","TR","TS","TT","TU","TV","TW","TX","TY","TZ",
        "DA","DB","DC","DD","DE","DF","DG","DH","DI","DJ","DK","DL","DM","DN","DO","DP","DQ","DR","DS","DT","DU","DV","DW","DX","DY","DZ"};
        Pattern pStartTwoChar = Pattern.compile("^[A-Z]{1}[A-Z]{1}.*");
        Matcher mStartTwoChar = pStartTwoChar.matcher(sortCode);
        if (mStartTwoChar.matches()) {
            isOk = false;
            for (String startTwoChar : startTwoCharStr) {
                if (sortCode.substring(0, 2).contains(startTwoChar)) {
                    isOk = true;
                    break;
                }
            }
        }
        if (!isOk) {
            return false;
        }

        //如下的：分类号不存在。
        /*String[] startThreeCharStr = {"TB0", "TD0", "TE7", "TF2", "TF9", "TG0", "TH0", "TH5", "TH9", "TJ1", "TL0", "TH5",
                "TP0", "TP4", "TP5", "TP9", "TQ7", "TQ8", "TU0", "TV0"};
        Pattern pStartThreeChar = Pattern.compile("^[A-Z]{1}[A-Z]{1}[0-9]{1}.*");
        Matcher mStartThreeChar = pStartThreeChar.matcher(sortCode);
        if (mStartThreeChar.matches()) {
            isOk = true;
            for (String startThreeChar : startThreeCharStr) {
                if (sortCode.substring(0, 3).contains(startThreeChar)) {
                    isOk = false;
                    break;
                }
            }
        }
        if (!isOk) {
            return false;
        }*/

        //首字母-数字:总计有160个，除这160之外，其他的字母-数字的组合都提示分类号不存在
//        Pattern pCharConnectNumber = Pattern.compile("^[A-Z]{1}\\-[0-9].*");
//        Matcher mCharConnectNumber = pCharConnectNumber.matcher(sortCode);
//
//        Pattern pTwoCharConnectNumber = Pattern.compile("^[A-Z]{2}\\-[0-9].*");
//        Matcher mTwoCharConnectNumber = pTwoCharConnectNumber.matcher(sortCode);
//
//        if (mCharConnectNumber.matches()||mTwoCharConnectNumber.matches()) {
//            isOk = false;
//            for (String startTwoChar : LibrarySortCode.librarySortCode) {
//                if (sortCode.contains(startTwoChar)) {
//                    isOk = true;
//                    break;
//                }
//            }
//        }

        return isOk;
    }

    /**
     * 根据流通操作日志类型得到操作单据状态
     *
     * @param circulateLogType circulateLogType
     * @return circulateLogType
     */
    public static String getBillStatusByCirculateLogType(String circulateLogType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(EnumCirculateLogType.BORROW.getValue(), EnumBillStatus.BORROW.getValue());
        map.put(EnumCirculateLogType.RETURN.getValue(), EnumBillStatus.RETURN.getValue());
        map.put(EnumCirculateLogType.LOSS.getValue(), EnumBillStatus.LOSS.getValue());
        map.put(EnumCirculateLogType.OUT_OLD.getValue(), EnumBillStatus.OUT_OLD.getValue());
        map.put(EnumCirculateLogType.SCRAP.getValue(), EnumBillStatus.SCRAP.getValue());
        map.put(EnumCirculateLogType.ORDER_BORROW.getValue(), EnumBillStatus.ORDER_BORROW.getValue());
        map.put(EnumCirculateLogType.RENEW.getValue(), EnumBillStatus.RENEW.getValue());
        map.put(EnumCirculateLogType.STAINED.getValue(), EnumBillStatus.STAINED.getValue());
        map.put(EnumCirculateLogType.CANCEL_ORDER.getValue(), EnumBillStatus.CANCEL_ORDER.getValue());
        map.put(EnumCirculateLogType.CANCEL_ORDER_BORROW.getValue(), EnumBillStatus.CANCEL_ORDER_BORROW.getValue());
        return map.get(circulateLogType);
    }

    /**
     * map转实体类
     *
     * @param map map
     * @param obj obj
     */
    public static void transMapToBean(Map<String, Object> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            logger.debug("transMapToBean Error " + e);
        }
    }

    /**
     * 把中文的符号转成英文的，目前只有“／”　“－”
     *
     * @param args 需要转换的字符串
     * @return String 已经转好的字符串
     */
    public static String changeChineseSymbol(String args) {
        //把中文－- 和／/换成英文的,让他们存在数据库是英文的，匹配的时候，也是
        if (StringUtils.isNotEmpty(args)) {
            if (args.contains("－")) {
                args = args.replace("－", "-");
            }
            if (args.contains("／")) {
                args = args.replace("／", "/");
            }
        }
        return args;
    }


    /**
     * 判断条形码读者证唯一
     *
     * @param barcodeCard 读者证条形码
     * @param id          主键
     * @return
     */
    public static Boolean isTrueCardBarcode(String barcodeCard, String id) {
        HashMap param = new HashMap<>();
        param.put("barcodeCard", barcodeCard);
        param.put("id", id);
        return isTrueCardBarcode(param);
    }

    /**
     * 判断条形码读者证唯一
     *
     * @param param 条件参数（扩展条件）
     * @return
     */
    public static Boolean isTrueCardBarcode(Map<String, Object> param) {
        if (param == null) {
            param = new HashMap<>();
        }
        param.put("orgId", UserUtils.getOrgId());
        //String barcodeCard = param.get("barcodeCard") != null ? param.get("barcodeCard").toString() : "";
        List<String> cardList = readerCardDao.findBarCode(param);
        if (cardList!=null && cardList.size()>0) {
            return false;
        }
        return true;
    }

    /**
     * 将给定字符串str左填充"0",直到str的位数等于len
     *
     * @param str 需要左边补充0填充的字符串
     * @param len 长度
     * @return 字符串
     */
    public static String leftPaddingZero(String str, int len) {
        //判断str字符串是否为空或者null
        if (str != null && !"".equals(str)) {
            //字符串长度小于指定长度，需要左填充
            if (str.length() < len) {
                //1.使用字符串的格式化，先左填充空格
                String format = "%" + len + "s";
                String tempResult = String.format(format, str);
                //2.使用String的replace函数将空格转换为指定字符即可
                return tempResult.replace(" ", "0");
            } else {
                return str;
            }
        } else {
            return "";
        }
    }

    public static Map resolveCirculateRate(List<CirculateStatistics> crList) {
        Map<String, Object> map = new HashMap<>();
        CirculateStatistics cRate = new CirculateStatistics();
        try {
            ReportCommon.totalcommon(crList, new String[]{"bookNum", "bookSpecies", "classPrice"}, cRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cRate.setBookNum(cRate.getBookNum() != null ? cRate.getBookNum() : 0);
        cRate.setBookSpecies(cRate.getBookSpecies() != null ? cRate.getBookSpecies() : 0);
        cRate.setClassPrice(cRate.getClassPrice() != null ? formatDouble(cRate.getClassPrice()) : 0d);
        cRate.setTotalSpeciesRate(cRate.getTotalSpeciesRate() != null ? cRate.getTotalSpeciesRate() : "0.00%");
        cRate.setTotalBookNumRate(cRate.getTotalBookNumRate() != null ? cRate.getTotalBookNumRate() : "0.00%");
        cRate.setTotalpriceRate(cRate.getTotalpriceRate() != null ? cRate.getTotalpriceRate() : "0.00%");
        for (CirculateStatistics cr : crList) {
            DecimalFormat df=new DecimalFormat(".##");
            Double.valueOf(df.format(cr.getClassPrice()!= null ? cr.getClassPrice() : 0d));

            double d=1252.2563;
            String st=df.format(d);
            cr.setClassPrice(formatDouble(cr.getClassPrice()!= null ? cr.getClassPrice() : 0d));
            cr.setTotalBookNum(cRate.getBookNum());
            cr.setTotalSpecies(cRate.getBookSpecies());
            cr.setTotalPrice(cRate.getClassPrice());
            if (!cr.getSpeciesRate().equals("0.00%")) {
                cRate.setTotalSpeciesRate("100.00%");
            }
            if (!cr.getBookNumRate().equals("0.00%")) {
                cRate.setTotalBookNumRate("100.00%");
            }
            if (!cr.getPriceRate().equals("0.00%")) {
                cRate.setTotalpriceRate("100.00%");
            }
            if (cr.getBookNum() == null) {
                cr.setBookNum(0);
            }
            if (cr.getBookSpecies() == null) {
                cr.setBookSpecies(0);
            }
            if (cr.getClassPrice() == null) {
                cr.setClassPrice(0.00);
            }
        }
        map.put("detailList", crList);
        map.put("totalBookNum", cRate.getBookNum());
        map.put("totalSpecies", cRate.getBookSpecies());
        map.put("totalPrice", formatDouble(cRate.getClassPrice()));
        map.put("totalSpeciesRate", cRate.getTotalSpeciesRate());
        map.put("totalBookNumRate", cRate.getTotalBookNumRate());
        map.put("totalpriceRate", cRate.getTotalpriceRate());
        return map;
    }

    /**
     * 格式化double保留2位小数
     * @param amount
     * @return
     */
    public static Double formatDouble(Double amount){
        return Double.valueOf(new DecimalFormat(".##").format(amount));
    }

}
    class ISBNFormatException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public ISBNFormatException() {
            super("ISBN Error ...");
        }

        public ISBNFormatException(String arg0) {
            super(arg0);
        }
    }
