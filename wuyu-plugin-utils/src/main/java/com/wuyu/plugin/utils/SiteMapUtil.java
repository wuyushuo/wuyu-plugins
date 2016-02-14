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

import java.util.List;
import java.util.Map;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class SiteMapUtil {


    /**
     * 生成行间隔的url地址
     * @param urls
     * @return
     */
    public static String sitemapTxt(List<String> urls){
        if(CollectionUtil.isEmpty(urls)){
            return StringUtil.EMPTY;
        }
        StringBuffer siteMap = new StringBuffer(1024);
        for(String url: urls){
            siteMap.append(url).append("\r\n");
        }
        return siteMap.toString();
    }

    /**
     * 生成xml格式的sitemap,默认页面权重值0.8
     * @param urls
     * @return
     */
    public static String sitemapXml(List<String> urls){
        StringBuffer siteMap = new StringBuffer(2048);
        siteMap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n")
                .append("<urlset>").append("\r\n\r\n");
        if(null != urls && urls.size() > 0){
            for (String url : urls) {
                siteMap.append("\t<url>").append("\r\n");
                siteMap.append("\t    <loc>").append(url).append("</loc>").append("\r\n");
                siteMap.append("\t    <priority>").append(0.8).append("</priority>").append("\r\n");
                siteMap.append("\t    <lastmod>").append(DateUtil.getNowTime("yyyy-MM-dd")).append("</lastmod>").append("\r\n");
                siteMap.append("\t    <changefreq>daily</changefreq>").append("\r\n");
                siteMap.append("\t</url>").append("\r\n");
            }
        }
        siteMap.append("\r\n").append("</urlset>");
        return siteMap.toString();
    }

    /**
     * 自定于权重的sitemap文件生成
     * @param urlWeights
     * @return
     */
    public static String sitemapXml(Map<String, Double> urlWeights){
        StringBuffer siteMap = new StringBuffer(2048);
        siteMap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n")
                .append("<urlset>").append("\r\n\r\n");
        if(null != urlWeights && urlWeights.size() > 0){
            for (Map.Entry<String, Double> entry : urlWeights.entrySet()) {
                siteMap.append("\t<url>").append("\r\n");
                siteMap.append("\t    <loc>").append(entry.getKey()).append("</loc>").append("\r\n");
                siteMap.append("\t    <priority>").append(entry.getValue()).append("</priority>").append("\r\n");
                siteMap.append("\t    <lastmod>").append(DateUtil.getNowTime("yyyy-MM-dd")).append("</lastmod>").append("\r\n");
                siteMap.append("\t    <changefreq>daily</changefreq>").append("\r\n");
                siteMap.append("\t</url>").append("\r\n");
            }
        }
        siteMap.append("\r\n").append("</urlset>");
        return siteMap.toString();
    }
}
