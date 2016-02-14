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

import java.util.HashSet;
import java.util.Set;

/**
 * from follows:
 * [http://www.lxway.com/48582412.htm]
 * [http://www.lxway.com/48582412.htm]
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class TreeDic {

    /**
     * dic tree node
     */
    private TreeNode treeNode;

    /**
     * load extends words
     * @param keys
     */
    public TreeDic(String keys[]) {
        Set<String> sets = new HashSet<String>(keys.length);
        for(int i=0; i<keys.length; i++){
            sets.add(keys[i]);
        }
        treeNode = TreeNode.initNode(sets);
    }

    /**
     * load extends words
     * @param keys
     */
    public TreeDic(Set<String> keys){
        treeNode = TreeNode.initNode(keys);
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }
}
