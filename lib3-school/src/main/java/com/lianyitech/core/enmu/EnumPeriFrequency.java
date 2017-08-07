package com.lianyitech.core.enmu;

import java.util.*;


/**
 *刊期出版频率
 */
public enum EnumPeriFrequency {
    DIURNAL,
    WEEKLY,
    TENDAY,
    SEMIMONTHLY,
    MONTHLY,
    BIMONTHLY,
    QUARTERLY,
    SEMIYEARLY,
    ANNUALS;




    private static Map<EnumPeriFrequency, String> typeMap = new HashMap<>();
    private static Map<String, EnumPeriFrequency> valueMap = new HashMap<>();
    private static Map<EnumPeriFrequency, String> nameMap = new HashMap<>();
    private static Map<String, EnumPeriFrequency> valueNameMap = new HashMap<>();

    static {
        typeMap.put(EnumPeriFrequency.DIURNAL, "0");
        typeMap.put(EnumPeriFrequency.WEEKLY, "1");
        typeMap.put(EnumPeriFrequency.TENDAY, "2");
        typeMap.put(EnumPeriFrequency.SEMIMONTHLY, "3");
        typeMap.put(EnumPeriFrequency.MONTHLY, "4");
        typeMap.put(EnumPeriFrequency.BIMONTHLY, "5");
        typeMap.put(EnumPeriFrequency.QUARTERLY, "6");
        typeMap.put(EnumPeriFrequency.SEMIYEARLY, "7");
        typeMap.put(EnumPeriFrequency.ANNUALS, "8");

        nameMap.put(EnumPeriFrequency.DIURNAL, "日刊");//如果需要，也可把“日刊”改为 “日刊：一年365期”
        nameMap.put(EnumPeriFrequency.WEEKLY, "周刊");
        nameMap.put(EnumPeriFrequency.TENDAY, "旬刊");
        nameMap.put(EnumPeriFrequency.SEMIMONTHLY, "半月刊");
        nameMap.put(EnumPeriFrequency.MONTHLY, "月刊");
        nameMap.put(EnumPeriFrequency.BIMONTHLY, "双月刊");
        nameMap.put(EnumPeriFrequency.QUARTERLY, "季刊");
        nameMap.put(EnumPeriFrequency.SEMIYEARLY, "半年刊");
        nameMap.put(EnumPeriFrequency.ANNUALS, "年刊");

        for (EnumPeriFrequency state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumPeriFrequency parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumPeriFrequency parseName(String name) {
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
        final ArrayList<EnumPeriFrequency> list = new ArrayList<>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumPeriFrequency>() {
            @Override
            public int compare(EnumPeriFrequency o1, EnumPeriFrequency o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<>();
        for (EnumPeriFrequency obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
