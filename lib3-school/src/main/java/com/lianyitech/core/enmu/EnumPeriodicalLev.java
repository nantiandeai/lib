package com.lianyitech.core.enmu;
import java.util.*;

/**
 * Created by chenxiaoding on 2017/3/15.
 * 期刊级别
 */
public enum EnumPeriodicalLev {
    CORE,
    NONCORE;


    private static Map<EnumPeriodicalLev, String> typeMap = new HashMap<EnumPeriodicalLev, String>();
    private static Map<String, EnumPeriodicalLev> valueMap = new HashMap<String, EnumPeriodicalLev>();
    private static Map<EnumPeriodicalLev, String> nameMap = new HashMap<EnumPeriodicalLev, String>();

    static {
        typeMap.put(EnumPeriodicalLev.CORE, "0");
        typeMap.put(EnumPeriodicalLev.NONCORE, "1");

        nameMap.put(EnumPeriodicalLev.CORE, "核心期刊");
        nameMap.put(EnumPeriodicalLev.NONCORE, "非核心期刊");

        for (EnumPeriodicalLev state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumPeriodicalLev parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid PeriodicalLev");
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
        final ArrayList<EnumPeriodicalLev> list = new ArrayList<EnumPeriodicalLev>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumPeriodicalLev>() {
            @Override
            public int compare(EnumPeriodicalLev o1, EnumPeriodicalLev o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumPeriodicalLev obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
