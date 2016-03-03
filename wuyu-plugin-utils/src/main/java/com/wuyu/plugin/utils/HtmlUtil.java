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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class HtmlUtil {

    private static String tempPreBlock = "%%%HTMLCOMPRESS~PRE&&&";
    private static String tempTextAreaBlock = "%%%HTMLCOMPRESS~TEXTAREA&&&";
    private static String tempScriptBlock = "%%%HTMLCOMPRESS~SCRIPT&&&";
    private static String tempStyleBlock = "%%%HTMLCOMPRESS~STYLE&&&";
    private static String tempJspBlock = "%%%HTMLCOMPRESS~JSP&&&";

    private static Pattern commentPattern = Pattern.compile("<!--\\s*[^\\[].*?-->", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static Pattern itsPattern = Pattern.compile(">\\s+?<", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static Pattern prePattern = Pattern.compile("<pre[^>]*?>.*?</pre>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static Pattern taPattern = Pattern.compile("<textarea[^>]*?>.*?</textarea>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static Pattern jspPattern = Pattern.compile("<%([^-@][\\w\\W]*?)%>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    // <script></script>
    private static Pattern scriptPattern = Pattern.compile("(?:<script\\s*>|<script type=['\"]text/javascript['\"]\\s*>)(.*?)</script>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static Pattern stylePattern = Pattern.compile("<style[^>()]*?>(.+)</style>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    // 单行注释，
    private static Pattern signleCommentPattern = Pattern.compile("//.*");
    // 字符串匹配
    private static Pattern stringPattern = Pattern.compile("(\"[^\"\\n]*?\"|'[^'\\n]*?')");
    // trim去空格和换行符
    private static Pattern trimPattern = Pattern.compile("\\n\\s*", Pattern.MULTILINE);
    private static Pattern trimPattern2 = Pattern.compile("\\s*\\r", Pattern.MULTILINE);
    // 多行注释
    private static Pattern multiCommentPattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static String tempSingleCommentBlock = "%%%HTMLCOMPRESS~SINGLECOMMENT&&&";  // //占位符
    private static String tempMulitCommentBlock1 = "%%%HTMLCOMPRESS~MULITCOMMENT1&&&";  // 占位符

    /**
     * 压缩html文件
     * @param html
     * @return
     */
    public static String compress(String html) {
        if (html == null || html.length() == 0) {
            return html;
        }

        List<String> preBlocks = new ArrayList<String>();
        List<String> taBlocks = new ArrayList<String>();
        List<String> scriptBlocks = new ArrayList<String>();
        List<String> styleBlocks = new ArrayList<String>();
        List<String> jspBlocks = new ArrayList<String>();

        String result = html;

        //preserve inline java code
        Matcher jspMatcher = jspPattern.matcher(result);
        while (jspMatcher.find()) {
            jspBlocks.add(jspMatcher.group(0));
        }
        result = jspMatcher.replaceAll(tempJspBlock);

        //preserve PRE tags
        Matcher preMatcher = prePattern.matcher(result);
        while (preMatcher.find()) {
            preBlocks.add(preMatcher.group(0));
        }
        result = preMatcher.replaceAll(tempPreBlock);

        //preserve TEXTAREA tags
        Matcher taMatcher = taPattern.matcher(result);
        while (taMatcher.find()) {
            taBlocks.add(taMatcher.group(0));
        }
        result = taMatcher.replaceAll(tempTextAreaBlock);

        //preserve SCRIPT tags
        Matcher scriptMatcher = scriptPattern.matcher(result);
        while (scriptMatcher.find()) {
            scriptBlocks.add(scriptMatcher.group(0));
        }
        result = scriptMatcher.replaceAll(tempScriptBlock);

        // don't process inline css
        Matcher styleMatcher = stylePattern.matcher(result);
        while (styleMatcher.find()) {
            styleBlocks.add(styleMatcher.group(0));
        }
        result = styleMatcher.replaceAll(tempStyleBlock);

        //process pure html
        result = processHtml(result);

        //process preserved blocks
        result = processPreBlocks(result, preBlocks);
        result = processTextareaBlocks(result, taBlocks);
        result = processScriptBlocks(result, scriptBlocks);
        result = processStyleBlocks(result, styleBlocks);
        result = processJspBlocks(result, jspBlocks);
        result = result.replace("\r\n", "").replace("\n", "");
        preBlocks = taBlocks = scriptBlocks = styleBlocks = jspBlocks = null;

        return result.trim();
    }

    private static String processHtml(String html) {
        String result = html;

        //remove comments
//        if(removeComments) {
        result = commentPattern.matcher(result).replaceAll("");
//        }

        //remove inter-tag spaces
//        if(removeIntertagSpaces) {
        result = itsPattern.matcher(result).replaceAll("><");
//        }

        //remove multi whitespace characters
//        if(removeMultiSpaces) {
        result = result.replaceAll("\\s{2,}", " ");
//        }

        return result;
    }

    private static String processJspBlocks(String html, List<String> blocks) {
        String result = html;
        for (int i = 0; i < blocks.size(); i++) {
            blocks.set(i, compressJsp(blocks.get(i)));
        }
        //put preserved blocks back
        while (result.contains(tempJspBlock)) {
            result = result.replaceFirst(tempJspBlock, Matcher.quoteReplacement(blocks.remove(0)));
        }

        return result;
    }

    private static String processPreBlocks(String html, List<String> blocks) {
        String result = html;

        //put preserved blocks back
        while (result.contains(tempPreBlock)) {
            result = result.replaceFirst(tempPreBlock, Matcher.quoteReplacement(blocks.remove(0)));
        }

        return result;
    }

    private static String processTextareaBlocks(String html, List<String> blocks) {
        String result = html;

        //put preserved blocks back
        while (result.contains(tempTextAreaBlock)) {
            result = result.replaceFirst(tempTextAreaBlock, Matcher.quoteReplacement(blocks.remove(0)));
        }

        return result;
    }

    private static String processScriptBlocks(String html, List<String> blocks) {
        String result = html;

//        if(compressJavaScript) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.set(i, compressJavaScript(blocks.get(i)));
        }
//        }

        //put preserved blocks back
        while (result.contains(tempScriptBlock)) {
            result = result.replaceFirst(tempScriptBlock, Matcher.quoteReplacement(blocks.remove(0)));
        }

        return result;
    }

    private static String processStyleBlocks(String html, List<String> blocks) {
        String result = html;

//        if(compressCss) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.set(i, compressCssStyles(blocks.get(i)));
        }
//        }

        //put preserved blocks back
        while (result.contains(tempStyleBlock)) {
            result = result.replaceFirst(tempStyleBlock, Matcher.quoteReplacement(blocks.remove(0)));
        }

        return result;
    }

    private static String compressJsp(String source) {
        //check if block is not empty
        Matcher jspMatcher = jspPattern.matcher(source);
        if (jspMatcher.find()) {
            String result = compressJspJs(jspMatcher.group(1));
            return (new StringBuilder(source.substring(0, jspMatcher.start(1))).append(result).append(source.substring(jspMatcher.end(1)))).toString();
        } else {
            return source;
        }
    }

    private static String compressJavaScript(String source) {
        //check if block is not empty
        Matcher scriptMatcher = scriptPattern.matcher(source);
        if (scriptMatcher.find()) {
            String result = compressJspJs(scriptMatcher.group(1));
            return (new StringBuilder(source.substring(0, scriptMatcher.start(1))).append(result).append(source.substring(scriptMatcher.end(1)))).toString();
        } else {
            return source;
        }
    }

    private static String compressCssStyles(String source) {
        //check if block is not empty
        Matcher styleMatcher = stylePattern.matcher(source);
        if (styleMatcher.find()) {
            // 去掉注释，换行
            String result = multiCommentPattern.matcher(styleMatcher.group(1)).replaceAll("");
            result = trimPattern.matcher(result).replaceAll("");
            result = trimPattern2.matcher(result).replaceAll("");
            return (new StringBuilder(source.substring(0, styleMatcher.start(1))).append(result).append(source.substring(styleMatcher.end(1)))).toString();
        } else {
            return source;
        }
    }

    private static String compressJspJs(String source) {
        String result = source;
        // 因注释符合有可能出现在字符串中，所以要先把字符串中的特殊符好去掉
        Matcher stringMatcher = stringPattern.matcher(result);
        while (stringMatcher.find()) {
            String tmpStr = stringMatcher.group(0);

            if (tmpStr.indexOf("//") != -1 || tmpStr.indexOf("") != -1) {
                String blockStr = tmpStr.replaceAll("//", tempSingleCommentBlock).replaceAll("/\\*", tempMulitCommentBlock1);

                result = result.replace(tmpStr, blockStr);
            }
        }
        // 去掉注释
        result = signleCommentPattern.matcher(result).replaceAll("");
        result = multiCommentPattern.matcher(result).replaceAll("");
        result = trimPattern2.matcher(result).replaceAll("");
        result = trimPattern.matcher(result).replaceAll(" ");
        // 恢复替换掉的字符串
        result = result.replaceAll(tempSingleCommentBlock, "//").replaceAll(tempMulitCommentBlock1, "");

        return result;
    }


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
