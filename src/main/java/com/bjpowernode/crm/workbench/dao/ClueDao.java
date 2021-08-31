package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    //添加clue
    int save(Clue clue);

    //获取clue信息
    List<Clue> getDataListByCondition(Map<String, Object> map);

    //获取搜索结果总条数
    int getTotalByCondition(Map<String, Object> map);

    //获取clue编辑信息
    Clue getClueList(String id);

    //更新按钮
    int update(Clue clue);

    //删除选中信息
    int delete(String[] ids);

    //获取clue详细信息
    Clue detail(String id);

    //convert中同过clueId获取clue信息
    Clue getById(String clueId);

    int deleteClue(String clueId);


}
