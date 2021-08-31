package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public boolean save(Activity activity) {

        boolean flag = true;

        int count = activityDao.save(activity);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        int total = activityDao.getTotalByCondition(map);

        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);


        return vo;
    }

    @Override
    public boolean delete(String[] ids) {


        boolean flag = true;

        int count1 = activityRemarkDao.getCountByAids(ids);

        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1 != count2){
            flag = false;
        }

        int count3 = activityDao.delete(ids);

        if (count3 != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        List<User> uList = userDao.getUserList();

        Activity activity = activityDao.getById(id);

        Map<String, Object> map = new HashMap<>();

        map.put("uList", uList);
        map.put("a", activity);


        return map;
    }

    @Override
    public boolean update(Activity activity) {

        boolean flag = true;

        int count = activityDao.update(activity);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> activityRemarks = activityRemarkDao.getRemarkListByAid(activityId);

        return activityRemarks;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemark(id);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if (count != 1){
            flag = false;
        }


        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String id) {

        List<Activity> activityList = activityDao.getActivityListByClueId(id);

        return activityList;
    }

    @Override
    public List<Activity> getActivityByName(Map<String, Object> map) {

        List<Activity> activityList = activityDao.getActivityByName(map);

        return activityList;
    }

    @Override
    public List<Activity> getActivityByOpenSearch(String aname) {

        List<Activity> aList = activityDao.getActivityByOpenSearch(aname);

        return aList;
    }
}
