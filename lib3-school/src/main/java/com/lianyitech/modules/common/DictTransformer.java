package com.lianyitech.modules.common;


import com.lianyitech.modules.sys.entity.Dict;

import java.util.HashMap;
import java.util.Map;

public class DictTransformer {
   private Map<String,Map<String,Dict>> map = new HashMap<>();
   public DictTransformer(Map<String,Map<String,Dict>> map){
       this.map=map;
   }
   public String getDictName(String pid,String value){
      Map<String, Dict> mapdic = map.get(pid);
        if(mapdic!=null && mapdic.get(value)!=null){
           return mapdic.get(value).getLabel();
        } else{
           return value;
        }
   }

    public static void main(String[] args) {
       String orgId= "00123";
        int hashCode = orgId.hashCode() % 30;
        if(hashCode<0){
            hashCode = -hashCode;
        }
        System.out.print(hashCode);
    }

}
