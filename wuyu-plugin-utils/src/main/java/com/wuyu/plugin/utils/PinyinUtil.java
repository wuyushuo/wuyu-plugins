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

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class PinyinUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PinyinUtil.class);

    public static String CNtoPinYin(String cn){
        return HanyuHold.getInstance().getStringPinYin(cn);
    }

    private static class HanyuHold{

        private final static Hanyu instance = new Hanyu();
        public static Hanyu getInstance(){
            return HanyuHold.instance;
        }
    }

    static class Hanyu{

        private HanyuPinyinOutputFormat format = null;
        private String[] pinyin;

        public Hanyu(){
            format = new HanyuPinyinOutputFormat();
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pinyin = null;
        }

        //转换单个字符
        public String getCharacterPinYin(char c){
            try{
                pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            }catch(BadHanyuPinyinOutputFormatCombination e){
                LOG.error(e.getLocalizedMessage(),e);
            }

            // 如果c不是汉字，toHanyuPinyinStringArray会返回null
            if(pinyin == null) return null;
            // 只取一个发音，如果是多音字，仅取第一个发音
            return pinyin[0];

        }

        //转换一个字符串
        public String getStringPinYin(String str){
            StringBuilder sb = new StringBuilder();
            String tempPinyin = null;
            for(int i = 0; i < str.length(); ++i){
                tempPinyin =getCharacterPinYin(str.charAt(i));
                if(tempPinyin == null){
                    // 如果str.charAt(i)非汉字，则保持原样
                    sb.append(str.charAt(i));
                }else{
                    sb.append(tempPinyin);
                }
            }
            return sb.toString();
        }
    }


    public static String toHanyuPinyinCapitalizedString(String str) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder resultPinyinStrBuf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String mainPinyinStrOfChar = getFirstHanyuPinyinString(str.charAt(i), format);

            if (null != mainPinyinStrOfChar) {
                resultPinyinStrBuf.append(toCapitalized(mainPinyinStrOfChar));
            } else {
                resultPinyinStrBuf.append(str.charAt(i));
            }
        }
        return resultPinyinStrBuf.toString();
    }

    public static String toPinyinWithNoSpecialCharacter(String text){
        if(StringUtil.isNotBlank(text)){
            text = toHanyuPinyinLowerCasedString(text);
            if(StringUtil.isNotBlank(text)){
                return getClearSpecialCharacters(text);
            }
        }
        return StringUtil.EMPTY;
    }

    public static String getClearSpecialCharacters(String text) {
        return isBlankOrNull(text)?"":text.replaceAll("[`~!@#$%^&*()+=|{}\':;,\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s·\"-]", "");
    }
    public static boolean isBlankOrNull(String str) {
        return str == null?true:str.trim().length() < 1;
    }

    public static String toCapitalized(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String toHanyuPinyinLowerCasedString(String str) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder resultPinyinStrBuf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String mainPinyinStrOfChar = getFirstHanyuPinyinString(str.charAt(i), format);

            if (null != mainPinyinStrOfChar) {
                resultPinyinStrBuf.append(mainPinyinStrOfChar);
            } else {
                resultPinyinStrBuf.append(str.charAt(i));
            }
        }
        return resultPinyinStrBuf.toString();
    }

    public static Set<String> toHanyuPinyinLowerCasedString2(String str) {
        List<String[]> data = new ArrayList<String[]>();
        for (int i = 0; i < str.length(); i++) {
            String[] arrays = getHanyuPinyinArrays(str.charAt(i));
            data.add(arrays);
        }
        return combination(data);
    }

    public static Set<String> toFirstHanyuPinyinArrays2(String str) {
        List<String[]> data = new ArrayList<String[]>();
        for (int i = 0; i < str.length(); i++) {
            String[] arrays = getFirstHanyuPinyinArrays(str.charAt(i));
            data.add(arrays);
        }
        return combination(data);
    }

    public static String toFirstPinyinLowerCasedString(String str) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder resultPinyinStrBuf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String mainPinyinStrOfChar = getFirstHanyuPinyinString(str.charAt(i), format);

            if (null != mainPinyinStrOfChar) {
                resultPinyinStrBuf.append(mainPinyinStrOfChar.charAt(0));
            } else {
                resultPinyinStrBuf.append(str.charAt(i));
            }
        }
        return resultPinyinStrBuf.toString();
    }

    public static String getFirstHanyuPinyinString(char ch, HanyuPinyinOutputFormat outputFormat) {
        String[] pinyinStrArray;
        try {
            pinyinStrArray = PinyinHelper.toHanyuPinyinStringArray(ch, outputFormat);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            return null;
        }

        if ((null != pinyinStrArray) && (pinyinStrArray.length > 0)) {
            return pinyinStrArray[0];
        } else {
            return null;
        }
    }

    /**
     * 获取指定字符的汉语拼音
     * <p/>
     * unicode编码范围：
     * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
     * 数字：[0x30,0x39]（或十进制[48, 57]）
     * 小写字母：[0x61,0x7a]（或十进制[97, 122]）
     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
     *
     * @param c
     * @return Set<String>
     */
    public static Set<String> getHanyuPinyinStrings(char c) {
        //汉语拼音格式输出设置
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //全部小写
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        //不带音标
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //使用字母“V”
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        Set<String> result = new HashSet<String>();
        String[] pinyins;
        if (isChineseChar(c)) {
            //转换中文
            try {
                pinyins = PinyinHelper.toHanyuPinyinStringArray(c, format);
                result.addAll(Arrays.asList(pinyins));
            } catch (Exception e) {
                LOG.error("Unable to get pinyin. Char: {}, error message: {}", c, e.getMessage());
            }
        } else if (isEnglishChar(c)) {
            //英文转换为大写，然后输出
            result.add(String.valueOf(Character.toUpperCase(c)));
        }
        return result;
    }

    public static String[] getHanyuPinyinArrays(char c) {
        Set<String> set = getHanyuPinyinStrings(c);
        return set.toArray(new String[set.size()]);
    }

    public static String[] getFirstHanyuPinyinArrays(char c) {
        Set<String> set = getHanyuPinyinStrings(c);
        String[] result = new String[set.size()];
        int i = 0;
        for (String val : set) {
            result[i++] = val.substring(0, 1);
        }
        return result;
    }

    public static boolean isChineseChar(char c) {
        int intC = (int) c;
        return (intC >= 19968 && intC <= 40869);
    }

    public static boolean isEnglishChar(char c) {
        int intC = (int) c;
        return (intC >= 65 && intC <= 90) || (intC >= 97 && intC <= 122);
    }

    public static Set<String> combination(List<String[]> data) {
        String sep = "-";
        int size = data.size();
        if (size < 2) {
            return new HashSet<String>();
        }
        List<Integer[]> parts = new ArrayList<Integer[]>();
        for (int i = 1; i < size; i++) {
            Integer[] part = new Integer[2];
            part[0] = i - 1;
            part[1] = i;
            parts.add(part);
        }
        List<List<String>> middleData = new ArrayList<List<String>>();
        for (Integer[] part : parts) {
            String[] data1 = data.get(part[0]);
            String[] data2 = data.get(part[1]);
            List<String> tmp = new ArrayList<String>();
            for (String value1 : data1) {
                for (String value2 : data2) {
                    tmp.add(value1 + sep + value2);
                }
            }
            middleData.add(tmp);
        }

        Set<String> delaData = new HashSet<String>(middleData.get(0));

        for (int i = 1; i < middleData.size(); i++) {
            List<String> result = new ArrayList<String>();
            List<String> nextData = middleData.get(i);
            for (String next : nextData) {
                for (String val : delaData) {
                    int start = val.lastIndexOf(sep);
                    String suffix = val.substring(start + 1);
                    int end = next.indexOf(sep);
                    String prefix = next.substring(0, end);
                    if (suffix.equals(prefix)) {
                        result.add(val + next.substring(end));
                    }
                }
            }
            delaData = new HashSet<String>(result);
        }
        Set<String> result = new HashSet<String>();
        for (String value : delaData) {
            result.add(value.replaceAll(sep, ""));
        }
        return result;
    }

}
