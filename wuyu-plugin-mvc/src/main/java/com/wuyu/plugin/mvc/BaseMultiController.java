//--------------------------------------------------------------------------
// Copyright (c) 2010-2020, En.dennisit or Cn.苏若年
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
// Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// Neither the name of the dennisit nor the names of its contributors
// may be used to endorse or promote products derived from this software
// without specific prior written permission.
// Author: dennisit@163.com | dongping.pu | 苏若年
//--------------------------------------------------------------------------
package com.wuyu.plugin.mvc;

import com.wuyu.plugin.utils.DateUtil;
import com.wuyu.plugin.utils.StringUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class BaseMultiController extends MultiActionController {

    public static final int DEFAULT_SIZE = 15;

    public static final String[] parsePatterns = new String[]{"yyyy-MM","yyyyMM","yyyy/MM",
            "yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd",
            "yyyyMMddHHmmss",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss"};


    /**
     * to page view with no data
     * @param viewPath
     * @return
     */
    public ModelAndView toView(String viewPath) {
        return new ModelAndView(viewPath,null);
    }

    public String directView(String redirect){
        return "redirect:" + redirect;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // param escapeHtml,forbidden XSS
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringUtil.escapeHtml4(text.trim()));
            }
            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        // date convert
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    setValue(DateUtil.parseDate(text, parsePatterns));
                } catch (ParseException e) {
                    System.err.println(e);
                }
            }
        });
    }

}
