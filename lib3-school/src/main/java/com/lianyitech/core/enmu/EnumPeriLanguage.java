package com.lianyitech.core.enmu;
import java.util.*;

/**
 * Created by chenxiaoding on 2017/3/15.
 * 期刊语种
 */
public enum EnumPeriLanguage {

    CHI,
    ENG;

    private static Map<EnumPeriLanguage, String> typeMap = new HashMap<EnumPeriLanguage, String>();
    private static Map<String, EnumPeriLanguage> valueMap = new HashMap<String, EnumPeriLanguage>();
    private static Map<EnumPeriLanguage, String> nameMap = new HashMap<EnumPeriLanguage, String>();

    static {
        typeMap.put(EnumPeriLanguage.CHI, "0");
        typeMap.put(EnumPeriLanguage.ENG, "1");

        nameMap.put(EnumPeriLanguage.CHI, "chi");
        nameMap.put(EnumPeriLanguage.ENG, "eng");

        for (EnumPeriLanguage state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumPeriLanguage parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid PerLanguage");
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
        final ArrayList<EnumPeriLanguage> list = new ArrayList<EnumPeriLanguage>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumPeriLanguage>() {
            @Override
            public int compare(EnumPeriLanguage o1, EnumPeriLanguage o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumPeriLanguage obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
