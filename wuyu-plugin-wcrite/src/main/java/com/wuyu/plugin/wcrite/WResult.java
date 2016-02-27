/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, dennisit.pu All rights reserved. 
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
 * Author: dennisit.pu (dennisit@163.com)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.wcrite;

import com.wuyu.plugin.wcrite.code.Istatus;
import com.wuyu.plugin.wcrite.http.HttpType;

import java.io.Serializable;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WResult<T> implements Serializable {

    // business status code
    private int code;

    // http status code
    private int http;

    // business code message
    private String message;

    // display net transaction
    private String display;

    // data
    private T data;

    public WResult() {
        this(Istatus.BAD_REQUEST);
    }

    public WResult(Istatus istatus){
        this(istatus, null);
    }

    public WResult(Istatus istatus, T data){
        this.code = istatus.getCode();
        this.http = istatus.getHttp();
        this.message = istatus.getMessage();
        this.display = istatus.getDisplay();
        this.data = data;
    }

    public WResult(int code){
        Istatus istatus = null;
        HttpType httpType = Istatus.valueOf(code);
        if(null == httpType){
            istatus = Istatus.BAD_REQUEST;
        }
        if(httpType instanceof Istatus){
            istatus = (Istatus) httpType;
        }
        this.code = istatus.getCode();
        this.http = istatus.getHttp();
        this.message = istatus.getMessage();
        this.display = istatus.getDisplay();
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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getDisplay() {
        return display;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
