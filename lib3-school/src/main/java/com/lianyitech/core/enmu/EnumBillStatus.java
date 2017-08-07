package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by zengy on 2016/9/2.
 * 流通操作单据状态枚举类
 */
public enum EnumBillStatus {
    RETURN,
    BORROW,
    LOSS,
    ORDER_BORROW,
    OUT_OLD,
    SCRAP,
    ORDER,
    CANCEL_ORDER,
    RENEW,
    STAINED,
    CANCEL_ORDER_BORROW,
    ORDER_TO_ORDER_BORROW;//加了个状态15 归还时预约自动转预借

    private static Map<EnumBillStatus, String> typeMap = new HashMap<>();
    private static Map<String, EnumBillStatus> valueMap = new HashMap<>();
    private static Map<EnumBillStatus, String> nameMap = new HashMap<>();

    static {
        typeMap.put(EnumBillStatus.RETURN, EnumCirculateLogType.RETURN.getValue()+EnumLibStoreStatus.IN_LIB.getValue());
        typeMap.put(EnumBillStatus.BORROW, EnumCirculateLogType.BORROW.getValue()+EnumLibStoreStatus.OUT_LIB.getValue());
        typeMap.put(EnumBillStatus.LOSS, EnumCirculateLogType.LOSS.getValue()+EnumLibStoreStatus.LOSS.getValue());
        typeMap.put(EnumBillStatus.ORDER_BORROW, EnumCirculateLogType.ORDER_BORROW.getValue()+EnumLibStoreStatus.LOSS.getValue());
        typeMap.put(EnumBillStatus.OUT_OLD, EnumCirculateLogType.OUT_OLD.getValue()+EnumLibStoreStatus.OUT_OLD.getValue());
        typeMap.put(EnumBillStatus.SCRAP, EnumCirculateLogType.SCRAP.getValue()+EnumLibStoreStatus.SCRAP.getValue());
        typeMap.put(EnumBillStatus.ORDER, EnumCirculateLogType.ORDER.getValue()+EnumLibStoreStatus.OUT_LIB.getValue());
        typeMap.put(EnumBillStatus.CANCEL_ORDER, EnumCirculateLogType.CANCEL_ORDER.getValue()+EnumLibStoreStatus.OUT_LIB.getValue());
        typeMap.put(EnumBillStatus.RENEW, EnumCirculateLogType.RENEW.getValue()+EnumLibStoreStatus.OUT_LIB.getValue());
        typeMap.put(EnumBillStatus.STAINED, EnumCirculateLogType.STAINED.getValue()+EnumLibStoreStatus.IN_LIB.getValue());
        typeMap.put(EnumBillStatus.CANCEL_ORDER_BORROW, EnumCirculateLogType.CANCEL_ORDER_BORROW.getValue()+EnumLibStoreStatus.IN_LIB.getValue());
        typeMap.put(EnumBillStatus.ORDER_TO_ORDER_BORROW,EnumCirculateLogType.RETURN.getValue()+EnumLibStoreStatus.ORDER_BORROW.getValue());

        nameMap.put(EnumBillStatus.RETURN, "归还成功");
        nameMap.put(EnumBillStatus.BORROW, "借阅成功");
        nameMap.put(EnumBillStatus.LOSS, "丢失成功");
        nameMap.put(EnumBillStatus.ORDER_BORROW, "预借成功");
        nameMap.put(EnumBillStatus.OUT_OLD, "剔旧成功");
        nameMap.put(EnumBillStatus.SCRAP, "报废成功");
        nameMap.put(EnumBillStatus.ORDER, "预约成功");
        nameMap.put(EnumBillStatus.CANCEL_ORDER, "取消预约成功");
        nameMap.put(EnumBillStatus.RENEW, "续借成功");
        nameMap.put(EnumBillStatus.STAINED, "污损成功");
        nameMap.put(EnumBillStatus.CANCEL_ORDER_BORROW, "取消预借成功");
        nameMap.put(EnumBillStatus.ORDER_TO_ORDER_BORROW,"预约自动转预借成功");
        for (EnumBillStatus state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumBillStatus parse(String value) {
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
        final ArrayList<EnumBillStatus> list = new ArrayList<>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumBillStatus>() {
            @Override
            public int compare(EnumBillStatus o1, EnumBillStatus o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<>();
        for (EnumBillStatus obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
