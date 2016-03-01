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
package ${genPackage}.service.impl;

import ${baseEntityPackageName}.${baseEntityName};
import ${basePackageName}.service.${baseEntityName}Service;
import ${basePackageName}.manager.${baseEntityName}Manager;
import com.wuyu.plugin.pager.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
@Service(value = "${bean.firstLower($baseEntityName)}Service")
public class ${baseEntityName}ServiceImpl implements ${baseEntityName}Service{

    @Autowired
    private ${baseEntityName}Manager ${bean.firstLower($baseEntityName)}Manager;

    @Override
    public ${baseEntityName} insert(${baseEntityName} entity) {
        return this.${bean.firstLower($baseEntityName)}Manager.insert(entity);
    }

    @Override
    public int update(${baseEntityName} entity) {
        return this.${bean.firstLower($baseEntityName)}Manager.update(entity);
    }

    @Override
    public int delete(#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey) {
        return this.${bean.firstLower($baseEntityName)}Manager.delete(primaryKey);
    }

    @Override
    public int deleteMulti(Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys) {
        return this.${bean.firstLower($baseEntityName)}Manager.deleteMulti(primaryKeys);
    }

    @Override
    public int deleteObjects(${baseEntityName} entity) {
        return this.${bean.firstLower($baseEntityName)}Manager.deleteObjects(entity);
    }

    @Override
    public ${baseEntityName} select(#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey) {
        return this.${bean.firstLower($baseEntityName)}Manager.select(primaryKey);
    }

    @Override
    public List<${baseEntityName}> selectMulti(Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys) {
        return this.${bean.firstLower($baseEntityName)}Manager.selectMulti(primaryKeys);
    }

    @Override
    public List<${baseEntityName}> selectObjects(${baseEntityName} entity) {
        return this.${bean.firstLower($baseEntityName)}Manager.selectObjects(entity);
    }

    @Override
    public Page<${baseEntityName}> selectPage(${baseEntityName} entity, long offset, int size) {
        return this.${bean.firstLower($baseEntityName)}Manager.selectPage(entity, offset, size);
    }

    public void set${baseEntityName}Manager(${baseEntityName}Manager ${bean.firstLower($baseEntityName)}Manager) {
        this.${bean.firstLower($baseEntityName)}Manager = ${bean.firstLower($baseEntityName)}Manager;
    }
}
