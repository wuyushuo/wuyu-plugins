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
package com.wuyu.plugin.utils;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class HtmlUtil {

    // 只有纯文本可以通过
    public static String getText(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.none());
    }

    // 以下标签可以通过
    // b, em, i, strong, u. 纯文本
    public static String getSimpleHtml(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.simpleText());
    }

    // 以下标签可以通过
    //a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, strike, strong, sub, sup, u, ul
    public static String getBasicHtml(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.basic());
    }

    //在basic基础上  增加图片通过
    public static String getBasicHtmlandimage(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.basicWithImages());
    }
    // 以下标签可以通过
    //a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul
    public static String getFullHtml(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.relaxed());
    }

    //只允许指定的html标签
    public static String clearTags(String html, String ...tags) {
        Whitelist wl = new Whitelist();
        return Jsoup.clean(html, wl.addTags(tags));
    }

    // 对关键字加上颜色
    public static String markKeywods (String keywords, String target) {
        if (StringUtils.isNotBlank(keywords)) {
            String[] arr = keywords.split(" ");
            for (String s : arr) {
                if (StringUtils.isNotBlank(s)) {
                    String temp = "<span class=\"highlight\">" + s + "</span>";
                    target = target.replaceAll(s, temp);
                }
            }
        }
        return target;
    }

    // 获取文章中的img url
    public static String getImgSrc(String html) {
        if (html == null)
            return null;
        Document doc = Jsoup.parseBodyFragment(html);
        Element image = doc.select("img").first();
        return image == null ? null : image.attr("src");
    }

    /**
     * 截取字符串长字，保留HTML格式
     *
     * @param content
     * @param len 字符长度
     */
    public static String truncateHTML(String content, int len) {
        Document dirtyDocument = Jsoup.parse(content);
        Element source = dirtyDocument.body();
        Document clean = Document.createShell(dirtyDocument.baseUri());
        Element dest = clean.body();
        truncateHTML(source, dest, len);
        return dest.html();
    }

    /**
     * 使用Jsoup预览
     *
     * @param source
     *            需要过滤的
     * @param dest
     *            过滤后的对象
     * @param len
     *            截取字符长度
     *
     *         Document dirtyDocument = Jsoup.parse(sb.toString());<br />
     *         Element source = dirtyDocument.body();<br />
     *         Document clean = Document.createShell(dirtyDocument.baseUri());<br />
     *         Element dest = clean.body();<br />
     *         int len = 6;<br />
     *         truncateHTML(source,dest,len);<br />
     *         System.out.println(dest.html());<br />
     */
    private static void truncateHTML(Element source, Element dest, int len) {
        List<Node> sourceChildren = source.childNodes();
        for (Node sourceChild : sourceChildren) {
            if (sourceChild instanceof Element) {
                Element sourceEl = (Element) sourceChild;
                Element destChild = createSafeElement(sourceEl);
                int txt = dest.text().length();
                if (txt >= len) {
                    break;
                } else {
                    len = len - txt;
                }
                dest.appendChild(destChild);
                truncateHTML(sourceEl, destChild, len);
            } else if (sourceChild instanceof TextNode) {
                int destLeng = dest.text().length();
                if (destLeng >= len) {
                    break;
                }
                TextNode sourceText = (TextNode) sourceChild;
                int txtLeng = sourceText.getWholeText().length();
                if ((destLeng + txtLeng) > len) {
                    int tmp = len - destLeng;
                    String txt = sourceText.getWholeText().substring(0, tmp);
                    TextNode destText = new TextNode(txt, sourceChild.baseUri());
                    dest.appendChild(destText);
                    break;
                } else {
                    TextNode destText = new TextNode(sourceText.getWholeText(), sourceChild.baseUri());
                    dest.appendChild(destText);
                }
            }
        }
    }

    /**
     * 按原Element重建一个新的Element
     * @param sourceEl
     * @return
     */
    private static Element createSafeElement(Element sourceEl) {
        String sourceTag = sourceEl.tagName();
        Attributes destAttrs = new Attributes();
        Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
        Attributes sourceAttrs = sourceEl.attributes();
        for (Attribute sourceAttr : sourceAttrs) {
            destAttrs.put(sourceAttr);
        }
        return dest;
    }

}
