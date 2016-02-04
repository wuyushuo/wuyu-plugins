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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class VelocityUtil {

    private static final String CHARSET = "UTF-8";

    /**
     * merge template with map data
     * @param templatePath template path
     * @param contextMap map data for velocity
     * @return
     */
    public static String mergeWithPath(String templatePath, Map<String, Object> contextMap){
        return mergeWithPath(templatePath, contextMap, CHARSET);
    }

    /**
     * merge template with map data
     * @param templatePath template path
     * @param contextMap map data for velocity
     * @param encoding utf8
     * @return
     */
    public static String mergeWithPath(String templatePath, Map<String, Object> contextMap,String encoding){
        Properties p = new Properties();
        p.put("input.encoding", encoding);
        p.put("output.encoding", encoding);
        p.put("resource.loader", "class");
        p.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
        VelocityContext velocityContext = new VelocityContext(contextMap);
        Template template = null;
        template = Velocity.getTemplate(templatePath, encoding);
        StringWriter sw = new StringWriter();
        template.merge(velocityContext, sw );
        return sw.toString();
    }


    /**
     * merge template with map data
     * @param templateContent content of velocity template
     * @param contextMap map data for velocity
     * @return
     */
    public static String mergeWithContent(String templateContent, Map<String, Object> contextMap){
        return mergeWithContent(templateContent, contextMap, CHARSET);
    }

    /**
     * merge template with map data
     * @param templateContent content of velocity template
     * @param contextMap map data for velocity
     * @param encoding utf8
     * @return
     */
    public static String mergeWithContent(String templateContent, Map<String, Object> contextMap,String encoding){
        Properties p = new Properties();
        p.put("input.encoding", encoding);
        p.put("output.encoding", encoding);
        p.put("resource.loader", "class");
        p.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
        VelocityContext velocityContext = new VelocityContext(contextMap);
        StringWriter sw = new StringWriter();
        Velocity.evaluate(velocityContext,sw, "template.log", templateContent);
        return sw.toString();
    }

}
