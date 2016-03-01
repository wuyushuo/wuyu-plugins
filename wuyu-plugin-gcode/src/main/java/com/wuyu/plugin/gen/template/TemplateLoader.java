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
package com.wuyu.plugin.gen.template;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class TemplateLoader {

    /**
     * 加载模板文件路径
     * @param templateRoot 模板文件查找目录(使用逐个查找的方式查找,先相对,相对没有获取到的话绝对路径获取)
     * @return
     */
    public static List<String> loadTemplateFiles(String templateRoot){
        return loadTemplateFiles(templateRoot,LoadType.Ergodic);
    }

    /**
     * 加载模板文件路径
     * @param templateRoot 模板文件的查询目录
     * @param loadType 加载类型: 相对路径,绝对路径,逐个去查找(先相对,相对没有获取到的话绝对路径获取)
     * @return
     */
    public static List<String> loadTemplateFiles(String templateRoot,LoadType loadType){
        List<String> templateFiles = new ArrayList<String>();
        if(LoadType.Relative.name().equals(loadType.name())){
            return Loader.recursiveLoadRelative(templateRoot, templateFiles);
        }
        if(LoadType.Absolute.name().equals(loadType.name())){
            return Loader.recursiveLoadAbsolute(templateRoot, templateFiles);
        }
        if(LoadType.Ergodic.name().equals(loadType.name())){
            templateFiles = Loader.recursiveLoadRelative(templateRoot, templateFiles);
            if(CollectionUtils.isEmpty(templateFiles)){
                templateFiles = new ArrayList<String>();
                return Loader.recursiveLoadAbsolute(templateRoot, templateFiles);
            }
        }
        return templateFiles;
    }

}
