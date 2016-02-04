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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ResourceUtil {

    /**
     * 获得ClassPath
     * @return ClassPath
     */
    public static String getClassPath() {
        return getClassPathURL().getPath();
    }

    /**
     * 获得ClassPath URL
     * @return ClassPath URL
     */
    public static URL getClassPathURL() {
        return ClassUtil.getClassLoader().getResource(StringUtil.EMPTY);
    }

    /**
     * 获得资源的URL
     *
     * @param resource 资源（相对Classpath的路径）
     * @return 资源URL
     */
    public static URL getURL(String resource) {
        return ClassUtil.getClassLoader().getResource(resource);
    }


    /**
     * 获得指定资源文件
     * @param resource 资源（相对Classpath的路径）
     * @return 资源文件
     */
    public static File getFile(String resource){
        try {
            return new File(getURL(resource).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getLocalizedMessage(),e);
        }
    }

}
