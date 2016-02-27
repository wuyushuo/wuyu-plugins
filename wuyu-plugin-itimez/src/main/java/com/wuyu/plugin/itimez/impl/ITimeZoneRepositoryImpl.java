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
import com.wuyu.plugin.itimez.bean.ITimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;
import java.util.Date;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ITimeZoneRepositoryImpl implements ITimeZoneRepository{

    private Logger LOG = LoggerFactory.getLogger(ITimeZoneRepositoryImpl.class);

    private JdbcTemplate jdbcTemplate;

    @Override
    public ITimeZone detailIndexZoneTime(long id) {
        try {
            if(id > 0){
                String psql = "select id, channel, latestIndexTime, latestIndexDate, remark from tb_index_zone Where id = ?";
                return (ITimeZone) jdbcTemplate.queryForObject(psql, new Object[]{id}, new BeanPropertyRowMapper(ITimeZone.class));
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * select update zone time by channel business
     * @param channel channel business
     * @return index zone time value，zero mean do full index, else do dela index
     */
    public synchronized long selectIndexZoneTime(String channel){
        try {
            if(null != channel && channel.trim().length() > 0 ){
                String psql = "select latestIndexTime from tb_index_zone where channel=?";
                return jdbcTemplate.queryForObject(psql, new Object[]{channel}, Long.class);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return 0L;
    }

    /**
     * select list total fo index time
     * @return
     */
    @Override
    public synchronized long selectIndexZoneTimeTotal() {
        try {
            String psql = "select count(0) from tb_index_zone";
            return jdbcTemplate.queryForObject(psql, Long.class);
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return 0;
    }

    /**
     * select list of index time zone list
     * @param offset
     * @param size
     * @return
     */
    @Override
    public synchronized List<ITimeZone> selectIndexZoneTimeList(int offset, int size) {
        try {
            offset = offset <=0 ? 0 : offset;
            String psql = "select id, channel, latestIndexTime, latestIndexDate, remark from tb_index_zone order by id desc limit ?, ?";
            return jdbcTemplate.query(psql, new Object[]{offset, size}, new BeanPropertyRowMapper(ITimeZone.class));
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public boolean insertIndexZoneTime(ITimeZone iTimeZone) {
        try {
            if(null != iTimeZone){
                String psql = "insert into tb_index_zone(channel, latestIndexTime, latestIndexDate, remark ) values (?,?,?,?)";
                int effect = jdbcTemplate.update(psql,
                        new Object[]{iTimeZone.getChannel(), iTimeZone.getLatestIndexTime(), iTimeZone.getLatestIndexDate(), iTimeZone.getRemark()},
                        new int[]{Types.VARCHAR, Types.BIGINT, Types.TIMESTAMP, Types.VARCHAR});
                return effect > 0;
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * update each channel lasted index time by channel
     * @param channel channel business
     * @param time
     * @return return update result success return true， else return false
     */
    public synchronized boolean updateIndexZoneTime(String channel, long time){
        try {
            if(null != channel && channel.trim().length() > 0 ){
                time = (time <=0) ? 0 : time;
                Date date = new Date(time);
                String psql = "update tb_index_zone set latestIndexTime=?, latestIndexDate=? where channel=?";
                int effect =  jdbcTemplate.update(psql, new Object[]{time, date, channel}, new int[]{Types.BIGINT, Types.TIMESTAMP ,Types.VARCHAR});
                return effect > 0;
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public boolean updateIndexZoneTime(long id, String channel, Date latestIndexDate, String remark) {
        try {
            if(null != channel && channel.trim().length() > 0 && null != latestIndexDate && id>0){
                long latestIndexTime = latestIndexDate.getTime();
                String psql = "update tb_index_zone set latestIndexTime=?, latestIndexDate=?, remark=?, channel=? WHERE id=?";
                int effect =  jdbcTemplate.update(psql, new Object[]{latestIndexTime, latestIndexDate, remark ,channel, id}, new int[]{Types.BIGINT, Types.TIMESTAMP ,Types.VARCHAR, Types.VARCHAR, Types.BIGINT});
                return effect > 0;
            }
        }catch (Exception e){
            System.err.println(e.getLocalizedMessage());
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    @Override
    public synchronized boolean zebackIndexZoneTime(String channel) {
        return updateIndexZoneTime(channel, 0);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
