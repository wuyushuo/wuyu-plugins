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
package com.wuyu.plugin.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class CollectionUtil {

    private CollectionUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isEmpty(Collection coll){
        return CollectionUtils.isEmpty(coll);
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static String asString(Collection c, String separator) {
        if (c == null) {
            return "";
        }
        StringBuilder result = new StringBuilder(256);
        boolean needSeparator = false;
        for (Object o : c) {
            if (o == null) {
                continue;
            }
            String str = o.toString().trim();
            if (str.equals("")) {
                continue;
            }
            if (needSeparator) {
                result.append(separator);
            }
            result.append(str);
            needSeparator = true;
        }
        return result.toString();
    }

}
