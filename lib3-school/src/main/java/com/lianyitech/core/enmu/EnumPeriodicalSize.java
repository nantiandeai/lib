package com.lianyitech.core.enmu;
import java.util.*;

/**
 * Created by chenxiaoding on 2017/3/15.
 * 期刊开本，16开，32开
 */
public enum EnumPeriodicalSize {

    SEXTODECIMO,
    THIRTYTWOMO;

    private static Map<EnumPeriodicalSize, String> typeMap = new HashMap<EnumPeriodicalSize, String>();
    private static Map<String, EnumPeriodicalSize> valueMap = new HashMap<String, EnumPeriodicalSize>();
    private static Map<EnumPeriodicalSize, String> nameMap = new HashMap<EnumPeriodicalSize, String>();

    static {
        typeMap.put(EnumPeriodicalSize.SEXTODECIMO, "0");
        typeMap.put(EnumPeriodicalSize.THIRTYTWOMO, "1");

        nameMap.put(EnumPeriodicalSize.SEXTODECIMO, "16开本");
        nameMap.put(EnumPeriodicalSize.THIRTYTWOMO, "32开本");

        for (EnumPeriodicalSize state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumPeriodicalSize parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid PeriodicalSize");
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
        final ArrayList<EnumPeriodicalSize> list = new ArrayList<EnumPeriodicalSize>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumPeriodicalSize>() {
            @Override
            public int compare(EnumPeriodicalSize o1, EnumPeriodicalSize o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumPeriodicalSize obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
