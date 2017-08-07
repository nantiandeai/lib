package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by zengy on 2016/9/2.
 * 馆藏状态枚举类
 */
public enum EnumLibStoreStatus {
    IN_LIB,
    OUT_LIB,
    OUT_OLD,
    SCRAP,
    ORDER_BORROW,
    LOSS,
    STAINS;

    private static Map<EnumLibStoreStatus, String> typeMap = new HashMap<EnumLibStoreStatus, String>();
    private static Map<String, EnumLibStoreStatus> valueMap = new HashMap<String, EnumLibStoreStatus>();
    private static Map<EnumLibStoreStatus, String> nameMap = new HashMap<EnumLibStoreStatus, String>();

    static {
        typeMap.put(EnumLibStoreStatus.IN_LIB, "0");
        typeMap.put(EnumLibStoreStatus.OUT_LIB, "1");
        typeMap.put(EnumLibStoreStatus.OUT_OLD, "2");
        typeMap.put(EnumLibStoreStatus.SCRAP, "3");
        typeMap.put(EnumLibStoreStatus.LOSS, "4");
        typeMap.put(EnumLibStoreStatus.ORDER_BORROW, "5");
        typeMap.put(EnumLibStoreStatus.STAINS, "6");

        nameMap.put(EnumLibStoreStatus.IN_LIB, "在馆");
        nameMap.put(EnumLibStoreStatus.OUT_LIB, "借出");
        nameMap.put(EnumLibStoreStatus.OUT_OLD, "剔旧");
        nameMap.put(EnumLibStoreStatus.SCRAP, "报废");
        nameMap.put(EnumLibStoreStatus.LOSS, "丢失");
        nameMap.put(EnumLibStoreStatus.ORDER_BORROW, "预借");
        nameMap.put(EnumLibStoreStatus.STAINS, "污损");
        for (EnumLibStoreStatus state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumLibStoreStatus parse(String value) {
        if (!valueMap.containsKey(value)) {
            throw new IllegalArgumentException(value + " is not a valid CardStatus");
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
        final ArrayList<EnumLibStoreStatus> list = new ArrayList<EnumLibStoreStatus>(typeMap.keySet());
        Collections.sort(list, new java.util.Comparator<EnumLibStoreStatus>() {
            @Override
            public int compare(EnumLibStoreStatus o1, EnumLibStoreStatus o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumLibStoreStatus obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
