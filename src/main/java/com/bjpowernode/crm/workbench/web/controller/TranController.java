package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("coming into TranController..");

        String path = request.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)){
            addTran(request, response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request, response);
        }else if("/workbench/transaction/save.do".equals(path)){
            save(request, response);
        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(request, response);
        }else if("/workbench/transaction/findTranHistoryByTranId.do".equals(path)){
            findTranHistoryByTranId(request, response);
        }else if("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request, response);
        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeState(request, response);
        }
    }

    private void changeState(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("coming into changeState..");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());


        boolean flag = ts.changeStage(t);

        Map<String, String> pMap = (Map<String, String>)this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String, Object> map = new HashMap<>();

        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);


    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into getCharts..");

        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());

        Map<String, Object> map = ts.getCharts();

        PrintJson.printJsonObj(response, map);
    }

    private void findTranHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into findTranHistoryByTranId..");

        String tranId = request.getParameter("id");

        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());


        List<TranHistory> thList = ts.findTranHistory(tranId);

        Map<String, String> map = (Map<String, String>)this.getServletContext().getAttribute("pMap");

        for (TranHistory th : thList){
            String stage = th.getStage();

            String possibility = map.get(stage);
            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response, thList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("coming into detail..");

        String id = request.getParameter("id");

        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        String stage = t.getStage();
        Map<String, String> pMap = (Map<String, String>)this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t", t);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("coming into save..");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerId = request.getParameter("customerId");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        String customerName = request.getParameter("customerName");

        Tran t = new Tran();

        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setCustomerId(customerId);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);


        TranService ts = (TranService)ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t, customerName);

        if (flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coming into getCustomerName..");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService)ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);





    }

    private void addTran(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("coming into addTran...");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
