package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    private TranHistoryDao tHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Tran t, String customerName) {

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        if (cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setOwner(t.getOwner());
            cus.setName(t.getName());
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(t.getCreateTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());

            int count1 = customerDao.save(cus);
            if (count1 != 1){
                flag = false;
            }


        }

        t.setCustomerId(cus.getId());

        int count2 = tranDao.save(t);

        if (count2 != 1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(t.getCreateTime());
        th.setCreateBy(t.getCreateBy());
        th.setTranId(t.getId());

        int count3 = tHistoryDao.save(th);
        if (count3 != 1){
            flag = false;
        }





        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> findTranHistory(String tranId) {

        List<TranHistory> thList = tranHistoryDao.findTranHistory(tranId);

        return thList;
    }

    @Override
    public Map<String, Object> getCharts() {

        Map<String, Object> map = new HashMap<>();

        int count = tranDao.getChartsCount();

        List<Map<String, Object>> dataList = tranDao.getChartsList();

        map.put("count", count);
        map.put("dataList", dataList);

        return map;
    }

    @Override
    public boolean changeStage(Tran t) {

        boolean flag = true;

        int count = tranDao.changeStage(t);

        if (count != 1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setCreateTime(t.getEditTime());
        th.setCreateBy(t.getEditBy());
        th.setPossibility(t.getPossibility());
        th.setTranId(t.getId());

        int count1 = tranHistoryDao.save(th);
        if (count1 != 1){
            flag = false;
        }

        return flag;
    }
}
