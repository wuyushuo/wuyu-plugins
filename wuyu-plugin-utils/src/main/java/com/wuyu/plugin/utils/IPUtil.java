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
 * Neither the name of the yinyuetai developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 *
 * @version 1.0.0
 */
public class IPUtil {

    /**
     * 获取经过CDN后的ip地址，我们通常都会走CDN，因此要跳过最后一个X-Forwarded-For（CDN地址）
     * 仅当我们的请求经过CDN直接发送到源站时，才能使用这个方法
     * See: org.jwing.mvc.utils.HttpUtils#getIpAddress
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String ip = getExternalIp(request.getHeader("X-Real-IP"));
        if (ip.length() > 0) {
            return ip;
        }
        ip = getExternalIp(request.getHeader("X-Forwarded-For"));
        if (ip.length() > 0) {
            return ip;
        }
        ip = getExternalIp(request.getHeader("Proxy-Client-IP"));
        if (ip.length() > 0) {
            return ip;
        }
        ip = getExternalIp(request.getHeader("WL-Proxy-Client-IP"));
        if (ip.length() > 0) {
            return ip;
        }
        return getExternalIp(request.getRemoteAddr());
    }

    private static String getExternalIp(String ips) {
        if (ips == null || (ips = ips.trim()).length() == 0 || "unknown".equalsIgnoreCase(ips)) {
            return "";
        }
        String[] ipArr = StringUtils.split(ips, ',');
        int len = ipArr.length;
        if (len == 0) { // unreachable
            return "";
        }
        int fromIndex = -1;
        for (int i = 0; i < len; i++) { // 排除掉前面的非外网ip
            if (checkExternalIp(ipArr[i]).length() > 0) {
                fromIndex = i;
                break;
            }
        }
        if (fromIndex == -1) {
            return "";
        }

        int remainLen = len - fromIndex;
        if (remainLen < 3) {
            return checkExternalIp(ipArr[fromIndex]);
        } else { // 去掉最后两个是cdn的ip
            int endIndex = len - 3;
            for (int i = endIndex; i >= fromIndex; i--) { // 从后往前查找
                String ip = checkExternalIp(ipArr[i]);
                if (ip.length() > 0) {
                    return ip;
                }
            }
            return "";
        }
    }

    private static String checkExternalIp(String ip) {
        return ip != null && (ip = ip.trim()).length() > 0 && !"unknown".equalsIgnoreCase(ip) && !ip.startsWith("127.") &&
                !ip.startsWith("10.") ? ip : "";
    }

    public static String ip2LongStr(String ip) {
        return String.valueOf(ip2Long(ip));
    }

    /**
     * Returns the long format of the provided IP address.
     * @param ipAddress the IP address
     * @return the long format of <code>ipAddress</code>
     * @throws IllegalArgumentException if <code>ipAddress</code> is invalid
     */
    public static long ip2Long(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new IllegalArgumentException("ip address cannot be null or empty");
        }
        String[] octets = ipAddress.split(java.util.regex.Pattern.quote("."));
        if (octets.length != 4) {
            throw new IllegalArgumentException("invalid ip address");
        }
        long ip = 0;
        for (int i = 3; i >= 0; i--) {
            long octet = Long.parseLong(octets[3 - i]);
            if (octet > 255 || octet < 0) {
                throw new IllegalArgumentException("invalid ip address");
            }
            ip |= octet << (i * 8);
        }
        return ip;
    }

    /**
     * Returns the 32bit dotted format of the provided long ip.
     * @param ip the long ip
     * @return the 32bit dotted format of <code>ip</code>
     * @throws IllegalArgumentException if <code>ip</code> is invalid
     */
    public static String long2Ip(long ip) {
        // if ip is bigger than 255.255.255.255 or smaller than 0.0.0.0
        if (ip > 4294967295l || ip < 0) {
            throw new IllegalArgumentException("invalid ip");
        }
        StringBuilder ipAddress = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int shift = i * 8;
            ipAddress.append((ip & (0xff << shift)) >> shift);
            if (i > 0) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

    public static void main(String[] args) {
        long num = ip2Long("0.0.0.1");
        java.lang.System.out.println(num);
        System.out.println(long2Ip(num));
    }

}
