package com.lianyitech.core.enmu;

import java.util.*;




/**
 * Created by yangKai on 2016/9/19.
 * yangKai
 */
public enum EnumCertType {
    STUDENTCARD,
    IDENTITYCARD,
    STUDENTCODE;

    private static Map<EnumCertType, String> typeMap = new HashMap<>();
    private static Map<String, EnumCertType> valueMap = new HashMap<>();
    private static Map<EnumCertType, String> nameMap = new HashMap<>();
    private static Map<String, EnumCertType> valueNameMap = new HashMap<>();

    static {
        typeMap.put(EnumCertType.STUDENTCARD, "1");
        typeMap.put(EnumCertType.IDENTITYCARD, "2");
        typeMap.put(EnumCertType.STUDENTCODE, "3");

        nameMap.put(EnumCertType.STUDENTCARD, "学生证");
        nameMap.put(EnumCertType.IDENTITYCARD, "身份证");
        nameMap.put(EnumCertType.STUDENTCODE, "学籍号");
        for (EnumCertType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumCertType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumCertType parseName(String name) {
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
        final ArrayList<EnumCertType> list = new ArrayList<>(typeMap.keySet());
        Collections.sort(list, new java.util.Comparator<EnumCertType>() {
            @Override
            public int compare(EnumCertType o1, EnumCertType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<>();
        for (EnumCertType obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
