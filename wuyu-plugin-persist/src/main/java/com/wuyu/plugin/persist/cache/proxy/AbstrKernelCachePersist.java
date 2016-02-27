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
package com.wuyu.plugin.persist.cache.proxy;

import com.wuyu.plugin.persist.cache.KernelCachePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public abstract class AbstrKernelCachePersist implements KernelCachePersist {

    @Autowired
    private RedisTemplate redisTemplate;

    public AbstrKernelCachePersist() {
    }

    public AbstrKernelCachePersist(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setCacheable(String cacheName, Object cacheValue) {
        redisTemplate.opsForValue().set(cacheName, cacheValue);
    }

    @Override
    public void setCacheable(String cacheName, Object cacheValue, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(cacheName, cacheValue);
        redisTemplate.expire(cacheName, timeout, unit);
    }

    @Override
    public void setCacheable(String cacheName, Object cacheValue, long timeoutSenconds) {
        setCacheable(cacheName, cacheValue, timeoutSenconds, TimeUnit.SECONDS);
    }

    @Override
    public void removeCacheable(String cacheName) {
        redisTemplate.expire(cacheName, 0, TimeUnit.SECONDS);
    }

}
