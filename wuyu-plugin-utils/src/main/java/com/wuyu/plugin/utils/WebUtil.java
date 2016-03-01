/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the wuyushuo developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.utils;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WebUtil extends WebUtils {

    /**
     * 获取cookie
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (null == cookie) return null;

        String value = cookie.getValue();
        try {
            value = URLDecoder.decode(value, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }


    /**
     * 清除 某个指定的cookie
     * @param response
     * @param key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, null, 0);
    }


    /**
     * 清除 某个指定的cookie
     * @param response
     * @param key
     */
    public static void removeCookie(HttpServletResponse response, String key, String domain) {
        setCookie(response, key, null, domain, 0);
    }


    /**
     * set cookie
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, "", -1);
    }

    /**
     * 设置cookie
     * @param response
     * @param name
     * @param value
     * @param maxAgeInSeconds
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        setCookie(response, name, value, "", maxAgeInSeconds);
    }

    /**
     * 设置cookie
     * @param response
     * @param name
     * @param value
     * @param maxAgeInSeconds
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String domain, int maxAgeInSeconds) {
        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {}
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAgeInSeconds);
        // 指定为httpOnly保证安全性
//		cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


}
