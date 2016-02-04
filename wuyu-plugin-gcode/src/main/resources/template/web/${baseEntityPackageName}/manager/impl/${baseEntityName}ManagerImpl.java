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
package ${genPackage}.manager.impl;

import ${basePackageName}.repository.${baseEntityName}Dao;
import ${baseEntityPackageName}.${baseEntityName};
import ${basePackageName}.manager.${baseEntityName}Manager;
import com.wuyu.plugin.datasource.annotation.DynamicDataSource;
import com.wuyu.plugin.pager.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
@DynamicDataSource
@Component(value = "${bean.firstLower($baseEntityName)}Manager")
public class ${baseEntityName}ManagerImpl implements ${baseEntityName}Manager{

    private final static Logger LOG = LoggerFactory.getLogger(${baseEntityName}ManagerImpl.class);

    @Autowired
    private ${baseEntityName}Dao ${bean.firstLower($baseEntityName)}Dao;

    @Resource(name = "transactionManager")
    private PlatformTransactionManager transactionManager;


    @Override
    public ${baseEntityName} insert(${baseEntityName} entity){
        try{
            int effect = ${bean.firstLower($baseEntityName)}Dao.insert(entity);
            if(effect > 0){
                return entity;
            }
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.insert error! " + e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public int update(${baseEntityName} entity) {
        int sqlResult = 0;
        try{
            sqlResult = ${bean.firstLower($baseEntityName)}Dao.update(entity);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.update error! " + e.getLocalizedMessage(), e);
        }
        return sqlResult;
    }

    @Override
    public int delete(#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey){
        int sqlResult = 0;
        try{
            sqlResult = ${bean.firstLower($baseEntityName)}Dao.delete(primaryKey);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.delete error!" + e.getLocalizedMessage(), e);
        }
        return sqlResult;
    }

    @Override
    public int deleteMulti(Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys) {
        int sqlResult = 0;
        try{
            sqlResult = ${bean.firstLower($baseEntityName)}Dao.deleteMulti(primaryKeys);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.deleteMulti error!" + e.getLocalizedMessage(), e);
        }
        return sqlResult;
    }

    @Override
    public int deleteObjects(${baseEntityName} entity) {
        int sqlResult = 0;
        try{
            sqlResult = ${bean.firstLower($baseEntityName)}Dao.deleteObjects(entity);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.deleteObjects error!" + e.getLocalizedMessage(), e);
        }
        return sqlResult;
    }

    @Override
    public ${baseEntityName} select(#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey) {
        ${baseEntityName} result = null;
        try{
            result = ${bean.firstLower($baseEntityName)}Dao.select(primaryKey);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.select error!" + e.getLocalizedMessage(), e);
        }
        return result;
    }

    @Override
    public List<${baseEntityName}> selectMulti(Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys) {
        List<${baseEntityName}> result = Collections.emptyList();
        try{
            result = ${bean.firstLower($baseEntityName)}Dao.selectMulti(primaryKeys);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.selectMulti error!" + e.getLocalizedMessage(), e);
        }
        return result;
    }

    @Override
    public List<${baseEntityName}> selectObjects(${baseEntityName} entity){
        List<${baseEntityName}> result = null;
        try{
            result = ${bean.firstLower($baseEntityName)}Dao.selectObjects(entity);
        }catch (Exception e) {
            LOG.error("${bean.firstLower($baseEntityName)}Dao.selectObjects error!", e);
        }
        return result;
    }

    @Override
    public Page<${baseEntityName}> selectPage(${baseEntityName} entity,long offset,int size){
        Page<${baseEntityName}> page  = null;
        try{
            if(offset <= 0){
                offset = 1L;
            }
            if(size <= 0){
                size = Page.DEFAULT_PAGE_SIZE;
            }
            List<${baseEntityName}> list = ${bean.firstLower($baseEntityName)}Dao.selectPagination(entity, (offset - 1) * size, size);
            long recordTotal = ${bean.firstLower($baseEntityName)}Dao.selectTotal(entity);
            page = new Page<${baseEntityName}>(list, recordTotal, offset, size);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.selectPage error!", e);
        }
        return page;
    }


    @Override
    public long selectTotal(${baseEntityName} entity) {
        try{
            return this.${bean.firstLower($baseEntityName)}Dao.selectTotal(entity);
        }catch (Exception e){
            LOG.error("${bean.firstLower($baseEntityName)}Dao.selectTotal error!", e);
        }
        return 0;
    }

    public void set${baseEntityName}Dao(${baseEntityName}Dao ${bean.firstLower($baseEntityName)}Dao) {
        this.${bean.firstLower($baseEntityName)}Dao = ${bean.firstLower($baseEntityName)}Dao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
