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
package ${genPackage}.repository;

import ${baseEntityPackageName}.${baseEntityName};
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
@Repository(value = "${bean.firstLower($baseEntityName)}Dao")
public interface ${baseEntityName}Dao {


    //===============================================================================
    // Write database operate
    //===============================================================================

    /**
     * insert and return primary key
     * @param entity entity fulled with new data
     * @return insert and full entity with primary key, return effect row number
     * @throws RuntimeException
     */
    public int insert(@Param("object") ${baseEntityName} entity) throws RuntimeException;

    /**
     * update object by object hold primary key
     * @param entity entity dependence original primary key fulled with new data
     * @return effect row numbers
     * @throws RuntimeException
     */
    public int update(@Param("object") ${baseEntityName} entity) throws RuntimeException;

    /**
     * delete object by primaryKey
     * @param primaryKey primaryKey of object
     * @return effect row numbers
     * @throws RuntimeException
     */
    public int delete(@Param("pKey") #foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey) throws RuntimeException;

    /**
     * delete object by collection of primary key
     * @param primaryKeys collection of primary key
     * @return effect row numners
     * @throws RuntimeException
     */
    public int deleteMulti(@Param("pKeys") Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys) throws RuntimeException;

    /**
     * delete object with condition
     * @param entity query condition
     * @return effect row numbers
     * @throws RuntimeException
     */
    public int deleteObjects(@Param("object") ${baseEntityName} entity) throws RuntimeException;


    //===============================================================================
    // Read database operate
    //===============================================================================

    /**
     * query entity by primary key
     * @param primaryKey collection of primary key
     * @return object result
     * @throws RuntimeException
     */
    public ${baseEntityName} select(@Param("pKey") #foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey) throws RuntimeException;


    /**
     * query entity by primary key collection
     * @param primaryKeys collection of primary key
     * @return collection of object result
     * @throws RuntimeException
     */
    public List<${baseEntityName}> selectMulti(@Param("pKeys") Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys) throws RuntimeException;

    /**
     * query object collection with condition
     * @param entity query condition
     * @return collection of object result
     * @throws RuntimeException
     */
    public List<${baseEntityName}> selectObjects(@Param("object") ${baseEntityName} entity) throws RuntimeException;

    /**
     * query entity with condition
     * @param entity query condition
     * @param offset page.offset
     * @param size page.size
     * @return collection of object result
     * @throws RuntimeException
     */
    public List<${baseEntityName}> selectPagination(@Param("object") ${baseEntityName} entity,
                                       @Param("offset") long offset,
                                       @Param("size") int size) throws RuntimeException;

    /**
     * query object collection size by condition
     * @param entity query cndition
     * @return total for row number
     * @throws RuntimeException
     */
    public long selectTotal(@Param("object") ${baseEntityName} entity) throws RuntimeException;

}
