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

import java.util.Random;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class RandomUtil {

    private static final String LETTER_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTER_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT = "0123456789";
    private static final String LETTER_DIGEST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private RandomUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * random length digit number
     * @param length length of random result
     */
    public static String randomNum(int length) {
        if(length < 1){
            return StringUtil.EMPTY;
        }
        int i;
        int count = 0;
        char[] str = DIGIT.toCharArray();
        StringBuffer tempString = new StringBuffer();
        Random random = new Random();
        while (count < length) {
            i = Math.abs(random.nextInt(DIGIT.length()));
            if (i >= 0 && i < str.length) {
                tempString.append(str[i]);
                count++;
            }
        }
        return tempString.toString();
    }

    /**
     * length of rand password
     * @param length
     * @return
     */
    public static final String randomPassword(int length){
        if(length < 1){
            return StringUtil.EMPTY;
        }
        char[] strArray = LETTER_DIGEST.toCharArray();
        StringBuffer pwd = new StringBuffer("");
        int count = 0;
        int tmp;
        Random r = new Random();
        while(count < length) {
            tmp = Math.abs(r.nextInt(LETTER_DIGEST.length()));
            if (tmp >= 0 && tmp < strArray.length) {
                pwd.append(strArray[tmp]);
                count++;
            }
        }
        return pwd.toString();
    }

    public static void main(String[] args){
        System.out.println(randomNum(6));
        System.out.println(randomPassword(8));
    }
}
