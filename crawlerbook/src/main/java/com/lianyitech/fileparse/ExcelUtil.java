package com.lianyitech.fileparse;

public class ExcelUtil {
    public static int getCellNum(String cellStr) {
        char[] cellStrArray = cellStr.toUpperCase().toCharArray();
        int len = cellStrArray.length;
        int n = 0;
        for(int i=0;i<len;i++){
            n += (((int)cellStrArray[i])-65+1)*Math.pow(26, len-i-1);
        }
        return n-1;
    }
    public static void main(String[] args) {
        System.out.print(getCellNum("b"));

    }
}