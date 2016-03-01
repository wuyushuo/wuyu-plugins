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

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class EntityTable implements Serializable{

	private static final long serialVersionUID = -2124771513691971465L;

	//字段主键的集合<主键名,主键类型> only on element
    private ConcurrentMap<String,Object> propertyPKeyType = new ConcurrentHashMap<String, Object>();

    //实体名到表名的映射<实体名,实体名对应的表名>
    private ConcurrentMap<String,String> entityToTable = new ConcurrentHashMap<String, String>();

    //属性到主键的别名映射<实体主键属性,表主键字段名>
    private ConcurrentMap<String,String> propertyToPKey = new ConcurrentHashMap<String,String>(1);

    //属性到字段的别名映射<实体属性,表字段名>
    private ConcurrentMap<String,String> propertyToColumn = new ConcurrentHashMap<String, String>();

    //表名到sql脚本的映射
    private ConcurrentMap<String,String> dbNameToSQL = new ConcurrentHashMap<String, String>();

    public ConcurrentMap<String, String> getEntityToTable() {
        return entityToTable;
    }

    public void setEntityToTable(ConcurrentMap<String, String> entityToTable) {
        this.entityToTable = entityToTable;
    }

    public ConcurrentMap<String, String> getPropertyToPKey() {
        return propertyToPKey;
    }

    public void setPropertyToPKey(ConcurrentMap<String, String> propertyToPKey) {
        this.propertyToPKey = propertyToPKey;
    }

    public ConcurrentMap<String, String> getPropertyToColumn() {
        return propertyToColumn;
    }

    public void setPropertyToColumn(ConcurrentMap<String, String> propertyToColumn) {
        this.propertyToColumn = propertyToColumn;
    }

    public ConcurrentMap<String, Object> getPropertyPKeyType() {
        return propertyPKeyType;
    }

    public void setPropertyPKeyType(ConcurrentMap<String, Object> propertyPKeyType) {
        this.propertyPKeyType = propertyPKeyType;
    }

    public ConcurrentMap<String, String> getDbNameToSQL() {
        return dbNameToSQL;
    }

    public void setDbNameToSQL(ConcurrentMap<String, String> dbNameToSQL) {
        this.dbNameToSQL = dbNameToSQL;
    }

}
