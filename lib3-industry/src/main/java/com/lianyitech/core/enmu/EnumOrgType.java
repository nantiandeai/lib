package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by 2016/9/2.
 * 机构类型举类
 */
public enum EnumOrgType {
    PROVINCE,//省
    CITY,//市
    COUNTY,//区县
    SCHOOLAREA;//学区

    private static Map<EnumOrgType, String> typeMap = new HashMap<EnumOrgType, String>();
    private static Map<String, EnumOrgType> valueMap = new HashMap<String, EnumOrgType>();
    private static Map<EnumOrgType, String> nameMap = new HashMap<EnumOrgType, String>();
    private static Map<String, EnumOrgType> valueNameMap = new HashMap<String, EnumOrgType>();

    static {
        typeMap.put(EnumOrgType.PROVINCE, "0");
        typeMap.put(EnumOrgType.CITY, "1");
        typeMap.put(EnumOrgType.COUNTY, "2");
        typeMap.put(EnumOrgType.SCHOOLAREA, "3");

        nameMap.put(EnumOrgType.PROVINCE, "省级");
        nameMap.put(EnumOrgType.CITY, "市级");
        nameMap.put(EnumOrgType.COUNTY, "区县级");
        nameMap.put(EnumOrgType.SCHOOLAREA, "学区");

        for (EnumOrgType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
            valueNameMap.put(state.getName(), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumOrgType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid");
        }
        return valueMap.get(value);
    }

    public static EnumOrgType parseName(String name) {
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
        final ArrayList<EnumOrgType> list = new ArrayList<EnumOrgType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumOrgType>() {
            @Override
            public int compare(EnumOrgType o1, EnumOrgType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumOrgType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
