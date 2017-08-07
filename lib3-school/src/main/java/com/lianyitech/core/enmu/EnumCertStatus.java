package com.lianyitech.core.enmu;

import java.util.*;


/**
 *读者证状态枚举
 */
public enum EnumCertStatus {
    VALID,
    INVALID,
    TOLOSS,
    PASSDATE,
    OLDCARD,
    STOPBY;


    private static Map<EnumCertStatus, String> typeMap = new HashMap<>();
    private static Map<String, EnumCertStatus> valueMap = new HashMap<>();
    private static Map<EnumCertStatus, String> nameMap = new HashMap<>();
    private static Map<String, EnumCertStatus> valueNameMap = new HashMap<>();

    static {
        typeMap.put(EnumCertStatus.VALID, "0");
        typeMap.put(EnumCertStatus.INVALID, "1");
        typeMap.put(EnumCertStatus.TOLOSS, "2");
        typeMap.put(EnumCertStatus.PASSDATE, "3");
        typeMap.put(EnumCertStatus.OLDCARD, "4");
        typeMap.put(EnumCertStatus.STOPBY, "5");

        nameMap.put(EnumCertStatus.VALID, "有效");
        nameMap.put(EnumCertStatus.INVALID, "失效");
        nameMap.put(EnumCertStatus.TOLOSS, "挂失");
        nameMap.put(EnumCertStatus.PASSDATE, "过期");
        nameMap.put(EnumCertStatus.OLDCARD, "旧证");
        nameMap.put(EnumCertStatus.STOPBY, "停借");

        for (EnumCertStatus state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumCertStatus parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumCertStatus parseName(String name) {
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
        final ArrayList<EnumCertStatus> list = new ArrayList<>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumCertStatus>() {
            @Override
            public int compare(EnumCertStatus o1, EnumCertStatus o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<>();
        for (EnumCertStatus obj : list) {
            if(obj.getValue().equals(EnumCertStatus.OLDCARD.getValue())){//这里列表忽略旧证
                continue;
            }
            Map<String, String> map = new HashMap<>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
