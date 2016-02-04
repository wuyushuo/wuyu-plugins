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
package com.wuyu.plugin.itimez.impl;

import com.wuyu.plugin.itimez.ITimeZoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.Date;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ITimeZoneRepositoryImpl implements ITimeZoneRepository{

    private Logger LOG = LoggerFactory.getLogger(ITimeZoneRepositoryImpl.class);

    private JdbcTemplate jdbcTemplate;

    /**
     * select update zone time by channel business
     * @param channel channel business
     * @return index zone time value，zero mean do full index, else do dela index
     */
    public synchronized long selectIndexZoneTime(String channel){
        try {
            String psql = "select latestIndexTime from tb_index_zone where channel=?";
            return jdbcTemplate.queryForObject(psql, new Object[]{channel}, Long.class);
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
            LOG.error(e.getLocalizedMessage(), e);
        }
        return 0L;
    }

    /**
     * update each channel lasted index time by channel
     * @param channel channel business
     * @param time
     * @return return update result success return true， else return false
     */
    public synchronized boolean updateIndexZoneTime(String channel, long time){
        try {
            Date date = new Date(time);
            String psql = "update tb_index_zone set latestIndexTime=?, latestIndexDate=? where channel=?";
            int effect =  jdbcTemplate.update(psql, new Object[]{time, date, channel}, new int[]{Types.BIGINT, Types.TIMESTAMP ,Types.VARCHAR});
            return effect > 0;
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
