package com.lianyitech.modules.analysis.dao;

import com.lianyitech.common.persistence.CrudDao;
import com.lianyitech.common.persistence.annotation.MyBatisDao;
import com.lianyitech.modules.analysis.entity.InputAnalysis;
import java.util.List;

/**
 * Created by jordan jiang on 2017/4/5.
 */
@MyBatisDao
public interface InputAnalysisDao  extends CrudDao<InputAnalysis> {

    /**
     * 流通应用情况列表
     * @param inputAnalysis 实体类
     * @return list结果
     */
    List<InputAnalysis> listCirculateAnalysis(InputAnalysis inputAnalysis);

    /**
     * 录入读者应用情况列表
     * @param inputAnalysis 实体类
     * @return list结果
     */
    List<InputAnalysis> listReaderAnalysis(InputAnalysis inputAnalysis);

    /**
     * 录入馆藏应用情况列表
     * @param inputAnalysis 实体类
     * @return list结果
     */
    List<InputAnalysis> listCopyAnalysis(InputAnalysis inputAnalysis);
}
