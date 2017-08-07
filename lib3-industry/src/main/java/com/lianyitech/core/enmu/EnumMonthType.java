package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by 2016/9/2.
 * 月份枚举
 */
public enum EnumMonthType {
    JANUARY ,//一月
    FEBRUARY ,//二月
    MARCH ,//三月
    APRIL ,//四月
    MAY,//五月
    JUNE,//六月
    JULY,//七月
    AUGUST,//八月
    SEPTEMBER ,//九月
    OCTOBER ,//十月
    NOVEMBER ,//十一月
    DECEMBER ;//十二月

    private static Map<EnumMonthType, String> typeMap = new HashMap<EnumMonthType, String>();
    private static Map<String, EnumMonthType> valueMap = new HashMap<String, EnumMonthType>();
    private static Map<EnumMonthType, String> nameMap = new HashMap<EnumMonthType, String>();
    private static Map<String, EnumMonthType> valueNameMap = new HashMap<String, EnumMonthType>();

    static {
        typeMap.put(EnumMonthType.JANUARY, "01");
        typeMap.put(EnumMonthType.FEBRUARY, "02");
        typeMap.put(EnumMonthType.MARCH, "03");
        typeMap.put(EnumMonthType.APRIL, "04");
        typeMap.put(EnumMonthType.MAY, "05");
        typeMap.put(EnumMonthType.JUNE, "06");
        typeMap.put(EnumMonthType.JULY, "07");
        typeMap.put(EnumMonthType.AUGUST, "08");
        typeMap.put(EnumMonthType.SEPTEMBER, "09");
        typeMap.put(EnumMonthType.OCTOBER, "10");
        typeMap.put(EnumMonthType.NOVEMBER, "11");
        typeMap.put(EnumMonthType.DECEMBER, "12");

        nameMap.put(EnumMonthType.JANUARY, "1月");
        nameMap.put(EnumMonthType.FEBRUARY, "2月");
        nameMap.put(EnumMonthType.MARCH, "3月");
        nameMap.put(EnumMonthType.APRIL, "4月");
        nameMap.put(EnumMonthType.MAY, "5月");
        nameMap.put(EnumMonthType.JUNE, "6月");
        nameMap.put(EnumMonthType.JULY, "7月");
        nameMap.put(EnumMonthType.AUGUST, "8月");
        nameMap.put(EnumMonthType.SEPTEMBER, "9月");
        nameMap.put(EnumMonthType.OCTOBER, "10月");
        nameMap.put(EnumMonthType.NOVEMBER, "11月");
        nameMap.put(EnumMonthType.DECEMBER, "12月");

        for (EnumMonthType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumMonthType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumMonthType parseName(String name) {
        if (!valueNameMap.containsKey(name)) {
            throw new IllegalArgumentException(name + " is not a valid");
        }
        return valueNameMap.get(name);
    }

    public String toName() {
        return nameMap.get(this);
    }

    public String getValue() {
        return value();
    }

    public String getName() {
        return toName();
    }

    public static List<Map<String, String>> list() {
        final ArrayList<EnumMonthType> list = new ArrayList<EnumMonthType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumMonthType>() {
            @Override
            public int compare(EnumMonthType o1, EnumMonthType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumMonthType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
