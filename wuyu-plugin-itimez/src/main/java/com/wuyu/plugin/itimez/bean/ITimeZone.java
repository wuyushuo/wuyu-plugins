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
package com.wuyu.plugin.itimez.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ITimeZone implements Serializable{

    /**
     * auto increment id
     */
    private Long id;

    /**
     * channel for business
     */
    private String channel;

    /**
     * latestIndexTime
     */
    private long latestIndexTime;

    /**
     * latestIndexDate just convert latestIndexTime to Date
     */
    private Date latestIndexDate;

    /**
     * business channerl description
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getLatestIndexTime() {
        return latestIndexTime;
    }

    public void setLatestIndexTime(long latestIndexTime) {
        this.latestIndexTime = latestIndexTime;
    }

    public Date getLatestIndexDate() {
        return latestIndexDate;
    }

    public void setLatestIndexDate(Date latestIndexDate) {
        this.latestIndexDate = latestIndexDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
