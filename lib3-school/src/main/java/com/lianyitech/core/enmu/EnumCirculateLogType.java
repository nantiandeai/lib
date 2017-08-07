package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by zengy on 2016/9/2.
 * 流通日志类型枚举类
 */
public enum EnumCirculateLogType {
    BORROW,
    RETURN,
    LOSS,
    OUT_OLD,
    SCRAP,
    ORDER,
    ORDER_BORROW,
    RENEW,
    STAINED,
    CANCEL_ORDER,
    CANCEL_ORDER_BORROW;

    private static Map<EnumCirculateLogType, String> typeMap = new HashMap<EnumCirculateLogType, String>();
    private static Map<String, EnumCirculateLogType> valueMap = new HashMap<String, EnumCirculateLogType>();
    private static Map<EnumCirculateLogType, String> nameMap = new HashMap<EnumCirculateLogType, String>();

    static {
        typeMap.put(EnumCirculateLogType.BORROW, "0");
        typeMap.put(EnumCirculateLogType.RETURN, "1");
        typeMap.put(EnumCirculateLogType.LOSS, "2");
        typeMap.put(EnumCirculateLogType.OUT_OLD, "3");
        typeMap.put(EnumCirculateLogType.SCRAP, "4");
        typeMap.put(EnumCirculateLogType.ORDER, "5");
        typeMap.put(EnumCirculateLogType.ORDER_BORROW, "6");
        typeMap.put(EnumCirculateLogType.RENEW, "7");
        typeMap.put(EnumCirculateLogType.STAINED, "8");
        typeMap.put(EnumCirculateLogType.CANCEL_ORDER, "9");
        typeMap.put(EnumCirculateLogType.CANCEL_ORDER_BORROW, "10");

        nameMap.put(EnumCirculateLogType.BORROW, "借书");
        nameMap.put(EnumCirculateLogType.RETURN, "还书");
        nameMap.put(EnumCirculateLogType.LOSS, "丢失");
        nameMap.put(EnumCirculateLogType.OUT_OLD, "剔旧");
        nameMap.put(EnumCirculateLogType.SCRAP, "报废");
        nameMap.put(EnumCirculateLogType.ORDER, "预约");
        nameMap.put(EnumCirculateLogType.ORDER_BORROW, "预借");
        nameMap.put(EnumCirculateLogType.RENEW, "续借");
        nameMap.put(EnumCirculateLogType.STAINED, "污损");
        nameMap.put(EnumCirculateLogType.CANCEL_ORDER, "取消预约");
        nameMap.put(EnumCirculateLogType.CANCEL_ORDER_BORROW, "取消预借");
        for (EnumCirculateLogType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumCirculateLogType parse(String value) {
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
        final ArrayList<EnumCirculateLogType> list = new ArrayList<EnumCirculateLogType>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumCirculateLogType>() {
            @Override
            public int compare(EnumCirculateLogType o1, EnumCirculateLogType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumCirculateLogType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
