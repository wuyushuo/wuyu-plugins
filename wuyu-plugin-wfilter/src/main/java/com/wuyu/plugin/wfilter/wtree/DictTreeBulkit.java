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
package com.wuyu.plugin.wfilter.wtree;

import com.wuyu.plugin.wfilter.conf.WConfs;

import java.util.HashMap;
import java.util.Map;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class DictTreeBulkit {

    /**
     * 将指定的词分成字构建到一棵树中。
     * @param tree 字树
     * @param sensitiveWord 敏感词
     * @return
     */
    public static void buildDictTree(Map<String, Map> tree, String sensitiveWord) {
        buildDictTree(tree, sensitiveWord, 0);
    }

    /**
     * @param tree
     * @param sensitiveWord
     * @param index
     * @return
     */
    private static Map<String, Map> buildDictTree(Map<String, Map> tree, String sensitiveWord, int index) {
        if (index == sensitiveWord.length()) {
            tree.put(WConfs.TREE_END_KEY, buileDictMap(sensitiveWord));
            return tree;
        }
        String next = sensitiveWord.substring(index, index + 1);
        Map<String, Map> subTree = tree.get(next);
        if (subTree == null) {
            subTree = new HashMap<String, Map>();
        }
        tree.put(next, buildDictTree(subTree, sensitiveWord, index + 1));
        return tree;
    }

    /**
     * 构造每个词典数元素的 map集合,词组和词长
     * @param sensitiveWord
     * @return
     */
    private static Map<String, Object> buileDictMap(String sensitiveWord) {
        Map<String, Object> wordMap = new HashMap<String, Object>();
        wordMap.put(WConfs.WORD_VALUE, sensitiveWord);
        wordMap.put(WConfs.WORD_LENGTH, sensitiveWord.length());
        return wordMap;
    }

}
