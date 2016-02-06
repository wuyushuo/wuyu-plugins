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
package com.wuyu.plugin.qiniu.auth;

/**
 * description: YinYueTai server support
 * created on 2015/12/22
 * @author pingdong.pu@yinyuetai.com
 * @version 1.0
 */
public class DefaultAuthKey implements AuthKey{

    public static String ACCESS_KEY = "3F_pwUeB72QP_6qvKwnIKigFQSdqYGuU_ltWXcZwe45";
    public static String SECRET_KEY = "Lib6_4ndl2Zx75eaI8Wu7HW8aZjUeXRB34sd4Z35";

    public static String DEFAULT_BUCKET_NAME = "test";

    @Override
    public String getAccessKey() {
        return ACCESS_KEY;
    }

    @Override
    public String getSecretKey() {
        return SECRET_KEY;
    }



}
