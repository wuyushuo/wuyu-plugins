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
package com.wuyu.plugin.gen.parser;

import com.wuyu.plugin.gen.bean.VersionInfo;
import com.wuyu.plugin.gen.util.BeanUtils;
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
public class TemplateParser {

    public static String DEFAULT_ENCODING = "UTF-8";

    /**
     * 模板解析
     * @param templateName 模板文件名称
     * @param context 上下文
     * @return 解析结果
     * @throws Exception
     */
    public static String parse(String templateName, Map<String, Object> context) throws Exception {
        return parse(templateName,context,new VersionInfo());
    }

    /**
     * 模板解析
     * @param templateName 模板文件名称
     * @param context 上下文
     * @return 解析结果
     * @throws Exception
     */
    public static String parse(String templateName, Map<String, Object> context,String encoding) throws Exception {
        return parse(templateName,context,encoding,new VersionInfo());
    }

    /**
     * 模板解析
     * @param templateName 模板文件名称
     * @param context 上下文
     * @return 解析结果
     * @throws Exception
     */
    public static String parse(String templateName, Map<String, Object> context,VersionInfo versionInfo) throws Exception {
        return parse(templateName,context,DEFAULT_ENCODING,versionInfo);
    }

    /**
     * 模板解析
     * @param templateName 模板文件名称
     * @param context 上下文
     * @return 解析结果
     * @throws Exception
     */
    public static String parse(String templateName, Map<String, Object> context,String encoding,VersionInfo versionInfo) throws Exception {
    	Properties p = new Properties();
        p.put("input.encoding", encoding);
        p.put("output.encoding", encoding);
        p.put("resource.loader", "class");
        p.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //p.put(Velocity.FILE_RESOURCE_LOADER_PATH, templateName);
        Velocity.init(p);

        context.put("versionInfo", versionInfo);
        context.put("bean", new BeanUtils());
        context.put("well", "#");

        VelocityContext velocityContext = new VelocityContext(context);
        Template template = null;
        templateName = templateName.replaceAll("\\\\", "\\/");
        template = Velocity.getTemplate(templateName, DEFAULT_ENCODING);
        StringWriter sw = new StringWriter();
        template.merge(velocityContext, sw );
        return sw.toString();
    }

}
