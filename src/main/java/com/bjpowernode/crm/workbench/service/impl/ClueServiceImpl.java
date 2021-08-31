package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import sun.plugin.util.UIUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);



    @Override
    public boolean save(Clue clue) {

        //默认为true
        boolean flag = true;

        //添加是否成功
        int count = clueDao.save(clue);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Object> map) {

        PaginationVo<Clue> vo = new PaginationVo<>();

        int total = clueDao.getTotalByCondition(map);

        List<Clue> dataList = clueDao.getDataListByCondition(map);

        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public Map<String, Object> getUserListAndClue(String id) {

        Map<String, Object> map = new HashMap<>();

        List<User> uList = userDao.getUserList();

        Clue clue = clueDao.getClueList(id);

        map.put("uList", uList);
        map.put("c", clue);

        return map;
    }

    @Override
    public boolean update(Clue clue) {

        boolean flag = true;

        int count = clueDao.update(clue);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean delete(String[] ids) {

        boolean flag = true;

        int count = clueDao.delete(ids);

        if (count != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue clue = clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unbind(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.unbind(id);

        if (count != 1){
            flag = false;
        }


        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for(String aid : aids){

            ClueActivityRelation car = new ClueActivityRelation();

            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);

            if (count != 1){
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

        boolean flag = true;

        String createTime = DateTimeUtil.getSysTime();

        Clue c = clueDao.getById(clueId);

        String company = c.getCompany();
        Customer cus = customerDao.getCustomerByName(company);

        if (cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());

            int count1 = customerDao.save(cus);
            if (count1 != 1){
                flag = false;
            }
        }

        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());

        int count2 = contactsDao.save(con);
        if (count2 != 1){
            flag = false;
        }

        List<ClueRemark> clueRemarks = clueRemarkDao.getListByClueId(clueId);

        for (ClueRemark clueRemark : clueRemarks){

            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(cus.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                flag = false;
            }
        }



        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);

        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList) {

            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();

            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1) {
                flag = false;
            }
        }

            if(t!=null){

                t.setSource(c.getSource());
                t.setOwner(c.getOwner());
                t.setNextContactTime(c.getNextContactTime());
                t.setDescription(c.getDescription());
                t.setCustomerId(cus.getId());
                t.setContactSummary(c.getContactSummary());
                t.setContactsId(con.getId());

                int count6 = tranDao.save(t);
                if(count6 != 1){
                    flag = false;
                }

                TranHistory th = new TranHistory();
                th.setId(UUIDUtil.getUUID());
                th.setCreateBy(createBy);
                th.setCreateTime(createTime);
                th.setExpectedDate(t.getExpectedDate());
                th.setMoney(t.getMoney());
                th.setStage(t.getStage());
                th.setTranId(t.getId());

                int count7 = tranHistoryDao.save(th);
                if (count7 != 1){
                    flag = false;
                }

            }

            for(ClueRemark clueRemark : clueRemarks){

                int count8 = clueRemarkDao.delete(clueRemark);

                if (count8 != 1){
                    flag = false;
                }

            }

            for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
                int count9 = clueActivityRelationDao.delete(clueActivityRelation);
                if (count9 != 1){
                    flag = false;
                }

            }

            int count10 = clueDao.deleteClue(clueId);
            if (count10 != 1){
                flag = false;
            }

        return flag;
    }
}
