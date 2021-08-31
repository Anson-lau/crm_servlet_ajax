package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            System.out.println("coming into clueController..");

            String path = request.getServletPath();

            if ("/workbench/clue/getUserList.do".equals(path)){
                getUserList(request, response);
            }else if ("/workbench/clue/save.do".equals(path)){
                save(request, response);
            }else if ("/workbench/clue/pageList.do".equals(path)){
                pageList(request, response);
            }else if("/workbench/clue/getUserListAndClue.do".equals(path)){
                getUserListAndClue(request, response);
            }else if("/workbench/clue/update.do".equals(path)){
                update(request, response);
            }else if("/workbench/clue/delete.do".equals(path)){
                delete(request, response);
            }else if("/workbench/clue/detail.do".equals(path)){
                detail(request, response);
            }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
                getActivityListByClueId(request, response);
            }else if("/workbench/clue/unbindByCarId.do".equals(path)){
                unbindByClueId(request, response);
            }else if("/workbench/clue/getActivityByName.do".equals(path)){
                getActivityByName(request, response);
            }else if("/workbench/clue/bund.do".equals(path)){
                bund(request, response);
            }else if("/workbench/clue/getActivityByOpenSearch.do".equals(path)){
                getActivityByOpenSearch(request, response);
            }else if("/workbench/clue/convert.do".equals(path)){
                convertByFormOrId(request, response);
            }
        }

    private void convertByFormOrId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("coming into convert...");

        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");

        Tran t = null;

        if ("a".equals(flag)){

            t = new Tran();

            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);

        }

        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());


        boolean cFlag = cs.convert(clueId, t, createBy);

        if (cFlag){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void getActivityByOpenSearch(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into getActivityByOpenSearch");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> a = as.getActivityByOpenSearch(aname);

        PrintJson.printJsonObj(response, a);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("coming into bund...");

        String cid = request.getParameter("cid");

        String[] aids = request.getParameterValues("aid");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid, aids);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getActivityByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into getActivityByName");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String, Object> map = new HashMap<>();

        map.put("aname", aname);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> activityList = as.getActivityByName(map);

        PrintJson.printJsonObj(response, activityList);


    }

    private void unbindByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into unbindByCarId...");

        String id = request.getParameter("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbind(id);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into getActivityListByClueId...");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService)ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> ac = as.getActivityListByClueId(id);

        PrintJson.printJsonObj(response, ac);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("coming into clue detail..");

        String id = request.getParameter("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("coming into clue delete...");

        String[] ids = request.getParameterValues("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response, flag);


    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into clue update...");

        String id = request.getParameter("edit-id");
        String owner = request.getParameter("edit-owner");
        String company = request.getParameter("edit-company");
        String appellation = request.getParameter("edit-appellation");
        String fullname = request.getParameter("edit-fullname");
        String job = request.getParameter("edit-job");
        String email = request.getParameter("edit-email");
        String phone = request.getParameter("edit-phone");
        String website = request.getParameter("edit-website");
        String mphone = request.getParameter("edit-mphone");
        String state = request.getParameter("edit-state");
        String source = request.getParameter("edit-source");
        String description = request.getParameter("edit-description");
        String contactSummary = request.getParameter("edit-contactSummary");
        String nextContactTime = request.getParameter("edit-nextContactTime");
        String address = request.getParameter("edit-address");

        String editBy = ((User)(request.getSession().getAttribute("user"))).getName();
        String editTime = DateTimeUtil.getSysTime();

        Clue clue = new Clue();

        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setEditBy(editBy);
        clue.setEditTime(editTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.update(clue);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("getUserListAndClue...");

        String id = request.getParameter("id");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        Map<String, Object> map = cs.getUserListAndClue(id);

        PrintJson.printJsonObj(response, map);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("coming into clue pageList...");


        String fullname =  request.getParameter("search-fullname");
        String company =  request.getParameter("search-company");
        String phone =  request.getParameter("search-phone" );
        String source =  request.getParameter("search-source" );
        String owner =  request.getParameter("search-owner" );
        String mphone =  request.getParameter("search-mphone" );
        String state =  request.getParameter("search-state" );
        int pageNo =  Integer.valueOf(request.getParameter("pageNo" ));
        int pageSize =  Integer.valueOf(request.getParameter("pageSize"));

        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<>();

        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("mphone", mphone);
        map.put("state", state);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        System.out.println("11111");

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        System.out.println("22222");
        PaginationVo<Clue> vo = cs.pageList(map);

        System.out.println("33333");

        PrintJson.printJsonObj(response, vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("coming into clue save...");


        //从页面获取的信息
        String owner = request.getParameter("create-owner");
        String company = request.getParameter("create-company");
        String appellation = request.getParameter("create-call");
        String fullname = request.getParameter("create-surname");
        String job = request.getParameter("create-job");
        String email = request.getParameter("create-email");
        String phone = request.getParameter("create-phone");
        String website = request.getParameter("create-website");
        String mphone = request.getParameter("create-mphone");
        String state = request.getParameter("create-status");
        String source = request.getParameter("create-source");
        String description = request.getParameter("create-describe");
        String contactSummary = request.getParameter("create-contactSummary");
        String nextContactTime = request.getParameter("create-nextContactTime");
        String address = request.getParameter("create-address");

        //创建id，createBy, createTime
        String id = UUIDUtil.getUUID();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();


        //创建clue对象，添加信息
        Clue clue = new Clue();

        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        //创建clueService

        ClueService cs = (ClueService)ServiceFactory.getService(new ClueServiceImpl());

        //添加是否成功
        boolean flag = cs.save(clue);

        //返回结果给页面
        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserList(HttpServletRequest requset, HttpServletResponse response) {

            System.out.println("coming into clue userlist");

            UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

            List<User> uList = us.getUserList();

            PrintJson.printJsonObj(response, uList);

        }
    }
