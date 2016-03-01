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
package com.wuyu.plugin.incrid.primaryid.impl;

import com.wuyu.plugin.incrid.primaryid.PrimaryIdRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class PrimaryIdRedisRepository implements PrimaryIdRepository{

    /**
     * redis template
     */
    private RedisTemplate<Integer, Integer> redisTemplate;

    /**
     * @param channel channel type
     * @return
     */
    public long incrementId(final String channel){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = String.valueOf(channel).getBytes();
                Long id = redisConnection.incr(key);
                if (null != id) {
                    return id;
                }
                return 0L;
            }
        });
    }

    public void setRedisTemplate(RedisTemplate<Integer, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<Integer, Integer> getRedisTemplate() {
        return redisTemplate;
    }
}
