package com.lianyitech.modules.catalog.utils;

import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;

import java.util.Comparator;
import java.util.Map;

public class Compare {
    public static Comparator<Subfield> subfieldComparator = new Comparator<Subfield>() {
        public int compare(Subfield o1, Subfield o2) {
            char c1 = o1.getCode(), c2 = o2.getCode();
            return Character.compare(c1, c2);
        }
    };
    public static Comparator<Map>  mapCompare =  new Comparator<Map>() {
        public int compare(Map o1, Map o2) {
            String c1 = (String) o1.get("tag"),c2 = (String) o2.get("tag");
            return c1.compareTo(c2);
        }
    };
    public static Comparator<DataField> dataComparator=new Comparator<DataField>() {
        public int compare(DataField o1, DataField o2) {
            String c1 =  o1.getTag(),c2 =  o2.getTag();
            return c1.compareTo(c2);
        }
    };
}
