package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ClueService {
    //添加clue
    boolean save(Clue clue);

    //更新信息
    PaginationVo<Clue> pageList(Map<String, Object> map);

    //获取编辑对象信息
    Map<String, Object> getUserListAndClue(String id);

    //更新编辑内容
    boolean update(Clue clue);

    //删除选定Clue信息
    boolean delete(String[] ids);

    //获取clue详细信息
    Clue detail(String id);

    //是否取消关联成功
    boolean unbind(String id);

    //搜索框里是否关联成功
    boolean bund(String cid, String[] aids);

    //线索是否转换
    boolean convert(String clueId, Tran t, String createBy);

}
