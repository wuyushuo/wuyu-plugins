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
package com.wuyu.plugin.itimez;

import com.wuyu.plugin.itimez.bean.ITimeZone;

import java.util.Date;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public interface ITimeZoneRepository {

    /**
     * detail iTimezone by id
     * @param id
     * @return
     */
    public ITimeZone detailIndexZoneTime(long id);

    /**
     * select update zone time by channel business
     * @param channel channel business
     * @return index zone time value，zero mean do full index, else do dela index
     */
    public long selectIndexZoneTime(String channel);

    /**
     * select list total fo index time
     * @return
     */
    public long selectIndexZoneTimeTotal();

    /**
     * select list of index time zone list
     * @param offset
     * @param size
     * @return
     */
    public List<ITimeZone> selectIndexZoneTimeList(int offset, int size);

    /**
     * insert new update index zone time
     * @param iTimeZone
     * @return
     */
    public boolean insertIndexZoneTime(ITimeZone iTimeZone);

    /**
     * update each channel lasted index time by channel
     * @param channel channel business
     * @param time
     * @return return update result success return true， else return false
     */
    public boolean updateIndexZoneTime(String channel, long time);


    /**
     * update each channel lasted index time by channel, for db info update
     * @param channel  channel business
     * @param latestIndexDate
     * @param remark description of remark
     * @return return update result success return true， else return false
     */
    public boolean updateIndexZoneTime(long id, String channel, Date latestIndexDate, String remark);

    /**
     * reset each channel lasted index itme to zero, for full index
     * @param channel
     * @return
     */
    public boolean zebackIndexZoneTime(String channel);
}
