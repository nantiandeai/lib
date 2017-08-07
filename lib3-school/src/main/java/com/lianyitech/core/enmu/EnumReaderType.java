package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by zengy on 2016/9/2.
 * 读者类型枚举类
 */
public enum EnumReaderType {
    STUDENT,
    TEACHER,
    OTHER,
    GROUP;

    private static Map<EnumReaderType, String> typeMap = new HashMap<EnumReaderType, String>();
    private static Map<String, EnumReaderType> valueMap = new HashMap<String, EnumReaderType>();
    private static Map<EnumReaderType, String> nameMap = new HashMap<EnumReaderType, String>();
    private static Map<String, EnumReaderType> valueNameMap = new HashMap<String, EnumReaderType>();

    static {
        typeMap.put(EnumReaderType.STUDENT, "0");
        typeMap.put(EnumReaderType.TEACHER, "1");
        typeMap.put(EnumReaderType.OTHER, "2");
        typeMap.put(EnumReaderType.GROUP,"3");


        nameMap.put(EnumReaderType.STUDENT, "学生");
        nameMap.put(EnumReaderType.TEACHER, "老师");
        nameMap.put(EnumReaderType.OTHER, "其他");
        nameMap.put(EnumReaderType.GROUP,"集体");
        for (EnumReaderType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumReaderType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumReaderType parseName(String name) {
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
        final ArrayList<EnumReaderType> list = new ArrayList<EnumReaderType>(typeMap.keySet());
        Collections.sort(list, new java.util.Comparator<EnumReaderType>() {
            @Override
            public int compare(EnumReaderType o1, EnumReaderType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumReaderType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
