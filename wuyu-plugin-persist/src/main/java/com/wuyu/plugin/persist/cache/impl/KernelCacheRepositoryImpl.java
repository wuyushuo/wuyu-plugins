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
package com.wuyu.plugin.persist.cache.impl;

import com.wuyu.plugin.persist.cache.KernelCacheRepository;
import com.wuyu.plugin.persist.except.CacheNotFoundCallback;
import com.wuyu.plugin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
@Component
public class KernelCacheRepositoryImpl implements KernelCacheRepository {

    private static final Logger LOG = LoggerFactory.getLogger(KernelCacheRepositoryImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public KernelCacheRepositoryImpl() {

    }

    public KernelCacheRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setCacheable(String cacheName, Object cacheValue) {
        if(StringUtil.isBlank(cacheName) || null == cacheValue){
            return;
        }
        redisTemplate.opsForValue().set(cacheName, cacheValue);
    }

    @Override
    public void setCacheable(String cacheName, Object cacheValue, long timeout, TimeUnit unit) {
        if(StringUtil.isBlank(cacheName) || null == cacheValue){
            return;
        }
        redisTemplate.opsForValue().set(cacheName, cacheValue);
        if(timeout > 0 ){
            unit = (null != unit) ? unit : TimeUnit.SECONDS ;
            redisTemplate.expire(cacheName, timeout, unit);
        }
    }

    @Override
    public void setCacheable(String cacheName, Object cacheValue, long timeoutSenconds) {
        setCacheable(cacheName, cacheValue, timeoutSenconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T getCacheable(String cacheName, CacheNotFoundCallback<T> callback) {
        if(StringUtil.isBlank(cacheName)){
            return null;
        }
        T cacheValue = (T) redisTemplate.opsForValue().get(cacheName);
        if(null == cacheValue){
            try {
                cacheValue = callback.execute();
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        return cacheValue;
    }

    @Override
    public void removeCacheable(String cacheName) {
        if(StringUtil.isBlank(cacheName)){
            return;
        }
        redisTemplate.expire(cacheName, 0, TimeUnit.SECONDS);
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
