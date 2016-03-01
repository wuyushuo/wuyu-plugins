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
package com.wuyu.plugin.nlpcn.nlplang;

import com.wuyu.plugin.nlpcn.keyword.extract.KeyWordExtract;
import org.nlpcn.commons.lang.finger.FingerprintService;
import org.nlpcn.commons.lang.jianfan.JianFan;
import org.nlpcn.commons.lang.pinyin.Pinyin;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.WordAlert;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class NlpcnUtil {

    /**
     * convert simplified chinese to tradition chinese
     * @param str
     * @return
     */
    public static String toTraditionalCN(String str){
        return JianFan.j2f(str);
    }

    /**
     * convert tradition chinese to simplified chinese
     * @param str
     * @return
     */
    public static String toSimplifiedCN(String str){
        return JianFan.f2j(str);
    }


    /**
     * convert each chinese to mapped pingyin
     * @param str
     * @return [chang, jiang, cheng, zhang]
     */

    public static List<String> pinyinList(String str) {
        return Pinyin.pinyin(str);
    }

    /**
     * convert each chinese to mapped pingyin
     * @param str
     * @return changjiangchengzhang
     */
    public static String pinyinString(String str){
        List<String> list = pinyinList(str);
        StringBuffer buffer = new StringBuffer(list.size());
        if(null != list && list.size() > 0){
            for(String c : list){
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * get each chinese first chart
     * @param str
     * @return [c, j, c, z]
     */
    public static List<String> firstChar(String str) {
        return Pinyin.firstChar(str);
    }

    /**
     * convert each chinese to pinyin with phonogram
     * @param str
     * @return [cháng, jiāng, chéng, zhăng]
     */
    public static List<String> unicodePinyin(String str) {
        return Pinyin.unicodePinyin(str);
    }

    /**
     * convert each chinese to pinyin with number phonogram
     * @param str
     * @return [chang2, jiang1, cheng2, zhang3]
     */
    public static List<String> tonePinyin(String str) {
        return Pinyin.tonePinyin(str);
    }

    /**
     * convert list to string
     * @param list
     * @return
     */
    public static String list2String(List<String> list) {
        return Pinyin.list2String(list);
    }

    /**
     * convert list to string skip null
     * @param list
     * @return
     */
    public static String list2StringSkipNull(List<String> list) {
        return Pinyin.list2StringSkipNull(list);
    }

    /**
     * 指纹去重
     * 任何一段信息文字，都可以对应一个不太长的随机数，作为区别它和其它信息的指纹（Fingerprint)。只要算法设计的好，任何两段信息的指纹都很难重复，就如同人类的指纹一样。
     * 信息指纹在加密、信息压缩和处理中有着广泛的应用。
     * 我们这里的做法是文章抽取特征词，压缩为md5指纹。利用这些指纹进行hash去重。广泛应用在。搜索结果推荐结果去重。
     * @param str
     * @return
     */
    public static String fingerprint(String str){
        return new FingerprintService().fingerprint(str);
    }

    /**
     * remove html tag
     * <pre>
     *     params: "<a>hello ansj</a><a href='www.baidu.com'>BaiDu</a>, suggest booke 《java pro》",
     *     output: "hello ansjBaiDu, suggest booke 《java pro》"
     * </pre>
     * @param str
     * @return
     */
    public static String removeHtmlTag(String str){
        return StringUtil.rmHtmlTag(str);
    }

    /**
     * correction character
     * <pre>
     *     params: ａｚ ＡＺ AZ az ０９•
     *     output: az az az az 09·
     * </pre>
     * @param str
     * @return az az az az 09
     */
    public static String correction(String str){
        char[] result = WordAlert.alertStr(str) ;
        return new String(result);
    }

    /**
     * extract keyword
     * @param content
     * @param number
     * @return
     */
    public static List<String> extractKeyWord(String content, int number){
        return KeyWordExtract.showTerm(content, number);
    }

    public static String ikToken(String str){
        if(StringUtil.isBlank(str)){
            return "";
        }
        try {
            StringReader sr = new StringReader(str);
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            StringBuffer ik_py = new StringBuffer("");
            while((lex=ik.next()) != null){
                ik_py.append(lex.getLexemeText()).append(" ");
            }
        }catch (Exception e){

        }
        return com.wuyu.plugin.utils.StringUtil.EMPTY;
    }

}
