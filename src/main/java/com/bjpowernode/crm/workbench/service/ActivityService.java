package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    //判斷是否添加成功
    boolean save(Activity activity);

    //查詢頁面條數内容
    PaginationVo<Activity> pageList(Map<String, Object> map);

    //删除选定内容
    boolean delete(String[] ids);

    //查询指定id内容
    Map<String, Object> getUserListAndActivity(String id);

    //更新选定id内容
    boolean update(Activity activity);

    //查看选定id详细
    Activity detail(String id);

    //查看评论备注
    List<ActivityRemark> getRemarkListByAid(String activityId);

    //删除评论备注
    boolean deleteRemark(String id);

    //添加备注
    boolean saveRemark(ActivityRemark ar);

    //更新备注框内容
    boolean updateRemark(ActivityRemark ar);

    //在线索详情获取activityList
    List<Activity> getActivityListByClueId(String id);

    //关联搜索框的搜索内容
    List<Activity> getActivityByName(Map<String, Object> map);

    //搜索框活动内容
    List<Activity> getActivityByOpenSearch(String aname);

}
