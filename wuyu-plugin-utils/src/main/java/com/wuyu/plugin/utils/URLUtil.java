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
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class URLUtil {

    /**
     * 创建URL对象
     * @param url URL
     * @return URL对象
     */
    public static URL url(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获得URL
     *
     * @param pathBaseClassLoader 相对路径（相对于classes）
     * @return URL
     */
    public static URL getURL(String pathBaseClassLoader) {
        return ClassUtil.getClassLoader().getResource(pathBaseClassLoader);
    }

    /**
     * 获得URL
     *
     * @param path 相对给定 class所在的路径
     * @param clazz 指定class
     * @return URL
     */
    public static URL getURL(String path, Class<?> clazz) {
        return clazz.getResource(path);
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param file URL对应的文件对象
     * @return URL
     */
    public static URL getURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error occured when get URL!");
        }
    }

    /**
     * 获得URL，常用于使用绝对路径时的情况
     *
     * @param files URL对应的文件对象
     * @return URL
     */
    public static URL[] getURLs(File... files) {
        final URL[] urls = new URL[files.length];
        try {
            for(int i = 0; i < files.length; i++){
                urls[i] = files[i].toURI().toURL();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error occured when get URL!", e);
        }

        return urls;
    }

    /**
     * 格式化URL链接
     *
     * @param url 需要格式化的URL
     * @return 格式化后的URL，如果提供了null或者空串，返回null
     */
    public static String formatUrl(String url) {
        if (StringUtil.isBlank(url)){
            return null;
        }
        if (url.startsWith("http://") || url.startsWith("https://")){
            return url;
        }
        return "http://" + url;
    }

    /**
     * 补全相对路径
     *
     * @param baseUrl 基准URL
     * @param relativePath 相对URL
     * @return 相对路径
     */
    public static String complateUrl(String baseUrl, String relativePath) {
        baseUrl = formatUrl(baseUrl);
        if (StringUtil.isBlank(baseUrl)) {
            return null;
        }

        try {
            final URL absoluteUrl = new URL(baseUrl);
            final URL parseUrl = new URL(absoluteUrl, relativePath);
            return parseUrl.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 编码URL
     * @param url URL
     * @param charset 编码
     * @return 编码后的URL
     */
    public static String encode(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 解码URL
     * @param url URL
     * @param charset 编码
     * @return 解码后的URL
     */
    public static String decode(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获得path部分<br>
     * URI -> http://www.aaa.bbb/search?scope=ccc&q=ddd
     * PATH -> /search
     *
     * @param uriStr URI路径
     * @return path
     */
    public static String getPath(String uriStr){
        URI uri = null;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        return uri == null ? null : uri.getPath();
    }
}
