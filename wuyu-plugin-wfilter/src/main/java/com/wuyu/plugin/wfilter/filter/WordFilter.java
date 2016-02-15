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
package com.wuyu.plugin.wfilter.filter;

import com.wuyu.plugin.wfilter.analy.TextAnalysis;
import com.wuyu.plugin.wfilter.conf.WConfs;
import com.wuyu.plugin.wfilter.wtree.DictTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * description:
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WordFilter{

    private static final Logger LOG = LoggerFactory.getLogger(WordFilter.class);

    private Map<String, Map> dictTree;
    private Character replaceTo = WConfs.DEFAULT_REPLACE_TO;
    private Boolean isEachReplace = WConfs.DEFAULT_ISEACH_REPLACE_TO;


    public WordFilter(){
        this.dictTree = DictTreeNode.getTree();
        this.replaceTo = DictTreeNode.getReplaceTo();
        this.isEachReplace = DictTreeNode.isEachReplace();
        LOG.debug("replaceTo：" + replaceTo);
        LOG.debug("isEachReplace:" + isEachReplace);
    }

    /**
     * 抓取文本中的敏感词集合
     * @param text
     * @return
     */
    public  Set<String> fetch(String text) {
        return TextAnalysis.getInstance().analysis(dictTree, text);
    }

    /**
     * 替换文本中的敏感词
     * @param text
     * @return
     */
    public String replace(String text) {
        return replace(text, this.replaceTo);
    }


    /**
     * 替换文本中的敏感词
     * @param text
     * @return
     */
    public String replace(String text, Character replacement) {
        return replace(text, replacement, this.isEachReplace);
    }

    /**
     * 替换文本中的敏感词
     * @param text
     * @param replacement
     * @return
     */
    public String replace(String text, Character replacement, boolean isEachReplace) {
        return TextAnalysis.getInstance().replace(dictTree, text, replacement, isEachReplace);
    }


    /**
     * 标记出文本中的敏感词
     * @param text
     * @return
     */
    public String highlight(String text) {
        return TextAnalysis.getInstance().mark(dictTree, text, WConfs.DEFAULT_START_TAG, WConfs.DEFAULT_END_TAG);
    }

    /**
     * 标记出文本中的敏感词
     * @param text
     * @param startTag
     * @param endTag
     * @return
     */
    public String highlight(String text, String startTag, String endTag) {
        return TextAnalysis.getInstance().mark(dictTree, text, startTag, endTag);
    }

}
