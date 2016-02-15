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
package com.wuyu.plugin.wfilter.conf;

/**
 * description:
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WConfs {

    /**
     * main illegal word path //com.yinyuetai.plugin.wfilter
     */
    public static final String MAIN_DICT_PATH = "com/wuyu/plugin/wfilter/illegal_word.txt";

    // 元素KEY 配置文件名称
    public static final String ELE_CONF_FILE_NAME = "WordFilter.cfg.xml";
    // 元素KEY 词库路径
    public static final String ELE_WORD_DICT = "word_dict";
    // 元素KEY 用于替换的字符
    public static final String ELM_REPLACE_TO = "replace_to";
    // 元素KEY 是否逐个字符替换
    public static final String ELM_ISEACH_REPLACE = "is_each_replace";


    // 在树当中标志一个词的结束
    public static final String TREE_END_KEY = "^";
    // 敏感词value标记
    public static final String WORD_VALUE = "v";
    // 敏感词长度标记
    public static final String WORD_LENGTH = "l";
    // 默认起始标记
    public static final String DEFAULT_START_TAG = "<font color=\"red\">";
    // 默认结束标记
    public static final String DEFAULT_END_TAG = "</font>";
    // 将关键词逐个字符替换成指定字符
    public static final char DEFAULT_REPLACE_TO = '*';
    // 是否逐个替换掉
    public static final boolean DEFAULT_ISEACH_REPLACE_TO = false;
    // 默认的编码
    public static final String DEFAULT_CHARSET = "UTF-8";

}
