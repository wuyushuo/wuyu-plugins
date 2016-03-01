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
package com.wuyu.plugin.nlpcn.keyword.extract;

import com.wuyu.plugin.nlpcn.keyword.compare.ValueSortComparator;
import com.wuyu.plugin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class KeyWordExtract {


    private static final Logger LOG = LoggerFactory.getLogger(KeyWordExtract.class);

    /**
     * get keyWord from sortTerm sorted by weight
     * @param content extract content
     * @param number number of keywords
     */
    public static List<String> showTerm(String content, int number) {
        Map<String, Integer> sortTerm = spliteWord(content);
        if(null != sortTerm && number > 0){
            return showTerm(sortTerm, number);
        }
        return Collections.emptyList();
    }

    /**
     * sort keyword with each weight
     * @param content
     * @return
     */
    private static Map<String, Integer> spliteWord(String content){
        if(StringUtil.isBlank(content)){
            return null;
        }
        Map<String, Integer> term = new TreeMap<String, Integer>();
        ValueSortComparator byc = new ValueSortComparator(term);
        Map<String, Integer> sortTerm = new TreeMap<String, Integer>(byc);
        StringBuffer sb = new StringBuffer();
        byte[] bt;
        InputStream is;
        Reader read;
        Lexeme t;
        IKSegmenter iks;
        try {
            //对content处理
            bt= content.getBytes();
            is= new ByteArrayInputStream(bt);
            read= new InputStreamReader(is);
            iks= new IKSegmenter(read,true);
            while ((t = iks.next()) != null) {
                String word=t.getLexemeText();
                sb.append(word + "/");
                int NofTerm;
                if(term.containsKey(word)){
                    NofTerm=term.get(word);
                    NofTerm++;
                    term.put(word, NofTerm);
                }else{
                    term.put(word, 1);
                }
                //System.out.println(word);
            }
            sortTerm.putAll(term);
            //System.out.println("删除之前：   "+sb.toString());
            sb.delete(sb.length() - 1, sb.length());
            //System.out.println("删除之后：   "+sb.toString());
            sb.delete(0, sb.length());
        }catch(Exception e){
            e.printStackTrace();
        }
        return sortTerm;
    }

    /**
     * get keyWord from sortTerm sorted by weight
     * @param sortTerm map of sortTerm
     * @param number number of keywords
     */
    private static List<String> showTerm(Map<String, Integer> sortTerm, int number) {
        List<String> keywords = new ArrayList<String>();
        if(null != sortTerm && number >0){
            int tmp = number > sortTerm.size()? sortTerm.size():number;
            Iterator iter = sortTerm.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String word = (String) entry.getKey();
                int n = (Integer) entry.getValue();
                LOG.debug("[term: " + word + ", occNum: " + n + "]");
                if(keywords.size()<=tmp){
                    keywords.add(word);
                }else{
                    break;
                }
            }
        }
        return keywords;
    }
}
