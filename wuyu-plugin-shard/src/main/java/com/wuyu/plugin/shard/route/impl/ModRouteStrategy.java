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
package com.wuyu.plugin.shard.route.impl;

import com.wuyu.plugin.shard.route.RouteStrategy;
import com.wuyu.plugin.utils.StringUtil;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ModRouteStrategy implements RouteStrategy{

    @Override
    public long route(String routeKey) {
        if(StringUtil.isNotBlank(routeKey)){
            Checksum checksum = new CRC32();
            byte[] bytes = routeKey.getBytes();
            checksum.update(bytes, 0, bytes.length);
            return checksum.getValue();
        }
        return 1L;
    }
}
