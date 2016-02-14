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
package com.wuyu.plugin.wfilter.load;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WConfs {

    /**
     * main illegal word path
     */
    public static final String MAIN_ILLEGAL_DICT = "com/wuyu/plugin/wfilter/illegal_word.txt";

    /**
     * configure file path
     */
    public static final String CONF_FILE_NAME = "WordFilter.cfg.xml";

    /**
     *  key of config illegal word dic path
     */
    public static final String CONF_KEY_iLLEGAL_DICT = "word_dict";

    /**
     * key of config illegal word replace to
     */
    public static final String CONF_KEY_REPLACE_TO = "replace_to";

    /**
     * value of  config illegal word replace to
     */
    public static final String CONF_KEY_REPLACE_TO_VALUE = "*";

    /**
     * key of config word dict split char
     */
    public static final String CONF_KEY_ILLEGAL_DICT_SPLIT = "word_dict_split";

    /**
     * value of config word dict split char
     */
    public static final String CONF_KEY_ILLEGAL_DICT_SPLIT_VALUE = ";";
}
