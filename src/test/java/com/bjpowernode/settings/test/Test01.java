package com.bjpowernode.settings.test;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test01 {

    public static void main(String[] args) {

//        String expireTime = "2019-10-10 10:10:10";

//        Date date = new Date();
//        System.out.println(date);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String str = sdf.format(date);
//        System.out.println(str);


//        String currentTime = DateTimeUtil.getSysTime();
//
//        int count = expireTime.compareTo(currentTime);
//        System.out.println(count);

//        String ip = "192.168.1.1";
//
//        String allowIps = "192.168.1.1, 192.168.1.2";
//
//        if (allowIps.contains(ip)){
//            System.out.println("allow to visiting system");
//        }else{
//            System.out.println("can no enter");
//        }

        String pwd = "123";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);

    }

}
