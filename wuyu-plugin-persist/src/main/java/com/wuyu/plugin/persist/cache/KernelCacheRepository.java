/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, dennisit.pu All rights reserved. 
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
 * Author: dennisit.pu (dennisit@163.com)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.persist.cache;

import com.wuyu.plugin.persist.except.CacheNotFoundCallback;
import java.util.concurrent.TimeUnit;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public interface KernelCacheRepository {

    public void setCacheable(String cacheName, Object cacheValue);

    public void setCacheable(String cacheName, Object cacheValue, long timeout, TimeUnit unit);

    public void setCacheable(String cacheName, Object cacheValue, long timeoutSenconds);

    public <T> T getCacheable(String cacheName, CacheNotFoundCallback<T> callback);

    public void removeCacheable(String cacheName);

}
