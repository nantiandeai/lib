package com.lianyitech.core.dataextraction;

/**
 * Created by wule on 2016/10/31.
 */
public class DataExtraction {
    public static void  StartTask(){
        SysOrgService sysOrgService = new SysOrgService();
        sysOrgService.selectSysOrg();
        FactBookDirectoryService factBookDirectoryService = new FactBookDirectoryService();
        factBookDirectoryService.selectBookDirectory();
        FactCirculateService factCirculateService = new FactCirculateService();
        factCirculateService.selectfactCirculate();
        DimOrgService dimOrgService = new DimOrgService();
        dimOrgService.selectDimOrg();

    }

}
