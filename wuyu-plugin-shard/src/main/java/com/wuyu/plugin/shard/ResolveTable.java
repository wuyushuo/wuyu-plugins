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
package com.wuyu.plugin.shard;

import com.wuyu.plugin.shard.route.RouteStrategy;
import com.wuyu.plugin.shard.route.impl.ModRouteStrategy;
import com.wuyu.plugin.shard.shard.ShardNumber;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ResolveTable {

    /**
     * route strategy
     * @param routeStrategy
     * @param routeKey
     * @param shardNumber
     * @return
     */
    public static long shard(RouteStrategy routeStrategy, String routeKey, ShardNumber shardNumber){
        return shard(routeStrategy, routeKey, shardNumber.getValue());
    }

    /**
     * route strategy
     * @param routeStrategy
     * @param routeKey
     * @param shardNumber
     * @return
     */
    public static long shard(RouteStrategy routeStrategy, String routeKey, int shardNumber){
        long routeVal = routeStrategy.route(routeKey);
        return routeVal % shardNumber;
    }

    /**
     * render table name
     * @param routeStrategy
     * @param routeKey
     * @param shardNumber
     * @param baseTabName
     * @return
     */
    public static String renderTabName(RouteStrategy routeStrategy,  String routeKey, ShardNumber shardNumber, String baseTabName){
        return renderTabName(shard(routeStrategy, routeKey, shardNumber), baseTabName);
    }

    /**
     * render table name
     * @param routeStrategy
     * @param routeKey
     * @param shardNumber
     * @param baseTabName
     * @return
     */
    public static String renderTabName(RouteStrategy routeStrategy,  String routeKey, int shardNumber, String baseTabName){
        return renderTabName(shard(routeStrategy, routeKey, shardNumber), baseTabName);
    }

    /**
     * render table name
     * @param routeStrategy
     * @param routeKey
     * @param shardNumber
     * @param baseTabName
     * @param tbSuffix
     * @return
     */
    public static String renderTabName(RouteStrategy routeStrategy, String routeKey, ShardNumber shardNumber, String baseTabName, String tbSuffix){
        return renderTabName(shard(routeStrategy, routeKey, shardNumber), baseTabName, tbSuffix);
    }


    /**
     * render table name
     * @param routeStrategy
     * @param routeKey
     * @param shardNumber
     * @param baseTabName
     * @param tbSuffix
     * @return
     */
    public static String renderTabName(RouteStrategy routeStrategy,  String routeKey, int shardNumber, String baseTabName, String tbSuffix){
        return renderTabName(shard(routeStrategy, routeKey, shardNumber), baseTabName, tbSuffix);
    }


    /**
     * render table name
     * @param index
     * @param baseTabName
     * @return
     */
    public static String renderTabName(long index, String baseTabName) {
        return renderTabName(index, baseTabName, "_0");
    }

    /**
     * render table name
     * @param index
     * @param baseTabName
     * @param tbSuffix _0,_00,_00
     * @return
     */
    public static String renderTabName(long index, String baseTabName, String tbSuffix) {
        String newTabName = null;
        String left = String.valueOf(tbSuffix.charAt(0));
        String right = tbSuffix.split("[" + left + "]")[1];
        StringBuffer placeholder = new StringBuffer();
        int indexlength = String.valueOf(index).length();
        int rightLength = right.length();
        if (indexlength < rightLength) {
            for (int i = 0; i < rightLength - indexlength; i++)
                placeholder.append(right.charAt(0));
            newTabName = baseTabName + left + placeholder.toString() + index;
        } else {
            newTabName = baseTabName + left + index;
        }
        return newTabName;
    }


    public static void main(String [] args){
        System.out.println(renderTabName(128, null));
        System.out.println(renderTabName(1, "tb_test", "_0000"));
        System.out.println(renderTabName(new ModRouteStrategy(), "1", 1, "tb_test"));
        for(int i=0; i<50; i++){
            System.out.println(renderTabName(new ModRouteStrategy(), String.valueOf(i), 1, "tb_test"));
        }
    }
}
