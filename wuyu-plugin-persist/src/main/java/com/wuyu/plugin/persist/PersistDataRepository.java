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
package com.wuyu.plugin.persist;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class PersistDataRepository {


    private String charset = "UTF-8";



    /*
    private RedisCacheManager redisCacheManager;*/

    public static final int DEFAULT_SIZE = 50;

    // following is global redis operate
    // http://www.cnblogs.com/yjmyzz/p/4113019.html

    /**
     * revise offset, only accept [1, Long.MAX_VALUE]
     * @param offset
     * @return
     */
    public long reviseOffset(long offset){
        return reviseOffset(offset, 1, Long.MAX_VALUE);
    }

    /**
     * revise offset, only accept [1, maxValue]
     * @param offset
     * @param maxValue
     * @return
     */
    public long reviseOffset(long offset, long maxValue){
        return reviseOffset(offset, 1, maxValue);
    }

    /**
     * revise offset, only accept [minValue, maxValue]
     * @param offset
     * @param minValue
     * @param maxValue
     * @return
     */
    public long reviseOffset(long offset, long minValue, long maxValue){
        return (offset <=  minValue) ? minValue : (offset >=maxValue ? maxValue : offset);
    }

    /**
     * revise size, only accept [minValue, maxValue]
     * @param size
     * @param maxValue
     * @return
     */
    public int reviseSize(int size, int maxValue){
        return reviseSize(size, 1, maxValue);
    }

    /**
     * revise size, only accept [minValue, maxValue]
     * @param size
     * @param maxValue
     * @return
     */
    public int reviseSize(int size, int min, int maxValue){
        return (size <= min) ? min : (size >= maxValue ? maxValue : size);
    }
}
