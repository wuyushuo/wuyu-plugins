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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class StringUtil {

    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String CRLF = "\r\n";
    public static final String NEWLINE = "\n";
    public static final String COMMA = ",";
    public static final String UNDERLINE = "_";
    public static final String EMPTY = "";

    public static boolean isEmpty(CharSequence cs) {
        return StringUtils.isEmpty(cs);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String escapeHtml4(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.indexOf(seq, searchSeq);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.lastIndexOf(seq, searchSeq);
    }

    public static String substring(String str, int start, int end){
        return StringUtils.substring(str, start, end);
    }

    public static String trim(String str){
        if(StringUtil.isBlank(str)){
            return StringUtil.EMPTY;
        }
        return str.trim();
    }

    public static long reformatLong(long value, long min, long maxValue){
        return value <= min ? min : ( value >= maxValue ? maxValue : value);
    }

    public static int reformatInteger(int value, int min, int maxValue){
        return value <= min ? min : ( value >= maxValue ? maxValue : value);
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        if (values.length == 0|| isBlank(template)) {
            return template;
        }
        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(sub(template, i, length));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(values[valueIndex++]);
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }

        return sb.toString();
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" example: abcdefgh 2 3 -> c abcdefgh 2 -3 -> cde
     *
     * @param string String
     * @param fromIndex 开始的index（包括）
     * @param toIndex 结束的index（不包括）
     * @return 字串
     */
    public static String sub(String string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;

            if (toIndex == 0) {
                toIndex = len;
            }
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        char[] strArray = string.toCharArray();
        char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
        return new String(newStrArray);
    }


    /**
     * 切割前部分
     *
     * @param string 字符串
     * @param toIndex 切割到的位置（不包括）
     * @return 切割后的字符串
     */
    public static String subPre(String string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 切割后部分
     *
     * @param string 字符串
     * @param fromIndex 切割开始的位置（包括）
     * @return 切割后的字符串
     */
    public static String subSuf(String string, int fromIndex) {
        if (isEmpty(string)) {
            return null;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 去掉指定前缀
     *
     * @param str 字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
     */
    public static String removePrefix(String str, String prefix) {
        if(isEmpty(str) || isEmpty(prefix)){
            return str;
        }

        if (str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map 参数值对
     * @return 格式化后的文本
     */
    public static String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes byte数组
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(byte[] bytes, String charset) {
        return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, String charset){
        if(data == null) {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    public static String str(ByteBuffer data, Charset charset){
        if(null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    /**
     * 字符串转换为byteBuffer
     * @param str 字符串
     * @param charset 编码
     * @return byteBuffer
     */
    public static ByteBuffer byteBuffer(String str, String charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }

    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(String str, String charset) {
        return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (null == charset) {
            return str.getBytes();
        }
        return str.getBytes(charset);
    }


    /**
     * 重复某个字符
     * @param c 被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public static String repeat(char c, int count) {
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    /**
     * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->hello_world
     * @param camelCaseStr 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }

        final int length = camelCaseStr.length();
        StringBuilder sb = new StringBuilder();
        char c;
        boolean isPreUpperCase = false;
        for (int i = 0; i < length; i++) {
            c = camelCaseStr.charAt(i);
            boolean isNextUpperCase = true;
            if (i < (length - 1)) {
                isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
            }
            if (Character.isUpperCase(c)) {
                if (!isPreUpperCase || !isNextUpperCase) {
                    if (i > 0) sb.append(UNDERLINE);
                }
                isPreUpperCase = true;
            } else {
                isPreUpperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
     * 例如：hello_world->HelloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String toCamelCase(String name) {
        if (name == null) {
            return null;
        }
        if (name.contains(UNDERLINE)) {
            name = name.toLowerCase();

            StringBuilder sb = new StringBuilder(name.length());
            boolean upperCase = false;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);

                if (c == '_') {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else
            return name;
    }


    public static StopWatch getStopWatch(){
        return SingletonHold.stopWatch;
    }

    private static class SingletonHold{
        public static StopWatch stopWatch = new StopWatch();
    }
}
