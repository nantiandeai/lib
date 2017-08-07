package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by 2016/9/2.
 * 学校类型举类
 */
public enum EnumSchoolType {
    PRIMARY,//小学
    PRIMARY_POINT,//小学教学点
    JUNIOR_MIDDLE,//初级中学
    COM_EMIDDLE,//完全中学
    SENIOR_HIGH,//高级中学
    NINE_YEAR,//九年一贯制
    TWELVE_YEARS;//十二年一贯制

    private static Map<EnumSchoolType, String> typeMap = new HashMap<EnumSchoolType, String>();
    private static Map<String, EnumSchoolType> valueMap = new HashMap<String, EnumSchoolType>();
    private static Map<EnumSchoolType, String> nameMap = new HashMap<EnumSchoolType, String>();
    private static Map<String, EnumSchoolType> valueNameMap = new HashMap<String, EnumSchoolType>();

    static {
        typeMap.put(EnumSchoolType.PRIMARY, "ly011002");//ly011002
        typeMap.put(EnumSchoolType.PRIMARY_POINT, "ly011003");
        typeMap.put(EnumSchoolType.JUNIOR_MIDDLE, "ly011006");
        typeMap.put(EnumSchoolType.COM_EMIDDLE, "ly011004");
        typeMap.put(EnumSchoolType.SENIOR_HIGH, "ly011005");
        typeMap.put(EnumSchoolType.NINE_YEAR, "ly011007");
        typeMap.put(EnumSchoolType.TWELVE_YEARS, "ly011008");

        nameMap.put(EnumSchoolType.PRIMARY, "小学");
        nameMap.put(EnumSchoolType.PRIMARY_POINT, "小学教学点");
        nameMap.put(EnumSchoolType.JUNIOR_MIDDLE, "初级中学");
        nameMap.put(EnumSchoolType.COM_EMIDDLE, "完全中学");
        nameMap.put(EnumSchoolType.SENIOR_HIGH, "高级中学");
        nameMap.put(EnumSchoolType.NINE_YEAR, "九年一贯制");
        nameMap.put(EnumSchoolType.TWELVE_YEARS, "十二年一贯制");

        for (EnumSchoolType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumSchoolType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumSchoolType parseName(String name) {
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
        final ArrayList<EnumSchoolType> list = new ArrayList<EnumSchoolType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumSchoolType>() {
            @Override
            public int compare(EnumSchoolType o1, EnumSchoolType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumSchoolType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
