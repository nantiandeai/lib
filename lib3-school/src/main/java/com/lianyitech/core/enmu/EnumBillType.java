package com.lianyitech.core.enmu;
import java.util.*;

/**
 * Created by zengy on 2016/9/2.
 * 流通操作单据类型枚举类
 */
public enum EnumBillType {
    BORROW_BILL,
    ORDER_BORROW_BILL,
    SCRAP_BILL,
    ORDER_BILL,
    //RENEW_BILL,

    STAINED_BILL;

    private static Map<EnumBillType, String> typeMap = new HashMap<EnumBillType, String>();
    private static Map<String, EnumBillType> valueMap = new HashMap<String, EnumBillType>();
    private static Map<EnumBillType, String> nameMap = new HashMap<EnumBillType, String>();

    static {
        typeMap.put(EnumBillType.BORROW_BILL, "0");
        typeMap.put(EnumBillType.ORDER_BORROW_BILL, "1");
        typeMap.put(EnumBillType.SCRAP_BILL, "2");
        typeMap.put(EnumBillType.ORDER_BILL, "3");
        //typeMap.put(EnumBillType.RENEW_BILL, "4");
        typeMap.put(EnumBillType.STAINED_BILL, "4");

        nameMap.put(EnumBillType.BORROW_BILL, "借书单");
        nameMap.put(EnumBillType.ORDER_BORROW_BILL, "预借单");
        nameMap.put(EnumBillType.SCRAP_BILL, "报废单");
        nameMap.put(EnumBillType.ORDER_BILL, "预约单");
        // nameMap.put(EnumBillType.RENEW_BILL, "续借单");//续
        nameMap.put(EnumBillType.STAINED_BILL, "污损单");
        for (EnumBillType state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumBillType parse(String value) {
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
        final ArrayList<EnumBillType> list = new ArrayList<EnumBillType>(typeMap.keySet());
        Collections.sort(list, new java.util.Comparator<EnumBillType>() {
            @Override
            public int compare(EnumBillType o1, EnumBillType o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (EnumBillType obj : list) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }
}
