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
package com.wuyu.plugin.wfilter.analy;
import com.wuyu.plugin.wfilter.conf.WConfs;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * description: from hymer text analysis
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class TextAnalysis {

    private TextAnalysis(){
        //
    }

    /**
     * sigleton hold
     */
    private static class TextAnalyzerHold{
        public static TextAnalysis instance = new TextAnalysis();
    }

    public static TextAnalysis getInstance(){
        return TextAnalyzerHold.instance;
    }

    /**
     * do text analysis and return collection of sensitive contained in text
     * @param tree
     * @param text
     * @return result of text`s contains sensitive word result
     */
    public Set<String> analysis(Map<String, Map> tree, String text) {
        Set<String> words = new LinkedHashSet<String>();
        if (text != null && text.trim().length() > 0) {
            analysis(tree, text, words);
        }
        return words;
    }

    /**
     * replace sensitive (each char replaced) word in text
     * @param tree
     * @param text
     * @param replacement
     * @return
     */
    public String replace(Map<String, Map> tree, String text, Character replacement, boolean isEachReplace) {
        if (replacement == null) {
            replacement = WConfs.DEFAULT_REPLACE_TO;
        }
        if (text != null && text.trim().length() > 0) {
            StringBuffer sb = new StringBuffer("");
            replace(tree, text, 0, replacement, isEachReplace, sb);
            return sb.toString();
        }
        return text;
    }

    /**
     * mark sensitive in text with startTag and endTag
     * @param tree
     * @param text
     * @param startTag
     * @param endTag
     * @return
     */
    public String mark(Map<String, Map> tree, String text, String startTag, String endTag) {
        if (text != null && text.trim().length() > 0) {
            StringBuffer sb = new StringBuffer("");
            mark(tree, text, 0, startTag, endTag, sb);
            return sb.toString();
        }
        return text;
    }

    /**
     *
     * @param tree
     * @param text
     * @param index
     * @param startTag
     * @param endTag
     * @param sb
     */
    private void mark(Map<String, Map> tree, String text, int index, String startTag, String endTag, StringBuffer sb) {
        int last = 0;
        int textLen = text.length();
        while (index < textLen) {
            String tmp = text.substring(index, index + 1);
            String nexts = text.substring(index);
            String word = "";
            word = findMaxWord(tree, nexts, 0, word);
            if (!"".equals(word)) {
                int wordLen = word.length();
                if (index >= last) {
                    sb.append(startTag + word + endTag);
                } else {
                    if (last < index + wordLen) {
                        sb.insert(sb.length() - endTag.length(), text.substring(last, index + wordLen));
                    }
                }
                last = index + wordLen;
            } else {
                if (index >= last) {
                    sb.append(tmp);
                }
            }
            index++;
        }
    }

    /**
     *
     * @param tree
     * @param text
     * @param index
     * @param word
     * @return
     */
    private String findMaxWord(Map<String, Map> tree, String text, int index, String word) {
        Map<String, Map> subTree = tree.get(text.substring(index, index + 1));
        if (subTree != null) {
            Map<String, Object> end = subTree.get(WConfs.TREE_END_KEY);
            if (end != null) {
                String sensitiveWord = (String) end.get(WConfs.WORD_VALUE);
                if (word.length() < sensitiveWord.length()) {
                    word = sensitiveWord;
                }
            }
            if ((index + 1) < text.length() && (end == null || subTree.size() > 1)) {
                return findMaxWord(subTree, text, index + 1, word);
            }
        }
        return word;
    }

    /**
     * analysis text
     * @param tree dict tree
     * @param text text content
     * @param words result words
     */
    private void analysis(Map<String, Map> tree, String text, Set<String> words) {
        int index = 0;
        while (index < text.length()) {
            findWord(tree, text, index, words);
            index++;
        }
    }

    /**
     *
     * @param tree
     * @param text
     * @param index
     * @param words
     */
    private void findWord(Map<String, Map> tree, String text, int index, Set<String> words) {
        Map<String, Map> subTree = tree.get(text.substring(index, index + 1));
        if (subTree != null) {
            Map<String, Object> end = subTree.get(WConfs.TREE_END_KEY);
            if (end != null) {
                words.add((String) end.get(WConfs.WORD_VALUE));
            }
            if ((index + 1) < text.length() && (end == null || subTree.size() > 1)) {
                findWord(subTree, text, index + 1, words);
            }
        }
    }


    private void replace(Map<String, Map> tree, String text, int index, char replacement, boolean isEachReplace, StringBuffer sb) {
        int last = 0;
        int textLen = text.length();
        while (index < textLen) {
            String tmp = text.substring(index, index + 1);
            String nexts = text.substring(index);
            String word = "";
            word = findMaxWord(tree, nexts, 0, word);
            if (!"".equals(word)) {
                int replaceLen = 0;
                int wordLen = word.length();
                if (index >= last) {
                    replaceLen = wordLen;
                } else {
                    replaceLen = index + wordLen - last;
                }
                if(!isEachReplace){
                    sb.append(replacement);
                }else{
                    while (replaceLen > 0) {
                        sb.append(replacement);
                        replaceLen--;
                    }
                }
                last = index + wordLen;
            } else {
                if (index >= last) {
                    sb.append(tmp);
                }
            }
            index++;
        }
    }


}
