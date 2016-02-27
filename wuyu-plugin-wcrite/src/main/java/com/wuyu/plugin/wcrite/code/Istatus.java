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
package com.wuyu.plugin.wcrite.code;

import com.wuyu.plugin.wcrite.http.HttpType;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public enum Istatus implements HttpType{

    // about code httpcode + (00000)[appId(2) + business code(3)]

    OK(20001000, HttpType.HTTP_OK, "OK", "执行成功"),

    BAD_REQUEST(40001000, HttpType.HTTP_BAD_REQUEST, "Bad Request", "错误请求"),

    INTERNAL_SERVER_ERROR(50001000, HttpType.HTTP_INTERNAL_SERVER_ERROR, "Internal Server Error", "服务器内部错误");


    // business status code
    private int code;

    // http status code
    private int http;

    // business code message
    private String message;

    // display net transaction
    private String display;

    private Istatus(int code, int http, String message, String display) {
        this.code = code;
        this.http = http;
        if(isBlank(message)){
            message = name();
        }
        this.message = message;
        this.display = display;
        IstatusMap.INSTANCE.put(code, this);
    }

    public static Istatus valueOf(int code) {
        return IstatusMap.INSTANCE.get(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getHttp() {
        return http;
    }

    public void setHttp(int http) {
        this.http = http;
    }

    public String getDisplay() {
        return display;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private static boolean isBlank(String str) {
        int strLen;
        if(str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

}
