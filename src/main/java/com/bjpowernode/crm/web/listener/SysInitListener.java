package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Listening...");
        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = ds.getAll();
        Set<String> set = map.keySet();

        for (String key : set){
            application.setAttribute(key, map.get(key));
        }

        System.out.println("Listening end...");





        Map<String, String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()){

            String key = e.nextElement();

            String value = rb.getString(key);

            pMap.put(key, value);

        }

        application.setAttribute("pMap", pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
