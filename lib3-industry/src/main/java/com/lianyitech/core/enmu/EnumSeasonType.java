package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by 2016/9/2.
 * 季度枚举
 */
public enum EnumSeasonType {
    FIRSTQUARTER,//第一季度
    SECONDQUARTER,//第二季度
    THIRDQUARTER ,//第三季度
    FOURTHQUARTER;//第四季度
    private static Map<EnumSeasonType, String> typeMap = new HashMap<EnumSeasonType, String>();
    private static Map<String, EnumSeasonType> valueMap = new HashMap<String, EnumSeasonType>();
    private static Map<EnumSeasonType, String> nameMap = new HashMap<EnumSeasonType, String>();
    private static Map<String, EnumSeasonType> valueNameMap = new HashMap<String, EnumSeasonType>();

    static {
        typeMap.put(EnumSeasonType.FIRSTQUARTER, "1");
        typeMap.put(EnumSeasonType.SECONDQUARTER, "2");
        typeMap.put(EnumSeasonType.THIRDQUARTER, "3");
        typeMap.put(EnumSeasonType.FOURTHQUARTER, "4");

        nameMap.put(EnumSeasonType.FIRSTQUARTER, "第一季度");
        nameMap.put(EnumSeasonType.SECONDQUARTER, "第二季度");
        nameMap.put(EnumSeasonType.THIRDQUARTER, "第三季度");
        nameMap.put(EnumSeasonType.FOURTHQUARTER, "第四季度");

        for (EnumSeasonType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumSeasonType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumSeasonType parseName(String name) {
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
        final ArrayList<EnumSeasonType> list = new ArrayList<EnumSeasonType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumSeasonType>() {
            @Override
            public int compare(EnumSeasonType o1, EnumSeasonType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumSeasonType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
