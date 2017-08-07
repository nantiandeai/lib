package com.lianyitech.core.enmu;

import java.util.*;

/**
 * Created by zym on 2016/9/29.
 * 流通操作流程图书状态加操作类型组合状态枚举类
 */
public enum EnumTypeAndStatus {
    TYPE_STATUS_ALLOW,//允许的流程type+status组合 "|00|30|51|60|71|81|05|11|21|35|42|85|90|91|105|46|"14
    TYPE_STATUS_ALLOW_UPDATE,//更新类型状态组合  "|05|11|21|35|42|85|90|91|105|71|81|"//将续借放入修改状态组合
    TYPE_STATUS_ALLOW_CREATE,//新建类型状态组合 "|00|30|51|60|46|"
    TYPE_BORROW_TYPE_ORDER,//借和预约操作"|0|5|"
    TYPE_BORROW_STATUS,//（类型状态组合）明确在借书单中的组合状态  "|00|"
    TYPE_ORDER_BORROW_STATUS,// （类型状态组合）明确在预借单的组合状态  "|60|105|05|"
    TYPE_SCRAP_STATUS,//（类型状态组合）明确在报废单组合状态 "|30|42|46|"
    TYPE_ORDER_STATUS,// （类型状态组合）明确在预约单组合状态 "|51|91|"
    // TYPE_RENEW_STATUS,//（类型状态组合）明确在续借单明确的组合状态"|71|";//续借单取消了
    //TYPE_STAINED_STATUS,//（类型状态组合）明确在污损单明确的组合状态 "|81|"
    TYPE_OTHER_STATUS,//无法辨别属于哪种单据的组合状态//"|11|21|71|"//流程图变了续借流程无法辨别是哪个单据了14
    RULE_VALIDATE_TYPE,//需要校验借阅规则的操作类型"|0|5|6|7|";
    TYPE_RETURN_LOSS;//还和丢的情况 |1|2|
    private static Map<EnumTypeAndStatus, String> typeMap = new HashMap<>();
    private static Map<String, EnumTypeAndStatus> valueMap = new HashMap<>();
    private static Map<EnumTypeAndStatus, String> nameMap = new HashMap<>();

    static {
        typeMap.put(EnumTypeAndStatus.TYPE_STATUS_ALLOW,//|00|30|51|60|71|81|05|11|21|35|42|85|91|105|46|
                "|" + EnumCirculateLogType.BORROW.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//00
                        "|" + EnumCirculateLogType.OUT_OLD.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//30
                        "|" + EnumCirculateLogType.ORDER.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//51
                        "|" + EnumCirculateLogType.ORDER_BORROW.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//60
                        "|" + EnumCirculateLogType.RENEW.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//71
                        "|" + EnumCirculateLogType.STAINED.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//81
                        "|" + EnumCirculateLogType.BORROW.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//05
                        "|" + EnumCirculateLogType.RETURN.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//11
                        "|" + EnumCirculateLogType.LOSS.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//21
                        "|" + EnumCirculateLogType.OUT_OLD.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//35
                        "|" + EnumCirculateLogType.SCRAP.getValue() + EnumLibStoreStatus.OUT_OLD.getValue() +//42
                        "|" + EnumCirculateLogType.RETURN.getValue() + EnumLibStoreStatus.LOSS.getValue() +//14-------
                        "|" + EnumCirculateLogType.STAINED.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//85
                        "|" + EnumCirculateLogType.CANCEL_ORDER.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//91
                        "|" + EnumCirculateLogType.CANCEL_ORDER.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//90
                        "|" + EnumCirculateLogType.CANCEL_ORDER_BORROW.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() + "|"+//105
                        "|" + EnumCirculateLogType.SCRAP.getValue() + EnumLibStoreStatus.STAINS.getValue() + "|"//46
        );
        typeMap.put(EnumTypeAndStatus.TYPE_STATUS_ALLOW_UPDATE,//|05|11|21|35|42|85|91|105|71|
                "|" + EnumCirculateLogType.BORROW.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//05
                        "|" + EnumCirculateLogType.RETURN.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//11
                        "|" + EnumCirculateLogType.RETURN.getValue() + EnumLibStoreStatus.LOSS.getValue() +//14-------
                        "|" + EnumCirculateLogType.LOSS.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//21
                        "|" + EnumCirculateLogType.OUT_OLD.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//35
                        "|" + EnumCirculateLogType.SCRAP.getValue() + EnumLibStoreStatus.OUT_OLD.getValue() +//42
                        "|" + EnumCirculateLogType.STAINED.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//85
                        "|" + EnumCirculateLogType.CANCEL_ORDER.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//90
                        "|" + EnumCirculateLogType.CANCEL_ORDER.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//91
                        "|" + EnumCirculateLogType.CANCEL_ORDER_BORROW.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//105
                        "|" + EnumCirculateLogType.RENEW.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() + "|"+//71
                        "|" + EnumCirculateLogType.STAINED.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() + "|"//81
        );
        typeMap.put(EnumTypeAndStatus.TYPE_STATUS_ALLOW_CREATE,//|00|30|51|60|
                "|" + EnumCirculateLogType.BORROW.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//00
                        "|" + EnumCirculateLogType.OUT_OLD.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//30
                        "|" + EnumCirculateLogType.ORDER.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//51
                        "|" + EnumCirculateLogType.ORDER_BORROW.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//60
                        "|" + EnumCirculateLogType.SCRAP.getValue() + EnumLibStoreStatus.STAINS.getValue() + "|"//46
        );
        typeMap.put(EnumTypeAndStatus.TYPE_BORROW_TYPE_ORDER,//"|0|5|"
                "|" + EnumCirculateLogType.BORROW.getValue() +//0
                        "|" + EnumCirculateLogType.ORDER.getValue() + "|"//5
        );
        typeMap.put(EnumTypeAndStatus.TYPE_BORROW_STATUS,//"|00|"
                "|" + EnumCirculateLogType.BORROW.getValue() + EnumLibStoreStatus.IN_LIB.getValue() + "|"
        );
        typeMap.put(EnumTypeAndStatus.TYPE_ORDER_BORROW_STATUS,//"|60|105|05|"
                "|" + EnumCirculateLogType.ORDER_BORROW.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//60
                        "|" + EnumCirculateLogType.CANCEL_ORDER_BORROW.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() +//105
                        "|" + EnumCirculateLogType.BORROW.getValue() + EnumLibStoreStatus.ORDER_BORROW.getValue() + "|"//05
        );
        typeMap.put(EnumTypeAndStatus.TYPE_SCRAP_STATUS,//"|30|42|46|"
                "|" + EnumCirculateLogType.OUT_OLD.getValue() + EnumLibStoreStatus.IN_LIB.getValue() +//30
                        "|" + EnumCirculateLogType.SCRAP.getValue() + EnumLibStoreStatus.OUT_OLD.getValue() + "|"+//42
                        "|" + EnumCirculateLogType.SCRAP.getValue() + EnumLibStoreStatus.STAINS.getValue() + "|"//46
        );
        typeMap.put(EnumTypeAndStatus.TYPE_ORDER_STATUS,//"|51|91|"
                "|" + EnumCirculateLogType.ORDER.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//51
                        "|" + EnumCirculateLogType.CANCEL_ORDER.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() + "|"//91
        );
        /*typeMap.put(EnumTypeAndStatus.TYPE_RENEW_STATUS,//"|71|";
                "|" + EnumCirculateLogType.RENEW.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() + "|"//71
        );*/
//        typeMap.put(EnumTypeAndStatus.TYPE_STAINED_STATUS,//"|81|"
//                "|" + EnumCirculateLogType.STAINED.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() + "|"//81
//        );
        typeMap.put(EnumTypeAndStatus.TYPE_OTHER_STATUS,//|11|21|71|"
                "|" + EnumCirculateLogType.RETURN.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//11
                        "|" + EnumCirculateLogType.RETURN.getValue() + EnumLibStoreStatus.LOSS.getValue() +//14-------
                        "|" + EnumCirculateLogType.LOSS.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() +//21
                        "|" + EnumCirculateLogType.RENEW.getValue() + EnumLibStoreStatus.OUT_LIB.getValue() + "|"//71
        );
        typeMap.put(EnumTypeAndStatus.RULE_VALIDATE_TYPE,//"|0|5|6|7|";
                "|" + EnumCirculateLogType.BORROW.getValue() +//0
                        "|" + EnumCirculateLogType.ORDER.getValue() +//5
                        "|" + EnumCirculateLogType.ORDER_BORROW.getValue() +//6
                        "|" + EnumCirculateLogType.RENEW.getValue() + "|"//7
        );
        typeMap.put(EnumTypeAndStatus.TYPE_RETURN_LOSS,//"|1|2|";
                "|" + EnumCirculateLogType.RETURN.getValue()
//                        +//1
//                        "|" + EnumCirculateLogType.LOSS.getValue() + "|"//2
        );
        nameMap.put(EnumTypeAndStatus.TYPE_STATUS_ALLOW, "允许的操作状态组合");
        nameMap.put(EnumTypeAndStatus.TYPE_STATUS_ALLOW_UPDATE, "更新类型状态组合");
        nameMap.put(EnumTypeAndStatus.TYPE_STATUS_ALLOW_CREATE, "新建类型状态组合");
        nameMap.put(EnumTypeAndStatus.TYPE_BORROW_TYPE_ORDER, "借和预约");
        nameMap.put(EnumTypeAndStatus.TYPE_BORROW_STATUS,"借书单明确的组合状态");
        nameMap.put(EnumTypeAndStatus.TYPE_ORDER_BORROW_STATUS,"预借单明确的组合状态");
        nameMap.put(EnumTypeAndStatus.TYPE_SCRAP_STATUS,"报废单明确的组合状态");
        nameMap.put(EnumTypeAndStatus.TYPE_ORDER_STATUS,"预约单明确的组合状态");
        // nameMap.put(EnumTypeAndStatus.TYPE_RENEW_STATUS,"续借单明确的组合状态");
        //nameMap.put(EnumTypeAndStatus.TYPE_STAINED_STATUS,"污损单明确的组合状态");
        nameMap.put(EnumTypeAndStatus.TYPE_OTHER_STATUS,"无法辨别属于哪种单据的组合状态");
        nameMap.put(EnumTypeAndStatus.RULE_VALIDATE_TYPE,"需要校验借阅规则的操作类型");
        nameMap.put(EnumTypeAndStatus.TYPE_RETURN_LOSS,"还和丢的情况");
        for (EnumTypeAndStatus state : typeMap.keySet()) {
            valueMap.put(typeMap.get(state), state);
        }
    }

    public String value() {
        return typeMap.get(this);
    }

    public static EnumTypeAndStatus parse(String value) {
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
        final ArrayList<EnumTypeAndStatus> list = new ArrayList<>(typeMap.keySet());
        Collections.sort(list, new Comparator<EnumTypeAndStatus>() {
            @Override
            public int compare(EnumTypeAndStatus o1, EnumTypeAndStatus o2) {
                return o1.value().compareTo(o2.value());
            }

        });

        List<Map<String, String>> data = new ArrayList<>();
        for (EnumTypeAndStatus obj : list) {
            Map<String, String> map = new HashMap<>();
            map.put("val", obj.getValue());
            map.put("name", obj.getName());
            data.add(map);
        }
        return data;
    }

    public static void main(String[] args) {
        System.out.print(EnumTypeAndStatus.TYPE_RETURN_LOSS.getValue());
    }
}

