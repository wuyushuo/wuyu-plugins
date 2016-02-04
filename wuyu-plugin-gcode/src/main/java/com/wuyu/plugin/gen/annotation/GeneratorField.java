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
package com.wuyu.plugin.gen.annotation;

import java.lang.annotation.*;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GeneratorField {

    /**
     * 是否为主键
     * @return
     */
    boolean primaryKey() default false;

    /**
     * 映射的字段名
     * @return
     */
    public String name() default "";

    /**
     * 是否可为空
     * @return
     */
    public boolean isNull() default true;

    /**
     * 默认值
     * @return
     */
    public String defaultValue() default "";

    /**
     * 注释
     * @return
     */
    public String comment() default "";

    /**
     * 字段类型
     * @return
     */
    public String type() default "varchar(40)";

}
