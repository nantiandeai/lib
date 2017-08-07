package com.lianyitech.core.enmu;
import java.util.*;

/**
 * Created by chenxiaoding on 2017/3/15.
 * 期刊类型
 */
public enum EnumPeriType {

    MAGAZINE,
    NEWSPAPER;

    private static Map<EnumPeriType, String> typeMap = new HashMap<EnumPeriType, String>();
    private static Map<String, EnumPeriType> valueMap = new HashMap<String, EnumPeriType>();
    private static Map<EnumPeriType, String> nameMap = new HashMap<EnumPeriType, String>();

    static {
        typeMap.put(EnumPeriType.MAGAZINE, "0");
        typeMap.put(EnumPeriType.NEWSPAPER, "1");

        nameMap.put(EnumPeriType.MAGAZINE, "杂志");
        nameMap.put(EnumPeriType.NEWSPAPER, "报纸");

        for (EnumPeriType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumPeriType parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid PerType");
        }
        return valueMap.get(value);
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
        final ArrayList<EnumPeriType> list = new ArrayList<EnumPeriType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumPeriType>() {
            @Override
            public int compare(EnumPeriType o1, EnumPeriType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumPeriType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
