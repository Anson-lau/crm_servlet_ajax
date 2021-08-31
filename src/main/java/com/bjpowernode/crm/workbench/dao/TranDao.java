package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    Tran detail(String id);

    int getChartsCount();

    List<Map<String, Object>> getChartsList();

    int changeStage(Tran t);

}
