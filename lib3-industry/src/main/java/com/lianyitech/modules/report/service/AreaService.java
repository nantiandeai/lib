package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.EnumOrgType;
import com.lianyitech.modules.report.dao.AreaDao;

import com.lianyitech.modules.report.entity.Area;
import com.lianyitech.modules.report.entity.CommonEntity;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by yangkai on 2016/11/1.
 */
@Service

public class AreaService extends CrudService<AreaDao, Area> {

    /**
     * 区域列表-根据类型，父级编号查询子区域
     *
     * @return
     */
    public Area findAreaByUser() {
        User user = UserUtils.getUser();
        String orgId = user.getOrgId();
        return dao.findByCode(orgId);
    }
    /**
     * 查询当前用户的区域信息，并修改参数对象的区域信息
     *
     * @return
     */
    public CommonEntity getArea(CommonEntity commonEntity) {

        Area area = findAreaByUser();
        if (area!=null){
            commonEntity.setProvince(area.getProvince());
            commonEntity.setCity(area.getCity());
            commonEntity.setCounty(area.getCounty());
            commonEntity.setSchoolArea(area.getSchoolArea());
            return commonEntity;
        }else{
            return null;
        }
    }
    /**
     * 区域列表-根据类型，父级编号查询子区域
     *
     * @param type
     * @param parentCode
     * @return
     */
    public List<Area> findByParentCode(String type, String parentCode) {
        User user = UserUtils.getUser();
        String orgId = user.getOrgId();
        String orgType = user.getOrgType();
        if (StringUtils.isNotEmpty(orgType)) {
            switch (orgType) {
                case "221":
                    orgType = "0";
                    break;
                case "222":
                    orgType = "1";
                    break;
                case "223":
                    orgType = "2";
                    break;
                case "224":
                    orgType = "3";
                    break;
            }
        }
        Area area = dao.findByCode(orgId);
        if(area==null){
//            area = new Area();
            return null;
        }
        area.setType(type);
        area.setParentCode(parentCode);
        area.setOrgType(orgType);
        List<Area> list = dao.findByParentCode(area);
        //构造下拉全部
        Area all = new Area();
        all.setCode("");
        if (StringUtils.isNotEmpty(orgType)) {
            if (orgType.equals(EnumOrgType.PROVINCE.getValue())) {//省级用户 市、区县列表构造全部
                if (type.equals("1")) {
                    all.setName("所选城市");
                    list.add(0, all);
                } else if (type.equals("2")) {
                    all.setName("所选区县");
                    list.add(0, all);
                }
            } else if (orgType.equals(EnumOrgType.CITY.getValue())) {//市级用户 区县列表构造全部
                if (type.equals("2")) {
                    all.setName("所选区县");
                    list.add(0, all);
                }
            }
        }
        return list;
    }

}