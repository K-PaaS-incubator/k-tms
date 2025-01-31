/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kglory.tms.web.util;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author leecjong
 */
@Component
public class SpringUtils {
    private static ApplicationContext wac = null;

    public static void initByClasspath() {
        if (wac == null) {
            wac = new ClassPathXmlApplicationContext(
                    new String[]{
                "/context/dispatcher-servlet.xml",
                "/context/servlet-context.xml",
                "/context/root-context.xml",
                "/context/batch-context.xml"
                    });
        }
    }

    public static void initByServletContext(ServletContext sc) {
        if (wac == null) {
            wac = WebApplicationContextUtils.getWebApplicationContext(sc);
        }
    }

    public static Object getBean(ServletContext sc, String beanName) {
        if (wac == null) {
            wac = WebApplicationContextUtils.getWebApplicationContext(sc);
        }
        return wac.getBean(beanName);
    }

    public static Object getBean(String beanName) {
    	if (wac == null) {
    		wac = new ClassPathXmlApplicationContext(
                    new String[]{
                "/context/dispatcher-servlet.xml",
                "/context/servlet-context.xml",
                "/context/root-context.xml",
                "/context/batch-context.xml"
            });
    	}
        return wac.getBean(beanName);
    }
}
