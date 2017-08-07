package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by 2016/9/2.
 * 季度枚举
 */
public enum EnumYearType {

    YEAR_2017,
    YEAR;//年;

    private static Map<EnumYearType, String> typeMap = new HashMap<EnumYearType, String>();
    private static Map<String, EnumYearType> valueMap = new HashMap<String, EnumYearType>();
    private static Map<EnumYearType, String> nameMap = new HashMap<EnumYearType, String>();
    private static Map<String, EnumYearType> valueNameMap = new HashMap<String, EnumYearType>();

    static {

        typeMap.put(EnumYearType.YEAR_2017, "2017");
        typeMap.put(EnumYearType.YEAR, "2016");
        nameMap.put(EnumYearType.YEAR_2017, "2017年");
        nameMap.put(EnumYearType.YEAR, "2016年");


        for (EnumYearType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumYearType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumYearType parseName(String name) {
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
        final ArrayList<EnumYearType> list = new ArrayList<EnumYearType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumYearType>() {
            @Override
            public int compare(EnumYearType o1, EnumYearType o2) {
                return o2.value().compareTo(o1.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumYearType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
