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
package com.wuyu.plugin.wfilter.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * from http://www.lxway.com/48582412.htm

 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class TreeNode {

    public TreeNode(char chat) {
        this.chat = chat;
    }

    public transient char chat;

    public String word;

    public boolean isEnd = false;

    public Map<Character, Map<Character, TreeNode>> tree = new ConcurrentHashMap<Character, Map<Character, TreeNode>>(1024*2);

    /**
     * build keyword to node tree
     * @param keys
     * @return
     */
    public static TreeNode initNode(Set<String> keys) {
        if(null == keys || keys.isEmpty()){
            return null;
        }
        TreeNode root = new TreeNode('-');
        //迭代keyWordSet
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            char[] chats = key.toCharArray();
            TreeNode rootNode = root;
            for (int j = 0; j < chats.length; j++) {
                rootNode = insert(chats.length == j + 1, rootNode, chats[j], key);
            }
        }
        return root;
    }


    /**
     * build method
     * @param isEnd
     * @param rootNode
     * @param chat
     * @param word
     * @return
     */
    private static TreeNode insert(boolean isEnd, TreeNode rootNode, char chat, String word) {
        if (isEnd) {
            TreeNode end = new TreeNode(chat);
            end.isEnd = true;
            Map<Character, TreeNode> map = rootNode.tree.get(chat);
            if (map == null) {
                map = new HashMap<Character, TreeNode>();
            }
            end.word = word;
            map.put(end.chat, end);
            rootNode.tree.put(chat, map);
            return end;
        } else {
            Map<Character, TreeNode> map = rootNode.tree.get(chat);
            if (map == null) {
                map = new HashMap<Character, TreeNode>();
                TreeNode node = new TreeNode(chat);
                map.put(node.chat, node);
                rootNode.tree.put(chat, map);
                return node;
            } else {
                return map.get(chat);
            }

        }
    }

}