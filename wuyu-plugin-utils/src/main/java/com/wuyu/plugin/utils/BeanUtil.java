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
package com.wuyu.plugin.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class BeanUtil extends BeanUtils {

    /**
     * 实例化对象
     * @param clazz 类
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实例化对象
     * @param clazz 类名
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazz) {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * copy 对象属性到另一个对象，默认不使用Convert
     * @param src
     * @param clazz 类名
     * @return T
     */
    public static <T> T copy(Object src, Class<T> clazz) {
        BeanCopier copier = BeanCopier.create(src.getClass(), clazz, false);
        T to = newInstance(clazz);
        copier.copy(src, to, null);
        return to;
    }

    /**
     * 拷贝对象
     * @param src 源对象
     * @param dist 需要赋值的对象
     */
    public static void copy(Object src, Object dist) {
        BeanCopier copier = BeanCopier.create(src.getClass(), dist.getClass(), false);
        copier.copy(src, dist, null);
    }

    /**
     * 将对象装成map形式
     * @param src
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map toMap(Object src) {
        return BeanMap.create(src);
    }

}
