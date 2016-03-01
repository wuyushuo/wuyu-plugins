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
package com.wuyu.plugin.gen.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class VersionInfo {

    private String organization = "wuyushuo";

    private String organizationURL = "www.wuyushuo.com";

    private String author = "dennisit.pu@wuyushuo.com";

    private Date date = new Date();

    private String version = "1.0";


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date());
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("author:").append(this.getAuthor()).append(",");
        buffer.append("date:").append(this.getDate()).append(",");
        buffer.append("version:").append(this.getVersion()).append("}");
        return buffer.toString();
    }

    public static void main(String[] args){
        System.out.println(new VersionInfo());
    }
}
